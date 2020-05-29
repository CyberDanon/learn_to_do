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
import ua.nure.mikisha.ltd.db.entity.Test;
import ua.nure.mikisha.ltd.db.entity.Theorical;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.DBException;
import ua.nure.mikisha.ltd.exception.Messages;
import ua.nure.mikisha.ltd.exception.ServerException;

public class TheoryBlocksManager {
	private static final String INSERT_THEORY = "Insert into theories(name, text, course_id) values (?,?,?)";
	private static final String UPDATE_THEORY = "UPDATE theories set name=?, text=? where id=?";
	private static final String DELETE_THEORY = "Delete from theories where id=?";
	private static final String GET_THEORY = "Select * from theories where id=?";
	private static final String GET_THEORY_FOR_COURSE = "Select * from theories where course_id=?";
	private static TheoryBlocksManager instance;
	private static final Logger LOG = Logger.getLogger(TheoryBlocksManager.class);
	DataSource ds;
	/**
	 * 
	 *singleton realisation of accessor to tests
	 */
	public static synchronized TheoryBlocksManager getInstance() throws DBException{
		if (instance == null) {
			instance = new TheoryBlocksManager();
		}
		return instance;
	}
	private TheoryBlocksManager() throws DBException{
		
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/user");
			LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			LOG.error(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_THEORY, ex);
			throw new DBException(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_THEORY, ex);
		}
	}
	public synchronized boolean CreateTheory(Theorical th) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(INSERT_THEORY);
			int index=0;
			pstmt.setString(++index, th.getName());
			pstmt.setString(++index, th.getText());
			pstmt.setInt(++index, th.getCourse_id());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.THEORY_ERR_CANNOT_CREATE_BLOCK, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean UpdateTheory(Theorical th) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(UPDATE_THEORY);
			int index=0;
			pstmt.setString(++index, th.getName());
			pstmt.setString(++index, th.getText());
			pstmt.setInt(++index, th.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.THEORY_ERR_CANNOT_UPDATE_THEORY, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean DeleteTheory(Theorical th) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(DELETE_THEORY);
			int index=0;
			pstmt.setInt(++index, th.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.THEORY_ERR_CANNOT_DELETE_THEORY, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized Theorical getTheory(Theorical th) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			int index=0;
			pstmt = con.prepareStatement(GET_THEORY);
			pstmt.setInt(++index, th.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				th = extractTheory(rs);
				th.setTests(TestsManager.getInstance().getTestsForTheory(th));
				return th;
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			throw new DBException(Messages.THEORY_ERR_CANNOT_OBTAIN_THEORY, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized List<Theorical> getTheoryForCourse(Course cour) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Theorical> th = new LinkedList();
		try {
			int index=0;
			pstmt = con.prepareStatement(GET_THEORY);
			pstmt.setInt(++index, cour.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				th.add(extractTheory(rs));
			} 
			return th;
		} catch (SQLException e) {
			throw new DBException(Messages.THEORY_ERR_CANNOT_OBTAIN_THEORY_LIST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	private Theorical extractTheory(ResultSet rs) throws SQLException {
		Theorical th= new Theorical();
		th.setId(rs.getInt("id"));
		th.setName(rs.getString("name"));
		th.setText(rs.getString("text"));
		th.setCourse_id(rs.getInt("course_id"));
		return th;
	}
}
