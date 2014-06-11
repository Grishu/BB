package com.zeal.peak.obejects;

public class DataObject {
	String profilename,imageurl,no_oflikes,no_ofhearts;

	public DataObject(String profilename, String imageurl) {
		super();
		this.profilename = profilename;
		this.imageurl = imageurl;
		this.no_ofhearts="";
		this.no_oflikes="";
	}

	public String getProfilename() {
		return profilename;
	}

	public void setProfilename(String profilename) {
		this.profilename = profilename;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getNo_oflikes() {
		return no_oflikes;
	}

	public void setNo_oflikes(String no_oflikes) {
		this.no_oflikes = no_oflikes;
	}

	public String getNo_ofhearts() {
		return no_ofhearts;
	}

	public void setNo_ofhearts(String no_ofhearts) {
		this.no_ofhearts = no_ofhearts;
	}
	
	

}
