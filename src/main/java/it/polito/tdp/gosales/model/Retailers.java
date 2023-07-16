package it.polito.tdp.gosales.model;

import java.util.Objects;

public class Retailers implements Comparable<Retailers>{

	private int code;
	private String name;
	private String type;
	private String country;
	
	public Retailers(int code, String name, String type, String country) {
		super();
		this.code = code;
		this.name = name;
		this.type = type;
		this.country = country;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Retailers other = (Retailers) obj;
		return code == other.code;
	}

	@Override
	public String toString() {
		return name+"\n";
	}

	@Override
	public int compareTo(Retailers o) {
		// TODO Auto-generated method stub
		return this.name.compareTo(o.getName());
	}
	
	
	

	
	
}