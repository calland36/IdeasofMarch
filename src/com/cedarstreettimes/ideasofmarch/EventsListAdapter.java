package com.cedarstreettimes.ideasofmarch;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class EventsListAdapter extends BaseAdapter {

	private static class ViewHolder {
		public TextView text;
	}

	private LayoutInflater layout;
	private List<String> events_list;
	private HttpURLConnection conn;
	private InputStream is;
	private Context contx;
	
	public EventsListAdapter(Activity context, ArrayList<String> list) {
		this.events_list = list;
		this.layout = LayoutInflater.from(context);
		this.contx = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layout.inflate(R.layout.events_adapter, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView
					.findViewById(R.id.textViewEvents);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.text.setText(events_list.get(position));
		return convertView;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return events_list.size();
	}

	public void setList(ArrayList<String> list){
		this.events_list = list;
	}
	
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
