package ua.nure.mikisha.ltd.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.db.entity.Answer;
import ua.nure.mikisha.ltd.db.entity.Question;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.DBException;
import ua.nure.mikisha.ltd.exception.Messages;
import ua.nure.mikisha.ltd.exception.ServerException;

public class AnswersManager {
	private static AnswersManager instance;
	private static final Logger LOG = Logger.getLogger(AnswersManager.class);
	private static final String INSERT_ANSWER = "insert into answers(text,question_id,right) values (?,?,?)";
	private static final String UPDATE_ANSWER = "update answers set text=?, right=? where id=?";
	private static final String DELETE_ANSWER = "delete from answers where id=?";
	private static final String GET_ANSWER = "select * from answers where id=?";
	private static final String GET_QUESTION_ANSWERS="select * from answers where question_id=?";
	DataSource ds; 
	/**
	 * 
	 *singleton realisation of accessor to answers
	 */
	public static synchronized AnswersManager getInstance() throws DBException{
		if (instance == null) {
			instance = new AnswersManager();
		}
		return instance;
	}
	private AnswersManager() throws DBException{
		
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/user");
			LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			LOG.error("", ex);
			throw new DBException(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_ANSWERS, ex);
		}
	}
	public synchronized boolean CreateAnswer(Answer answ) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(INSERT_ANSWER);
			int index=0;
			pstmt.setString(++index, answ.getText());
			pstmt.setInt(++index, answ.getQuestion());
			pstmt.setInt(++index, answ.getRight());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.ANSW_ERR_CANNOT_CREATE_ANSWER, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean UpdateAnswer(Answer answ) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(UPDATE_ANSWER);
			int index=0;
			pstmt.setString(++index, answ.getText());
			pstmt.setInt(++index, answ.getRight());
			pstmt.setInt(++index, answ.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.ANSW_ERR_CANNOT_UPDATE_ANSWER, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean DeleteAnswer(Answer answ) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(DELETE_ANSWER);
			int index=0;
			pstmt.setInt(++index, answ.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.ANSW_ERR_CANNOT_DELETE_ANSWER, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized Answer getAnswer(Answer answ) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			int index=0;
			pstmt = con.prepareStatement(GET_ANSWER);
			pstmt.setInt(++index, answ.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return extractAnswer(rs);
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			throw new DBException(Messages.ANSW_ERR_CANNOT_OBTAIN_ANSWERS_LIST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized List<Answer> getAnswers(Question quest) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Answer> answers = new LinkedList();
		try {
			int index=0;
			pstmt = con.prepareStatement(GET_QUESTION_ANSWERS);
			pstmt.setInt(++index, quest.getId());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				answers.add(extractAnswer(rs));
			} 
			if (answers.size()==0){
				throw new SQLException();
			}
		} catch (SQLException e) {
			throw new DBException(Messages.ANSW_ERR_CANNOT_OBTAIN_ANSWERS_LIST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
		return answers;
	}
	private Answer extractAnswer(ResultSet rs) throws SQLException {
		Answer answ = new Answer();
		answ.setId(rs.getInt("id"));
		answ.setQuestion(rs.getInt("question_id"));
		answ.setRight(rs.getInt("right"));
		answ.setText(rs.getString("text"));
		return answ;
	}
	
}
