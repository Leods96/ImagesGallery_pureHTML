package utils;

import java.util.List;
import beans.ImageBean;

public interface ChunkExtractor {
	static public List<ImageBean> extractChunk(List<ImageBean> images, int startIndex, int endIndex) {
		int cs = ImagesScrollManager.imagesChunkSize;
		if(images.size() < cs) {
			return images;
		}
		if(endIndex >= images.size()) {
			return images.subList(images.size() - cs, images.size());
		}
		if(startIndex <= 0) {
			return images.subList(0, cs);
		}
		return images.subList(startIndex, endIndex);
	}
}
