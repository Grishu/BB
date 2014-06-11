package com.zeal.Vo;

public class FollowerContactVo {

	private String m_profileName, m_imageUrl;
	private int m_userId;
	boolean selected=false;
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

	public String getM_imageUrl() {
		return m_imageUrl;
	}

	public void setM_imageUrl(String m_imageUrl) {
		this.m_imageUrl = m_imageUrl;
	}
	
	public boolean isSelected()  
	{
	   return selected;
	}
	 
	public void setSelected(boolean selected)
	{
	    this.selected = selected;
	}
	 

}
