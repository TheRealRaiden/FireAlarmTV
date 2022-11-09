package ch.firealarmtv.handlers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;

import ch.firealarmtv.config.PropertiesConfigHandler;
import ch.firealarmtv.model.Alarm;
import ch.firealarmtv.model.Coordinate;
import ch.firealarmtv.model.Hydrant;

public class HydrantsHandler {

	public ArrayList<Hydrant> getHydrantsForAlarm(PropertiesConfigHandler config, Alarm alarm) {

		ArrayList<Hydrant> hydrantList = new ArrayList<Hydrant>();

		Coordinate alarmPosWGS = new Coordinate(alarm.getLat(), alarm.getLon(), Coordinate.WGS84);
		Coordinate alarmPosLV = alarmPosWGS.getLV95();

		String query = config.getStr("map.hydrants.api") + "?where=1%3D1&objectIds=&time=&geometry="
				+ alarmPosLV.getLon() + "%2C" + alarmPosLV.getLat()
				+ "&geometryType=esriGeometryPoint&inSR=&spatialRel=esriSpatialRelIntersects" + "&distance="
				+ config.getInt("map.hydrants.api.radius")
				+ "&units=esriSRUnit_Meter&relationParam=&outFields=No_hydrant&returnGeometry=true"
				+ "&maxAllowableOffset=&geometryPrecision=&outSR=&havingClause=&gdbVersion=&historicMoment="
				+ "&returnDistinctValues=false&returnIdsOnly=false&returnCountOnly=false&returnExtentOnly=false"
				+ "&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false"
				+ "&multipatchOption=xyFootprint&resultOffset=&resultRecordCount=&returnTrueCurves=false"
				+ "&returnExceededLimitFeatures=false&quantizationParameters=&returnCentroid=false&sqlFormat=none"
				+ "&resultType=&featureEncoding=esriDefault&datumTransformation=&f="
				+ config.getStr("map.hydrants.api.type");

		System.out.println(query);

		// Execute the JSon and get the result
		JsonNode root = null;

		try {
			root = JsonHandler.getJson(new URL(query));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Iterate through the node "features" and get all child nodes to get all
		if (root != null) {
			JsonNode pathResult = root.path("features");

			String json = root.toString();

			int size = pathResult.size();

			for (int i = 0; i < size; i++) {
				String id = JsonPath.read(json, "$.features[" + i + "].properties.No_hydrant");
				Double lat = JsonPath.read(json, "$.features[" + i + "].geometry.coordinates[1]");
				Double lon = JsonPath.read(json, "$.features[" + i + "].geometry.coordinates[0]");
				hydrantList.add(new Hydrant(id, lat, lon));
			}
		}

		return hydrantList;

	}

}
