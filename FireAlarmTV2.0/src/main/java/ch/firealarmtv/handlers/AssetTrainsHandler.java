package ch.firealarmtv.handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ch.firealarmtv.config.PropertiesConfigHandler;
import ch.firealarmtv.objects.AssetTrain;
import ch.firealarmtv.objects.Personnel;
import ch.firealarmtv.objects.Vehicle;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class AssetTrainsHandler {

	private PropertiesConfigHandler config;
	private ArrayList<AssetTrain> assetTrainList = new ArrayList<AssetTrain>();

	public AssetTrainsHandler(PropertiesConfigHandler config) {
		this.config = config;
		parseXml(this.config.getStr("train.config.file"));
	}
	
	public AssetTrain getTrainFromId(int id) {
		
		for(int i=0;i<assetTrainList.size();i++) {
			if(assetTrainList.get(i).isThisTrain(id))
				return assetTrainList.get(i);
		}
		return null;
	}

	private void parseXml(String assetXmlFilePath) {

		File inputFile = new File(assetXmlFilePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nTrainList = doc.getElementsByTagName("train");
			//System.out.println("----------------------------");

			// Iterate through all <train> elements
			for (int i = 0; i < nTrainList.getLength(); i++) {

				// Create individual train node
				Node nTrain = nTrainList.item(i);
				//System.out.print("\nCurrent Train :");

				// Test is node is an element node
				if (nTrain.getNodeType() == Node.ELEMENT_NODE) {

					// Transform to Train Element
					Element eTrain = (Element) nTrain;

					// Get the train id from attribute
					int trainId = Integer.parseInt(eTrain.getAttribute("id"));
					//System.out.println(trainId);

					// Create new train object with id
					AssetTrain train = new AssetTrain(trainId);

					// Get all <vehicle> elements:
					NodeList nVehicleList = eTrain.getElementsByTagName("vehicle");

					// Iterate through all <vehicle> elements
					for (int j = 0; j < nVehicleList.getLength(); j++) {

						// Create individual vehicle node
						Node nVehicle = nVehicleList.item(j);

						// Test if node is an element node
						if (nVehicle.getNodeType() == Node.ELEMENT_NODE) {

							// Transform to Vehicle Element
							Element eVehicle = (Element) nVehicle;

							// Get vehicle name from attribute
							String vehicleName = eVehicle.getAttribute("name");
							//System.out.println("\t Vehicle:" + vehicleName);

							// Create new vehicle object with name
							Vehicle vehicle = new Vehicle(vehicleName);

							// Get all <personnel> from vehicle
							NodeList nPersonnelList = eVehicle.getElementsByTagName("personnel");

							// Iterate through all <personnel> elements
							for (int k = 0; k < nPersonnelList.getLength(); k++) {

								// Create individual personnel node
								Node nPersonnel = nPersonnelList.item(k);

								// Test if personnel is an element node
								if (nVehicle.getNodeType() == Node.ELEMENT_NODE) {

									// Get personnel element content
									String personnelName = nPersonnel.getTextContent();
									//System.out.println("\t\t Personnel:" + personnelName);
									
									// Create new personnel object with name
									Personnel personnel = new Personnel(personnelName);
									
									// Add personnel to current vehicle
									vehicle.addPersonnel(personnel);

								}
							}

							// Add vehicle to current Train
							train.addVehicle(vehicle);

						}

					}

					// Add current train to Train List
					assetTrainList.add(train);

				}
			}

		} catch (ParserConfigurationException e) {
			Console.writelnError("\t - Error while Loading Asset Train File! Exiting Application");
			System.exit(-1);
		} catch (SAXException e) {
			Console.writelnError("\t - Error while Loading Asset Train File! Exiting Application");
			System.exit(-1);
		} catch (IOException e) {
			Console.writelnError("\t - Error while Loading Asset Train File! Exiting Application");
			System.exit(-1);
		}
	}

}
