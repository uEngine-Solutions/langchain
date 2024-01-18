/*
 * Created on 2004. 12. 15.
 */
package org.uengine.util.dao;

import java.sql.Connection;
import java.util.Date;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.uengine.kernel.GlobalContext;
import org.uengine.trace.connection.ConnectionTrace;
import org.uengine.trace.connection.ConnectionTraceContext;

/**
 * @author Jinyoung Jang
 */
public class DefaultConnectionFactory implements ConnectionFactory {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public static Class USE_CLASS = null;
	
	protected DefaultConnectionFactory(){}

	public Connection getConnection() throws Exception{
		Connection conn = this.getDataSource().getConnection();
		if ("true".equalsIgnoreCase(GlobalContext.getPropertyString("trace.connection"))) {
			ConnectionTraceContext.getInstatnce().getConnectionTraces().add(new ConnectionTrace(conn, Thread.currentThread().getStackTrace(), new Date()));
		}
		return conn; 
	}
	
	public DataSource getDataSource() {
		DataSource ds = null;
		try {
			InitialContext ctx = new InitialContext();
			ds = (DataSource) ctx.lookup(GlobalContext.DATASOURCE_JNDI_NAME);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return ds;
	}
	
	public static DefaultConnectionFactory create() {
		if(USE_CLASS==null){
			try{
				USE_CLASS = Class.forName(GlobalContext.getPropertyString("defaultconnectionfactory.class"));
			}catch(Exception e){
				USE_CLASS = DefaultConnectionFactory.class;
			}
		}
		
		try {
			return (DefaultConnectionFactory) USE_CLASS.newInstance();
		} catch (Exception e) {
			return null;
		}
	}

}
