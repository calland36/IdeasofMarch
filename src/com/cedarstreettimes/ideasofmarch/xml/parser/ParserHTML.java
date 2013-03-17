package com.cedarstreettimes.ideasofmarch.xml.parser;

import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import android.util.Log;


public class ParserHTML {

	public ParserHTML(){
	}
	
	public ArrayList<String> scrapeEvents(String... args) throws Exception {
	   org.jsoup.nodes.Document doc = Jsoup.connect("http://www.cedarstreettimes.com").get();
	   Elements elements = doc.select("p.minipost");
	   String s = elements.toString();
	   s = Jsoup.parse(s).text();
	   String[] events = s.split("¥");
	   for(int i=0;i<events.length;i++){
		   Log.d("APP", "i: "+events[i]);
	   	   events[i] = events[i].trim();
	   }
	   ArrayList<String> al = new ArrayList<String>(Arrays.asList(events));
	   return al;
	}

}
