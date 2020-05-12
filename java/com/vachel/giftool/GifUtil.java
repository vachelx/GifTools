package com.vachel.giftool;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUtil {

	public static boolean isGif(String str) {
		return isEndWid(str, "gif");
	}

	private static boolean isEndWid(String str, String ext) {
		if (str == null || "".equals(str.trim())) {
			return false;
		}

		int position = str.lastIndexOf(".");
		if (position == -1 || (position == str.length() - 1)) {
			return false;
		}
		String suffix = str.substring(position + 1);
		if (ext.equalsIgnoreCase(suffix)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isGif(InputStream in) throws IOException {
		if (!in.markSupported()) {
			throw new IllegalArgumentException("Input stream must support mark");
		}

		byte[] b = new byte[6];

		try {
			in.mark(30);
			in.read(b);
		} finally {
			in.reset();
		}

		return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8' && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
	}

	public static void resizeGif(InputStream in, OutputStream out, int maxWidth, int maxHeight) throws IOException {
		checkParams(in, out, maxWidth, maxHeight);
		//
		GifFrame[] frameList = null;
		try {
			GifDecoder gifDecoder = new GifDecoder();
			int code = gifDecoder.read(in);
			if (code == GifDecoder.STATUS_OK) {//解码成功
				frameList = gifDecoder.getFrames();
			} else if (code == gifDecoder.STATUS_FORMAT_ERROR) {//图片格式不是GIF
			} else {//图片读取失败
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (frameList == null) {
			return;
		}

		AnimatedGifEncoder ge = new AnimatedGifEncoder();
		ge.start(out);
		ge.setRepeat(0);

		int ratio = Math.round(frameList.length * 1.0f / 10);
		if (ratio < 1) {
			ratio = 1;
		}
		for (int i= 0; i< frameList.length; i++){
			if (i % ratio == 0) {
				Bitmap frame = frameList[i].image;
				int delay = frameList[i].delay;
				ge.setDelay(delay * ratio);
				ge.addFrame(frame, true, maxWidth);
			}
		}

		ge.finish();
	}

	private static void checkParams(InputStream in, OutputStream out, int maxWidth, int maxHeight)
			throws IOException {
		if (in == null) {
			throw new IOException("InputStream can not be null ");
		}
		if (out == null) {
			throw new IOException("OutputStream can not be null ");
		}
		if (maxWidth < 1 || maxHeight < 1) {
			throw new IOException("maxWidth or maxHeight can not be less than 1 ");
		}
	}

}
