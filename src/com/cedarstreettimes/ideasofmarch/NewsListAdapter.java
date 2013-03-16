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
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class NewsListAdapter extends BaseAdapter {

	private static class ViewHolder {
		public TextView article;
		public ImageView imageLeft, imageRight;
	}

	private LayoutInflater layout;
	private List<Article> news_list;
	private HttpURLConnection conn;
	private InputStream is;
	
	public NewsListAdapter(Activity context, ArrayList<Article> lista) {
		this.news_list = lista;
		this.layout = LayoutInflater.from(context);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layout.inflate(R.layout.news_adapter, null);
			holder = new ViewHolder();
			holder.article = (TextView) convertView
					.findViewById(R.id.textViewArticle);
			holder.imageLeft = (ImageView) convertView
					.findViewById(R.id.imageViewArticleLeft);
			holder.imageRight = (ImageView) convertView
					.findViewById(R.id.imageViewArticleRight);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.article.setText(news_list.get(position).getArticleText());
		/*
		try {
			conn = (HttpURLConnection) new URL(news_list.get(position).getImageUrl()).openConnection();
			is = new BufferedInputStream(conn.getInputStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		holder.image.setImageBitmap(BitmapFactory.decodeStream(is));
		*/
		
		if(position%2 == 0){
			holder.imageLeft.setVisibility(View.VISIBLE);
			holder.imageRight.setVisibility(View.GONE);
		}else{
			holder.imageLeft.setVisibility(View.GONE);
			holder.imageRight.setVisibility(View.VISIBLE);
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
