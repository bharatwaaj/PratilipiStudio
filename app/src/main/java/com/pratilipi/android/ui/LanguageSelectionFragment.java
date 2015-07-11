package com.pratilipi.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pratilipi.android.R;
import com.pratilipi.android.util.PConstants;

public class LanguageSelectionFragment extends BaseFragment {

	public static final String TAG_NAME = "Language Selection";

	private View mRootView;
	private Button mHindiButton;
	private Button mTamilButton;
	private Button mGujaratiButton;
	private View mGoButton;

	private PConstants.CONTENT_LANGUAGE mLanguageSelected;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_language_selection,
				container, false);

		mHindiButton = (Button) mRootView.findViewById(R.id.hindi_button);
		mTamilButton = (Button) mRootView.findViewById(R.id.tamil_button);
		mGujaratiButton = (Button) mRootView.findViewById(R.id.gujarati_button);
		mGoButton = mRootView.findViewById(R.id.go_button);

		mLanguageSelected = null;

		mHindiButton.setTag(PConstants.CONTENT_LANGUAGE.HINDI);
		mHindiButton.setOnClickListener(mLanguageSelectionListener);

		mTamilButton.setTag(PConstants.CONTENT_LANGUAGE.TAMIL);
		mTamilButton.setOnClickListener(mLanguageSelectionListener);

		mGujaratiButton.setTag(PConstants.CONTENT_LANGUAGE.GUJARATI);
		mGujaratiButton.setOnClickListener(mLanguageSelectionListener);

		mGoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mLanguageSelected != null) {
					mParentActivity.mApp.setContentLanguage(mLanguageSelected
							.toString());
					mParentActivity.mApp.setContentLanguageId(mLanguageSelected
							.getId());
					mParentActivity.mApp
							.setContentLanguageHashCode(mLanguageSelected
									.getHashCode());

					mParentActivity.mStack.popAll();
					mParentActivity.showNextView(new StoreFragment());
				} else {
					mParentActivity.showError("Please select some language.");
				}
			}
		});

		return mRootView;
	}

	View.OnClickListener mLanguageSelectionListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getTag() != null && !v.getTag().equals(mLanguageSelected)) {

				if (mLanguageSelected != null) {
					Button previousSelection = (Button) mRootView
							.findViewWithTag(mLanguageSelected);
					if (previousSelection != null) {
						previousSelection.setSelected(false);
					}
				}
				mLanguageSelected = (PConstants.CONTENT_LANGUAGE) v.getTag();
				v.setSelected(true);
			}
		}
	};
}
