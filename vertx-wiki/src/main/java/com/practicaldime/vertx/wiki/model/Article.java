package com.practicaldime.vertx.wiki.model;

import io.vertx.core.json.JsonObject;

public class Article {

	private Long id;
	private String title;
	private String url;

	public Article() {
		this.id = -1l;
	}

	public Article(String title, String url) {
		this(-1l, title, url);
	}

	public Article(JsonObject json) {
		this.id = json.getLong("id");
		this.title = json.getString("title");
		this.url = json.getString("url");
	}

	public Article(Long id, String title, String url) {
		this.id = id;
		this.title = title;
		this.url = url;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public Article setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public Article setUrl(String url) {
		this.url = url;
		return this;
	}
}
