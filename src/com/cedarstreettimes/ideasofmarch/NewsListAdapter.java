package com.cedarstreettimes.ideasofmarch;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

/** class NewsListAdapter that extends from BaseAdapter*/
public class NewsListAdapter extends BaseAdapter {
	
	/** Static class to use a ViewHolder to save here the reference to the elements*/
	private static class ViewHolder {
		public TextView article, title;
		public ImageView image;
	}
	/** Variable of LayoutInflater to inflate an adapter to a ListView*/
	private LayoutInflater layout;
	/** List<String> where all data is taken*/
	private List<Article> news_list;
	/** To make a HTTP Request if there is no text and an image*/
	private HttpURLConnection conn;
	/** To buffer the image we get with HTTP request*/
	private InputStream is;
	/** Context to be called inside getView() method*/
	private Context contx;
	
	/** Constructor 
	 * @param context       To get the LayoutInflater, we need the context
	 * @param list			To know the data I have to put into the list*/
	public NewsListAdapter(Activity context, ArrayList<Article> list) {
		this.news_list = list;
		this.layout = LayoutInflater.from(context);
		this.contx = context;
	}
	
	/** Method to inflate the View - Called for all elements of the list
	 * @param position 		Which position are we referring to.
	 * @param convertView	To know if it was inflated before
	 * @param parent		To know the parent of this View*/
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layout.inflate(R.layout.news_adapter, null);
			holder = new ViewHolder();
			holder.article = (TextView) convertView
					.findViewById(R.id.textViewArticle);
			holder.image = (ImageView) convertView
					.findViewById(R.id.imageViewArticle);
			holder.title = (TextView) convertView
					.findViewById(R.id.textViewTitle);
			convertView.setTag(holder);
			
			final String url = news_list.get(position).getUrl();
			/** Make listeners to the list elements, so when you click will launch the WebSite - ARTICLE TEXT*/
			holder.article.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	            	contx.startActivity(browserIntent);
					
				}
			});
			/** Make listeners to the list elements, so when you click will launch the WebSite - TITLE*/
			convertView.setOnClickListener(new View.OnClickListener()
	        {	
	            //@Override
	            public void onClick(View v) 
	            {
	            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	            	contx.startActivity(browserIntent);
	            }
	        });
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setTypeface(null, Typeface.BOLD);
		holder.article.setText(news_list.get(position).getArticleText());
		holder.title.setText(news_list.get(position).getTitle());
		
		/** If this element have no text, then will have an image*/
		if(news_list.get(position).getArticleText().equals("")){
			holder.image.setVisibility(View.VISIBLE);
			String [] arr = news_list.get(position).getContextText().split("\"");
			for(int i = 0; i<arr.length; i++){
				if(arr[i].startsWith("http")){
					try {
						conn = (HttpURLConnection) new URL(arr[i]).openConnection();
						is = new BufferedInputStream(conn.getInputStream());
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					holder.image.setImageBitmap(BitmapFactory.decodeStream(is));
					break;
				}
			}
		}else{
			holder.image.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	/** To get the size of the List we have
	 * @return Integer		The size of the list*/
	public int getCount() {
		// TODO Auto-generated method stub
		return news_list.size();
	}
	/** To update the list
	 * @param list			To override the actual list with what we send*/
	public void setList(ArrayList<Article> list){
		this.news_list = list;
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
