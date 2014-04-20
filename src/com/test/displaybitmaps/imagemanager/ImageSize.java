package com.test.displaybitmaps.imagemanager;

/**
 * ImageSize used to download images by needed size
 * 
 * 
 * @author "Hatem Toumi"
 */
public enum ImageSize {

	magazine {
		@Override
		public int getSize() {
			return 800;
		}
	},
	xlarge {
		@Override
		public int getSize() {
			return 300;
		}
	},
	large {
		@Override
		public int getSize() {
			return 200;
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