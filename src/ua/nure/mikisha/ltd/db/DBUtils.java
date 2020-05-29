package ua.nure.mikisha.ltd.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ua.nure.mikisha.ltd.exception.DBException;
import ua.nure.mikisha.ltd.exception.Messages;
import ua.nure.mikisha.ltd.exception.ServerException;


public final class DBUtils {
	DataSource datasource;
	static DBUtils instance;
	private static final Logger LOG = Logger.getLogger(DBUtils.class);
	public static synchronized DBUtils getInstance() throws DBException{
		if (instance == null) {
			instance = new DBUtils();
		}
		return instance;
	}
	private DBUtils() throws DBException{
		
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			datasource = (DataSource) envContext.lookup("jdbc/user");
			LOG.trace("Data source ==> " + datasource);
		} catch (NamingException ex) {
			LOG.error(Messages.DB_ERR_CANNOT_ACCESS_TO_SOURCE, ex);
			throw new DBException(Messages.DB_ERR_CANNOT_ACCESS_TO_SOURCE, ex);
		}
	}
	public static void close(Connection con) throws ServerException {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				throw new ServerException(Messages.SERV_ERR_CANNOT_CLOSE_CONNECTION, e);
			}
		}
	}

	public static void close(Statement stmt) throws ServerException {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				throw new ServerException(Messages.SERV_ERR_CANNOT_CLOSE_OBJECT, e);
			}
		}
	}

	public static void close(ResultSet rs) throws ServerException {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new ServerException(Messages.SERV_ERR_CANNOT_CLOSE_OBJECT, e);
			}
		}
	}

	public static void rollback(Connection con) throws ServerException {
		if (con != null) {
			try {
				con.rollback();
			} catch (SQLException e) {
				throw new ServerException(Messages.SERV_ERR_CANNOT_CLOSE_OBJECT, e);
			}
		}
	}
	public static Connection get_conn(DataSource ds) throws DBException {
		try {
			return getInstance().datasource.getConnection();
		} catch(SQLException e) {
			throw new DBException(Messages.SERV_ERR_CANNOT_CLOSE_OBJECT, e);
		}
	}
}
