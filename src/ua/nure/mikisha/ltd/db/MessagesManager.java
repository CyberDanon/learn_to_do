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
import ua.nure.mikisha.ltd.db.entity.FAQmessage;
import ua.nure.mikisha.ltd.db.entity.Message;
import ua.nure.mikisha.ltd.db.entity.Question;
import ua.nure.mikisha.ltd.db.entity.Test;
import ua.nure.mikisha.ltd.db.entity.User;
import ua.nure.mikisha.ltd.exception.DBException;
import ua.nure.mikisha.ltd.exception.Messages;
import ua.nure.mikisha.ltd.exception.ServerException;

public class MessagesManager {
	private static final String CREATE_FAQ = "Insert int faq(text) values (?)";
	private static final String UPDATE_FAQ = "update faq set answer=? where id=?";
	private static final String FAQ_ANSWERED = "Select * from faq where not answer='answer'";
	private static final String FAQ_UNANSWERED = "Select * from faq where answer = 'answer'";
	private static final Logger LOG = Logger.getLogger(MessagesManager.class);
	private static final String CREATE_MESSAGE = "Insert into messages values(user_login,topic,text,course_id,time) values (?,?,?,?,?)";
	private static final String GET_MESSAGE = "Select * from messages where id=?";
	private static final String GET_USER_MESSAGES = "Select * from messages where user_login=? order by time DESC";
	private static final String DELETE_MESSAGE = "Delete from messages where id=?";
	private static final String DELETE_BAN_NOTIFICATION = "Delete from messages where id=? and topic like'%subs_banned%'";
	private static final String DELETE_BAN_NOTIFICATION_DIRECT = "Delete from messages where id=? and topic like'%subs_banned%' and user_login=?";
	private static final String DELETE_UNBAN_NOTIFICATION = "Delete from messages where id=? and topic like'%subs_unbanned%'";
	private static final String GET_FAQ = "Select * from faq where id=?";
	private static MessagesManager instance;
	
	DataSource ds;
	/**
	 * 
	 *singleton realisation of accessor to questions
	 */
	public static synchronized MessagesManager getInstance() throws DBException{
		if (instance == null) {
			instance = new MessagesManager();
		}
		return instance;
	}
	private MessagesManager() throws DBException{
		
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/user");
			LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			LOG.error(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_MESSAGES, ex);
			throw new DBException(Messages.APP_ERR_UNABLE_TO_ACCESS_TO_MESSAGES, ex);
		}
	}
	public synchronized boolean delete_banned_notif(Course course) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(DELETE_BAN_NOTIFICATION);
			int index=0;
			pstmt.setInt(++index, course.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.MES_ERR_CANNOT_DELETE_MESSAGE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean delete_banned_notif(Course course, User user) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(DELETE_BAN_NOTIFICATION_DIRECT);
			int index=0;
			pstmt.setInt(++index, course.getId());
			pstmt.setString(++index, user.getLogin());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.MES_ERR_CANNOT_DELETE_MESSAGE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean CreateMessage(User user, Course course, String text, String topic) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(CREATE_MESSAGE);
			int index=0;
			pstmt.setString(++index, user.getLogin());
			pstmt.setString(++index, topic);
			pstmt.setString(++index, text);
			pstmt.setInt(++index, course.getId());
			pstmt.setLong(++index, System.currentTimeMillis());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.QUEST_ERR_CANNOT_CREATE_MESSAGE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean CreateFAQ(FAQmessage f) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(CREATE_FAQ);
			int index=0;
			pstmt.setString(++index, f.getText());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.QUEST_ERR_CANNOT_CREATE_MESSAGE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean UpdateFAQ(FAQmessage f) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(CREATE_FAQ);
			int index=0;
			pstmt.setString(++index, f.getAnswer());
			pstmt.setInt(++index, f.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.QUEST_ERR_CANNOT_CREATE_MESSAGE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized boolean DeleteMessage(Message mes) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(DELETE_MESSAGE);
			int index=0;
			pstmt.setInt(++index, mes.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.MES_ERR_CANNOT_DELETE_MESSAGE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized Message getMessage(Message mes) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			int index=0;
			pstmt = con.prepareStatement(GET_MESSAGE);
			pstmt.setInt(++index, mes.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Message message = new Message();
				message = extractMessage(rs);
				return message;
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			throw new DBException(Messages.MES_ERR_CANNOT_OBTAIN_QUESTION, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized FAQmessage getFAQ(FAQmessage mes) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			int index=0;
			pstmt = con.prepareStatement(GET_FAQ);
			pstmt.setInt(++index, mes.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return extractFAQ(rs);
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			throw new DBException(Messages.MES_ERR_CANNOT_OBTAIN_QUESTION, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
	}
	public synchronized List<Message> getMessagesForUser(User user) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Message> messages = new LinkedList();
		try {
			int index=0;
			pstmt = con.prepareStatement(GET_USER_MESSAGES);
			pstmt.setString(++index, user.getLogin());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Message messsage = extractMessage(rs);
				messages.add(messsage);
			} 
		} catch (SQLException e) {
			throw new DBException(Messages.MESSAGE_ERR_CANNOT_OBTAIN_MESSAGES_LIST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
		return messages;
	}
	public synchronized List<FAQmessage> get_aswered_faq() throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<FAQmessage> messages = new LinkedList();
		try {
			int index=0;
			pstmt = con.prepareStatement(FAQ_ANSWERED);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				FAQmessage messsage = extractFAQ(rs);
				messages.add(messsage);
			} 
		} catch (SQLException e) {
			throw new DBException(Messages.MESSAGE_ERR_CANNOT_OBTAIN_MESSAGES_LIST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
		return messages;
	}
	public synchronized List<FAQmessage> get_unaswered_faq() throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		List<User> users = new ArrayList<>();
 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<FAQmessage> messages = new LinkedList();
		try {
			int index=0;
			pstmt = con.prepareStatement(FAQ_UNANSWERED);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				FAQmessage messsage = extractFAQ(rs);
				messages.add(messsage);
			} 
		} catch (SQLException e) {
			throw new DBException(Messages.MESSAGE_ERR_CANNOT_OBTAIN_MESSAGES_LIST, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
		return messages;
	}
	private Message extractMessage(ResultSet rs) throws SQLException {
		Message mes = new Message();
		mes.setId(rs.getInt("id"));
		mes.setUser_login(rs.getString("user_login"));
		mes.setCourse_id(rs.getInt("course_id"));
		mes.setTopic(rs.getString("topic"));
		mes.setText(rs.getString("text"));
		mes.setTime(rs.getLong("time"));
		return mes;
	}
	private FAQmessage extractFAQ(ResultSet rs) throws SQLException {
		FAQmessage mes = new FAQmessage();
		mes.setId(rs.getInt("id"));
		mes.setText(rs.getString("text"));
		mes.setAnswer(rs.getString("answer"));
		return mes;
	}
	public boolean delete_unbanned_notif(Course course) throws DBException, ServerException {
		Connection con = DBUtils.get_conn(ds);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(DELETE_UNBAN_NOTIFICATION);
			int index=0;
			pstmt.setInt(++index, course.getId());
			int res = pstmt.executeUpdate();
			if (res>0) {
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(Messages.MES_ERR_CANNOT_DELETE_MESSAGE, e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
			DBUtils.close(con);
		}
		
	}
}
