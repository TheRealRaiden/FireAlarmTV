package ch.firealarmtv.objects;

import java.util.ArrayList;

public class AssetTrain {

	private int id;
	private ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();

	/**
	 * @param id
	 */
	public AssetTrain(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isThisTrain(int id) {
		return this.id == id;
	}

	public ArrayList<Vehicle> getVehiclelList() {
		return vehicleList;
	}

	public Vehicle getVehicle(int id) {
			return vehicleList.get(id);

	}

	public void addVehicle(Vehicle vehicle) {
		vehicleList.add(vehicle);
	}


}
