package com.zeal.Vo;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchImageVideoVo implements Parcelable {

	private String m_sName, m_sMediaUrl, m_sUserId;

	public String getM_sUserId() {
		return m_sUserId;
	}

	public void setM_sUserId(String m_sUserId) {
		this.m_sUserId = m_sUserId;
	}

	public SearchImageVideoVo() {
	}

	public SearchImageVideoVo(Parcel in) {
		readFromParcel(in);
	}

	public String getM_sName() {
		return m_sName;
	}

	public void setM_sName(String m_sName) {
		this.m_sName = m_sName;
	}

	public String getM_sMediaUrl() {
		return m_sMediaUrl;
	}

	public void setM_sMediaUrl(String m_sMediaUrl) {
		this.m_sMediaUrl = m_sMediaUrl;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(m_sName);
		dest.writeString(m_sMediaUrl);
		dest.writeString(m_sUserId);
	}

	public void readFromParcel(Parcel p_in) {

		m_sName = p_in.readString();
		m_sMediaUrl = p_in.readString();
		m_sUserId = p_in.readString();
	}

	public static final Parcelable.Creator<SearchImageVideoVo> CREATOR = new Parcelable.Creator<SearchImageVideoVo>() {
		public SearchImageVideoVo createFromParcel(Parcel s) {
			return new SearchImageVideoVo(s);
		}

		public SearchImageVideoVo[] newArray(int size) {
			return new SearchImageVideoVo[size];
		}
	};
}
