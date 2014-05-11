package com.test.displaybitmaps.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.GridView;

import com.test.displaybitmaps.R;
import com.test.displaybitmaps.imagemanager.ImageCacheParams;
import com.test.displaybitmaps.imagemanager.ImageFetcher;

public class MainActivity extends FragmentActivity {
	private static final String TAG = "MainActivity";

	private GridView mGridView;

	private int mImageThumbSize;
	private int mImageThumbSpacing;
	private ImageAdapter mAdapter;
	private ImageFetcher mImageFetcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mImageThumbSize = getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_size);
		mImageThumbSpacing = getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_spacing);

		ImageCacheParams cacheParams = new ImageCacheParams(
				getApplicationContext());

		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
													// app memory

		// The ImageFetcher takes care of loading images into our ImageView
		// children asynchronously
		mImageFetcher = new ImageFetcher(getApplicationContext());
		mImageFetcher.setLoadingImage(R.drawable.ic_launcher);
		mImageFetcher.addImageCache(cacheParams);

		// initialize views
		initViews();
	}

	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		super.onPause();
		mImageFetcher.setPauseWork(false);
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mImageFetcher.closeCache();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mImageFetcher.getImageCache().clearMemoryCache();
	}

	/**
	 * Initialize views
	 */
	private void initViews() {
		mGridView = (GridView) findViewById(R.id.gridView);
		mAdapter = new ImageAdapter(getApplicationContext(), mImageFetcher);

		mGridView.setAdapter(mAdapter);
		mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView,
					int scrollState) {
				// Pause fetcher to ensure smoother scrolling when flinging
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					// Before Honeycomb pause image loading on scroll to help
					// with performance
					if (Build.VERSION.SDK_INT < VERSION_CODES.HONEYCOMB) {
						mImageFetcher.setPauseWork(true);
					}
				} else {
					mImageFetcher.setPauseWork(false);
				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

		// This listener is used to get the final width of the GridView and then
		// calculate the
		// number of columns and the width of each column. The width of each
		// column is variable
		// as the GridView has stretchMode=columnWidth. The column width is used
		// to set the height
		// of each view so we get nice square thumbnails.
		mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@TargetApi(VERSION_CODES.JELLY_BEAN)
					@Override
					public void onGlobalLayout() {
						if (mAdapter.getNumColumns() == 0) {
							final int numColumns = (int) Math.floor(mGridView
									.getWidth()
									/ (mImageThumbSize + mImageThumbSpacing));
							if (numColumns > 0) {
								final int columnWidth = (mGridView.getWidth() / numColumns)
										- mImageThumbSpacing;
								mAdapter.setNumColumns(numColumns);
								mAdapter.setItemHeight(columnWidth);

								Log.d(TAG, "onCreateView - numColumns set to "
										+ numColumns);

								if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
									mGridView.getViewTreeObserver()
											.removeOnGlobalLayoutListener(this);
								} else {
									mGridView.getViewTreeObserver()
											.removeGlobalOnLayoutListener(this);
								}
							}
						}
					}
				});
	}
}
