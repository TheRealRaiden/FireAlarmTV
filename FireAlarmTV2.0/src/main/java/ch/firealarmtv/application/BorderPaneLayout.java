package ch.firealarmtv.application;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class BorderPaneLayout {
	
	public static BorderPane createBorderPane() {
		BorderPane borderpane = new BorderPane();
		
		//borderpane.getStyleClass().add("bg-red");
		
		Label top = createLabel("Top", "bg-red");
		borderpane.setTop(top);
		
		Label left = createLabel("Left", "bg-blue");
		borderpane.setLeft(left);
		
		return borderpane;
	}
	
	private static Label createLabel(String text, String styleClass) {
		Label label = new Label(text);
		label.getStyleClass().add(styleClass);
		return label;
	}

}
