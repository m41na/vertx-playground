package com.practicaldime.tutorial.entity;

import io.vertx.core.json.JsonObject;

public class Location implements Comparable<Location> {

	private Long id;
	private String city;
	private String state;
	private String zip;
	private String country;

	public Location() {
		super();
	}

	public Location(Long id, String city, String state, String zip, String country) {
		super();
		this.id = id;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}
	
	public Location(JsonObject row) {
		setId(row.getLong("location_id"));
		setCity(row.getString("loc_city"));
		setState(row.getString("loc_state"));
		setZip(row.getString("loc_zip"));
		setCountry(row.getString("country_code"));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
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
		Location other = (Location) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", city=" + city + ", state=" + state + ", zip=" + zip + ", country=" + country
				+ "]";
	}

	@Override
	public int compareTo(Location that) {
		if (this == that) return 0;

		// primitive numbers follow this form
		if (this.id != null && that.id != null) {
			if (this.id != null && this.id < that.id) return -1;
			if (this.id != null && this.id > that.id) return 1;
		}

		// compare city
		if (this.city != null && that.city != null) {
			int comparison = this.city.compareTo(that.city);
			if (comparison != 0) return comparison;
		}

		// compare state
		if (this.state != null && that.state != null) {
			int comparison = this.state.compareTo(that.state);
			if (comparison != 0) return comparison;
		}

		// compare country
		if (this.country != null && that.country != null) {
			int comparison = this.country.compareTo(that.country);
			if (comparison != 0) return comparison;
		}

		return 0;
	}
}
