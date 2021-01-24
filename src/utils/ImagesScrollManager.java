package utils;

public class ImagesScrollManager {
	public static int imagesChunkSize = 5;
	
	private boolean isBegin;
	private boolean isEnd;
	private int chunkIndex;

	public int getChunkIndex() {
		return chunkIndex;
	}

	public void setChunkIndex(int chunkIndex) {
		this.chunkIndex = chunkIndex;
	}

	public boolean isBegin() {
		return isBegin;
	}

	public void setBegin(boolean isBegin) {
		this.isBegin = isBegin;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}
	
	public int getImagesChunkSize() {
		return imagesChunkSize;
	}
}
