package com.allen.test.tool.hotspots;

import java.util.ArrayList;

import android.net.Uri;
import okhttp3.HttpUrl;

public class WeiboUtil {
	final static String URL = "http://api.weibo.com/search/hot_word.json";
	final static String SID = "t_euicard";
	final static String APP_KEY = "457364138";
	final static String APP_SECRET = "48c9d7fadcdf6851047cf3bd6eee1304";
	final static int COUNT_DEFAULT = -1;

	public static Uri getWeiboUri(String spot) {

		StringBuilder sb = new StringBuilder();
		sb.append("sinaweibo://searchall?q=");
		sb.append(spot);
		sb.append("&luicode=10000360&lfid=leshi_euizhuomian_sousuo_160824");
		return Uri.parse(sb.toString());
	}

	/**
	 * unicode 转字符串
	 */
	public static String unicode2String(String unicode) {

		StringBuffer string = new StringBuffer();

		String[] hex = unicode.split("\\\\u");

		for (int i = 1; i < hex.length; i++) {

			// 转换出每一个代码点
			int data = Integer.parseInt(hex[i], 16);

			// 追加成string
			string.append((char) data);
		}

		return string.toString();
	}

	static class Spots {
		String cat; // create time
		ArrayList<Spot> data; // spots

		public void format() {
			if (data == null)
				return;

			for (Spot spot : data) {
				spot.format();
			}
		}
	}

	static class Spot {
		String word; // title
		int num; // level
		int flag; // 1-new,2-hot 4-burst
		String title;

		public void format() {
			if (word == null)
				return;
			if (word.startsWith("\\")) {
				title = unicode2String(word);
			} else {
				title = word;
			}

		}
	}

	static class ParamsBuilder {
		final static String SOURCE_FIELD = "source";
		final static String SID_FIELD = "sid";
		final static String CAT_FIELD = "cat";
		final static String COUNT_FIELD = "count";

		private String source;
		private String sid;
		private String cat;
		private int count = COUNT_DEFAULT;

		public ParamsBuilder() {
		};

		public ParamsBuilder(String source, String sid, String cat, int count) {
			this.source = source;
			this.sid = sid;
			this.cat = cat;
			this.count = count;
		}

		public ParamsBuilder setSource(String source) {
			this.source = source;
			return this;
		}

		public ParamsBuilder setSid(String sid) {
			this.sid = sid;
			return this;
		}

		public ParamsBuilder setCat(String cat) {
			this.cat = cat;
			return this;
		}

		public ParamsBuilder setCount(int count) {
			this.count = count;
			return this;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			StringBuilder sb = new StringBuilder();
			sb.append(URL);

			sb.append("?");
			sb.append(SOURCE_FIELD + "=");
			if (source != null && source.length() > 0) {
				sb.append(source);
			}

			sb.append("&");
			sb.append(SID_FIELD + "=");
			if (sid != null && sid.length() > 0) {
				sb.append(sid);
			}

			if (cat != null && cat.length() > 0) {
				sb.append("&");
				sb.append(CAT_FIELD + "=");
				sb.append(cat);
			}

			if (count > 0) {
				sb.append("&");
				sb.append(COUNT_FIELD + "=");
				sb.append(count);
			}
			return sb.toString();
		}

		public HttpUrl generateUri() {

			if (sid != null && sid.length() > 0 && source != null && source.length() > 0) {
				return HttpUrl.parse(toString());
			} else {
				return null;
			}
		}

	}
}
