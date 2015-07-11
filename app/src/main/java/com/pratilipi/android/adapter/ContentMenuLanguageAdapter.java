package com.pratilipi.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.FontManager;

public class ContentMenuLanguageAdapter extends ArrayAdapter<Integer> {

	private Context context;
	private int resource;
	private int selectedItem;

	public ContentMenuLanguageAdapter(Context context, int resource,
			Integer[] objects) {
		super(context, resource, objects);
		this.context = context;
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(resource,
					parent, false);
		}

		int item = getItem(position);
		((TextView) convertView)
				.setText(context.getResources().getString(item));
		if (position == selectedItem) {
			((TextView) convertView).setCompoundDrawablesWithIntrinsicBounds(
					null, null,
					context.getResources().getDrawable(R.drawable.arrow_right),
					null);
		} else {
			((TextView) convertView).setCompoundDrawablesWithIntrinsicBounds(
					null, null, null, null);
		}
		((TextView) convertView).setTypeface(FontManager.getInstance().get(
				AppState.getInstance().getMenuLanguageTypeface()));

		return convertView;
	}

	public void setSelectedItem(int position) {
		selectedItem = position;
	}
}
