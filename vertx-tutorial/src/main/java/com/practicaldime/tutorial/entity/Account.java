package com.practicaldime.tutorial.entity;

public class Account implements Comparable<Account>{

	public static enum Status {
		PENDING, ACTIVE, INACTIVE, DISABLED
	}
	
	public static enum Role {
		GUEST, ROLE, ADMIN
	}
	
	private Player player;
	private String username;
	private char[] password;
	private String phoneNUm;
	private String recToken;
	private String mfaToken;
	private String mfaMethod;
	
	public Account() {
		super();
	}

	public Account(Player player, String username, char[] password, String phoneNUm, String recToken, String mfaToken,
			String mfaMethod) {
		super();
		this.player = player;
		this.username = username;
		this.password = password;
		this.phoneNUm = phoneNUm;
		this.recToken = recToken;
		this.mfaToken = mfaToken;
		this.mfaMethod = mfaMethod;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public String getPhoneNUm() {
		return phoneNUm;
	}

	public void setPhoneNUm(String phoneNUm) {
		this.phoneNUm = phoneNUm;
	}

	public String getRecToken() {
		return recToken;
	}

	public void setRecToken(String recToken) {
		this.recToken = recToken;
	}

	public String getMfaToken() {
		return mfaToken;
	}

	public void setMfaToken(String mfaToken) {
		this.mfaToken = mfaToken;
	}

	public String getMfaMethod() {
		return mfaMethod;
	}

	public void setMfaMethod(String mfaMethod) {
		this.mfaMethod = mfaMethod;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		Account other = (Account) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [player=" + player + ", username=" + username
				+ ", phoneNUm=" + phoneNUm + ", recToken=" + recToken + ", mfaMethod="
				+ mfaMethod + "]";
	}

	@Override
	public int compareTo(Account that) {
		if (this == that) return 0;

		// compare username
		if (this.username != null && that.username != null) {
			int comparison = this.username.compareTo(that.username);
			if (comparison != 0) return comparison;
		}

		// compare player
		if (this.player != null && that.player != null) {
			int comparison = this.player.compareTo(that.player);
			if (comparison != 0) return comparison;
		}

		return 0;
	}
}
