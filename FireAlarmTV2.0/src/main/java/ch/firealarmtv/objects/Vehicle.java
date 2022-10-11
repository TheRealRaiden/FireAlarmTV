package ch.firealarmtv.objects;

import java.util.ArrayList;

public class Vehicle {

	private String name;
	private ArrayList<Personnel> personnelList = new ArrayList<Personnel>();

	/**
	 * @param name
	 */
	public Vehicle(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Personnel> getPersonnelList() {
		return personnelList;
	}

	/**
	 * @param id
	 */
	public Personnel getPersonnel(int id) {
		return personnelList.get(id);
	}

	/**
	 * @param personnel
	 */
	public void addPersonnel(Personnel personnel) {
		personnelList.add(personnel);
	}

}
