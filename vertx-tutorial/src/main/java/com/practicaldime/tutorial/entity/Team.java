package com.practicaldime.tutorial.entity;

import java.util.Date;

public class Team implements Comparable<Team>{

	private String title;
	private Date dateCreated;
	private Player captain;
	private Location location;
	
	public Team() {
		super();
	}

	public Team(String title, Date dateCreated, Player captain, Location location) {
		super();
		this.title = title;
		this.dateCreated = dateCreated;
		this.captain = captain;
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Player getCaptain() {
		return captain;
	}

	public void setCaptain(Player captain) {
		this.captain = captain;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((captain == null) ? 0 : captain.hashCode());
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
		Team other = (Team) obj;
		if (captain == null) {
			if (other.captain != null)
				return false;
		} else if (!captain.equals(other.captain))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public int compareTo(Team that) {
		if (this == that) return 0;

		// compare title
		if (this.title != null && that.title != null) {
			int comparison = this.title.compareTo(that.title);
			if (comparison != 0) return comparison;
		}

		// compare captain
		if (this.captain != null && that.captain != null) {
			int comparison = this.captain.compareTo(that.captain);
			if (comparison != 0) return comparison;
		}

		return 0;
	}
}
