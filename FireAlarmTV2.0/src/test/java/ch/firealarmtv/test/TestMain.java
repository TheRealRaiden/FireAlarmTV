package ch.firealarmtv.test;

import java.util.ArrayList;

import ch.firealarmtv.config.PropertiesConfigHandler;
import ch.firealarmtv.handlers.AssetTrainsHandler;
import ch.firealarmtv.model.AssetTrain;
import ch.firealarmtv.model.Personnel;
import ch.firealarmtv.model.Vehicle;

public class TestMain {

	public static void main(String[] args) {
		/*
		 * String file =
		 * "/Users/panda/Documents/Firefighters/FireAlarmTV/2.0/assetTrains.xml";
		 * 
		 * AssetTrainsHandler handler = new AssetTrainsHandler();
		 * handler.parseXml(file);
		 * 
		 * AssetTrain train = handler.getTrainFromId(1);
		 * 
		 */
		
		PropertiesConfigHandler config = new PropertiesConfigHandler("/Users/panda/Documents/Firefighters/FireAlarmTV/2.0/config.properties");
		
		AssetTrainsHandler handler = new AssetTrainsHandler(config);
		
		AssetTrain train = handler.getTrainFromId(1);
		
		ArrayList<Vehicle> listVhc = train.getVehiclelList();
		
		System.out.println("Number of vhc:"+listVhc.size());
		
		for(int i=0; i<listVhc.size();i++) {
			Vehicle vhc = listVhc.get(i);
			
			System.out.println(vhc.getName());
			
			ArrayList<Personnel> listPers = vhc.getPersonnelList();
			for(int j=0; j<listPers.size(); j++) {
				
				Personnel pers = listPers.get(j);
				System.out.println("\t"+pers.getName());
				
			}
		}
		
		

	}

}
