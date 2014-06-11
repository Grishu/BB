package com.zeal.Vo;

import android.os.Parcel;
import android.os.Parcelable;

public class FeedsVo implements Parcelable {
	public String m_profileName, m_imageUrl, m_hearts, m_likes, m_videoUrl;
	public int m_imageId, m_followerID;

	public boolean m_isCheckedPosition;

	public FeedsVo() {
	}

	public FeedsVo(Parcel in) {
		readFromParcel(in);
	}

	public int getM_followerID() {
		return m_followerID;
	}

	public void setM_followerID(int m_followerID) {
		this.m_followerID = m_followerID;
	}

	public String getM_videoUrl() {
		return m_videoUrl;
	}

	public void setM_videoUrl(String m_videoUrl) {
		this.m_videoUrl = m_videoUrl;
	}

	public boolean isM_isCheckedPosition() {
		return m_isCheckedPosition;
	}

	public void setM_isCheckedPosition(boolean m_isCheckedPosition) {
		this.m_isCheckedPosition = m_isCheckedPosition;
	}

	public int getM_imageId() {
		return m_imageId;
	}

	public void setM_imageId(int m_imageId) {
		this.m_imageId = m_imageId;
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

	public String getM_hearts() {
		return m_hearts;
	}

	public void setM_hearts(String m_hearts) {
		this.m_hearts = m_hearts;
	}

	public String getM_likes() {
		return m_likes;
	}

	public void setM_likes(String m_likes) {
		this.m_likes = m_likes;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(m_profileName);
		dest.writeString(m_imageUrl);
		dest.writeString(m_hearts);
		dest.writeString(m_likes);
		dest.writeInt(m_imageId);
		dest.writeInt(m_followerID);
	}

	public void readFromParcel(Parcel p_in) {

		this.m_profileName = p_in.readString();
		this.m_imageUrl = p_in.readString();
		this.m_hearts = p_in.readString();
		this.m_likes = p_in.readString();
		this.m_imageId = p_in.readInt();
		this.m_followerID=p_in.readInt();
	}

	/*
	 * public static final Creator<FeedsVo> CREATOR1 = new Creator<FeedsVo>() {
	 * 
	 * public FeedsVo createFromParcel(Parcel source) { return new
	 * FeedsVo(source); }
	 * 
	 * public FeedsVo[] newArray(int size) { return new FeedsVo[size]; } };
	 */

	public static final Parcelable.Creator<FeedsVo> CREATOR = new Parcelable.Creator<FeedsVo>() {
		public FeedsVo createFromParcel(Parcel s) {
			return new FeedsVo(s);
		}

		public FeedsVo[] newArray(int size) {
			return new FeedsVo[size];
		}
	};
}
