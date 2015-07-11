package com.pratilipi.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.ProfileLanguageFragmentPagerAdapter;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.widget.SlidingTabLayout;

public class ProfileLanguageFragment extends BaseFragment {

	public static final String TAG_NAME = "Profile Language";

	private View mRootView;
	private SlidingTabLayout mSlidingTabLayout;
	private ViewPager mViewPager;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(
				R.layout.fragment_profile_language_selection, container, false);

		mViewPager = (ViewPager) mRootView
				.findViewById(R.id.profile_language_select_pager);
		mSlidingTabLayout = (SlidingTabLayout) mRootView
				.findViewById(R.id.profile_language_select_slide);

		mViewPager.setAdapter(new ProfileLanguageFragmentPagerAdapter(
				getChildFragmentManager(), mParentActivity));
		if (mParentActivity.mApp.getMenuLanguageLocale().equals(
				PConstants.MENU_LANGUAGE.HINDI.locale)) {
			mSlidingTabLayout.setCustomTabView(
					R.layout.layout_header_tab_red_hindi, 0);
		} else if (mParentActivity.mApp.getMenuLanguageLocale().equals(
				PConstants.MENU_LANGUAGE.TAMIL.locale)) {
			mSlidingTabLayout.setCustomTabView(
					R.layout.layout_header_tab_red_tamil, 0);
		} else if (mParentActivity.mApp.getMenuLanguageLocale().equals(
				PConstants.MENU_LANGUAGE.GUJARATI.locale)) {
			mSlidingTabLayout.setCustomTabView(
					R.layout.layout_header_tab_red_gujarati, 0);
		} else {
			mSlidingTabLayout.setCustomTabView(R.layout.layout_header_tab_red,
					0);
		}
		mSlidingTabLayout.setDistributeEvenly(true);
		mSlidingTabLayout.setViewPager(mViewPager);
		mSlidingTabLayout
				.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

					@Override
					public int getIndicatorColor(int position) {
						return getResources().getColor(R.color.background_red);
					}
				});

		Bundle bundle = getArguments();
		if (bundle != null) {
			int menuType = bundle.getInt("MENU_TYPE", 0);
			mViewPager.setCurrentItem(menuType);
		}

		return mRootView;
	}
}
