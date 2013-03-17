package com.cedarstreettimes.ideasofmarch.xml.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cedarstreettimes.ideasofmarch.Article;

/** Class ParserXML is to parse XML WebSites and add them to the APP*/
public class ParserXML {

	private URL rssUrl;
	
	/** Constructor to create empty object*/
	public ParserXML(){
	}
	/** Constructor to create object with an URL 
	 * @param String	The URL String we want to parse*/
	public ParserXML(String url){
		
		try {			
			this.rssUrl = new URL(url);
			
		}catch (MalformedURLException e){			
			throw new RuntimeException(e);
		}
	}
	
	/** Method that make all the stuff of parse the data from XML
	 * @return ArrayList<Article>	return a List with the data saved inside*/
	public ArrayList<Article> parse(){
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		ArrayList<Article> news_list = new ArrayList<Article>();

		try{
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(this.getInputStream());
			Element root = dom.getDocumentElement();
			NodeList items = root.getElementsByTagName("item");

			for (int i=0; i<items.getLength(); i++){
				
				Article article = new Article();
				Node item = items.item(i);
				NodeList dataNews = item.getChildNodes();

				for (int j=0; j<dataNews.getLength(); j++){
					
					Node data = dataNews.item(j);
					String label = data.getNodeName();

					if (label.equals("title")){
						
						String text = getNodeText(data);
						article.setTitle(text);
						
					}else if (label.equals("link")){
						
						article.setUrl(data.getFirstChild().getNodeValue());
						
					}else if (label.equals("description")){
						
						String texto = getNodeText(data);

						article.setArticleText(texto);
					}else if (label.equals("pubDate"))
					{
						article.setDate(data.getFirstChild().getNodeValue());
					}else if (label.equals("content:encoded"))
					{
						article.setContextText(getNodeText(data));
					}
				}
				news_list.add(article);
			}
		}catch (Exception ex){
			throw new RuntimeException(ex);
		} 
		return news_list;
	}
	
	/** To get the data(text) from a XML Node
	 * @param Node		We receive a node to take all data
	 * @return String	The whole text we already get the data*/
	private String getNodeText(Node data){
		StringBuilder text = new StringBuilder();
		NodeList fragments = data.getChildNodes();

		for (int k=0;k<fragments.getLength();k++){
			text.append(fragments.item(k).getNodeValue());
		}
		/** In the text there are &#8217; that suppose to be " ' "*/
		return text.toString().replace("&#8217;", "'");
	}

	private InputStream getInputStream(){
		try {
			return rssUrl.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
