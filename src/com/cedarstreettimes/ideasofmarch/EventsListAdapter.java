package com.cedarstreettimes.ideasofmarch;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** class EventsListAdapter that extends from BaseAdapter*/
public class EventsListAdapter extends BaseAdapter {

	/** Static class to use a ViewHolder to save here the reference to the element*/
	private static class ViewHolder {
		public TextView text;
	}

	/** Variable of LayoutInflater to inflate an adapter to a ListView*/
	private LayoutInflater layout;
	/** List<String> where all data is taken*/
	private List<String> events_list;
	
	/** Constructor 
	 * @param context       To get the LayoutInflater, we need the context
	 * @param list			To know the data I have to put into the list*/
	public EventsListAdapter(Activity context, ArrayList<String> list) {
		this.events_list = list;
		this.layout = LayoutInflater.from(context);
	}

	/** Method to inflate the View - Called for all elements of the list
	 * @param position 		Which position are we referring to.
	 * @param convertView	To know if it was inflated before
	 * @param parent		To know the parent of this View*/
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
	/** To get the size of the List we have
	 * @return Integer		The size of the list*/
	public int getCount() {
		// TODO Auto-generated method stub
		return events_list.size();
	}
	/** To update the list
	 * @param list			To override the actual list with what we send*/
	public void setList(ArrayList<String> list){
		this.events_list = list;
	}
	/** NOT IMPLEMENTED - Method to getItem from the list, sending position
	 * @param Integer		The position where is the Item we want*/
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	/** NOT IMPLEMENTED - Method to getItemID from the list, sending position
	 * @param Integer		The position where is the Item we want to get the Id*/
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
