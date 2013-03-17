package com.cedarstreettimes.ideasofmarch.xml.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

import com.cedarstreettimes.ideasofmarch.Article;


public class ParserXML {


	private URL rssUrl;

	public ParserXML(String url){
		
		try {			
			this.rssUrl = new URL(url);
			
		}catch (MalformedURLException e){			
			throw new RuntimeException(e);
		}
	}

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

	private String getNodeText(Node data){
		StringBuilder text = new StringBuilder();
		NodeList fragments = data.getChildNodes();

		for (int k=0;k<fragments.getLength();k++){
			text.append(fragments.item(k).getNodeValue());
		}

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
