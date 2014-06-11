package com.zeal.Vo;

public class LookAroundVo {

	private String m_profileName;
	private String m_ImageUrl;
	private String m_distance,m_statusMesg;
	public String getM_statusMesg() {
		return m_statusMesg;
	}
	public void setM_statusMesg(String m_statusMesg) {
		this.m_statusMesg = m_statusMesg;
	}
	private int m_userId;
	
	public int getM_userId() {
		return m_userId;
	}
	public void setM_userId(int m_userId) {
		this.m_userId = m_userId;
	}
	public String getM_profileName() {
		return m_profileName;
	}
	public void setM_profileName(String m_profileName) {
		this.m_profileName = m_profileName;
	}
	public String getM_ImageUrl() {
		return m_ImageUrl;
	}
	public void setM_ImageUrl(String m_ImageUrl) {
		this.m_ImageUrl = m_ImageUrl;
	}
	public String getM_distance() {
		return m_distance;
	}
	public void setM_distance(String m_distance) {
		this.m_distance = m_distance;
	}
}
