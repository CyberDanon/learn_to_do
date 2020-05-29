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

import ua.nure.mikisha.ltd.db.entity.Answer;
import ua.nure.mikisha.ltd.db.entity.Question;
import ua.nure.mikisha.ltd.db.entity.Test;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.DBException;
import ua.nure.mikisha.ltd.exception.Messages;
import ua.nure.mikisha.ltd.exception.ServerException;

public class QuestionsManager {
	
private static final Logger LOG = Logger.getLogger(QuestionsManager.class);

	private static final String INSERT_QUESTION = "Insert into question(text,test_id,rac) values(?,?,?)";
	private static final String UPDATE_QUESTION = "update question set text=?, rac=? where id=?";
	private static final String DELETE_QUESTION = "Delete from question where id=?";
	private static final String GET_QUESTION = "select * from answers where id=?";
	private static final String GET_QUESTIONS_FOR_TEST = "select * from questions where test_id=?";
	
	private static QuestionsManager instance;
	
	DataSource ds;
	/**
	 * 
	 *singleton realisation of accessor to questions
	 */
	public static synchronized QuestionsManager getInstance() throws DBException{
		if (instance == null) {
			instance = new QuestionsManager();
		}
		return instance;
	}
	private QuestionsManager() throws DBException{
		
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/user");
			LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			LOG.error(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_QUESTIONS, ex);
			throw new DBException(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_QUESTIONS, ex);
		}
	}
	public synchronized boolean CreateQuestion(Question quest) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(INSERT_QUESTION);
			int index=0;
			pstmt.setString(++index, quest.getText());
			pstmt.setInt(++index, quest.getTest());
			pstmt.setInt(++index, quest.getRac());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.QUEST_ERR_CANNOT_CREATE_QUESTION, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean UpdateQuestion(Question quest) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(UPDATE_QUESTION);
			int index=0;
			pstmt.setString(++index, quest.getText());
			pstmt.setInt(++index, quest.getRac());
			pstmt.setInt(++index, quest.getId());
			int res = pstmt.executeUpdate();
			AnswersManager am = AnswersManager.getInstance();
			for(Answer answ:quest.getAnswers()) {
				am.UpdateAnswer(answ);
			}
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.QUEST_ERR_CANNOT_UPDATE_QUESTION, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean DeleteQuestion(Question quest) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(DELETE_QUESTION);
			int index=0;
			pstmt.setInt(++index, quest.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				List<Answer> answers = quest.getAnswers();
				for(Answer answ:answers) {
					AnswersManager.getInstance().DeleteAnswer(answ);
				}
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.QUEST_ERR_CANNOT_DELETE_QUESTION, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized Question getQuestion(Question quest) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			int index=0;
			pstmt = con.prepareStatement(GET_QUESTION);
			pstmt.setInt(++index, quest.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Question question = new Question();
				question = extractQuestion(rs);
				question.setAnswers(AnswersManager.getInstance().getAnswers(question));
				return question;
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			throw new DBException(Messages.QUEST_ERR_CANNOT_OBTAIN_QUESTION, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized List<Question> getQuestions(Test test) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Question> quests = new LinkedList();
		try {
			int index=0;
			pstmt = con.prepareStatement(GET_QUESTIONS_FOR_TEST);
			pstmt.setInt(++index, test.getId());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Question quest = extractQuestion(rs);
				quest = this.getQuestion(quest);
				quests.add(quest);
			} 
			if (quests.size()==0){
				throw new SQLException();
			}
		} catch (SQLException e) {
			throw new DBException(Messages.QUEST_ERR_CANNOT_OBTAIN_QUESTIONS_LIST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
		return quests;
	}
	private Question extractQuestion(ResultSet rs) throws SQLException {
		Question quest = new Question();
		quest.setId(rs.getInt("id"));
		quest.setTest(rs.getInt("test_id"));
		quest.setRac(rs.getInt("rac"));
		quest.setText(rs.getString("text"));
		return quest;
	}
}
