package org.uengine.kernel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class RoleHistory implements Serializable {

	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	private Date date;
	private List<String> resourceNames;
	private List<String> endpoints;

	public RoleHistory() {
	}

	public RoleHistory(Date date, List<String> endpoints, List<String> resourceNames) {
		this.date = date;
		this.endpoints = endpoints;
		this.resourceNames = resourceNames;
	}

	public List<String> getResourceNames() {
		return resourceNames;
	}

	public void setResourceNames(List<String> resourceNames) {
		this.resourceNames = resourceNames;
	}

	public String getDateStr() {
		if (date != null) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
		}
		return null;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<String> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<String> endpoints) {
		this.endpoints = endpoints;
	}
	
	public String getToMappingStr() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < endpoints.size(); i++) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(resourceNames.get(i)).append("/").append(endpoints.get(i));
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
