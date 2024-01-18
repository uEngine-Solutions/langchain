package org.uengine.trace.connection;

import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.uengine.kernel.GlobalContext;

public class ConnectionTraceServlet extends HttpServlet {
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	private boolean isTrace;
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		this.isTrace = "true".equalsIgnoreCase(servletConfig.getInitParameter("isTrace"));
		GlobalContext.getProperties().put("trace.connection", String.valueOf(isTrace));
		
		if (isTrace) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(10 * 1000);
							Iterator<ConnectionTrace> itr = ConnectionTraceContext.getInstatnce().getConnectionTraces().iterator();
							while (itr.hasNext()) {
								ConnectionTrace ct = itr.next();
								if (ct.getConnection() == null || ct.isClosed()) {
									itr.remove();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();	
		}
	}

	@Override
	public void destroy() {
		if (isTrace) {
			try {
				Iterator<ConnectionTrace> itr = ConnectionTraceContext.getInstatnce().getConnectionTraces().iterator();
				while (itr.hasNext()) {
					ConnectionTrace ct = itr.next();
					if (ct.getConnection() != null && !ct.isClosed()) {
						ct.getConnection().close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
