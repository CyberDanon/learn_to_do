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

import ua.nure.mikisha.ltd.db.entity.Admin;
import ua.nure.mikisha.ltd.db.entity.Course;
import ua.nure.mikisha.ltd.db.entity.Student;
import ua.nure.mikisha.ltd.db.entity.Teacher;
import ua.nure.mikisha.ltd.db.entity.Theorical;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.DBException;
import ua.nure.mikisha.ltd.exception.Messages;
import ua.nure.mikisha.ltd.exception.ServerException;

public class CourseManager {
	private static final String FILTER_STUDENT = "Select subscribes.course_id as course_id, sum(subscribes.rate)"
			+ " as s from subscribes,courses where subscribes.course_id=courses.id"
			+ " and subscribes.user_login=? and courses.name like '&?&' and courses.verification=1"
			+ " order by s group by course_id";
	private static final String FILTER_RECOMS = "Select * from courses where id not in (Select subscribes.course_id as course_id, sum(subscribes.rate)"
			+ " as s from subscribes,courses where subscribes.course_id=courses.id"
			+ "and  NOT subscribes.user_login=? and courses.name like '&?&' and courses.verification=1"
			+ " order by s group by course_id) and not owner=?";
	private static final String FILTER_ADMIN = "Select * from courses where name like '%?%'";
	private static final String FILTER_TEACHER = "Select * from courses where owner=? and name like '%?%'";
	private static final String FILTER_ADMIN_UNBANNED = "Select * from courses where name like '%?%' and verification=1";
	private static final String FILTER_ADMIN_BANNED = "Select * from courses where name like '%?%' and verification=0";
	private static final String INSERT_COURSE = "Insert into cources(name, description, owner, price) values (?,?,?,?)";
	private static final String UPDATE_COURSE = "UPDATE cources set name=?, description=?,price=? where id=?";
	private static final String DELETE_COURSE = "Delete from cources where id=?";
	private static final String GET_COURSE = "Select * from cources where id=?";
	private static final String GET_COURSES_FOR_STUDENT = "Select * from subscribes where user_id=?";
	private static final String GET_COURSES_FOR_TEACHER = "Select * from cources where owner=?";
	private static final String GET_COURSES_FOR_ADMIN_UNBANNED = "Select * from cources where verification=1";
	private static final String GET_COURSES_FOR_ADMIN_BANNED = "Select * from cources where verification=0";
	private static final String CHANGE_VERIF = "Update cources set verification=? where id=?";
	private static final String GET_ALL = "select * from coures";
	private static CourseManager instance;
	private static final Logger LOG = Logger.getLogger(TheoryBlocksManager.class);
	DataSource ds;
	/**
	 * 
	 *singleton realisation of accessor to tests
	 */
	public static synchronized CourseManager getInstance() throws DBException{
		if (instance == null) {
			instance = new CourseManager();
		}
		return instance;
	}
	private CourseManager() throws DBException{
		
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/user");
			LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			LOG.error(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_COURSE, ex);
			throw new DBException(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_COURSE, ex);
		}
	}
	private synchronized boolean change_verification(Course c,int ver) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(CHANGE_VERIF);
			int index=0;
			pstmt.setInt(++index, ver);
			pstmt.setInt(++index, c.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.COURSE_ERR_CANNOT_BAN_UNBAN_COURSE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean ban_course(Course c) throws DBException, ServerException {
		return this.getInstance().change_verification(c,0);
	}
	public synchronized boolean unban_course(Course c) throws DBException, ServerException {
		return this.getInstance().change_verification(c,1);
	}
	public synchronized boolean CreateCourse(Course c) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(INSERT_COURSE);
			int index=0;
			pstmt.setString(++index, c.getName());
			pstmt.setString(++index, c.getDescription());
			pstmt.setString(++index, c.getOwner());
			pstmt.setInt(++index, c.getPrice());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.COURSE_ERR_CANNOT_CREATE_COURSE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean UpdateCourse(Course c) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(UPDATE_COURSE);
			int index=0;
			pstmt.setString(++index, c.getName());
			pstmt.setString(++index, c.getDescription());
			pstmt.setInt(++index, c.getPrice());
			pstmt.setInt(++index, c.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.COURSE_ERR_CANNOT_UPDATE_COURSE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean DeleteCourse(Course c) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(DELETE_COURSE);
			int index=0;
			pstmt.setInt(++index, c.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.COURSE_ERR_CANNOT_DELETE_COURSE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized Course getCourse(Course c) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			int index=0;
			pstmt = con.prepareStatement(GET_COURSE);
			pstmt.setInt(++index, c.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				c = extractCourse(rs);
				c.setTh(TheoryBlocksManager.getInstance().getTheoryForCourse(c));
				c.setTests(TestsManager.getInstance().getTestsForCourse(c));
				return c;
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			throw new DBException(Messages.COURSE_ERR_CANNOT_OBTAIN_COURSE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized List<Course> getCoursesForTeacher(Teacher t) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Course> c = new LinkedList();
		try {
			int index=0;
			pstmt = con.prepareStatement(GET_COURSES_FOR_TEACHER);
			pstmt.setString(++index, t.getLogin());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Course course = extractCourse(rs);
				course = this.getInstance().getCourse(course);
				c.add(course);
			} 
			return c;
		} catch (SQLException e) {
			throw new DBException(Messages.COURSES_ERR_CANNOT_OBTAIN_COURSE_LIST_FOR_TEACHER, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	private synchronized List<Course> admin_getCoursesForAdmin(String req) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Course> c = new LinkedList();
		try {
			int index=0;
			pstmt = con.prepareStatement(req);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Course course = extractCourse(rs);
				course = this.getInstance().getCourse(course);
				c.add(course);
			} 
			return c;
		} catch (SQLException e) {
			throw new DBException(Messages.COURSES_ERR_CANNOT_OBTAIN_COURSE_LIST_FOR_ADMIN, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized List<Course> admin_unbanned() throws DBException, ServerException{
		return this.getInstance().admin_getCoursesForAdmin(GET_COURSES_FOR_ADMIN_UNBANNED);
	}
	public synchronized List<Course> admin_banned() throws DBException, ServerException{
		return this.getInstance().admin_getCoursesForAdmin(GET_COURSES_FOR_ADMIN_BANNED);
	}
	
	public synchronized List<Course> doFilter(User user, String name, boolean fl,boolean fl2,String req) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Course> c = new LinkedList();
		try {
			int index=0;
			pstmt = con.prepareStatement(req);
			if (fl) {
			pstmt.setString(++index, user.getLogin());
			}
			pstmt.setString(++index, name);
			if (fl2) {
				pstmt.setString(++index, user.getLogin());
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Course course = extractCourse(rs);
				course = this.getInstance().getCourse(course);
				c.add(course);
			} 
			return c;
		} catch (SQLException e) {
			throw new DBException(Messages.COURSES_ERR_CANNOT_OBTAIN_COURSE_FILTER, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized List<Course> recom_filter(User user, String name) throws DBException, ServerException{
		return doFilter(user,name, true,true, FILTER_RECOMS);
	}
	public synchronized List<Course> student_filter(User user, String name) throws DBException, ServerException{
		return doFilter(user,name, true,false, FILTER_STUDENT);
	}
	public synchronized List<Course> admin_filter_unbanned(User user, String name) throws DBException, ServerException{
		return doFilter(user,name, false,false, FILTER_ADMIN_UNBANNED);
	}
	public synchronized List<Course> admin_filter_banned(User user, String name) throws DBException, ServerException{
		return doFilter(user,name, false,false, FILTER_ADMIN_BANNED);
	}
	public synchronized List<Course> admin_filter_all(User user, String name) throws DBException, ServerException{
		return doFilter(user,name, false, false,FILTER_ADMIN);
	}
	public synchronized List<Course> teacher_filter(User user, String name) throws DBException, ServerException{
		return doFilter(user,name, true, false,FILTER_TEACHER);
	}
	public synchronized List<Course> getCoursesForStudent(Student s) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Course> c = new LinkedList();
		try {
			int index=0;
			pstmt = con.prepareStatement(GET_COURSES_FOR_TEACHER);
			pstmt.setString(++index, s.getLogin());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Course course = new Course();
				course.setId(rs.getInt("course_id"));
				course = this.getInstance().getCourse(course);
				c.add(course);
			} 
			return c;
		} catch (SQLException e) {
			throw new DBException(Messages.COURSES_ERR_CANNOT_OBTAIN_COURSE_LIST_FOR_STUDENT, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized List<Course> get_all() throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Course> c = new LinkedList();
		try {
			int index=0;
			pstmt = con.prepareStatement(GET_ALL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				c.add(extractCourse(rs));
			} 
			return c;
		} catch (SQLException e) {
			throw new DBException(Messages.COURSES_ERR_CANNOT_OBTAIN_COURSE_LIST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	private Course extractCourse(ResultSet rs) throws SQLException {
		Course c= new Course();
		c.setId(rs.getInt("id"));
		c.setName(rs.getString("name"));
		c.setDescription(rs.getString("description"));
		c.setOwner(rs.getString("owner"));
		c.setPrice(rs.getInt("price"));
		c.setImage(rs.getString("image"));
		c.setVerification(rs.getInt("verification"));
		return c;
	}
}
