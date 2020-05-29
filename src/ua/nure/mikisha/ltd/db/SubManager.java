package ua.nure.mikisha.ltd.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.Student;
import ua.nure.mikisha.ltd.db.entity.Teacher;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.DBException;
import ua.nure.mikisha.ltd.exception.Messages;
import ua.nure.mikisha.ltd.exception.ServerException;

public class SubManager {
	private static final String GET_RATE = "Select rate from subscribes where user_login=? and course_id=?";
	private static final String RATE = "UPDATE subscribes SET rate=? where user_login=? and course_id=?";
	private static final String SUB = "INSERT INTO Subscribes values (?,?,?)";
	private static final String UNSUB = "DELETE FROM Subscribes where user_id=? and course_id=?";
	private static final String RECOMS = "Select * from cources where id not in (select course_id from subscribes where user_id=?)"
			+ " and not owner=?";
	private static final String GET_SUBSCRIBERS = "Select user_login from subscribes where course_id=?";
	private static final String GET_MONEY = "Select price from subscribes where user_login=? and course_id=?";
	private static SubManager instance;
	private static final Logger LOG = Logger.getLogger(SubManager.class);
	DataSource ds;

	/**
	 * 
	 * singleton realisation of accessor to subscribes
	 */
	public static synchronized SubManager getInstance() throws DBException {
		if (instance == null) {
			instance = new SubManager();
		}
		return instance;
	}

	private SubManager() throws DBException {

		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/user");
			LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			LOG.error(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_SUBS, ex);
			throw new DBException(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_SUBS, ex);
		}
	}

	private synchronized boolean exec(User user, Course c, String com, boolean fl) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(com);
			int index = 0;
			pstmt.setString(++index, user.getLogin());
			pstmt.setInt(++index, c.getId());
			if (fl) {
				pstmt.setInt(++index, c.getPrice());
			}
			int res = pstmt.executeUpdate();
			if (res > 0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.SUBS_ERR_CANNOT_EXEC_ACTION, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}

	public synchronized boolean rate(User user, Course c, int grade) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(RATE);
			int index = 0;
			pstmt.setString(++index, user.getLogin());
			pstmt.setInt(++index, c.getId());
			pstmt.setInt(++index, grade);
			int res = pstmt.executeUpdate();
			if (res > 0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.SUBS_ERR_CANNOT_EXEC_ACTION, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}

	public synchronized List<Course> getRecoms(Student s) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Course> c = new LinkedList();
		try {
			int index = 0;
			pstmt = con.prepareStatement(RECOMS);
			pstmt.setString(++index, s.getLogin());
			pstmt.setString(++index, s.getLogin());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Course course = new Course();
				course.setId(rs.getInt("id"));
				course = CourseManager.getInstance().getCourse(course);
				c.add(course);
			}
			return c;
		} catch (SQLException e) {
			throw new DBException(Messages.SUBS_ERR_CANNOT_OBTAIN_COURSE_LIST_FOR_STUDENT, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}

	public synchronized List<User> getSubscribers(Course course) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<User> c = new LinkedList();
		try {
			int index = 0;
			pstmt = con.prepareStatement(RECOMS);
			pstmt.setInt(++index, course.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				User user = new Student();
				user.setLogin(rs.getString("user_login"));
				user = UserManager.getInstance().getUser(user.getLogin());
				c.add(user);
			}
			return c;
		} catch (SQLException e) {
			throw new DBException(Messages.SUBS_ERR_CANNOT_OBTAIN_COURSE_LIST_FOR_STUDENT, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}

	public synchronized boolean SUB(User u, Course c) throws DBException, ServerException {
		return exec(u, c, SUB, true);
	}

	public synchronized boolean UNSUB(User u, Course c) throws DBException, ServerException {
		return exec(u, c, UNSUB, false);
	}

	public synchronized boolean safety_unfollow(User user, Course course) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Course> c = new LinkedList();
		try {
			int index = 0;
			pstmt = con.prepareStatement(GET_MONEY);
			pstmt.setString(++index, user.getLogin());
			pstmt.setInt(++index, course.getId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int money = rs.getInt("price");
				this.getInstance().UNSUB(user, course);
				user.setBalance(user.getBalance()+money);
				UserManager um = UserManager.getInstance();
				um.pay(user);
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new DBException(Messages.SUBS_ERR_CANNOT_OBTAIN_COURSE_LIST_FOR_STUDENT, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}

	public synchronized int getRate(User user, Course course) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Course> c = new LinkedList();
		try {
			int index = 0;
			pstmt = con.prepareStatement(GET_RATE);
			pstmt.setString(++index, user.getLogin());
			pstmt.setInt(++index, course.getId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("rate");
			} else {
				return 0;
			}
		} catch (SQLException e) {
			throw new DBException(Messages.SUBS_ERR_CANNOT_OBTAIN_GRADE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
}
