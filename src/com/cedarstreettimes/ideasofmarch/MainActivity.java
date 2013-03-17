package com.cedarstreettimes.ideasofmarch;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.cedarstreettimes.ideasofmarch.xml.parser.ParserXML;
import com.cedarstreettimes.ideasofmarch.xml.parser.URL;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MainActivity extends Activity {

	private PullToRefreshListView news_listview, update_listview, coplog_listview, events_listview;
	private ArrayList<Article> news_list, update_list, coplog_list, event_list;
	private NewsListAdapter news_adapter, coplog_adapter;
	private ParserXML pXml;
	private NotificationManager mNManager;
	private static final int NOTIFY_ID = 1100;
	
	TabSpec tab1News, tab2Updates, tab3CopLog, tab4Events;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//SEND IN BACKGROUND
		news_list = new ArrayList<Article>();
		coplog_list = new ArrayList<Article>();
		//setListViewNews();
		//setListViewCopLog();
		
		TabHost host = (TabHost) findViewById(R.id.tabhost);
	    host.setup ();
	    createTabs(host);
	    host.setCurrentTabByTag("News");
	    news_listview = (PullToRefreshListView) findViewById(R.id.listViewNews);
	    update_listview = (PullToRefreshListView) findViewById(R.id.listViewUpdates);
	    coplog_listview = (PullToRefreshListView) findViewById(R.id.listViewCopLog);
	    events_listview = (PullToRefreshListView) findViewById(R.id.listViewEvents);
	    
	    //createListeners(news_listview, update_listview, coplog_listview, events_listview);
	    /////////////////////////////
	    
	    news_listview.setDisableScrollingWhileRefreshing(true);
	    update_listview.setDisableScrollingWhileRefreshing(true);
	    coplog_listview.setDisableScrollingWhileRefreshing(true);
	    events_listview.setDisableScrollingWhileRefreshing(true);
		
		news_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {
			
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new AsyncTask<String, Void, Integer>() {

					@Override
					protected Integer doInBackground(String... urls) {
						setListViewNews();
						return 0;
					}

					@Override
					protected void onPostExecute(Integer result) {
						news_adapter.notifyDataSetChanged();
						news_listview.onRefreshComplete();
					}
				}.execute();
			}
			
		});
		
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
		
		coplog_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new AsyncTask<String, Void, Integer>() {

					@Override
					protected Integer doInBackground(String... urls) {
						setListViewCopLog();
						return 0;
					}

					@Override
					protected void onPostExecute(Integer result) {
						news_adapter.notifyDataSetChanged();
						coplog_listview.onRefreshComplete();
					}
				}.execute();
				
			}
		});
		
		events_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {
			
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new AsyncTask<String, Void, Integer>() {

					@Override
					protected Integer doInBackground(String... urls) {
						//RELOAD DATA
						Intent intent = new Intent("android.intent.action.MAIN");
						intent.setComponent(ComponentName.unflattenFromString("com.cedarstreettimes.ideasofmarch/com.cedarstreettimes.ideasofmarch.MainActivity"));
						intent.addCategory("android.intent.category.LAUNCHER");
						createNotification("Title","Text",intent);
						return 0;
					}

					@Override
					protected void onPostExecute(Integer result) {
						events_listview.onRefreshComplete();
					}
				}.execute();
				 
			}
		});
	    
	    
	    
	    ////////////////////////////
	    news_adapter = new NewsListAdapter(MainActivity.this, news_list);
	    coplog_adapter = new NewsListAdapter(MainActivity.this, coplog_list);
	    news_listview.setAdapter(news_adapter);
	    coplog_listview.setAdapter(coplog_adapter);
	    
	    

	}
	
	public void setListViewNews(){
		pXml = new ParserXML(URL.news_URL);
		news_list = pXml.parse();
		news_adapter.setList(news_list);
		
	}
	
	public void setListViewCopLog(){
		pXml = new ParserXML(URL.coplog_URL);
		coplog_list = pXml.parse();
		news_adapter.setList(coplog_list);
	}
	
	/**
	 * Method to create tabs and add a title, image...
	 * @param host TabHost is already setup() so we have to add tab
	 */
	public void createTabs(TabHost host){
		tab1News = host.newTabSpec("News");
	    tab1News.setIndicator("News", getResources().getDrawable(android.R.drawable.star_on));
	    tab1News.setContent(R.id.listViewNews);
	    host.addTab(tab1News);

	    tab2Updates = host.newTabSpec("Updates");
	    tab2Updates.setIndicator("Updates", getResources().getDrawable(android.R.drawable.star_on));
	    tab2Updates.setContent(R.id.listViewUpdates);
	    host.addTab(tab2Updates);
	    
	    tab3CopLog = host.newTabSpec("CopLog");
	    tab3CopLog.setIndicator("CopLog", getResources().getDrawable(android.R.drawable.star_on));
	    tab3CopLog.setContent(R.id.listViewCopLog);
	    host.addTab(tab3CopLog);
	    
	    tab4Events = host.newTabSpec("Events");
	    tab4Events.setIndicator("Events", getResources().getDrawable(android.R.drawable.star_on));
	    tab4Events.setContent(R.id.listViewEvents);
	    host.addTab(tab4Events);
	}
	
	/**
	 * Method to create all listeners at once
	 * @param news_listview     ListView of news at tab1
	 * @param update_listview   ListView of updates at tab2
	 * @param coplog_listview   ListView of cop log at tab3
	 * @param events_listview   ListView of events at tab4
	 */
	public void createListeners(PullToRefreshListView news_listview, 
			PullToRefreshListView update_listview, 
			PullToRefreshListView coplog_listview,
			PullToRefreshListView events_listview){
		
		
	}
	public void createNotification(CharSequence contentTitle, CharSequence contentText, Intent msgIntent){
		String ns = Context.NOTIFICATION_SERVICE;
		mNManager = (NotificationManager) getSystemService(ns);
		final Notification msg = new Notification(R.drawable.arrow_down,
		"New event of importance", System.currentTimeMillis());
		Context context = getApplicationContext();
		PendingIntent intent = PendingIntent.getActivity(
			MainActivity.this, 0, msgIntent,
			Intent.FLAG_ACTIVITY_NEW_TASK);
		msg.defaults |= Notification.DEFAULT_SOUND;
		msg.flags |= Notification.FLAG_AUTO_CANCEL;
		msg.setLatestEventInfo(context, contentTitle, contentText,
				intent);
		mNManager.notify(NOTIFY_ID, msg);
	}

}
