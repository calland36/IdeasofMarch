package com.cedarstreettimes.ideasofmarch;
/** Class to define Article model
 * 
 * @author Group S
 */
public class Article {
	/** Variables of an article to specify his attributes */
	private String article_text, url, image_url;
	public Article(){
		this.article_text = "This is a test" +
				"This is a test This is a test This is a test This is a test This is a test This is a test This is a test This is a test This is a test This is a test This is a test This is a test This is a test This is a test This is a test This is a test This is a test This is a test ";
		this.url = "http://www.google.es/";
		
	}
	/** A get() method to get 'article_text' attribute
	 * @return article_text that is the text of an article
	 * */
	public String getArticleText(){
		return this.article_text;
	}
	
	/** A get() method to get 'url' attribute
	 * @return url that is the text of an article
	 * */
	public String getUrl(){
		return this.url;
	}
	/** A get() method to get 'image_url' attribute
	 * @return image_url, is the URL where the image is in the cloud
	 */
	public String getImageUrl(){
		return this.image_url;
	}
}
