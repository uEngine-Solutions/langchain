/*
 * Created on 2004. 11. 3.
 */
package org.uengine.persistence.dao;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.UEngineException;
import org.uengine.persistence.DAOType;
import org.uengine.persistence.processinstance.ProcessInstanceDAO;
import org.uengine.persistence.processvariable.ProcessVariableDAO;
import org.uengine.util.UEngineUtil;
import org.uengine.util.dao.ConnectionFactory;
import org.uengine.util.dao.ConnectiveDAO;

/**
 * @author Jinyoung Jang
 * @author Jong-uk Jeong
 */

public abstract class DAOFactory{
	
	public static Object USE_CLASS_OBJECT;
	
	ConnectionFactory connectionFactory;
		public ConnectionFactory getConnectionFactory() {
			return connectionFactory;
		}
		public void setConnectionFactory(ConnectionFactory factory) {
			connectionFactory = factory;
		}

	abstract public WorkListDAO findWorkListDAOByEndpoint(Map options) throws Exception;
	abstract public WorkListDAO findWorkListDAOByTaskId(Map options) throws Exception;
	abstract public WorkListDAO createWorkListDAOForInsertCall(Map options) throws Exception;
	abstract public WorkListDAO createWorkListDAOForUpdate(Map options) throws Exception;
	abstract public KeyGeneratorDAO createKeyGenerator(String forWhat, Map options) throws Exception;
	abstract public ProcessInstanceDAO createProcessInstanceDAOForArchive() throws Exception;
	abstract public ProcessVariableDAO findProcessVariableDAOByInstanceId() throws Exception;
	abstract public Calendar getNow() throws Exception;

	abstract public String getSequenceSql(String seqName) throws Exception;
	
	abstract public String getDBMSProductName() throws Exception;
	abstract public String getSystemDateFunction() throws Exception;

	public static DAOFactory getInstance(ConnectionFactory tc) {
		if (USE_CLASS_OBJECT == null) {
			String property = GlobalContext.getPropertyString("daofactory.class");
			try {
				USE_CLASS_OBJECT = Class.forName(property);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		if (USE_CLASS_OBJECT != null) {
			try {
				DAOFactory daoFactory = (DAOFactory) ((Class<?>) USE_CLASS_OBJECT).newInstance();
				daoFactory.setConnectionFactory(tc);
				return daoFactory;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return null;
	}
	
	/**
	 * @deprecated DAOFactory should be provided with ConnectionFactory.
	 */

	public static DAOFactory getInstance() {
		return getInstance(null);
	}

	protected Object create(Class whatKind, String sqlStmt) throws Exception {
		return ConnectiveDAO.createDAOImpl(getConnectionFactory(), sqlStmt, whatKind);
	}
	
	protected static final Map<Class<DAOType>, Class<DAOType>> cachedClassTypes = new ConcurrentHashMap<Class<DAOType>, Class<DAOType>>(); 

	@SuppressWarnings("unchecked")
	public Class<DAOType> getDAOTypeClass(Class<DAOType> clsType) throws UEngineException {
		Class<DAOType> clazz = cachedClassTypes.get(clsType);
		if (clazz == null) {
			try {
				clazz = (Class<DAOType>) Class.forName(clsType.getPackage().getName() + "." + getDBMSProductName() + UEngineUtil.getClassNameOnly(clsType));
				cachedClassTypes.put(clsType, clazz);
			} catch (ClassNotFoundException e) {
				clazz = clsType;
				cachedClassTypes.put(clsType, clazz);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return clazz;
	}
	
}
