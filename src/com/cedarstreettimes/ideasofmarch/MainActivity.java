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
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.cedarstreettimes.ideasofmarch.xml.parser.ParserHTML;
import com.cedarstreettimes.ideasofmarch.xml.parser.ParserXML;
import com.cedarstreettimes.ideasofmarch.xml.parser.URL;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MainActivity extends Activity {

	/** Variables of PullToRefreshListView, which is a library that extends of ListView and add Refresh header*/
	private PullToRefreshListView news_listview, community_listview, coplog_listview, events_listview;
	/** To work with the data, we save it in this ArrayList<Article>*/
	private ArrayList<Article> news_list, coplog_list;
	/** To work with the data, we save it in this ArrayList<String>*/
	private ArrayList<String> event_list, community_list;
	/** Adapters to edit each item in the list - BaseAdapter*/
	private NewsListAdapter news_adapter, coplog_adapter;
	/** Adapters to edit each item in the list - BaseAdapter*/
	private EventsListAdapter event_adapter, community_adapter;
	/** Variable to call parser method to get all data from XML*/
	private ParserXML pXml;
	/** Variable to call parser method to get all data from HTML*/
	private ParserHTML pHtml;
	private NotificationManager mNManager;
	private static final int NOTIFY_ID = 1100;
	
	/** Variables for the different OptionsMenu, so we can know what was selected */
	private static final int MNU_OPT_FB = 1;
	private static final int MNU_OPT_TW = 2;
	private static final int MNU_OPT_MA = 3;
	
	/** LinearLayout to show loading state when making a GET request*/
	private LinearLayout loadinglayout;
	/** Different's tabs of the TabHost*/
	private TabSpec tab1News, tab2Updates, tab3CopLog, tab4Events;

	private AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** To remove the Title to the APP*/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		/** Assign loadinglayout with his XML element*/
		loadinglayout = (LinearLayout) findViewById(R.id.loadingLayout);
		/** I hide this layout in case it is visible*/
		loadinglayout.setVisibility(LinearLayout.GONE);
		
		/** Assign listviews from their XML elements*/
		news_listview = (PullToRefreshListView) findViewById(R.id.listViewNews);
	    community_listview = (PullToRefreshListView) findViewById(R.id.listViewUpdates);
	    coplog_listview = (PullToRefreshListView) findViewById(R.id.listViewCopLog);
	    events_listview = (PullToRefreshListView) findViewById(R.id.listViewEvents);
		
	    /** Initialize the lists*/
		news_list = new ArrayList<Article>();
		coplog_list = new ArrayList<Article>();
		community_list = new ArrayList<String>();
		event_list = new ArrayList<String>();
		
		/** Initialize the Adapters - NewsListAdapter & EventsListAdapter
		 * @param Context       Context to call this View method from the adapter
		 * @param ArrayList		The List to inflate the ListView*/
		news_adapter = new NewsListAdapter(MainActivity.this, news_list);
	    coplog_adapter = new NewsListAdapter(MainActivity.this, coplog_list);
	    event_adapter = new EventsListAdapter(MainActivity.this, event_list); 
	    community_adapter = new EventsListAdapter(MainActivity.this, community_list); 
	    
	    /** Add those Adapter to the ListView we want*/
	    news_listview.setAdapter(news_adapter);
	    coplog_listview.setAdapter(coplog_adapter);
	    events_listview.setAdapter(event_adapter);
	    community_listview.setAdapter(community_adapter);
	    
	    /** Assign TabHost from the XML element*/
		TabHost host = (TabHost) findViewById(R.id.tabhost);
		/** Need to call setup() before adding tabs*/
	    host.setup ();
	    /** Call createTabs method*/
	    createTabs(host);
	    /** Say which one is going to have the focus */
	    host.setCurrentTabByTag("News");
	    
	    /** I don't allow ListView to Scroll while is Refreshing*/
	    news_listview.setDisableScrollingWhileRefreshing(true);
	    community_listview.setDisableScrollingWhileRefreshing(true);
	    coplog_listview.setDisableScrollingWhileRefreshing(true);
	    events_listview.setDisableScrollingWhileRefreshing(true);
		
	    /** RefreshListener on news_listview so we know when the user pull to refresh*/
		news_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {
			
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				/** new AsyncTask to refresh the ListView in background*/
				new AsyncTask<String, Void, Integer>() {
					/** Work to do in background (Request)*/
					@Override
					protected Integer doInBackground(String... urls) {
						/** Call setListViewNews() method*/
						setListViewNews();
						return 0;
					}
					/** After doing the doInBackground method we can use View elements*/
					@Override
					protected void onPostExecute(Integer result) {
						/** I notify that the data was changed so refresh the adapter*/
						news_adapter.notifyDataSetChanged();
						/** Stop refreshing*/
						news_listview.onRefreshComplete();
					}
				}.execute();
			}
			
		});
		
		 /** RefreshListener on update_listview so we know when the user pull to refresh*/
		community_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {
			
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				/** new AsyncTask to refresh the ListView in background*/
				new AsyncTask<String, Void, Integer>() {
					/** Work to do in background (Request)*/
					@Override
					protected Integer doInBackground(String... urls) {
						/** Call setListViewUpdate() method*/
						setListViewCommunity();
						return 0;
					}
					/** After doing the doInBackground method we can use View elements*/
					@Override
					protected void onPostExecute(Integer result) {
						/** I notify that the data was changed so refresh the adapter*/
						community_adapter.notifyDataSetChanged();
						/** Stop refreshing*/
						community_listview.onRefreshComplete();
					}
				}.execute();
			}
		});
		
		 /** RefreshListener on coplog_listview so we know when the user pull to refresh*/
		coplog_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				/** new AsyncTask to refresh the ListView in background*/
				new AsyncTask<String, Void, Integer>() {
					/** Work to do in background (Request)*/
					@Override
					protected Integer doInBackground(String... urls) {
						/** Call setListViewCopLog() method*/
						setListViewCopLog();
						return 0;
					}
					/** After doing the doInBackground method we can use View elements*/
					@Override
					protected void onPostExecute(Integer result) {
						/** I notify that the data was changed so refresh the adapter*/
						coplog_adapter.notifyDataSetChanged();
						/** Stop refreshing*/
						coplog_listview.onRefreshComplete();
					}
				}.execute();
				
			}
		});
		
		 /** RefreshListener on events_listview so we know when the user pull to refresh*/
		events_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {
			
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				/** new AsyncTask to refresh the ListView in background*/
				new AsyncTask<String, Void, Integer>() {
					/** Work to do in background (Request)*/
					@Override
					protected Integer doInBackground(String... urls) {
						/** Call setListViewEvents() method*/
						setListViewEvents();
						return 0;
					}
					/** After doing the doInBackground method we can use View elements*/
					@Override
					protected void onPostExecute(Integer result) {
						/** I notify that the data was changed so refresh the adapter*/
						event_adapter.notifyDataSetChanged();
						/** Stop refreshing*/
						events_listview.onRefreshComplete();
					}
				}.execute();
				 
			}
		});
		
		/** new AsyncTask to load the first time when the APP runs
		 * And display the 'splash screen'*/
		new AsyncTask<String, Void, Integer>() {
			/** Method that execute before doing onBackground*/
	    	@Override
			protected void onPreExecute() {
	    		/** Put loadinglayout to VISIBLE*/
				loadinglayout.setVisibility(LinearLayout.VISIBLE);
			}
	    	
	    	/** Work to do in background (Request)*/
			@Override
			protected Integer doInBackground(String... urls) {
				//MAKE REQUESTS
				setListViewNews();
				return 0;
			}
			/** After doing the doInBackground method we can use View elements*/
			@Override
			protected void onPostExecute(Integer result) {
				news_adapter.notifyDataSetChanged(); 
				/** Put loadinglayout to GONE, so we hide it*/
				loadinglayout.setVisibility(LinearLayout.GONE);
				
			}
		}.execute();
		
		/** new AsyncTask to load the first time when the APP runs, after news ListView and the 'splash screen'*/
		new AsyncTask<String, Void, Integer>() {
			/** Work to do in background (Requests)*/
			@Override
			protected Integer doInBackground(String... urls) {
				//MAKE REQUESTS
				setListViewCopLog();
				return 0;
			}
		}.execute();
		
		/** new AsyncTask to load the first time when the APP runs, after news ListView and the 'splash screen'*/
		new AsyncTask<String, Void, Integer>() {
			/** Work to do in background (Requests) */
			@Override
			protected Integer doInBackground(String... urls) {
				//MAKE REQUESTS
				setListViewEvents();
				return 0;
			}
		}.execute();
		
		/** new AsyncTask to load the first time when the APP runs, after news ListView and the 'splash screen'*/
		new AsyncTask<String, Void, Integer>() {
			/** Work to do in background (Requests)*/
			@Override
			protected Integer doInBackground(String... urls) {
				//MAKE REQUESTS
				setListViewCommunity();
				return 0;
			}
		}.execute();
		
		/**Ads here:*/
	    new AsyncTask<String, Void, Integer>() {
			/** Work to do in background (Request)*/
			@Override
			protected Integer doInBackground(String... urls) {
				return 0;
			}
			/** After doing the doInBackground method we can use View elements*/
			@Override
			protected void onPostExecute(Integer result) {
				adView=new AdView(MainActivity.this, AdSize.BANNER,"a15144e917e69cb" );
			    RelativeLayout layout = (RelativeLayout)findViewById(R.id.relative);
				//layout.addView(adView);
				adView.loadAd(new AdRequest());
			}
		}.execute();
	}
	
	/** Download the news from the WebSite and added to the ListView news.*/
	public void setListViewNews(){
		pXml = new ParserXML(URL.news_URL);
		news_list = pXml.parse();
		news_adapter.setList(news_list);
	}
	
	/** Download the CopLog from the WebSite and added to the ListView coplog.*/
	public void setListViewCopLog(){
		pXml = new ParserXML(URL.coplog_URL);
		coplog_list = pXml.parse();
		coplog_adapter.setList(coplog_list);
	}
	
	/** Download the Calendar Events from the WebSite and added to the ListView events.*/
	public void setListViewEvents() {
		pHtml = new ParserHTML(URL.events_URL);
		try {
			event_list = pHtml.scrapeEvents();
			event_adapter.setList(event_list);
			//event_adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setListViewCommunity(){
		pHtml = new ParserHTML(URL.community_URL);
		try {
			community_list = pHtml.scrapeUpdates();
			community_adapter.setList(community_list);
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

	    tab2Updates = host.newTabSpec("Community");
	    tab2Updates.setIndicator("Community", getResources().getDrawable(android.R.drawable.star_on));
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
	
	/** Create an OptionsMenu that is display if you press MENU button
	 * @return true    Always return true, except when something goes wrong*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/**Add all the Options, with their label name and Icon*/
		menu.add(Menu.NONE, MNU_OPT_FB, Menu.NONE, "FaceBook").setIcon(
				R.drawable.facebook);
		menu.add(Menu.NONE, MNU_OPT_TW, Menu.NONE, "Twitter").setIcon(
				R.drawable.twitter);
		menu.add(Menu.NONE, MNU_OPT_MA, Menu.NONE, "Mail").setIcon(
				android.R.drawable.ic_dialog_email);
		return true;
	}

	/** Create OptionsItemSelected, method that is called when you select one of the OptionsMenu
	 * @return true     Always return true when select one of the OptionsMenu*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent browserIntent;
		/** Switch to know which one you press*/
		switch (item.getItemId()) {
		case MNU_OPT_FB:
			/** If you press 1 (MNU_OPT_FB) launch this intent*/
			browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/cedarstreettimes/"));
		    startActivity(browserIntent);
			return true;
		case MNU_OPT_TW:
			/** If you press 2 (MNU_OPT_TW) launch this intent*/
			browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/CedarStTimes/"));
		    startActivity(browserIntent);
			return true;
		case MNU_OPT_MA:
			/** If you press 3 (MNU_OPT_MA) launch this intent*/
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
	
	@Override
	public void onDestroy() {
		if(adView !=null) {
			adView.destroy();
		}
		super.onDestroy();
	}

}
