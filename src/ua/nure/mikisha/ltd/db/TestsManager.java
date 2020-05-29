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
import ua.nure.mikisha.ltd.db.entity.Question;
import ua.nure.mikisha.ltd.db.entity.Test;
import ua.nure.mikisha.ltd.db.entity.Theorical;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.DBException;
import ua.nure.mikisha.ltd.exception.Messages;
import ua.nure.mikisha.ltd.exception.ServerException;

public class TestsManager {
	private static final String PASS = "INSERT INTO test_results values (?,?,?)";
	private static final String GET_GRADE = "Select grade from test_results where user_login=? and test_id=?";
	private static final String CREATE_TEST_COURSE = "Insert into course_tests values (?,?)";
	private static final String CREATE_THEORY_COURSE = "Insert into theory_tests values (?,?)";
	private static final String INSERT_TEST = "Insert into tests(name,description) values (?,?)";
	private static final String UPDATE_TEST = "UPDATE tests set name=?, description=? where id=?";
	private static final String DELETE_TEST = "Delete from tests where id=?";
	private static final String GET_TEST = "Select* from tests where id=?";
	private static final String GET_THEORY_TESTS = "Select * from theory_tests where theory_id=?";
	private static final String GET_COURSE_TESTS = "Select * from course_tests where course_id=?";
	private static TestsManager instance;
	private static final Logger LOG = Logger.getLogger(TestsManager.class);
	DataSource ds;
	/**
	 * 
	 *singleton realisation of accessor to tests
	 */
	public static synchronized TestsManager getInstance() throws DBException{
		if (instance == null) {
			instance = new TestsManager();
		}
		return instance;
	}
	private TestsManager() throws DBException{
		
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/user");
			LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			LOG.error(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_TESTS, ex);
			throw new DBException(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_TESTS, ex);
		}
	}
	public synchronized boolean CreateTest(Test test) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(INSERT_TEST);
			int index=0;
			pstmt.setString(++index, test.getName());
			pstmt.setString(++index, test.getDescription());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.TEST_ERR_CANNOT_CREATE_TEST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean UpdateTest(Test test) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(UPDATE_TEST);
			int index=0;
			pstmt.setString(++index, test.getName());
			pstmt.setString(++index, test.getDescription());
			pstmt.setInt(++index, test.getId());
			int res = pstmt.executeUpdate();
			QuestionsManager qm = QuestionsManager.getInstance();
			for(Question quest:test.getQuestions()){
				qm.UpdateQuestion(quest);
			}
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.TEST_ERR_CANNOT_UPDATE_TEST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean DeleteTest(Test test) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(DELETE_TEST);
			int index=0;
			pstmt.setInt(++index, test.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				for (Question quest:test.getQuestions()) {
					QuestionsManager.getInstance().DeleteQuestion(quest);
				}
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.TEST_ERR_CANNOT_DELETE_TEST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized Test getTest(Test test) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			int index=0;
			pstmt = con.prepareStatement(GET_TEST);
			pstmt.setInt(++index, test.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				test = extractTest(rs);
				test.setQuestions(QuestionsManager.getInstance().getQuestions(test));
				return test;
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			throw new DBException(Messages.TEST_ERR_CANNOT_OBTAIN_TEST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized int getGrade(User user,Test test) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			int index=0;
			pstmt = con.prepareStatement(GET_GRADE);
			pstmt.setString(++index, user.getLogin());
			pstmt.setInt(++index, test.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt("grade");
			} else {
				return 0;
			}
		} catch (SQLException e) {
			throw new DBException(Messages.TEST_ERR_CANNOT_OBTAIN_TEST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized List<Test> getTestsForCourse(Course course) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Test> tests = new LinkedList();
		try {
			int index=0;
			pstmt = con.prepareStatement(GET_COURSE_TESTS);
			pstmt.setInt(++index, course.getId());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Test test = new Test();
				test.setId(rs.getInt("test_id"));
				test = this.getInstance().getTest(test);
				tests.add(test);
			} 
		} catch (SQLException e) {
			throw new DBException(Messages.TEST_ERR_CANNOT_OBTAIN_TEST_LIST_FOR_COURSE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
		return tests;
	}
	public synchronized List<Test> getTestsForTheory(Theorical th) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Test> tests = new LinkedList();
		try {
			int index=0;
			pstmt = con.prepareStatement(GET_THEORY_TESTS);
			pstmt.setInt(++index, th.getId());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Test test = new Test();
				test.setId(rs.getInt("theory_id"));
				test = this.getInstance().getTest(test);
				tests.add(test);
			} 
		} catch (SQLException e) {
			throw new DBException(Messages.TEST_ERR_CANNOT_OBTAIN_TEST_LIST_FOR_THEORICAL_BLOCK, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
		return tests;
	}
	public synchronized boolean CreateTheoryTest(Test test, Theorical th) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(CREATE_THEORY_COURSE);
			int index=0;
			pstmt.setInt(++index, th.getId());
			pstmt.setInt(++index, test.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.TEST_ERR_CANNOT_CREATE_CONNECTION, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean CreateCourseTest(Test test, Course c) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(CREATE_TEST_COURSE);
			int index=0;
			pstmt.setInt(++index, c.getId());
			pstmt.setInt(++index, test.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.TEST_ERR_CANNOT_CREATE_CONNECTION, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean pass(User user,Test test,int grade) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(PASS);
			int index=0;
			pstmt.setString(++index,  user.getLogin());
			pstmt.setInt(++index, test.getId());
			pstmt.setInt(++index, grade);
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.TEST_ERR_CANNOT_CREATE_TEST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	private Test extractTest(ResultSet rs) throws SQLException {
		Test test = new Test();
		test.setId(rs.getInt("id"));
		test.setName(rs.getString("test_id"));
		test.setDescription(rs.getString("description"));
		return test;
	}
}
