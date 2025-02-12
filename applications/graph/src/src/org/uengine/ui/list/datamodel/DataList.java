package org.uengine.ui.list.datamodel;

import java.util.ArrayList;

import org.uengine.kernel.GlobalContext;

public class DataList extends ArrayList<DataMap<String, Object>> implements java.io.Serializable {

	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	private int totalPageNo;

	private long totalCount;

	private String errCode;

	public void setTotalPageNo(int totalPageNo) {
		this.totalPageNo = totalPageNo;
	}

	public int getTotalPageNo() {
		return totalPageNo;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	
}
