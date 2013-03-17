package com.cedarstreettimes.ideasofmarch;


/** Class to define Article model
 * 
 * @author Group S
 */
public class Article {
	/** Variables of an article to specify his attributes */
	private String article_title, date, article_text, url, content_text;
	/** Empty constructor to create empty Objects*/
	public Article(){
	}
	
	/** To get the Title of the Article
	 * @return String	The article title*/
	public String getTitle(){
		return this.article_title;
	}
	/** To set the Title of the article
	 * @param String	The title we want to be set*/
	public void setTitle(String title){
		this.article_title = title;
	}
	/** To get the Date of the Article
	 * @return String	The article date*/
	public String getDate(){
		return this.date;
	}
	/** To set the Date of the article
	 * @param String	The date we want to be set*/
	public void setDate(String date){
		this.date = date;
	}
	
	/** A get() method to get 'url' attribute
	 * @return url that is the text of an article
	 * */
	public String getUrl(){
		return this.url;
	}
	/** To set the URL of the article
	 * @param String	The URL we want to be set*/
	public void setUrl(String url){
		this.url = url;
	}
	
	/** A get() method to get 'article_text' attribute
	 * @return article_text that is the text of an article
	 * */
	public String getArticleText(){
		return this.article_text;
	}
	/** To set the Description Text of the article
	 * @param String	The Description Text we want to be set*/
	public void setArticleText(String article_text){
		this.article_text = article_text;
	}
	/** To get the Full Text of the Article
	 * @return String	The article full text*/
	public String getContextText(){
		return this.content_text;
	}
	/** To set the Full Text of the article
	 * @param String	The Article Full Text we want to be set*/
	public void setContextText(String text){
		this.content_text = text;
	}
}
