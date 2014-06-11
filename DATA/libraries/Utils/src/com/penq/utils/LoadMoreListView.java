package com.penq.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class LoadMoreListView extends ListView implements OnScrollListener{


	private static final String TAG = "LoadMoreListView";

	/**
	 * Listener that will receive notifications every time the list scrolls.
	 */
	private OnScrollListener mOnScrollListener;
	private LayoutInflater mInflater;
	//public Boolean bbcd=true;

	// footer view
	private RelativeLayout mFooterView;
	// private TextView mLabLoadMore;
	private ProgressBar mProgressBarLoadMore;

	// Listener to process load more items when user reaches the end of the list
	private OnLoadMoreListener mOnLoadMoreListener;
	// To know if the list is loading more items
	private boolean mIsLoadingMore = false;
	private int mCurrentScrollState;

	
	
	

	public LoadMoreListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		mInflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

/*// footer
mFooterView = (RelativeLayout) mInflater.inflate(
		R.layout.loadmorefooter, this, false);

 * mLabLoadMore = (TextView) mFooterView
 * .findViewById(R.id.load_more_lab_view);
 
mProgressBarLoadMore = (ProgressBar) mFooterView
		.findViewById(R.id.load_more_progressBar);*/

//add(mFooterView);

super.setOnScrollListener(this);
		
	}

	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}
		if (mOnLoadMoreListener != null) {

			/*if (visibleItemCount == totalItemCount) {
				mProgressBarLoadMore.setVisibility(View.GONE);
				// mLabLoadMore.setVisibility(View.GONE);
				return;
			}*/
			boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

			if (!mIsLoadingMore && loadMore
					&& mCurrentScrollState != SCROLL_STATE_IDLE) {
				//mProgressBarLoadMore.setVisibility(View.VISIBLE);
				// mLabLoadMore.setVisibility(View.VISIBLE);
				mIsLoadingMore = true;
				onLoadMore();
			}

		}
		
	}
	

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		// TODO Auto-generated method stub
		mOnScrollListener = l;
	}
	
	
	
	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		mOnLoadMoreListener = onLoadMoreListener;
	}
	
	
	public void onLoadMore() {
		Log.d(TAG, "onLoadMore");
		if (mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onLoadMore();
		}
	}
	public void onLoadMoreComplete() {
		mIsLoadingMore = false;
		//mProgressBarLoadMore.setVisibility(View.GONE);
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		mCurrentScrollState = scrollState;

		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
		
	}
	public interface OnLoadMoreListener {
		/**
		 * Called when the list reaches the last item (the last item is visible
		 * to the user)
		 */
		public void onLoadMore();
	}
}
