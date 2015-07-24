package com.pratilipi.android.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratilipi.android.R;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.model.ShelfContent;
import com.pratilipi.android.ui.ReaderActivity;
import com.pratilipi.android.ui.SplashActivity;
import com.pratilipi.android.util.FontManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ShelfContentAdapter extends ArrayAdapter<ShelfContent> {

	SplashActivity activity;
	int resource;

	static class ViewHolder {
		ImageView imageView;
		TextView titleTextView;
		TextView titleEnTextView;
		ImageView overflowMenuIcon;
	}

	public ShelfContentAdapter(Context context, int resource,
							   List<ShelfContent> list) {
		super(context, resource, list);
		this.activity = (SplashActivity) context;
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(resource,
					parent, false);

			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.image_view);
			viewHolder.titleTextView = (TextView) convertView
					.findViewById(R.id.title_text_view);
//			viewHolder.titleEnTextView = (TextView) convertView
//					.findViewById(R.id.title_text_view);
			viewHolder.overflowMenuIcon = (ImageView) convertView.findViewById(R.id.overflow_cardlist);
			final View finalConvertView = convertView;
			viewHolder.overflowMenuIcon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PopupMenu menu = new PopupMenu(activity,v);
					try {
						Field[] fields = menu.getClass().getDeclaredFields();
						for (Field field : fields) {
						 	if ("mPopup".equals(field.getName())) {
								field.setAccessible(true);
								Object menuPopupHelper = field.get(menu);
								Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
								Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
								setForceIcons.invoke(menuPopupHelper, true);
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					menu.getMenuInflater().inflate(R.menu.popup_menu, menu.getMenu());
					menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem item) {
							switch (item.getItemId()) {
								case R.id.popupaction1:
									AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
									builder.setCancelable(true);
									builder.setMessage(R.string.remove_from_shelf_dialog);
									builder.setTitle(R.string.remove_from_shelf_title);
									builder.setPositiveButton(R.string.remove_from_shelf_positive, new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											Toast.makeText(getContext(), "Removed from Shelf !", Toast.LENGTH_LONG).show();
										}
									});
									builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											return;
										}
									});
									builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
										public void onCancel(DialogInterface dialog) {
											return;
										}
									});
									builder.show();
									break;
								case R.id.addasShortcut:
									Intent shortcutIntent = new Intent(getContext(), ReaderActivity.class);
									shortcutIntent.setAction(Intent.ACTION_MAIN);
									Intent intent = new Intent();
									intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
									intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Pratilipi"); //Add Book Name here
									intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getContext(), R.id.icon)); //Add Book Image Here
									intent.setAction("com.android.launcher.actiona.INSTALL_SHORTCUT");
									getContext().sendBroadcast(intent);
									break;
								default:
									return false;
							}
							return true;
						}
					});
					menu.show();
				}
			});
		convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ShelfContent shelfContent = getItem(position);
		Gson gson = new Gson();
		Book book = gson.fromJson(shelfContent.content, new TypeToken<Book>() {
		}.getType());
		activity.mImageLoader.displayImage(book.coverImageUrl,
				viewHolder.imageView);
		viewHolder.titleTextView.setText(book.title);
		viewHolder.titleTextView.setTypeface(FontManager.getInstance().get(
				shelfContent.language));
		return convertView;
	}

}
