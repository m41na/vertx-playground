package com.practicaldime.tutorial.entity;

import java.sql.Date;

public class Player implements Comparable<Player>{

	private Long id;
	private String firstName;
	private String lastName;
	private Date dateJoined;
	private Long wowPoints;
	private Location location;
	
	public Player() {
		super();
	}

	public Player(Long id, String firstName, String lastName, Date dateJoined, Long wowPoints, Location location) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateJoined = dateJoined;
		this.wowPoints = wowPoints;
		this.location = location;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateJoined() {
		return dateJoined;
	}

	public void setDateJoined(Date dateJoined) {
		this.dateJoined = dateJoined;
	}

	public Long getWowPoints() {
		return wowPoints;
	}

	public void setWowPoints(Long wowPoints) {
		this.wowPoints = wowPoints;
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
		result = prime * result + ((dateJoined == null) ? 0 : dateJoined.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((wowPoints == null) ? 0 : wowPoints.hashCode());
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
		Player other = (Player) obj;
		if (dateJoined == null) {
			if (other.dateJoined != null)
				return false;
		} else if (!dateJoined.equals(other.dateJoined))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (wowPoints == null) {
			if (other.wowPoints != null)
				return false;
		} else if (!wowPoints.equals(other.wowPoints))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dateJoined=" + dateJoined
				+ ", wowPoints=" + wowPoints + ", location=" + location + "]";
	}

	@Override
	public int compareTo(Player that) {
		if (this == that) return 0;

		// primitive numbers follow this form
		if (this.id != null && that.id != null) {
			if (this.id != null && this.id < that.id) return -1;
			if (this.id != null && this.id > that.id) return 1;
		}

		// compare firstName
		if (this.firstName != null && that.firstName != null) {
			int comparison = this.firstName.compareTo(that.firstName);
			if (comparison != 0) return comparison;
		}

		// compare lastName
		if (this.lastName != null && that.lastName != null) {
			int comparison = this.lastName.compareTo(that.lastName);
			if (comparison != 0) return comparison;
		}

		// compare location
		if (this.location != null && that.location != null) {
			int comparison = this.location.compareTo(that.location);
			if (comparison != 0) return comparison;
		}

		return 0;
	}
}
