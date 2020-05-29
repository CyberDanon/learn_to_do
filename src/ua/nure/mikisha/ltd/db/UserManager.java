package ua.nure.mikisha.ltd.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.db.entity.Admin;
import ua.nure.mikisha.ltd.db.entity.Student;
import ua.nure.mikisha.ltd.db.entity.Teacher;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.db.entity.User_out;
import ua.nure.mikisha.ltd.exception.DBException;
import ua.nure.mikisha.ltd.exception.Messages;
import ua.nure.mikisha.ltd.exception.ServerException;

/**
 * 
 * @author Mikisha Danylo
 *
 */

public class UserManager {
	private static final String BECOME_ADMIN = "UPDATE users set is_adm=1, is_teacher=0 where login=?";
	private static final String BECOME_STUDENT = "UPDATE users set is_adm=0, is_teacher=0 where login=?";
	private static final String BECOME_TEACHER = "UPDATE users set is_adm=0, is_teacher=1 where login=?";
    private static final String SQL_FIND_ALL_USERS = "select * from users";
	
    private static final String SQL_GET_LIST = "select * from users where is_adm=0";
    
	private static final String SQL_FIND_USER_BY_LOGIN = "select * from users where login = ?";

	private static final String SQL_CREATE_USER = "INSERT INTO users(login,password,mail) VALUES (?,?,?)";
	
	private static final String CHANGE_PASS= "UPDATE users SET password=? WHERE login=?";

	private static final String SQL_DELETE_USER = "DELETE FROM users WHERE login=?";

	private static final String SQL_ACCESSCHANGE = "UPDATE users SET is_banned=? where login=?"; 
	
	private static final String SQL_PAY="UPDATE users set balance=? where login like=?";
	
	private static final String SQL_GRADE = "Select sum(grade) as grade from test_results where user_login=?";
	
	private static final Logger LOG = Logger.getLogger(UserManager.class);
	
	private static UserManager instance;
	
