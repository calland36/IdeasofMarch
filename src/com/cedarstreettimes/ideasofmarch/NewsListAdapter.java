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


public class NewsListAdapter extends BaseAdapter {

	private static class ViewHolder {
		public TextView article, title;
		public ImageView image;
	}

	private LayoutInflater layout;
	private List<Article> news_list;
	private HttpURLConnection conn;
	private InputStream is;
	private Context contx;
	
	public NewsListAdapter(Activity context, ArrayList<Article> lista) {
		this.news_list = lista;
		this.layout = LayoutInflater.from(context);
		this.contx = context;
	}

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
			holder.article.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	            	contx.startActivity(browserIntent);
					
				}
			});
			
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

	public int getCount() {
		// TODO Auto-generated method stub
		return news_list.size();
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
