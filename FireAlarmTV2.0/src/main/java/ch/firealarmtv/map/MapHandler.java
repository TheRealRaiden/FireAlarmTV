package ch.firealarmtv.map;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import ch.firealarmtv.config.PropertiesConfigHandler;
import ch.firealarmtv.handlers.HydrantsHandler;
import ch.firealarmtv.objects.Alarm;
import ch.firealarmtv.objects.Hydrant;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MapHandler {
	
	public MapView createMap(Alarm alarm, PropertiesConfigHandler config) {

		// Create a new OSM view
		MapView mapView = new MapView();

		// Create the handler to help with map
		MapHandler mapHandler = new MapHandler();

		// Creat the marker for the alarm location
		MapPoint alarmLocation = new MapPoint(alarm.getLat(), alarm.getLon());
		mapHandler.addAlarmPoint(mapView, alarmLocation, config);

		// Center map on alarm location
		mapView.setCenter(alarmLocation);

		// Set the zoom level from config file
		mapView.setZoom(config.getDbl("map.zoom"));

		// Get all Hydrants for alarm and display them
		mapHandler.addHydrantsForAlarm(mapView, alarm, config);

		return mapView;
	}

	public void addAlarmPoint(MapView view, MapPoint alarmPoint, PropertiesConfigHandler config) {

		//MapPoint alarmLocation = new MapPoint(alarm.getLat(), alarm.getLon());
		AlarmMapLayer alarmLayer = new AlarmMapLayer();
		
		HBox hbPin = new HBox();
		hbPin.setLayoutX(config.getDbl("map.pin.offset.x"));
		hbPin.setLayoutY(config.getDbl("map.pin.offset.y"));

		FileInputStream imgPin=null;
		try {
			imgPin = new FileInputStream(config.getStr("app.path") + "/" + config.getStr("map.pin"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ImageView mapPin = new ImageView(new Image(imgPin));
		mapPin.setPreserveRatio(true);
		mapPin.setFitHeight(config.getDbl("map.pin.size"));
		//mapPin.setX(500);	
		hbPin.getChildren().add(mapPin);
		
		//TODO: Idea: maybe add a red circle with opacity around the location

		alarmLayer.addPoint(alarmPoint, hbPin);
		view.addLayer(alarmLayer);

	}
	
	public void addHydrantsForAlarm(MapView view, Alarm alarm, PropertiesConfigHandler config) {
		
		HydrantsHandler hydrantsHandler = new HydrantsHandler();
		ArrayList<Hydrant> hydrantList = hydrantsHandler.getHydrantsForAlarm(config, alarm);
		
		for(Hydrant hydrant : hydrantList) {
			addHydrant(view, hydrant, config);		
		}
		
	}
	
	public void addHydrant(MapView view, Hydrant hydrant, PropertiesConfigHandler config) {

		MapPoint hydrantPoint = new MapPoint(hydrant.getLat(), hydrant.getLon());
		AlarmMapLayer hydrantLayer = new AlarmMapLayer();
		
		StackPane spHydrant = new StackPane();
		spHydrant.setAlignment(Pos.CENTER);
		//If the hydrant is not in the right place correct offset here:
		//spHydrant.setLayoutX(config.getDbl("map.pin.offset.x"));
		//spHydrant.setLayoutY(config.getDbl("map.pin.offset.y"));
		
		Circle hyCircle = new Circle(17, Color.BLUE);
		hyCircle.setOpacity(70);	
		hyCircle.setStrokeType(StrokeType.OUTSIDE);
		hyCircle.setStroke(Color.WHITE);
		
		String hydrantNumber = String.valueOf(hydrant.getId());
		Text txtHydrantId = new Text(hydrantNumber);
		txtHydrantId.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 13));
		txtHydrantId.setFill(Color.WHITE);
		
		spHydrant.getChildren().addAll(hyCircle, txtHydrantId);


		hydrantLayer.addPoint(hydrantPoint, spHydrant);
		view.addLayer(hydrantLayer);

	}

}