	DataSource ds;
	/**
	 * 
	 *singleton realisation of accessor to Users
	 */
	public static synchronized UserManager getInstance() throws DBException{
		if (instance == null) {
			instance = new UserManager();
		}
		return instance;
	}
	private UserManager() throws DBException{
		
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/user");
			LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			LOG.error(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_USERS, ex);
			throw new DBException(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_USERS, ex);
		}
	}
	/**
	 * 
	 *bun/unban action
	 */
	private synchronized boolean acess_change(int status,String user) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(SQL_ACCESSCHANGE);
			int index=0;
			pstmt.setInt(++index, status);
			pstmt.setString(++index, user);
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.USER_ERR_CANNOT_OBTAIN_USER_BY_ID, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	/**
	 * 
	 *update of user balance
	 */
	public synchronized boolean pay(User user) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(SQL_PAY);
			int index=0;
			pstmt.setInt(++index, user.getBalance());
			pstmt.setString(++index, user.getLogin());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new DBException(Messages.USER_ERR_CANNOT_OBTAIN_USER_BY_ID, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	/**
	 * 
	 ban throw the access_change
	 */
	public synchronized boolean ban(String user) throws DBException, ServerException {
		return acess_change(1,user);
	}
	/**
	 * 
	 *unban throw the acess_change
	 */
	public synchronized boolean un_ban(String user) throws DBException, ServerException {
		return acess_change(0,user);
	}
	/**
	 * 
	 *returns all Users
	 */
	public synchronized List<User> findAllUsers() throws ServerException, DBException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_ALL_USERS);
			while (rs.next()) {
				users.add(extractUser(rs));
			}
		} catch (SQLException e) {
			throw new DBException(Messages.USER_ERR_CANNOT_OBTAIN_USER_LIST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(stmt);
			DBUtils.close(con);
		}
		return users;
	}
	/**
	 * 
	 * getting user via it's login - operation for logging into system
	 */
	public synchronized User getUser(String userLogin) throws ServerException, DBException { 
		Connection con = DBUtils.get_conn(ds);
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
			pstmt.setString(1, userLogin);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = extractUser(rs);
			}
		} catch(SQLException e){
			throw new DBException(Messages.USER_ERR_CANNOT_OBTAIN_USER_BY_LOGIN, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
		return user;
	}
	/**
	 * 
	 * create user with it's login and password and adds the basic characteristics
	 */
	public synchronized boolean insertUser(User user) throws ServerException, DBException {
		Connection con = DBUtils.get_conn(ds);
		boolean res = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS);
			int k = 0;
			pstmt.setString(++k, user.getLogin());
			pstmt.setString(++k, user.getPassword());
			pstmt.setString(++k, user.getMail());
			if (pstmt.executeUpdate() > 0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					res = true;
					con.commit();
				}
			}
		} catch(SQLException e){
			throw new DBException(Messages.USER_ERR_CANNOT_CREATE_USER, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}

		return res;
	}
	/**
	 * 
	 * updating of user's password
	 */
	public synchronized boolean updateUser(User user) throws ServerException, DBException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(CHANGE_PASS);
			int k = 0;
			pstmt.setString(++k, user.getPassword());
			pstmt.setString(++k, user.getLogin());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch(SQLException e){
			throw new DBException(Messages.USER_ERR_CANNOT_UPDATE_USER_DATA, e);
		}finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	/**
	 * 
	 * delete user - for future compability
	 */
	public synchronized boolean deleteUser(String login) throws SQLException, ServerException, DBException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_DELETE_USER);

			int k = 1;
			pstmt.setString(++k, login);

			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}

	/**
	 * 
	 * extracts user from result set
	 */
	public static synchronized User extractUser(ResultSet rs) throws SQLException {
		User user;
		if (rs.getInt("is_adm")==1) {
			user = new Admin();
		} else {
			if (rs.getInt("is_teacher")==0) {
			user = new Student();
			} else {
				user = new Teacher();
			}
			user.setBalance(rs.getInt("balance"));
			user.setBonuses(rs.getInt("bonuses"));
		}
		user.setLogin(rs.getString("login"));
		user.setPassword(rs.getString("password"));
		user.setIs_banned(rs.getInt("is_banned"));
		
		return user;
	}
	/**
	 * 
	 * select users, who isn't admin - command for administrators
	 */
	public synchronized List<User> getUserList() throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_GET_LIST);

			while (rs.next()) {
				users.add(extractUser(rs));
			}
		} catch (SQLException e) {
			throw new DBException(Messages.USER_ERR_CANNOT_OBTAIN_USER_LIST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(stmt);
			DBUtils.close(con);
		}
		return users;
	}
	public synchronized List<User_out> getUsersTop() throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User_out> users = new ArrayList<>();
 
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_GET_LIST);

			while (rs.next()) {
				User_out u = new User_out();
				User user = extractUser(rs);
				int grade = this.getInstance().getUserGrade(user.getLogin());
				u.setGrade(grade);
				u.setLogin(user.getLogin());
				u.setIs_banned(user.getIs_banned());
				users.add(u);
			}
		} catch (SQLException e) {
			throw new DBException(Messages.USER_ERR_CANNOT_OBTAIN_USER_LIST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(stmt);
			DBUtils.close(con);
		}
		return users;
	}
	public synchronized int getUserGrade(String userLogin) throws ServerException, DBException { 
		Connection con = DBUtils.get_conn(ds);
		int grade=0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_GRADE);
			pstmt.setString(1, userLogin);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				grade = rs.getInt("grade");
			}
		} catch(SQLException e){
			throw new DBException(Messages.USER_ERR_CANNOT_OBTAIN_USER_BY_LOGIN, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
		return grade;
	}
	public synchronized User become_admin(User user) throws ServerException, DBException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(BECOME_ADMIN);
			int k = 0;
			pstmt.setString(++k, user.getLogin());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				user = this.getInstance().getUser(user.getLogin());
			} else {
				throw new SQLException();
			}
		} catch(SQLException e){
			throw new DBException(Messages.USER_ERR_CANNOT_UPDATE_USER_DATA, e);
		}finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
		return user;
	}
	public synchronized User become_student(User user) throws ServerException, DBException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(BECOME_STUDENT);
			int k = 0;
			pstmt.setString(++k, user.getLogin());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				user = this.getInstance().getUser(user.getLogin());
			} else {
				throw new SQLException();
			}
		} catch(SQLException e){
			throw new DBException(Messages.USER_ERR_CANNOT_UPDATE_USER_DATA, e);
		}finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
		return user;
	}
	public synchronized User become_teacher(User user) throws ServerException, DBException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(BECOME_TEACHER);
			int k = 0;
			pstmt.setString(++k, user.getLogin());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				user = this.getInstance().getUser(user.getLogin());
			} else {
				throw new SQLException();
			}
		} catch(SQLException e){
			throw new DBException(Messages.USER_ERR_CANNOT_UPDATE_USER_DATA, e);
		}finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
		return user;
	}
}
