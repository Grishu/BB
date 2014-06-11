package com.zeal.Vo;

public class ContactVo {
	private String m_sName, m_sEmail;
	boolean selected = false;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getM_sName() {
		return m_sName;
	}

	public void setM_sName(String m_sName) {
		this.m_sName = m_sName;
	}

	public String getM_sEmail() {
		return m_sEmail;
	}

	public void setM_sEmail(String m_sEmail) {
		this.m_sEmail = m_sEmail;
	}

}
