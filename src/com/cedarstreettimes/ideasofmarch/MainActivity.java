package com.cedarstreettimes.ideasofmarch;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.cedarstreettimes.ideasofmarch.xml.parser.ParserHTML;
import com.cedarstreettimes.ideasofmarch.xml.parser.ParserXML;
import com.cedarstreettimes.ideasofmarch.xml.parser.URL;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MainActivity extends Activity {

	private PullToRefreshListView news_listview, update_listview, coplog_listview, events_listview;
	private ArrayList<Article> news_list, update_list, coplog_list;
	private ArrayList<String> event_list;
	private NewsListAdapter news_adapter, coplog_adapter, update_adapter;
	private EventsListAdapter event_adapter;
	private ParserXML pXml;
	private ParserHTML pHtml;
	private NotificationManager mNManager;
	private static final int NOTIFY_ID = 1100;
	
	private static final int MNU_OPT_FB = 1;
	private static final int MNU_OPT_TW = 2;
	private static final int MNU_OPT_MA = 3;
	
	TabSpec tab1News, tab2Updates, tab3CopLog, tab4Events;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		news_listview = (PullToRefreshListView) findViewById(R.id.listViewNews);
	    update_listview = (PullToRefreshListView) findViewById(R.id.listViewUpdates);
	    coplog_listview = (PullToRefreshListView) findViewById(R.id.listViewCopLog);
	    events_listview = (PullToRefreshListView) findViewById(R.id.listViewEvents);
		
		//SEND IN BACKGROUND
		news_list = new ArrayList<Article>();
		coplog_list = new ArrayList<Article>();
		//update_list = new ArrayList<Article>();
		event_list = new ArrayList<String>();
		
		
		news_adapter = new NewsListAdapter(MainActivity.this, news_list);
	    coplog_adapter = new NewsListAdapter(MainActivity.this, coplog_list);
	    event_adapter = new EventsListAdapter(MainActivity.this, event_list); 
	    		
	    news_listview.setAdapter(news_adapter);
	    coplog_listview.setAdapter(coplog_adapter);
	    events_listview.setAdapter(event_adapter);
	    
		setListViewNews();
		setListViewCopLog();
		setListViewEvents();
		
		TabHost host = (TabHost) findViewById(R.id.tabhost);
	    host.setup ();
	    createTabs(host);
	    host.setCurrentTabByTag("News");
	    
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
						coplog_adapter.notifyDataSetChanged();
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
						setListViewEvents();
						Log.d("APP", "ASDFASDFASDF"+event_list.size());
						//RELOAD DATA
						/*
						Intent intent = new Intent("android.intent.action.MAIN");
						intent.setComponent(ComponentName.unflattenFromString("com.cedarstreettimes.ideasofmarch/com.cedarstreettimes.ideasofmarch.MainActivity"));
						intent.addCategory("android.intent.category.LAUNCHER");
						createNotification("Title","Text",intent);
						*/
						return 0;
					}

					@Override
					protected void onPostExecute(Integer result) {
						event_adapter.notifyDataSetChanged();
						events_listview.onRefreshComplete();
					}
				}.execute();
				 
			}
		});
	    

	}
	
	public void setListViewNews(){
		pXml = new ParserXML(URL.news_URL);
		news_list = pXml.parse();
			news_adapter.setList(news_list);
		
	}
	
	public void setListViewCopLog(){
		pXml = new ParserXML(URL.coplog_URL);
		coplog_list = pXml.parse();
		coplog_adapter.setList(coplog_list);
	}
	
	public void setListViewEvents() {
		pHtml = new ParserHTML();
		try {
			event_list = pHtml.scrapeEvents();
			event_adapter.setList(event_list);
			//event_adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MNU_OPT_FB, Menu.NONE, "FaceBook").setIcon(
				R.drawable.facebook);
		menu.add(Menu.NONE, MNU_OPT_TW, Menu.NONE, "Twitter").setIcon(
				R.drawable.twitter);
		menu.add(Menu.NONE, MNU_OPT_MA, Menu.NONE, "Mail").setIcon(
				android.R.drawable.ic_dialog_email);
		

		return true;
	}

	// Get menu selection
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent browserIntent;
		switch (item.getItemId()) {
		case MNU_OPT_FB:
			browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/cedarstreettimes/"));
		    startActivity(browserIntent);
			return true;
		case MNU_OPT_TW:
			browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/CedarStTimes/"));
		    startActivity(browserIntent);
			return true;
		case MNU_OPT_MA:
			browserIntent = new Intent(Intent.ACTION_SEND);
			browserIntent.setType("text/plain");
			browserIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"editor@cedarstreettimes.com"});
			browserIntent.putExtra(Intent.EXTRA_SUBJECT, "Doubt");
			browserIntent.putExtra(Intent.EXTRA_TEXT, "Hi Editor! ");
			startActivity(Intent.createChooser(browserIntent, "Send Email"));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
