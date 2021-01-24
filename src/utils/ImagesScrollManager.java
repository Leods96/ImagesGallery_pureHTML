package utils;

import java.util.List;

public class ImagesScrollManager {
	public static int imagesChunkSize = 5;
	
	private boolean isBegin;
	private boolean isEnd;
	private int chunkNumber;

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

	public int getChunkNumber() {
		return chunkNumber;
	}

	public void setChunkNumber(int chunkNumber) {
		this.chunkNumber = chunkNumber;
	}
}
