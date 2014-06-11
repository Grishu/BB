package com.zeal.peekaboo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.zeal.Vo.FeedsVo;
import com.zeal.peak.obejects.ImageCache.ImageCacheParams;
import com.zeal.peak.obejects.ImageFetcher;
import com.zeal.peak.obejects.RecyclingImageView;
import com.zeal.peak.utils.CommonUtils;

public class Vfsurprise extends Fragment {
	LayoutInflater li;
	ViewFlipper viewFlipper;
	// List<String> data;
	int pos = 0;
	ImageFetcher mImageFetcher;
	private String m_Url, m_result, m_userIdVal, m_sImgVidUrl, m_sHeartUrl,
			m_sLikeUrl, m_heartCount, m_sReportUrl;
	private String profileName, m_hearts, m_likes, m_imageId, m_followerID,
			m_followValue;
	private Bundle m_bundle;
	private ProgressDialog m_prgDialog;
	private Boolean m_status;
	private ArrayList<FeedsVo> m_arrSurpVo;
	private Context m_context;
	private Bitmap m_bitmap;
	private boolean m_isResult = false;
	private TextView m_tvNoData;

	private BackIme m_etSearch;
	private FrameLayout m_flSearchLayout;

	
	private Button m_btnPhoto, m_btnVideo, m_btnUser;
	private InputMethodManager imm;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater
				.inflate(R.layout.vfsurprisefragment, container, false);
		m_context = Vfsurprise.this.getActivity();
		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(),
				"hai");
		imm = (InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
													// app memory

		// The ImageFetcher takes care of loading images into our ImageView
		// children asynchronously
		mImageFetcher = new ImageFetcher(getActivity(), 100);
		mImageFetcher.setLoadingImage(R.drawable.no_image);
		// data = Arrays.asList(Images.imageThumbUrls);

		mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(),
				cacheParams);
		viewFlipper = (ViewFlipper) v.findViewById(R.id.surprise_container);
		m_tvNoData = (TextView) v.findViewById(R.id.vfs_tvNodata);

		m_flSearchLayout=(FrameLayout)v.findViewById(R.id.vfs_flSearch);
		

		m_etSearch = (BackIme) v.findViewById(R.id.vfs_etSearch);
		m_flSearchLayout = (FrameLayout) v.findViewById(R.id.vfs_flSearch);
		m_btnPhoto = (Button) v.findViewById(R.id.vfs_btnPhoto);
		m_btnVideo = (Button) v.findViewById(R.id.vfs_btnVideo);
		m_btnUser = (Button) v.findViewById(R.id.vfs_btnUser);

		m_btnPhoto.setOnClickListener(m_onClickListener);
		m_btnVideo.setOnClickListener(m_onClickListener);
		m_btnUser.setOnClickListener(m_onClickListener);


		viewFlipper.setInAnimation(getActivity(), R.animator.in_from_right);
		viewFlipper.setOutAnimation(getActivity(), R.animator.out_to_left);
		Tabbedmaincontroller.m_ivsetting.setVisibility(View.VISIBLE);
		m_prgDialog = new ProgressDialog(m_context);
		m_bundle = this.getArguments();
		m_userIdVal = m_bundle.getString("user_Id");
		System.err.println("Surprise UserId=====>" + m_userIdVal);
		m_arrSurpVo = new ArrayList<FeedsVo>();


		m_etSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				m_flSearchLayout.setVisibility(View.VISIBLE);
				
			}
		});

		m_etSearch.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				m_flSearchLayout.setVisibility(View.VISIBLE);
				viewFlipper.stopFlipping();
				return false;
			}
		});
		
		m_etSearch.setOnEditTextImeBackListener(new EditTextImeBackListener() {

			@Override
			public void onImeBack(String text) {

				m_flSearchLayout.setVisibility(View.GONE);
				viewFlipper.startFlipping();
				m_etSearch.clearFocus();
			}
		});

		m_etSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					HideSearchLayout();
				}
				return false;
			}
		});

		SurpriseWS m_suprise = new SurpriseWS();
		m_suprise.execute();

		Tabbedmaincontroller.m_ivReport
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						Toast.makeText(m_context, "Report Clicked.",
//								Toast.LENGTH_LONG).show();
						callReportWS m_report = new callReportWS();
						m_report.execute();
					}
				});
		li = inflater;
		Log.e("Child Display", String.valueOf(viewFlipper.getDisplayedChild()));
		// viewFlipper.onFinishTemporaryDetach(new on)
		viewFlipper.getInAnimation().setAnimationListener(
				new Animation.AnimationListener() {
					public void onAnimationStart(Animation animation) {
					}

					public void onAnimationRepeat(Animation animation) {
					}

					public void onAnimationEnd(Animation animation) {
						int a = viewFlipper.getDisplayedChild();
						Log.e("Flipper Child-->",
								String.valueOf(viewFlipper.getDisplayedChild()));

						if (a > 1) {

							if (pos >= m_arrSurpVo.size() - 1) {
								pos = 0;
							} else {
								pos++;
							}

							System.out.println("Position========>" + pos);
							View v1 = li.inflate(R.layout.s_p_item, null);
							ImageView iv = (ImageView) v1
									.findViewById(R.id.Sp_next);
							ImageView iv_heart = (ImageView) v1
									.findViewById(R.id.sp_imageHeart);
							ImageView iv_like = (ImageView) v1
									.findViewById(R.id.sp_imageLike);

							ImageView profimage = (RecyclingImageView) v1
									.findViewById(R.id.sp_image);
							TextView m_tvProfileName = (TextView) v1
									.findViewById(R.id.sp_profile);
							TextView m_tvHeart = (TextView) v1
									.findViewById(R.id.sp_hearts);
							TextView m_tvLikes = (TextView) v1
									.findViewById(R.id.sp_likes);

							profimage.setTag(pos);
							profimage
									.setOnClickListener(new View.OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated method
											// stub
											Intent in = new Intent(
													getActivity(),
													SurpriseFullscreen.class);
											in.putExtra("pos", Integer
													.parseInt(arg0.getTag()
															.toString()));
											// in.putExtra("pos",
											// m_arrSurpVo.get(pos)
											// .getM_imageId());
											startActivity(in);
										}
									});

							if (m_arrSurpVo.get(pos).getM_imageUrl()
									.contains(".jpg")
									|| m_arrSurpVo.get(pos).getM_imageUrl()
											.contains(".png")) {
								m_bitmap = BitmapFactory.decodeFile(m_arrSurpVo
										.get(pos).getM_imageUrl());
								mImageFetcher.loadImage(m_arrSurpVo.get(pos)
										.getM_imageUrl(), profimage);
							} /*
							 * else if (m_arrSurpVo.get(pos).getM_imageUrl()
							 * .contains(".mp4")) { m_bitmap =
							 * ThumbnailUtils.extractThumbnail( ThumbnailUtils
							 * .createVideoThumbnail( m_arrSurpVo .get(pos)
							 * .getM_imageUrl(),
							 * MediaStore.Video.Thumbnails.FULL_SCREEN_KIND),
							 * 50, 50); mImageFetcher.setLoadingImage(m_bitmap);
							 * }
							 */else {
								mImageFetcher.loadImage(R.drawable.no_image,
										profimage);
							}
							System.out.println("Image Flip %%%%%--->"
									+ m_arrSurpVo.get(pos).getM_imageUrl());
							m_tvProfileName.setText(m_arrSurpVo.get(pos)
									.getM_profileName());
							m_tvHeart.setText(m_arrSurpVo.get(pos)
									.getM_hearts());
							m_tvLikes
									.setText(m_arrSurpVo.get(pos).getM_likes());

							// mImageFetcher.loadImage(m_arrSurpVo.get(pos)
							// .getM_imageUrl(), profimage);
							iv.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									viewFlipper.showNext();
									viewFlipper.stopFlipping();
									viewFlipper.setFlipInterval(6000);
									viewFlipper.startFlipping();
								}
							});

							/*
							 * View v2=li.inflate(R.layout.layoutinflation,
							 * null); v2.setBackgroundColor(Color.BLUE);
							 */
							// viewFlipper.addView(v1);
							// viewFlipper.addView(v2);
							// viewFlipper.removeViewAt(a - 2);

						}
					}
				});

		return v;
	}

	/**
	 * Common click listener
	 */
	OnClickListener m_onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.vfs_btnPhoto:

				if (TextUtils.isEmpty(m_etSearch.getText().toString().trim())) {

					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							m_context);

					// Setting Dialog Title
					alertDialog.setTitle(getResources().getString(
							R.string.app_name));
					// Setting Dialog Message
					alertDialog.setMessage("Please enter keyword to search.");
					alertDialog.setNeutralButton("OK", null);
					alertDialog.show();
				} else {
					HideSearchLayout();

					imm.hideSoftInputFromWindow(m_btnPhoto.getWindowToken(), 0);
					startActivity(new Intent(m_context,
							SearchImageActivity.class).putExtra("Search_Tag",
							m_etSearch.getText().toString()).putExtra("PV",
							false));
					m_etSearch.setText("");
				}
				break;
			case R.id.vfs_btnVideo:
				if (TextUtils.isEmpty(m_etSearch.getText().toString().trim())) {

					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							m_context);

					// Setting Dialog Title
					alertDialog.setTitle(getResources().getString(
							R.string.app_name));
					// Setting Dialog Message
					alertDialog.setMessage("Please enter keyword to search.");
					alertDialog.setNeutralButton("OK", null);
					alertDialog.show();
				} else {
					HideSearchLayout();

					imm.hideSoftInputFromWindow(m_btnVideo.getWindowToken(), 0);
					startActivity(new Intent(m_context,
							SearchImageActivity.class).putExtra("Search_Tag",
							m_etSearch.getText().toString()).putExtra("PV",
							true));
					m_etSearch.setText("");
				}
				break;
			case R.id.vfs_btnUser:
				if (TextUtils.isEmpty(m_etSearch.getText().toString().trim())) {

					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							m_context);

					// Setting Dialog Title
					alertDialog.setTitle(getResources().getString(
							R.string.app_name));
					// Setting Dialog Message
					alertDialog.setMessage("Please enter keyword to search.");
					alertDialog.setNeutralButton("OK", null);
					alertDialog.show();
				} else {
					HideSearchLayout();

					imm.hideSoftInputFromWindow(m_btnUser.getWindowToken(), 0);
					startActivity(new Intent(m_context,
							SearchImageActivity.class).putExtra("Search_Tag",
							m_etSearch.getText().toString()).putExtra("User",
							true));
					m_etSearch.setText("");
				}
				break;

			default:
				break;
			}

		}
	};

	private void HideSearchLayout() {
		m_flSearchLayout.setVisibility(View.GONE);
		viewFlipper.startFlipping();
		m_etSearch.clearFocus();
	}

	private void ViewSupriseImages() {
		System.err.println("ViewSuprise Method=====>" + m_arrSurpVo.size());
		for (int i = 0; i < m_arrSurpVo.size(); i++) {

			View v3 = li.inflate(R.layout.s_p_item, null);
			ImageView iv = (ImageView) v3.findViewById(R.id.Sp_next);
			ImageView iv_heart = (ImageView) v3
					.findViewById(R.id.sp_imageHeart);
			ImageView iv_like = (ImageView) v3.findViewById(R.id.sp_imageLike);
			ImageView profimage = (RecyclingImageView) v3
					.findViewById(R.id.sp_image);
			TextView m_tvProfileName = (TextView) v3
					.findViewById(R.id.sp_profile);
			TextView m_tvHeart = (TextView) v3.findViewById(R.id.sp_hearts);
			TextView m_tvLikes = (TextView) v3.findViewById(R.id.sp_likes);

			profimage.setTag(i);
			iv_heart.setTag(i);
			iv_like.setTag(i);

			if (m_arrSurpVo.get(i).getM_imageUrl().contains(".jpg")
					|| m_arrSurpVo.get(i).getM_imageUrl().contains(".png")) {
				m_bitmap = BitmapFactory.decodeFile(m_arrSurpVo.get(i)
						.getM_imageUrl());
				mImageFetcher.loadImage(m_arrSurpVo.get(i).getM_imageUrl(),
						profimage);
			} /*
			 * else if (m_arrSurpVo.get(i).getM_imageUrl().contains(".mp4")) {
			 * m_bitmap = ThumbnailUtils.extractThumbnail(ThumbnailUtils
			 * .createVideoThumbnail(m_arrSurpVo.get(i) .getM_imageUrl(),
			 * MediaStore.Video.Thumbnails.MINI_KIND), 50, 50);
			 * mImageFetcher.setLoadingImage(m_bitmap); }
			 */else {
				mImageFetcher.loadImage(R.drawable.no_image, profimage);
			}

			System.out.println("Image %%%%%--->"
					+ m_arrSurpVo.get(i).getM_imageUrl() + "FollowerID===="
					+ m_arrSurpVo.get(i).getM_followerID());
			m_tvProfileName.setText(m_arrSurpVo.get(i).getM_profileName());
			m_tvHeart.setText(m_arrSurpVo.get(i).getM_hearts());
			m_tvLikes.setText(m_arrSurpVo.get(i).getM_likes());

			profimage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent in = new Intent(getActivity(),
							SurpriseFullscreen.class);
					in.putExtra("pos", Integer.parseInt(v.getTag().toString()));
					in.putParcelableArrayListExtra("parcel", m_arrSurpVo);
					startActivity(in);
				}
			});
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					viewFlipper.showNext();
					viewFlipper.stopFlipping();
					viewFlipper.setFlipInterval(6000);
					viewFlipper.startFlipping();
				}
			});

			m_followValue = String
					.valueOf(m_arrSurpVo.get(i).getM_followerID());
			Log.e("Follow===", m_followerID);
			m_tvProfileName.setTag(Integer.valueOf(m_followValue));
			m_tvProfileName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Tabbedmaincontroller.m_ivback.setVisibility(View.VISIBLE);
					Tabbedmaincontroller.m_ivsetting
							.setVisibility(View.INVISIBLE);
					PeakAboo.m_isSurprise = true;
					FragmentTransaction fm = getFragmentManager()
							.beginTransaction();
					Fragment llf = new OtherProfileFragment();
					Bundle m_bundle = new Bundle();
					m_bundle.putString("Follower_Id",
							String.valueOf(v.getTag()));
					m_bundle.putString("user_Id", m_userIdVal);
					llf.setArguments(m_bundle);
					// fm.addToBackStack("OtherProfile");
					fm.replace(R.id.fragmentswitcherframe, llf);
					fm.commit();
				}
			});

			m_imageId = String.valueOf(m_arrSurpVo.get(i).getM_imageId());
			System.out.println("Image Id------" + m_imageId);
			v3.findViewById(R.id.ll_clk_hrts)
					.setTag(Integer.valueOf(m_imageId));

			v3.findViewById(R.id.ll_clk_hrts).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							viewFlipper.stopFlipping();
							m_heartCount = "";

							callHeartWS m_heartWS = new callHeartWS();
							TextView tvv = (TextView) v
									.findViewById(R.id.sp_hearts);

							m_heartWS.execute(tvv, (Integer) v.getTag());

						}
					});
			v3.findViewById(R.id.sp_ll_like_layout).setTag(
					Integer.valueOf(m_imageId));
			v3.findViewById(R.id.sp_ll_like_layout).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							viewFlipper.stopFlipping();

							callLikeWS m_likeWS = new callLikeWS();
							TextView m_tvLike = (TextView) v
									.findViewById(R.id.sp_likes);
							m_likeWS.execute(m_tvLike, (Integer) v.getTag());

						}
					});
			viewFlipper.addView(v3);
		}
	}

	/**
	 * Call the webservice of heart
	 */
	class callHeartWS extends AsyncTask<String, Integer, String> {
		TextView tv;
		int m_imgId;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		public void execute(TextView tvv, int p_imgId) {
			tv = tvv;
			m_imgId = p_imgId;
			this.execute();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/heart.php?uid=76&pid=5
//			http://192.168.1.193/PeekaBoo_Webservice/like.php?uid=98&pid=38&flag=0
			m_sHeartUrl = CommonUtils.parseJSON("heart.php?uid=" + m_userIdVal
					+ "&pid=" + m_imgId +"&flag=0");
			try {
				JSONObject m_jObj = new JSONObject(m_sHeartUrl);
				m_status = m_jObj.getBoolean("Status");
				m_heartCount = m_jObj.getString("Hearts");
				System.out.println("HEart Count------>" + m_heartCount);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return m_heartCount;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				tv.setText(result);
			}
		}
	}

	/**
	 * Call the webservice of Like
	 */
	class callLikeWS extends AsyncTask<String, Integer, String> {
		TextView tv;
		int m_likeImgid;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		public void execute(TextView p_tvv, int p_likeImgId) {
			// TODO Auto-generated method stub
			tv = p_tvv;
			m_likeImgid = p_likeImgId;
			this.execute();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/like.php?uid=76&pid=552
//			http://192.168.1.193/PeekaBoo_Webservice/like.php?uid=98&pid=38&flag=0
			m_sLikeUrl = CommonUtils.parseJSON("like.php?uid=" + m_userIdVal
					+ "&pid=" + m_likeImgid + "&flag=0");
			try {
				JSONObject m_jObj = new JSONObject(m_sLikeUrl);
				m_status = m_jObj.getBoolean("Status");
				m_heartCount = m_jObj.getString("Likes");
				System.out.println("Like Count------>" + m_heartCount);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return m_heartCount;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				tv.setText(result);
			}
		}
	}

	/**
	 * Call the Surprise Webservice.
	 * 
	 */
	class SurpriseWS extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			m_prgDialog.setTitle("Loading...");
			m_prgDialog.setCancelable(false);
			m_prgDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			m_Url = CommonUtils
					.parseJSON("surprise_tag.php?uid=" + m_userIdVal);
			try {
				JSONObject m_jsonObj = new JSONObject(m_Url);
				m_status = m_jsonObj.getBoolean("Status");
				if (m_status) {
					m_isResult = true;
					JSONArray m_jArry = m_jsonObj.getJSONArray("result");
					FeedsVo m_feedVo;
					for (int i = 0; i < m_jArry.length(); i++) {
						// for (int j = 0; j <= i; j++) {
						// JSONObject jo = m_jArry.getJSONObject(j);
						// JSONArray resultJA = jo.getJSONArray("result");

						// for (int k = 0; k < resultJA.length(); k++) {
						// JSONObject jo_inside = resultJA
						// .getJSONObject(k);

						JSONObject jo_inside = m_jArry.getJSONObject(i);
						// for (int k = 0; k < jo_inside.length(); k++) {
						JSONObject m_resultObj = jo_inside;
						// .getJSONObject("result");
						m_followerID = m_resultObj.getString("UserID");
						profileName = m_resultObj.getString("Profile_Name");

						m_hearts = m_resultObj.getString("Hearts");
						m_likes = m_resultObj.getString("Likes");
						// m_imageId = m_resultObj.getString("ImageID");

						if (m_resultObj.has("ImageID")) {
							m_imageId = m_resultObj.getString("ImageID");
						} else {
							m_imageId = "1";
						}
						// video id
						/*
						 * if (m_resultObj.has("VideoID")) { m_imageId =
						 * m_resultObj.getString("VideoID"); }
						 */

						if (m_resultObj.has("MediaUrl")) {
							m_sImgVidUrl = m_resultObj.getString("MediaUrl");
						}
						// video url
						/*
						 * if (m_resultObj.has("Video_url")) { m_sImgVidUrl =
						 * m_resultObj .getString("Video_url"); }
						 */

						m_feedVo = new FeedsVo();
						m_feedVo.setM_profileName(profileName);
						m_feedVo.setM_imageUrl(m_sImgVidUrl);
						m_feedVo.setM_likes(m_likes);
						m_feedVo.setM_hearts(m_hearts);
						m_feedVo.setM_imageId(Integer.valueOf(m_imageId));
						m_feedVo.setM_followerID(Integer.valueOf(m_followerID));
						m_arrSurpVo.add(m_feedVo);
						System.out.println("Size of Surprise Arraylist-->"
								+ m_arrSurpVo.size());
						// }
					}
				} else {
					m_isResult = false;
					m_result = m_jsonObj.getString("result");
					Log.d("Result", m_result);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (!m_isResult) {
				m_tvNoData.setVisibility(View.VISIBLE);
				viewFlipper.setVisibility(View.INVISIBLE);
				m_tvNoData.setText(result);
			} else {
				m_tvNoData.setVisibility(View.INVISIBLE);
				viewFlipper.setVisibility(View.VISIBLE);
			}
			m_prgDialog.dismiss();
			ViewSupriseImages();
		}
	}

	/**
	 * Call the webservice of Reports
	 */
	class callReportWS extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			m_prgDialog.setTitle("Loading...");
			m_prgDialog.setCancelable(false);
			m_prgDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://zealousys.com/PeekaBoo_Webservice/report.php?uid=76&imgid=27
			m_sReportUrl = CommonUtils.parseJSON("report.php?uid="
					+ m_userIdVal + "&imgid=27");

			try {
				JSONObject m_jObj = new JSONObject(m_sReportUrl);
				Boolean m_status = m_jObj.getBoolean("status");
				if (m_status) {
					Toast.makeText(m_context, m_jObj.getString("result"),
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(m_context, m_jObj.getString("result"),
							Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return m_sReportUrl;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			m_prgDialog.dismiss();
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		mImageFetcher.setPauseWork(false);
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		mImageFetcher.setExitTasksEarly(false);
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mImageFetcher.closeCache();
		super.onDestroy();
	}
	
}
