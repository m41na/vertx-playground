package com.practicaldime.tutorial.entity;

import java.util.Date;

public class Game implements Comparable<Game>{

	public static enum Status {
		WIP, READY
	}
	
	public static enum Scope {
		GENERAL, INVITATIONAL, RESTRICTED
	}
	
	private Long id;
	private String title;	
	private Date playTime;
	private Player organizer;
	private Status status;
	private Scope scope;
	
	public Game() {
		super();
	}

	public Game(Long id, String title, Date playTime, Player organizer, Status status, Scope scope) {
		super();
		this.id = id;
		this.title = title;
		this.playTime = playTime;
		this.organizer = organizer;
		this.status = status;
		this.scope = scope;
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

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPlayTime() {
		return playTime;
	}

	public void setPlayTime(Date playTime) {
		this.playTime = playTime;
	}

	public Player getOrganizer() {
		return organizer;
	}

	public void setOrganizer(Player organizer) {
		this.organizer = organizer;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((organizer == null) ? 0 : organizer.hashCode());
		result = prime * result + ((playTime == null) ? 0 : playTime.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (organizer == null) {
			if (other.organizer != null)
				return false;
		} else if (!organizer.equals(other.organizer))
			return false;
		if (playTime == null) {
			if (other.playTime != null)
				return false;
		} else if (!playTime.equals(other.playTime))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", title=" + title + ", playTime=" + playTime + ", organizer=" + organizer
				+ ", status=" + status + ", scope=" + scope + "]";
	}

	@Override
	public int compareTo(Game that) {
		if (this == that) return 0;

		// primitive numbers follow this form
		if (this.id != null && that.id != null) {
			if (this.id != null && this.id < that.id) return -1;
			if (this.id != null && this.id > that.id) return 1;
		}

		// compare title
		if (this.title != null && that.title != null) {
			int comparison = this.title.compareTo(that.title);
			if (comparison != 0) return comparison;
		}

		// compare organizer
		if (this.organizer != null && that.organizer != null) {
			int comparison = this.organizer.compareTo(that.organizer);
			if (comparison != 0) return comparison;
		}

		// compare location
		if (this.playTime != null && that.playTime != null) {
			int comparison = this.playTime.compareTo(that.playTime);
			if (comparison != 0) return comparison;
		}

		return 0;
	}
}
