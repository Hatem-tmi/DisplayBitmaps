package com.test.displaybitmaps.imagemanager;

/**
 * ImageSize used to download images by needed size
 * 
 * 
 * @author "Hatem Toumi"
 */
public enum ImageSize {

	xxlarge {
		@Override
		public int getSize() {
			return 800;
		}
	},
	xlarge {
		@Override
		public int getSize() {
			return 500;
		}
	},
	large {
		@Override
		public int getSize() {
			return 300;
		}
	},
	medium {
		@Override
		public int getSize() {
			return 150;
		}
	},
	small {
		@Override
		public int getSize() {
			return 100;
		}
	},
	thumbnail {
		@Override
		public int getSize() {
			return 80;
		}
	};

	public abstract int getSize();
}