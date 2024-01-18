package org.uengine.trace.connection;

import java.util.ArrayList;
import java.util.List;

public class ConnectionTraceContext {

	// Collections.synchronizedList(new ArrayList<ConnectionTrace>());
	private List<ConnectionTrace> connectionTraces = new ArrayList<ConnectionTrace>();
	public List<ConnectionTrace> getConnectionTraces() {
		return connectionTraces;
	}

	private ConnectionTraceContext() {
	}

	private static class ConnectionTraceContextSingletonHolder {
		static final ConnectionTraceContext single = new ConnectionTraceContext();
	}

	public static ConnectionTraceContext getInstatnce() {
		return ConnectionTraceContextSingletonHolder.single;
	}

}
