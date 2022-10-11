package ch.firealarmtv.objects;

public class Alarm {

	private String receivedAt; // Time of alarm reception in backend
	private String type; // Alarm Type
	private String city; // City name
	private String address; // Address String
	private double lat; // Latitude coordinate of alarm
	private double lon; // Longitude coordinate of alarm
	private String remark; // Remark / Annex field of alarm
	private String rawText; // Raw alarm text
	private String normalizedText; // Normalized alarm text
	private int assetTrainID; // ID of Assets Train Type
	private boolean isCancelled; // To identify cancellation alarms
	private boolean isInfo;

	/**
	 * @param receivedAt
	 * @param type
	 * @param city
	 * @param address
	 * @param lat
	 * @param lon
	 * @param remark
	 * @param rawText
	 * @param normalizedText
	 * @param assetTrainID
	 * @param isCancelled
	 * @param isInfo
	 */
	public Alarm(String receivedAt, String type, String city, String address, double lat, double lon, String remark,
			String rawText, String normalizedText, int assetTrainID, boolean isCancelled, boolean isInfo) {
		super();
		this.receivedAt = receivedAt;
		this.type = type;
		this.city = city;
		this.address = address;
		this.lat = lat;
		this.lon = lon;
		this.remark = remark;
		this.rawText = rawText;
		this.normalizedText = normalizedText;
		this.assetTrainID = assetTrainID;
		this.isCancelled = isCancelled;
		this.isInfo = isInfo;
	}

	public Alarm(String receivedAt, String type, String city, String address, double lat, double lon, String remark,
			int assetTrainID, boolean isCancelled, boolean isInfo) {
		super();
		this.receivedAt = receivedAt;
		this.type = type;
		this.city = city;
		this.address = address;
		this.lat = lat;
		this.lon = lon;
		this.remark = remark;
		this.assetTrainID = assetTrainID;
		this.isCancelled = isCancelled;
		this.isInfo = isInfo;
	}

	public String getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(String receivedAt) {
		this.receivedAt = receivedAt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public String getNormalizedText() {
		return normalizedText;
	}

	public void setNormalizedText(String normalizedText) {
		this.normalizedText = normalizedText;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public int getTrainType() {
		return assetTrainID;
	}

	public void setTrainType(int trainType) {
		this.assetTrainID = trainType;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public int getAssetTrainID() {
		return assetTrainID;
	}

	public void setAssetTrainID(int assetTrainID) {
		this.assetTrainID = assetTrainID;
	}

	public boolean isInfo() {
		return isInfo;
	}

	public void setInfo(boolean isInfo) {
		this.isInfo = isInfo;
	}

}
