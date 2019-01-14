package com.practicaldime.vertx.bricks.model;

import io.vertx.core.json.JsonObject;

public class Player {

	private String id;
	private String username;
	private String email;
	
	public Player() {
		super();
	}

	public Player(String id, String username, String email) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
	}
	
	public Player(JsonObject obj) {
		super();
		this.id = obj.getString("id");
		this.username = obj.getString("username");
		this.email = obj.getString("email");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
