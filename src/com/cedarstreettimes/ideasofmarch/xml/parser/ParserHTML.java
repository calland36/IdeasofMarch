package com.cedarstreettimes.ideasofmarch.xml.parser;

import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import android.util.Log;

/** Class ParserHTML is to parse HTML WebSites and add them to the APP*/
public class ParserHTML {
	
	private String htmlUrl;
	
	/** Constructor to create empty object*/
	public ParserHTML(){
	}
	
	public ParserHTML(String url){
		this.htmlUrl = url;
	}
	
	/** Method that make all the stuff of parse the data from HTML
	 * @return ArrayList<Article>	return a List with the data saved inside*/
	public ArrayList<String> scrapeEvents(String... args) throws Exception {
	   org.jsoup.nodes.Document doc = Jsoup.connect(htmlUrl).get();
	   Elements elements = doc.select("p.minipost");
	   String s = elements.toString();
	   s = Jsoup.parse(s).text();
	   String[] events = s.split("¥");
	   for(int i=0;i<events.length;i++){
	   	   events[i] = events[i].trim();
	   }
	   ArrayList<String> al = new ArrayList<String>(Arrays.asList(events));
	   return al;
	}
	
	public ArrayList<String> scrapeUpdates(String... args) throws Exception {
        ArrayList<String> updates = new ArrayList<String>();
        org.jsoup.nodes.Document doc = Jsoup.connect(htmlUrl).get();
        Elements tweetUsers = doc.select("span.username");
        Elements tweetTexts = doc.select("span.tweetcontent");
        Elements tweetTimes = doc.select("span.timestamp");
        for(int i=0;i<tweetUsers.size();i++){
            updates.add(tweetTexts.get(i).html()+"\n"+tweetUsers.get(i).html()+" - "+tweetTimes.get(i).html());
            Log.d("APP",updates.get(i));
        }
        
        return updates;
    }
}
