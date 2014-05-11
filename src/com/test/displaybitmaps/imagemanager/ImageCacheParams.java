package com.test.displaybitmaps.imagemanager;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap.CompressFormat;

/**
 * A holder class that contains cache parameters.
 */
public class ImageCacheParams {

	private static final String DISK_CACHE_DIR = "images_cache";

	// Default memory cache size in kilobytes
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5; // 5MB

	// Default disk cache size in bytes
	private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB

	// Compression settings when writing images to disk cache
	private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
	private static final int DEFAULT_COMPRESS_QUALITY = 70;

	// Constants to easily toggle various caches
	private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
	private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
	private static final boolean DEFAULT_INIT_DISK_CACHE_ON_CREATE = false;

	public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
	public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
	public File diskCacheDir;
	public CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
	public int compressQuality = DEFAULT_COMPRESS_QUALITY;
	public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
	public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
	public boolean initDiskCacheOnCreate = DEFAULT_INIT_DISK_CACHE_ON_CREATE;

	/**
	 * Create a set of image cache parameters that can be provided to
	 * {@link ImageCache#getInstance(android.support.v4.app.FragmentManager, ImageCacheParams)}
	 * or
	 * {@link ImageWorker#addImageCache(android.support.v4.app.FragmentManager, ImageCacheParams)}
	 * .
	 * 
	 * @param context
	 *            A context to use. A unique sub-directory name that will be
	 *            appended to the application cache directory. Usually "cache"
	 *            or "images" is sufficient.
	 */
	public ImageCacheParams(Context context) {
		diskCacheDir = ImageCache.getDiskCacheDir(context, DISK_CACHE_DIR);
	}

	/**
	 * Sets the memory cache size based on a percentage of the max available VM
	 * memory. Eg. setting percent to 0.2 would set the memory cache to one
	 * fifth of the available memory. Throws {@link IllegalArgumentException} if
	 * percent is < 0.01 or > .8. memCacheSize is stored in kilobytes instead of
	 * bytes as this will eventually be passed to construct a LruCache which
	 * takes an int in its constructor.
	 * 
	 * This value should be chosen carefully based on a number of factors Refer
	 * to the corresponding Android Training class for more discussion:
	 * http://developer.android.com/training/displaying-bitmaps/
	 * 
	 * @param percent
	 *            Percent of available app memory to use to size memory cache
	 */
	public void setMemCacheSizePercent(float percent) {
		if (percent < 0.01f || percent > 0.8f) {
			throw new IllegalArgumentException(
					"setMemCacheSizePercent - percent must be "
							+ "between 0.01 and 0.8 (inclusive)");
		}
		memCacheSize = Math.round(percent * Runtime.getRuntime().maxMemory()
				/ 1024);
	}
}
