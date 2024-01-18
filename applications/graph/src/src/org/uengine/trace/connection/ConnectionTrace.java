package org.uengine.trace.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ConnectionTrace {
	
	public ConnectionTrace() {
	}

	public ConnectionTrace(Connection connection, StackTraceElement[] stackTraceElements, Date connectedDate) {
		this.connection = connection;
		this.stackTraceElements = stackTraceElements;
		this.connectedDate = connectedDate;
	}

	private Connection connection;
	private StackTraceElement[] stackTraceElements;
	private Date connectedDate;

	public String getStackTraceElementsToString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 2; i < getStackTraceElements().length; i++) {
			StackTraceElement ste = getStackTraceElements()[i];
			if (ste.getClassName().equals("javax.servlet.http.HttpServlet")) {
				break;
			}
			sb.append(ste.getClassName() + "." + ste.getMethodName() + "(" + ste.getFileName() + ":" + ste.getLineNumber() + ")").append("<br/>");
		}
		return sb.toString();
	}
	
	
	public StackTraceElement[] getStackTraceElements() {
		return stackTraceElements;
	}

	public void setStackTraceElements(StackTraceElement[] stackTraceElements) {
		this.stackTraceElements = stackTraceElements;
	}
	
	public Date getConnectedDate() {
		return connectedDate;
	}
	
	public String getConnectedDateToString() {
		if (connectedDate != null) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(connectedDate);
		}
		return null;
	}

	public void setConnectedDate(Date connectedDate) {
		this.connectedDate = connectedDate;
	}

	public boolean isClosed() {
		try {
			return this.getConnection().isClosed();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
