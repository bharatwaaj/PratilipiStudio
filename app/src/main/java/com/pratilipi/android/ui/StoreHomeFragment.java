package com.pratilipi.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratilipi.android.R;
import com.pratilipi.android.adapter.StoreHomeAdapter;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.model.StoreContent;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.util.StoreHomeDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreHomeFragment extends BaseFragment {

	public static final String TAG_NAME = "Store Home";
	private static List<StoreContent> mList = new ArrayList<>();
	public static String mLanguage;

	private View mRootView;
	private ListView mListView;
	private View mProgressBar;
	private StoreHomeAdapter mAdapter;
    private StoreHomeDataSource mDataSource;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_store_home, container,
				false);

		mListView = (ListView) mRootView.findViewById(R.id.list_view);
		mProgressBar = mRootView.findViewById(R.id.progress_bar);

		mAdapter = new StoreHomeAdapter(mParentActivity,
				R.layout.layout_store_home_list_view_item, mList);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();

		if (mList.size() == 0
				|| !mParentActivity.mApp.getContentLanguage().equals(mLanguage)) {
            mList.clear();
            mLanguage = mParentActivity.mApp.getContentLanguage();
            mDataSource = new StoreHomeDataSource(mParentActivity);
            mDataSource.open();
            String content = mDataSource.getContent(mLanguage);
            if (content != null) {
                Gson gson = new Gson();
                List<StoreContent> list = gson.fromJson(content, new TypeToken<List<StoreContent>>() {
                }.getType());
                mList.addAll(list);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                requestStoreHomeTopContent();
            }
		} else {
            mListView.setVisibility(View.VISIBLE);
		}

		return mRootView;
	}

	private void requestStoreHomeTopContent() {
		HttpGet storeHomeListingsRequest = new HttpGet(this,
				PConstants.STORE_TOP_CONTENT_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.STORE_TOP_CONTENT_URL);
		requestHashMap.put("languageId",
				mParentActivity.mApp.getContentLanguageHashCode());

		storeHomeListingsRequest.run(requestHashMap);
	}

	@Override
	public Boolean setPostStatus(JSONObject finalResult, String postUrl,
			int responseCode) {
		return null;
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		if (PConstants.STORE_TOP_CONTENT_URL.equals(getUrl)) {
			if (finalResult != null) {
				try {
					JSONObject responseObject = finalResult
							.getJSONObject("response");
					if (responseObject != null) {
						JSONArray listingArray = responseObject
								.getJSONArray("elements");
						if (listingArray != null) {
							for (int i = 0; i < listingArray.length(); i++) {
								JSONObject listingObject = listingArray
										.getJSONObject(i);
								String name = listingObject.getString("name");
								String id = listingObject.getString("id");
								JSONArray contentArray = listingObject
										.getJSONArray("content");
								List<Book> content = new ArrayList<>();
								for (int j = 0; j < contentArray.length(); j++) {
									Book book = new Book(
											contentArray.getJSONObject(j));
									content.add(book);
								}
								StoreContent storeListing = new StoreContent(
										id, name, content);
								mList.add(storeListing);
							}
						}
					}
                    Gson gson = new Gson();
                    mDataSource.createStoreHomeContent(gson.toJson(mList), mLanguage);
					mProgressBar.setVisibility(View.GONE);
					mAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

    @Override
    public void onResume() {
        if (mDataSource != null) {
            mDataSource.open();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mDataSource != null) {
            mDataSource.close();
        }
        super.onPause();
    }

	@Override
	public void onDestroy() {
		if (mList != null && mList.size() > 0) {
			mList.clear();
		}
		super.onDestroy();
	}

}
