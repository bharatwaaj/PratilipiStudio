package com.pratilipi.android.util;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;

public class FontManager {

	private static HashMap<String, Typeface> mTypefaceCache = new HashMap<>(7);

	public static FontManager getInstance() {
		return FontManager.InstanceHolder.INSTANCE;
	}

	public static void initialize(Context context) {

		Typeface type = Typeface.createFromAsset(context.getAssets(),
				"fonts/Montserrat-Regular.ttf");
		mTypefaceCache.put("regular", type);

		type = Typeface.createFromAsset(context.getAssets(),
				"fonts/Montserrat-Light.ttf");
		mTypefaceCache.put("light", type);

		type = Typeface.createFromAsset(context.getAssets(),
				"fonts/Montserrat-Bold.ttf");
		mTypefaceCache.put("bold", type);

		type = Typeface.createFromAsset(context.getAssets(),
				"fonts/Rupee_Foradian.ttf");
		mTypefaceCache.put("rupee", type);

		type = Typeface
				.createFromAsset(context.getAssets(), "fonts/Mangal.ttf");
		mTypefaceCache.put("hindi", type);

		type = Typeface.createFromAsset(context.getAssets(), "fonts/Tamil.ttf");
		mTypefaceCache.put("tamil", type);

		type = Typeface.createFromAsset(context.getAssets(),
				"fonts/Gujarati.ttf");
		mTypefaceCache.put("gujarati", type);

	}

	public Typeface get(String style) {
		Typeface type = mTypefaceCache.get(style);

		if (type == null) {
			type = mTypefaceCache.get(AppState.getInstance()
					.getContentLanguage());
		}

		return type;
	}

	// Making FontManager a singleton class
	private static class InstanceHolder {
		private static final FontManager INSTANCE = new FontManager();
	}
}
