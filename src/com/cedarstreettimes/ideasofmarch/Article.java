package com.cedarstreettimes.ideasofmarch;

import android.util.Log;

/** Class to define Article model
 * 
 * @author Group S
 */
public class Article {
	/** Variables of an article to specify his attributes */
	private String article_title, date, article_text, url, content_text;
	public Article(){
	}
	
	
	public String getTitle(){
		return this.article_title;
	}
	
	public void setTitle(String title){
		this.article_title = title;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	/** A get() method to get 'url' attribute
	 * @return url that is the text of an article
	 * */
	public String getUrl(){
		return this.url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	/** A get() method to get 'article_text' attribute
	 * @return article_text that is the text of an article
	 * */
	public String getArticleText(){
		return this.article_text;
	}
	
	public void setArticleText(String article_text){
		this.article_text = article_text;
	}
	
	public String getContextText(){
		return this.content_text;
	}
	
	public void setContextText(String text){
		this.content_text = text;
	}
}
