package com.cedarstreettimes.ideasofmarch;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MainActivity extends Activity {

	private PullToRefreshListView news_listview, update_listview, coplog_listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TabHost host = (TabHost) findViewById(R.id.tabhost);
	    host.setup ();

	    TabSpec tab1News = host.newTabSpec("News");
	    tab1News.setIndicator("News", getResources().getDrawable(android.R.drawable.star_on));
	    tab1News.setContent(R.id.listViewNews);
	    host.addTab(tab1News);

	    TabSpec tab2Updates = host.newTabSpec("Updates");
	    tab2Updates.setIndicator("Updates", getResources().getDrawable(android.R.drawable.star_on));
	    tab2Updates.setContent(R.id.listViewUpdates);
	    host.addTab(tab2Updates);
	    
	    TabSpec tab3CopLog = host.newTabSpec("CopLog");
	    tab3CopLog.setIndicator("CopLog", getResources().getDrawable(android.R.drawable.star_on));
	    tab3CopLog.setContent(R.id.listViewCopLog);
	    host.addTab(tab3CopLog);

	    //host.setCurrentTabByTag("News");
	    
	    
	    news_listview = (PullToRefreshListView) findViewById(R.id.listViewNews);
	    news_listview.setDisableScrollingWhileRefreshing(true);
	    
	    news_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new AsyncTask<String, Void, Integer>() {

					@Override
					protected Integer doInBackground(String... urls) {
						//RELOAD DATA
						return 0;
					}

					@Override
					protected void onPostExecute(Integer result) {
						news_listview.onRefreshComplete();
					}
				}.execute();
				
			}
		});
	    
	    update_listview = (PullToRefreshListView) findViewById(R.id.listViewUpdates);
	    update_listview.setDisableScrollingWhileRefreshing(true);
	    
	    update_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new AsyncTask<String, Void, Integer>() {

					@Override
					protected Integer doInBackground(String... urls) {
						//RELOAD DATA
						return 0;
					}

					@Override
					protected void onPostExecute(Integer result) {
						update_listview.onRefreshComplete();
					}
				}.execute();
				
			}
		});
	    
	    coplog_listview = (PullToRefreshListView) findViewById(R.id.listViewCopLog);
	    coplog_listview.setDisableScrollingWhileRefreshing(true);
	    
	    coplog_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new AsyncTask<String, Void, Integer>() {

					@Override
					protected Integer doInBackground(String... urls) {
						//RELOAD DATA
						return 0;
					}

					@Override
					protected void onPostExecute(Integer result) {
						coplog_listview.onRefreshComplete();
					}
				}.execute();
				
			}
		});
				

	}

}
