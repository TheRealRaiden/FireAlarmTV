package ch.firealarmtv.application;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import ch.firealarmtv.config.PropertiesConfigHandler;
import ch.firealarmtv.handlers.AssetTrainsHandler;
import ch.firealarmtv.handlers.Console;
import ch.firealarmtv.map.MapHandler;
import ch.firealarmtv.model.Alarm;
import ch.firealarmtv.model.AssetTrain;
import ch.firealarmtv.model.Personnel;
import ch.firealarmtv.model.Vehicle;
import ch.firealarmtv.service.AlarmStompSessionHandler;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

	// Configs & Logs
	private static PropertiesConfigHandler config;
	public static Logger logger = LoggerFactory.getLogger(App.class);

	// Alarm handling fields
	private ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
	private Alarm currentAlarm;
	private static AssetTrainsHandler trainHandler;

	// JFX Application basics
	private BorderPane root;
	private Scene scene;

	// Screen resolution fields
	private static int screenWidth;
	private static int screenHeight;

	// Content fields
	private Rectangle headerBg;
	private Rectangle footerBg;
	private VBox vbPastAlarms;
	private Text headerAlarmType;
	private Text headerAlarmDate;
	private Text headerAlarmTime;
	private Text headerAlarmAddr;
	private Text headerAlarmRemarks;

	// Animation fields
	private StackPane headerStackPane;
	private StackPane footerStackPane;
	private FillTransition animHeaderBg;
	private FillTransition animFooterBg;
	private FillTransition animAlarmType;
	private FillTransition animAlarmAddr;

	// Map
	private MapView mapView;

	public static void main(String[] args) {
		// config = new PropertiesConfigHandler(args[0]);

		Console.writeln("================================================");
		Console.writeln("FireAlarmTV\n", Console.ANSI_GREEN);
		Console.writeln("Vers.:\t2.0");
		Console.writeln("Author:\tTarek Amiri");
		Console.writeln("CSP:\tBataillon SP Fribourg - Compagnie Moncor");
		Console.writeln("================================================\n");

		Console.writeln("Pre-startup process:", Console.ANSI_GREEN);
		preStartUp();

		Console.writeln("\nLaunching display...:", Console.ANSI_GREEN);
		launch();
		
		
	}

	public static void preStartUp() {

		Console.write("> Detecting screen resolution: ");
		determineScreenResolution();

		Console.write("> Loading startup file: ");
		String configFile = "/Users/panda/Documents/Firefighters/FireAlarmTV/2.0/config.properties";
		Console.writeln(configFile, Console.ANSI_PURPLE);
		config = new PropertiesConfigHandler(configFile);

		Console.write("> Loading alarm train file: ");
		String alarmTrainFile = config.getStr("train.config.file");
		Console.writeln(alarmTrainFile, Console.ANSI_PURPLE);
		trainHandler = new AssetTrainsHandler(config);		

	}

	@Override
	public void start(Stage stage) throws Exception {	
		
		//AlarmStompSessionHandler session = new AlarmStompSessionHandler(this, config);

		// For testing only:
		createListofTestAlarms();
		currentAlarm = alarmList.get(alarmList.size() - 1);

		// Set the root structure
		root = new BorderPane();

		// Set Alarm Header
		root.setTop(createAlarmHeader());

		// Set leftHbox for Alarm History
		root.setLeft(createPastAlarms());

		// Set center for Map
		root.setCenter(createMapContainer());

		// Set footer for fire engines and ressources
		root.setBottom(createFooter());

		scene = new Scene(root, config.getInt("scene.width"), config.getInt("scene.height"));

		// Set Title, icon and maximize the window to entire screen
		stage.setTitle(config.getStr("stage.title"));
		stage.getIcons().add(new Image(config.getStr("stage.icon")));
		// stage.setMaximized(config.getBool("stage.maximized"));
		stage.setFullScreen(true);
		stage.setScene(scene);

		stage.show();

		// ==================================================
		// Testing part for dev only
		// ==================================================

		activateAlarm(currentAlarm);

		// ==================================================
	}

	private StackPane createAlarmHeader() {

		headerStackPane = new StackPane();

		// Create and size the Background
		headerStackPane.setAlignment(Pos.TOP_LEFT);
		headerBg = new Rectangle();
		headerBg.setFill(Color.TRANSPARENT);
		headerBg.setStrokeType(StrokeType.OUTSIDE);
		headerBg.setStroke(Color.LIGHTGRAY);
		// rec.widthProperty().bind(stackPane.widthProperty());
		headerBg.setWidth(screenWidth);
		// rec.heightProperty().bind(stackPane.heightProperty());
		headerBg.setHeight(screenHeight * 0.17);

		// Add every thing
		headerStackPane.getChildren().add(headerBg);

		return headerStackPane;

	}

	private VBox createPastAlarms() {

		// Create a container which is vertically filled
		vbPastAlarms = new VBox();

		// Set Border only on the right side
		vbPastAlarms.setBorder(new Border(new BorderStroke(null, Color.DARKGRAY, null, null, BorderStrokeStyle.NONE,
				BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, CornerRadii.EMPTY,
				new BorderWidths(1), Insets.EMPTY)));

		// Create the Title
		VBox vbTitle = new VBox();
		Text titleText = new Text("Historique Alarmes");
		titleText.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
		vbTitle.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		vbTitle.setPadding(new Insets(10, 0, 10, 20));
		vbTitle.getChildren().add(titleText);

		// VBox Container to write all the past alarms
		VBox vbAlarmContent = new VBox();
		vbAlarmContent.setPadding(new Insets(10, 20, 0, 20));

		// Iterate through list of alarms and populate the panel
		for (int i = alarmList.size() - 1; i > -1; i--) {
			writePastAlarm(alarmList.get(i), vbAlarmContent);
		}

		vbPastAlarms.getChildren().addAll(vbTitle, vbAlarmContent);
		// vBox.getChildren().addAll(vbox, new Separator(Orientation.VERTICAL));

		return vbPastAlarms;

	}

	private VBox createAssetTrain(Alarm alarm) throws IOException {

		// Create a container which is vertically filled
		VBox vbox = new VBox();

		// Set Border only on the right side
		vbox.setBorder(new Border(new BorderStroke(null, Color.DARKGRAY, null, null, BorderStrokeStyle.NONE,
				BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, CornerRadii.EMPTY,
				new BorderWidths(1), Insets.EMPTY)));

		// Create the Title
		VBox vbTitle = new VBox();
		Text titleText = new Text("Train de sortie 1er échelon");
		titleText.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
		vbTitle.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		vbTitle.setPadding(new Insets(10, 0, 10, 20));
		vbTitle.getChildren().add(titleText);

		vbox.getChildren().add(vbTitle);

		// Get the asset train for the alarm
		AssetTrain train = trainHandler.getTrainFromId(alarm.getTrainType());
		ArrayList<Vehicle> vhcList = train.getVehiclelList();

		// Iterate through list of vehicles from alarm train
		for (int i = 0; i < vhcList.size(); i++) {
			// Create HBox Container for each vehicle
			HBox hbCurrentVhc = new HBox();
			hbCurrentVhc.setPadding(new Insets(10, 0, 20, 20));
			hbCurrentVhc.setAlignment(Pos.TOP_LEFT);

			// Get the current vehicle
			Vehicle curVhc = vhcList.get(i);

			// =========================================================================
			// Add the image to the left

			HBox hbVhcImgContainer = new HBox();
			hbVhcImgContainer.setPadding(new Insets(10, 0, 0, 0));
			FileInputStream input = new FileInputStream(
					config.getStr("train.img.prefix") + "/" + curVhc.getName() + config.getStr("train.img.suffix"));
			ImageView imgVhc = new ImageView(new Image(input));
			imgVhc.setPreserveRatio(true);
			imgVhc.setFitWidth(150);
			hbVhcImgContainer.getChildren().add(imgVhc);
			hbCurrentVhc.getChildren().add(hbVhcImgContainer);
			input.close();
			// =========================================================================

			// Create a new VBox to the right for the personnel (content must be centered)
			VBox vbPersContainer = new VBox();
			vbPersContainer.setPadding(new Insets(10, 20, 20, 20));

			// Get the personnel list from current vehicle
			ArrayList<Personnel> listPers = curVhc.getPersonnelList();

			HBox hbLine = new HBox();

			int persCounter = 0;

			// Iterate through personnel
			for (int j = 0; j < listPers.size(); j++) {

				// Get the Personnel
				Personnel pers = listPers.get(j);

				FileInputStream inputPers = new FileInputStream(
						config.getStr("train.img.prefix") + "/" + pers.getName() + config.getStr("train.img.suffix"));
				ImageView imgPers = new ImageView(new Image(inputPers));
				imgPers.setPreserveRatio(true);
				imgPers.setFitHeight(100);

				if (j > 1 && persCounter == 0) {
					persCounter++;
					vbPersContainer.getChildren().add(hbLine);
					hbLine = new HBox();

				} else if (j > 1 && persCounter > 0 && persCounter != 4) {
					persCounter++;
				} else if (persCounter == 4) {
					persCounter = 0;
					vbPersContainer.getChildren().add(hbLine);
					hbLine = new HBox();
				}

				hbLine.getChildren().add(imgPers);

				inputPers.close();

			}

			// Add the personnel line to container
			vbPersContainer.getChildren().add(hbLine);

			// Add personnel container to vehocle
			hbCurrentVhc.getChildren().add(vbPersContainer);

			// Add the current vehicle and personnel container to the train container
			vbox.getChildren().add(hbCurrentVhc);
		}

		return vbox;

	}

	private StackPane createMapContainer() {
		StackPane stackPane = new StackPane();
		stackPane.setAlignment(Pos.CENTER);

		Rectangle rec = new Rectangle();
		rec.setFill(Color.TRANSPARENT);
		rec.widthProperty().bind(stackPane.widthProperty().subtract(50));
		rec.heightProperty().bind(stackPane.heightProperty().subtract(50));

		stackPane.getChildren().addAll(rec);

		return stackPane;
	}

	private StackPane createFooter() {

		footerStackPane = new StackPane();

		// Create and size the Background
		footerStackPane.setAlignment(Pos.CENTER);
		footerBg = new Rectangle();
		footerBg.setFill(Color.TRANSPARENT);
		// rec.widthProperty().bind(stackPane.widthProperty());
		footerBg.setWidth(screenWidth);
		// rec.heightProperty().bind(stackPane.heightProperty());
		footerBg.setHeight(40);

		// Add every thing
		footerStackPane.getChildren().add(footerBg);

		return footerStackPane;
	}

	private void writePastAlarm(Alarm alarm, VBox vBoxText) {

		String dateTimeStr = instantToDate(alarm.getReceivedAt(), "dd.MM.YY") + " - "
				+ instantToDate(alarm.getReceivedAt(), "HH:mm");
		Text dateTimeText = new Text(dateTimeStr);
		dateTimeText.setFont(Font.font("Helvetica", FontWeight.EXTRA_BOLD, 20));
		if (!alarm.isCancelled()) {
			dateTimeText.setFill(Color.RED);
		} else {
			dateTimeText.setFill(Color.GREEN);
		}
		Text alarmType = new Text(alarm.getType());
		Text alarmAdr = new Text(alarm.getAddress() + ", " + alarm.getCity());
		Text spacer = new Text("");

		vBoxText.getChildren().addAll(dateTimeText, alarmType, alarmAdr, spacer);
		vBoxText.setTranslateX(10);
	}

	private static void determineScreenResolution() {

		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		screenHeight = (int) screenBounds.getHeight();
		screenWidth = (int) screenBounds.getWidth();

		Console.writeln(screenWidth + "x" + screenHeight, Console.ANSI_PURPLE);

	}

	public void activateAlarm(@SuppressWarnings("exports") Alarm alarm) {

		// ====================================================================
		// Create and display Texts
		// ====================================================================

		// Alarm Type
		if (!alarm.isCancelled()) {
			headerAlarmType = new Text(alarm.getType());
		} else {
			headerAlarmType = new Text("QUITTANCE: " + alarm.getType());
		}
		headerAlarmType.setFont(Font.font(null, FontWeight.EXTRA_BOLD, config.getDbl("alarm.type.font.size")));
		headerAlarmType.setFill(Color.web(config.getStr("alarm.type.color1")));
		StackPane.setMargin(headerAlarmType, new Insets(10, 10, 10, 15));

		// Alarm Date

		headerAlarmDate = new Text(instantToDate(alarm.getReceivedAt(), "dd.MM.YYYY"));
		headerAlarmDate.setFont(Font.font(null, FontWeight.BOLD, config.getDbl("alarm.date.font.size")));
		headerAlarmDate.setFill(Color.web(config.getStr("alarm.date.color")));
		StackPane.setMargin(headerAlarmDate, new Insets(40, 10, 10, screenWidth - 220));

		// Alarm Time
		headerAlarmTime = new Text(instantToDate(alarm.getReceivedAt(), "HH:mm"));
		headerAlarmTime.setFont(Font.font(null, FontWeight.BOLD, config.getDbl("alarm.time.font.size")));
		headerAlarmTime.setFill(Color.web(config.getStr("alarm.time.color")));
		StackPane.setMargin(headerAlarmTime, new Insets(90, 0, 10, screenWidth - 95));

		// Alarm Address
		headerAlarmAddr = new Text(alarm.getAddress() + ", " + alarm.getCity());
		headerAlarmAddr.setFont(Font.font(null, FontWeight.BOLD, config.getDbl("alarm.address.font.size")));
		headerAlarmAddr.setFill(Color.web(config.getStr("alarm.address.color1")));
		StackPane.setMargin(headerAlarmAddr, new Insets(55, 10, 10, 15));

		// Alarm remarks / annex message
		headerAlarmRemarks = new Text("> " + alarm.getRemark());
		headerAlarmRemarks.setFont(Font.font(null, FontWeight.NORMAL, config.getDbl("alarm.remarks.font.size")));
		headerAlarmRemarks.setFill(Color.web(config.getStr("alarm.remarks.color1")));
		StackPane.setMargin(headerAlarmRemarks, new Insets(120, 10, 0, 15));

		// Add everything
		headerStackPane.getChildren().addAll(headerAlarmType, headerAlarmAddr, headerAlarmRemarks, headerAlarmDate,
				headerAlarmTime);

		// ====================================================================
		// Create and play animations
		// ====================================================================

		// Set the blinking header background
		animHeaderBg = new FillTransition();
		animHeaderBg.setDuration(Duration.millis(config.getDbl("anim.alarm.duration")));
		animHeaderBg.setInterpolator(Interpolator.EASE_BOTH);
		animHeaderBg.setShape(headerBg);
		animHeaderBg.setAutoReverse(true);
		animHeaderBg.setCycleCount(Animation.INDEFINITE);

		// Set the blinking footer background
		animFooterBg = new FillTransition();
		animFooterBg.setDuration(Duration.millis(config.getDbl("anim.alarm.duration")));
		animFooterBg.setInterpolator(Interpolator.EASE_BOTH);
		animFooterBg.setShape(footerBg);
		animFooterBg.setAutoReverse(true);
		animFooterBg.setCycleCount(Animation.INDEFINITE);

		// Set the blinking alarm type text
		animAlarmType = new FillTransition();
		animAlarmType.setDuration(Duration.millis(config.getDbl("anim.alarm.duration")));
		animAlarmType.setInterpolator(Interpolator.EASE_BOTH);
		animAlarmType.setShape(headerAlarmType);
		animAlarmType.setAutoReverse(true);
		animAlarmType.setCycleCount(Animation.INDEFINITE);

		// Set the blinking alarm address text
		animAlarmAddr = new FillTransition();
		animAlarmAddr.setDuration(Duration.millis(config.getDbl("anim.alarm.duration")));
		animAlarmAddr.setInterpolator(Interpolator.EASE_BOTH);
		animAlarmAddr.setShape(headerAlarmAddr);
		animAlarmAddr.setAutoReverse(true);
		animAlarmAddr.setCycleCount(Animation.INDEFINITE);
		animAlarmAddr.setToValue(Color.web(config.getStr("alarm.address.color3")));

		// Determine if it's a real alarm
		if (!alarm.isCancelled()) {

			animHeaderBg.setToValue(Color.web(config.getStr("header.bg.color1")));
			animHeaderBg.setFromValue(Color.TRANSPARENT);

			animFooterBg.setToValue(Color.web(config.getStr("footer.bg.color1")));
			animFooterBg.setFromValue(Color.TRANSPARENT);

			animAlarmType.setToValue(Color.web(config.getStr("alarm.type.color3")));

		} else {

			animHeaderBg.setToValue(Color.web(config.getStr("header.bg.color2")));
			animHeaderBg.setFromValue(Color.TRANSPARENT);

			animFooterBg.setToValue(Color.web(config.getStr("footer.bg.color2")));
			animFooterBg.setFromValue(Color.TRANSPARENT);

			animAlarmType.setToValue(Color.web(config.getStr("alarm.type.color3")));
		}

		// ====================================================================
		// Create map with alarm
		// ====================================================================
		MapHandler mapHandler = new MapHandler();
		root.setCenter(mapHandler.createMap(alarm, config));

		// ====================================================================
		// Create asset train from alarm
		// ====================================================================
		if (!alarm.isCancelled()) {
			try {
				root.setLeft(createAssetTrain(alarm));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Play all the animations
		animHeaderBg.play();
		animAlarmType.play();
		animAlarmAddr.play();
		animFooterBg.play();

	}

	@SuppressWarnings("unused")
	private void resetAlarm() {
		// headerStackPane.getChildren().removeAll(headerAlarmType, headerAlarmAddr,
		// headerAlarmRemarks);
		animHeaderBg.stop();
		animAlarmType.stop();
		animAlarmAddr.stop();
		animFooterBg.stop();
		// Set Alarm Header
		root.setTop(createAlarmHeader());

		// Set leftHbox for Alarm History
		root.setLeft(createPastAlarms());

		// Set center for Map
		root.setCenter(createMapContainer());

		// Set footer for fire engines and ressources
		root.setBottom(createFooter());

	}

	private void createListofTestAlarms() {

		alarmList.add(new Alarm("2022-09-07T10:13:54.629422Z", "FEU - DANS/PROCHE BATIMENT", "CORMINBOEUF",
				"CHAMP-DE-LA-CROIX 67", 46.81034659920626, 7.101794369561745, "VILLA EN FEU", 1, false, false));

		alarmList.add(new Alarm("2022-09-15T01:35:54.629422Z", "PETITE INONDATION", "VILLARS-SUR-GLANE",
				"RUE DU CENTRE 22", 46.79484213607141, 7.133552569561309, "APPARTEMENT DUPONT", 4, false, false));

		alarmList.add(
				new Alarm("2022-09-22T14:35:57.629422Z", "SAUVETAGE", "VILLARS-SUR-GLANE", "RTE DE VILLARS-VERT 16",
						46.79953575706146, 7.129834569561478, "PERSONNE BLOQUE DANS ASCENCEUR", 5, false, false));

		alarmList.add(new Alarm("2022-09-25T18:30:57.629422Z", "FEU DANS/PROCHE BATIMENT", "VILLARS-SUR-GLANE",
				"RTE DU BUGNON 6", 46.7947442014621, 7.10804665265526, "PARKING EN FEU", 1, false, false));

		alarmList.add(new Alarm("2022-09-25T18:30:57.629422Z", "FEU DANS/PROCHE BATIMENT", "CORMINBOEUF",
				"CHAMP-DE-LA-CROIX 67", 46.81035209804639, 7.101784997205363, "VILLA EN FEU", 1, false, false));

	}

	private String instantToDate(String instantStr, String format) {

		Instant instant = Instant.parse(instantStr);
		Date myDate = Date.from(instant);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(myDate);

	}

}