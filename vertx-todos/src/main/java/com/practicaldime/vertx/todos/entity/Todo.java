package com.practicaldime.vertx.todos.entity;

import io.vertx.core.json.JsonObject;

public class Todo {

	private String id;
	private String title;
	private Boolean completed;

	public Todo() {
	}

	public Todo(Todo other) {
		this.id = other.id;
		this.title = other.title;
		this.completed = other.completed;
	}

	public Todo(JsonObject obj) {
		this.id = obj.getString("_id");
		this.title = obj.getString("title");
		this.completed = obj.getValue("completed") instanceof Boolean? obj.getBoolean("completed") : Boolean.valueOf(obj.getString("completed"));
	}

	public Todo(String id, String title, Boolean completed) {
		this.id = id;
		this.title = title;
		this.completed = completed;
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.put("_id", id);
		json.put("title", title);
		json.put("completed", completed);
		return json;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean isCompleted() {
		return getOrElse(completed, false);
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Todo todo = (Todo) o;

		if (id != todo.id)
			return false;
		if (!title.equals(todo.title))
			return false;
		return (completed != null ? !completed.equals(todo.completed) : todo.completed != null);

	}

	@Override
	public int hashCode() {
		int result = 33;
		result = 31 * result + (id != null? id.hashCode() : 0);
		result = 31 * result + (title != null? title.hashCode() : 0);
		result = 31 * result + (completed != null ? completed.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Todo -> {" + "id=" + id + ", title='" + title + '\'' + ", completed=" + completed + " '}'";
	}

	private <T> T getOrElse(T value, T defaultValue) {
		return value == null ? defaultValue : value;
	}

	public Todo merge(Todo todo) {
		return new Todo(id, getOrElse(todo.title, title), getOrElse(todo.completed, completed));
	}
}
