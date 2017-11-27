import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

public class FlightBookerInterface extends Application{

	public static void main(String[] args){
		launch(args);
	}

	//ArrayLists
	ArrayList<Boolean> inSpan, outSpan, toddlerPass, babyPass;
	ArrayList<String> passengerFirstName, passengerLastName, passengerDNI, passengerDateOfBirth;

	//Arrays
	int[] prices = {100,120,80,40,240,200,60,150,140,250,280};
	String[] destinations = {"Cork (ORK)", "Madrid (MAD)", "St Brieuc (SBK)", "Jersey (JER)", "Paris (CDG)", "Stansted (STN)", "Malaga (APG)"};

	//Variables
	String ticketType = "", dayIn, dayOut, outboundFlightDetails, inboundFlightDetails, outboundLeavingA, outboundArrivingA, outboundLeavingB, outboundArrivingB, inboundLeavingA, 
			inboundArrivingA, inboundLeavingB, inboundArrivingB, dateA, outgoingLblTempB, ingoingLblTempB, ingoingLblTempA, outgoingTempA, outgoingLblTempA,
			buttonLbl, date, outboundLeaving, inboundLeaving;
	float priceOut, priceIn, tempPrice;
	Boolean weekendOut = false, weekendReturn = false, cardValid;
	int totalAdult, totalChild, totalToddler, totalBaby, adult=0, child=0, toddlerChild = 0, baby=0, numPeople = 0, flightPrice = 0, price, checkedPassengers, babies, children, adults, passNum = 1,
			checkBaggageFees, y = 20, dniReductions, dniCount = 0, checkedBagCount = 0, totalFlightPriceOneWay, totalFlightPriceReturn, totalFlightCost;

	//UI Elements
	Label dniLabel, title, outboundText, inboundText, inDateLbl, outDateLbl, adultLbl, childLbl, babyLbl, babyCount, adultCount, oldChildCount, childCount, destinationError,
	peopleError, oldChildLbl, adultPrice, childPrice, toddlerPrice, babyPrice, childNumLbl, outgoingLbl, ingoingLbl, outgoingLblB, ingoingLblB, passengerLabel,
	fNameLabel, lNameLabel, dobLabel;

	AnchorPane introPane, infoPane, outgoingTimeB, outgoingTimeA, ingoingTimeA, ingoingTimeB, passInfo;
	Button adultDown, adultUp, childDown, childUp, oldChildDown, oldChildUp, babyUp, babyDown, proceedWithBooking, outboundA, outboundB, inboundA, 
	inboundB, contButton, backButton, nextPass, dniCheck;

	ComboBox<String> outbound, inbound, cardType, cardExpMonth, cardExpYear, tickType;
	RadioButton onewayFlight, returnFlight, outgoingSelectionB, ingoingSelectionB, outgoingSelectionA, ingoingSelectionA;
	Tooltip tooltip;
	DatePicker inDate, outDate, dob;
	Stage primaryStage;
	Scene scene, passengerInfoScene, paymentScene, statementScene;
	ToggleGroup ingoing, outgoing;
	TextField firstName, lastName, dniNo, cardNumIn;

	// Final 

	String from, to, leavingDate, backDate, totalCheckedBags, totalDeductions, totalCost;

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Scene 1 - Main Scene
		introPane = new AnchorPane();
		introPane.setStyle("-fx-background-color:#19198c;");
		title = makeWhiteLabel("JaviAir - Flight Booker", "Arial", 55, 0, 0);
		title.setStyle("-fx-font-weight: bolder; -fx-font-size:55px;");
		title.setMaxWidth(Double.MAX_VALUE);
		AnchorPane.setLeftAnchor(title, 0.0);
		AnchorPane.setRightAnchor(title, 0.0);
		title.setAlignment(Pos.CENTER);
		title.setTranslateY(10);
		proceedWithBooking = makeButton("Proceed with Booking", "Arial", 16, 525, 390, 200);
		proceedWithBooking.setDisable(true);

		outbound = new ComboBox<String>();
		outbound.setPromptText("---");
		outbound.setValue("---");
		outbound.setTranslateY(100);
		outbound.setTranslateX(200);
		outbound.setStyle("-fx-font-family: Arial; -fx-font-size: 15px; -fx-background-radius: 15px;");

		adultPrice = makeWhiteLabel("", "Arial", 20, 400, 240);
		toddlerPrice = makeWhiteLabel("", "Arial", 20, 400, 290);
		childPrice = makeWhiteLabel("", "Arial", 20, 400, 340);
		babyPrice = makeWhiteLabel("", "Arial", 20, 400, 390);

		for(int i = 0; i < destinations.length; i++){
			outbound.getItems().add(destinations[i]);
		}
		outbound.setOnAction(e -> {
			inbound.getItems().clear();
			IncomingList();
		});
		outbound.setPrefWidth(150);
		outboundText = makeYellowLabel("From", "Arial", 20, 100, 100);

		inbound = new ComboBox<String>();
		inbound.setPromptText("---");
		inbound.setValue("---");
		inbound.setTranslateY(130);
		inbound.setTranslateX(200);
		inbound.setPrefWidth(150);
		inbound.setOnAction(c -> {
			if(!inbound.getValue().equals("---"))
				proceedWithBooking.setDisable(false);

			if(
					outbound.getValue().equals(destinations[0]) && inbound.getValue().equals(destinations[1]) ||
					outbound.getValue().equals(destinations[1]) && inbound.getValue().equals(destinations[0]) ||
					outbound.getValue().equals(destinations[0]) && inbound.getValue().equals(destinations[2]) ||
					outbound.getValue().equals(destinations[2]) && inbound.getValue().equals(destinations[0]) ||
					outbound.getValue().equals(destinations[4]) && inbound.getValue().equals(destinations[6]) ||
					outbound.getValue().equals(destinations[6]) && inbound.getValue().equals(destinations[4])
					){
				price = prices[0];
				adultPrice.setText("€"+ price);
				toddlerPrice.setText("€"+ price);
				childPrice.setText("€60");
				babyPrice.setText("Free");
			}
			else if(
					outbound.getValue().equals(destinations[0]) && inbound.getValue().equals(destinations[3]) ||
					outbound.getValue().equals(destinations[3]) && inbound.getValue().equals(destinations[0]) ||
					outbound.getValue().equals(destinations[5]) && inbound.getValue().equals(destinations[6]) ||
					outbound.getValue().equals(destinations[6]) && inbound.getValue().equals(destinations[5])
					){
				price = prices[1];
				adultPrice.setText("€"+ price);
				toddlerPrice.setText("€"+ price);
				childPrice.setText("€60");
				babyPrice.setText("Free");
			}
			else if(
					outbound.getValue().equals(destinations[1]) && inbound.getValue().equals(destinations[2]) ||
					outbound.getValue().equals(destinations[2]) && inbound.getValue().equals(destinations[1]) ||
					outbound.getValue().equals(destinations[1]) && inbound.getValue().equals(destinations[3]) ||
					outbound.getValue().equals(destinations[3]) && inbound.getValue().equals(destinations[1])
					){
				price = prices[5];
				adultPrice.setText("€"+ price);
				toddlerPrice.setText("€"+ price);
				childPrice.setText("€60");
				babyPrice.setText("Free");
			}
			else if(
					outbound.getValue().equals(destinations[2]) && inbound.getValue().equals(destinations[4]) ||
					outbound.getValue().equals(destinations[4]) && inbound.getValue().equals(destinations[2])
					){
				price = prices[7];
				adultPrice.setText("€"+ price);
				toddlerPrice.setText("€"+ price);
				childPrice.setText("€60");
				babyPrice.setText("Free");
			}
			else if(
					outbound.getValue().equals(destinations[2]) && inbound.getValue().equals(destinations[6]) ||
					outbound.getValue().equals(destinations[6]) && inbound.getValue().equals(destinations[2])
					){
				price = prices[8];
				adultPrice.setText("€"+ price);
				toddlerPrice.setText("€"+ price);
				childPrice.setText("€60");
				babyPrice.setText("Free");
			}
			else if(
					outbound.getValue().equals(destinations[0]) && inbound.getValue().equals(destinations[4]) ||
					outbound.getValue().equals(destinations[4]) && inbound.getValue().equals(destinations[0]) ||
					outbound.getValue().equals(destinations[2]) && inbound.getValue().equals(destinations[5]) ||
					outbound.getValue().equals(destinations[5]) && inbound.getValue().equals(destinations[2])
					){
				price = prices[2];
				adultPrice.setText("€"+ price);
				toddlerPrice.setText("€"+ price);
				childPrice.setText("€60");
				babyPrice.setText("Free");
			}
			else if(
					outbound.getValue().equals(destinations[3]) && inbound.getValue().equals(destinations[4]) ||
					outbound.getValue().equals(destinations[4]) && inbound.getValue().equals(destinations[3]) ||
					outbound.getValue().equals(destinations[3]) && inbound.getValue().equals(destinations[5]) ||
					outbound.getValue().equals(destinations[5]) && inbound.getValue().equals(destinations[3])
					){
				price = prices[9];
				adultPrice.setText("€"+ price);
				toddlerPrice.setText("€"+ price);
				childPrice.setText("€60");
				babyPrice.setText("Free");
			}
		});
		inboundText = makeYellowLabel("To", "Arial", 20, 100, 130);
		inbound.setStyle("-fx-font-family: Arial; -fx-font-size: 15px; -fx-background-radius: 15px;");

		onewayFlight = new RadioButton("One-Way");
		onewayFlight.setStyle("-fx-font-family: Arial; -fx-font-size: 20px; -fx-font-weight: bold;");
		onewayFlight.setTranslateX(100);
		onewayFlight.setTranslateY(180);
		onewayFlight.setTextFill(Color.YELLOW);
		returnFlight = new RadioButton("Return");
		returnFlight.setTranslateX(250);
		returnFlight.setTranslateY(180);
		returnFlight.setStyle("-fx-font-family: Arial; -fx-font-size: 20px; -fx-font-weight: bold;");
		returnFlight.setTextFill(Color.YELLOW);
		returnFlight.setSelected(true);

		ToggleGroup trip = new ToggleGroup();
		onewayFlight.setToggleGroup(trip);
		returnFlight.setToggleGroup(trip);

		onewayFlight.setOnAction(e -> {
			inDateLbl.setVisible(false);
			inDate.setVisible(false);
		});

		returnFlight.setOnAction(e -> {
			inDateLbl.setVisible(true);
			inDate.setVisible(true);
		});

		outDateLbl = makeYellowLabel("Departure Date", "Arial", 20, 380, 100);
		inDateLbl = makeYellowLabel("Return Date", "Arial", 20, 380, 130);

		adultLbl = makeYellowLabel("Adult (18+):", "Arial", 20, 100, 240);
		oldChildLbl = makeYellowLabel("Child (6-18):", "Arial", 20, 100, 290);
		childLbl = makeYellowLabel("Toddler (1-5):", "Arial", 20, 100, 340);
		babyLbl = makeYellowLabel("Baby (0-1):", "Arial", 20, 100, 390);
		childNumLbl = makeWhiteLabel("*Only 2 children under 6 Permitted per adult", "Arial", 10, 100, 430);

		adultDown = makeButton("-", "Arial", 15, 250, 240, 30);
		adultDown.setDisable(true);
		adultDown.setStyle("-fx-background-radius: 30px; ");
		adultDown.setOnAction(e -> {
			if(adult <= 1)
				adultDown.setDisable(true);

			oldChildUp.setDisable(false);
			childUp.setDisable(false);
			babyUp.setDisable(false);
			adultUp.setDisable(false);
			adult--;
			numPeople--;
			adultCount.setText("" + adult);
		});

		adultCount = makeWhiteLabel("0", "Arial", 20, 300, 240);

		adultUp = makeButton("+", "Arial", 15, 335, 240, 30);
		adultUp.setStyle("-fx-background-radius: 30px; ");
		adultUp.setOnAction(e -> {
			if(adult >= 7 || numPeople >= 7){
				oldChildUp.setDisable(true);
				childUp.setDisable(true);
				babyUp.setDisable(true);
				adultUp.setDisable(true);
			}
			adultDown.setDisable(false);
			adult++;
			numPeople++;
			adultCount.setText("" + adult);
		});

		childDown = makeButton("-", "Arial", 15, 250, 290, 30);
		childDown.setDisable(true);
		childDown.setStyle("-fx-background-radius: 30px; ");
		childDown.setOnAction(e -> {
			if(child  <= 1)
				childDown.setDisable(true);

			oldChildUp.setDisable(false);
			childUp.setDisable(false);
			babyUp.setDisable(false);
			adultUp.setDisable(false);
			child--;
			numPeople--;
			childCount.setText("" + child);

		});

		childCount = makeWhiteLabel("0", "Arial", 20, 300, 290);

		childUp = makeButton("+", "Arial", 15, 335, 290, 30);
		childUp.setStyle("-fx-background-radius: 30px; ");
		childUp.setOnAction(e -> {
			if(child >= 7 || numPeople >= 7){
				oldChildUp.setDisable(true);
				childUp.setDisable(true);
				babyUp.setDisable(true);
				adultUp.setDisable(true);
			}
			childDown.setDisable(false);
			child++;
			numPeople++;
			childCount.setText("" + child);
		});

		oldChildDown = makeButton("-", "Arial", 15, 250, 340, 30);
		oldChildDown.setDisable(true);
		oldChildDown.setStyle("-fx-background-radius: 30px; ");
		oldChildDown.setOnAction(e -> {
			if(toddlerChild <= 1){
				oldChildDown.setDisable(true);
			}
			oldChildUp.setDisable(false);
			childUp.setDisable(false);
			babyUp.setDisable(false);
			adultUp.setDisable(false);
			toddlerChild--;
			numPeople--;
			oldChildCount.setText("" + toddlerChild);
		});

		oldChildCount = makeWhiteLabel("0", "Arial", 20, 300, 340);

		oldChildUp = makeButton("+", "Arial", 15, 335, 340, 30);
		oldChildUp.setStyle("-fx-background-radius: 30px; ");
		oldChildUp.setOnAction(e -> {
			if(toddlerChild >= 7 || numPeople >= 7){
				oldChildUp.setDisable(true);
				childUp.setDisable(true);
				babyUp.setDisable(true);
				adultUp.setDisable(true);
			}
			oldChildDown.setDisable(false);
			toddlerChild++;
			numPeople++;
			oldChildCount.setText("" + toddlerChild);
		});

		babyDown = makeButton("-", "Arial", 15, 250, 390, 30);
		babyDown.setDisable(true);
		babyDown.setStyle("-fx-background-radius: 30px; ");
		babyDown.setOnAction(e -> {
			if(baby <= 1){
				babyDown.setDisable(true);
			}
			oldChildUp.setDisable(false);
			childUp.setDisable(false);
			babyUp.setDisable(false);
			adultUp.setDisable(false);
			baby--;
			numPeople--;
			babyCount.setText("" + baby);
		});

		babyCount = makeWhiteLabel("0", "Arial", 20, 300, 390);

		babyUp = makeButton("+", "Arial", 15, 335, 390, 30);
		babyUp.setStyle("-fx-background-radius: 30px; ");
		babyUp.setOnAction(e -> {
			if(baby >= 7 || numPeople >= 7){
				oldChildUp.setDisable(true);
				childUp.setDisable(true);
				babyUp.setDisable(true);
				adultUp.setDisable(true);
			}
			babyDown.setDisable(false);
			baby++;
			numPeople++;
			babyCount.setText("" + baby);
		});

		outDate = new DatePicker();
		outDate.setValue(LocalDate.now());
		outDate.setPrefWidth(150);
		outDate.setTranslateX(550);
		outDate.setTranslateY(100);
		outDate.setEditable(false);

		inDate = new DatePicker();
		inDate.setValue(outDate.getValue().plusDays(1));
		inDate.setPrefWidth(150);
		inDate.setTranslateX(550);
		inDate.setTranslateY(130);
		inDate.setEditable(false);

		Callback<DatePicker, DateCell> dayCellFactoryA = new Callback<DatePicker, DateCell>()
		{
			public DateCell call(final DatePicker datePicker)
			{
				return new DateCell()
				{
					@Override
					public void updateItem(LocalDate item, boolean empty)
					{
						super.updateItem(item, empty);

						long p = ChronoUnit.DAYS.between(outDate.getValue(), item);
						setTooltip(new Tooltip("You're going away for " + p + " days"));

						if (item.isBefore(LocalDate.now()) || item.isAfter(LocalDate.now().plusMonths(6))) {
							setDisable(true);
							setStyle("-fx-background-color: #DC143C;");

						}

						inDate.setValue(outDate.getValue().plusDays(1));

						if(item.getMonthValue()==3 || item.getMonthValue()==4){
							if((outbound.getValue().equals(destinations[2]) && inbound.getValue().equals(destinations[5]))||
									(outbound.getValue().equals(destinations[5]) && inbound.getValue().equals(destinations[2]))){
								setDisable(true);
								setStyle("-fx-background-color: #DC143C;");
							}
						}

						if(item.getMonthValue()==4){
							if((outbound.getValue().equals(destinations[4]) && inbound.getValue().equals(destinations[5]))||
									(outbound.getValue().equals(destinations[5]) && inbound.getValue().equals(destinations[4]))){
								setDisable(true);
								setStyle("-fx-background-color: #DC143C;");
							}
						}
					}
				};
			}
		};
		Callback<DatePicker, DateCell> dayCellFactoryB = new Callback<DatePicker, DateCell>()
		{
			public DateCell call(final DatePicker datePicker)
			{
				return new DateCell()
				{
					@Override
					public void updateItem(LocalDate item, boolean empty)
					{
						float tempPrice = price;
						super.updateItem(item, empty);

						long p = ChronoUnit.DAYS.between(outDate.getValue(), item);
						setTooltip(new Tooltip("You're going away for " + p + " days"));

						if (item.isBefore(outDate.getValue().plusDays(1)) || item.isAfter(LocalDate.now().plusMonths(6))) {
							setDisable(true);
							setStyle("-fx-background-color: #DC143C;");
						}

						if(item.getMonthValue()==3 || item.getMonthValue()==4){
							if((outbound.getValue().equals(destinations[2]) && inbound.getValue().equals(destinations[5]))||
									(outbound.getValue().equals(destinations[5]) && inbound.getValue().equals(destinations[2]))){
								setDisable(true);
								setStyle("-fx-background-color: #DC143C;");
							}
						}

						if(item.getMonthValue()==4){
							if((outbound.getValue().equals(destinations[4]) && inbound.getValue().equals(destinations[5]))||
									(outbound.getValue().equals(destinations[5]) && inbound.getValue().equals(destinations[4]))){
								setDisable(true);
								setStyle("-fx-background-color: #DC143C;");
							}
						}
					}
				};
			}
		};

		outDate.setDayCellFactory(dayCellFactoryA);
		inDate.setDayCellFactory(dayCellFactoryB);

		proceedWithBooking.setStyle("-fx-background-radius: 30px; ");


		peopleError = makeYellowLabel("", "Arial", 15, 500, 260);
		peopleError.setPrefWidth(300);
		peopleError.setWrapText(true);

		proceedWithBooking.setOnAction(e -> {

			Boolean passFlag = true;
			if(adult < 1){
				peopleError.setText("*1 adult required per booking. Maximum, 2 children under 6 are permitted per adult.");
				passFlag = false;
			}
			if(numPeople == 0){
				peopleError.setText("*1 adult required per booking. Maximum, 2 children under 6 are permitted per adult.");
				passFlag = false;
			}
			if((adult*2) < (toddlerChild+baby)){
				peopleError.setText("*1 adult required per booking. Maximum, 2 children under 6 are permitted per adult.");
				passFlag = false;
			}

			Boolean flag = getFlightPrices();
			dayOut = outDate.getValue().getDayOfWeek().toString();
			dayIn = inDate.getValue().getDayOfWeek().toString();


			if(dayOut.equals("FRIDAY") || dayOut.equals("SATURDAY") || dayOut.equals("SUNDAY")){
				weekendOut = true;
			}

			if(dayIn.equals("FRIDAY") || dayIn.equals("SATURDAY") || dayIn.equals("SUNDAY")){
				weekendReturn = true;
			}

			if(passFlag == true){
				if(flag == true){
					Scene TimeSelection = TimeSelection(primaryStage);
					primaryStage.setScene(TimeSelection);
				}
			}
		});

		introPane.getChildren().addAll(title, outbound, inbound, outboundText, inboundText, onewayFlight, returnFlight, outDateLbl, inDateLbl, 
				outDate, inDate, adultLbl, childLbl, babyLbl, adultDown, adultUp, childDown, childUp, babyDown, babyUp, adultCount, childCount, babyCount,
				proceedWithBooking, peopleError, oldChildUp, oldChildDown, oldChildLbl, oldChildCount, adultPrice, toddlerPrice,
				babyPrice, childPrice, childNumLbl);

		scene = new Scene(introPane, 800, 450);
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("file:icon.png"));
		primaryStage.setTitle("JaviAir - Fight Booker");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	public Scene TimeSelection(Stage primaryStage){
		y = 20;
		Stage stage = primaryStage;
		infoPane = new AnchorPane();
		date = outDate.getValue().toString().substring(8,10);
		Scene infoScene = null;
		Boolean[] twoTimes = GetTimes(); 
		Boolean out = twoTimes[0];
		Boolean in = twoTimes[1];

		if(outDate.getValue().toString().substring(5,7).equals("01")){date += " Jan ";}
		else if(outDate.getValue().toString().substring(5,7).equals("02")){date += " Feb ";}
		else if(outDate.getValue().toString().substring(5,7).equals("03")){date += " Mar ";}
		else if(outDate.getValue().toString().substring(5,7).equals("04")){date += " Apr ";}
		else if(outDate.getValue().toString().substring(5,7).equals("05")){date += " May ";}
		else if(outDate.getValue().toString().substring(5,7).equals("06")){date += " Jun ";}
		else if(outDate.getValue().toString().substring(5,7).equals("07")){date += " Jul ";}
		else if(outDate.getValue().toString().substring(5,7).equals("08")){date += " Aug ";}
		else if(outDate.getValue().toString().substring(5,7).equals("09")){date += " Sept ";}
		else if(outDate.getValue().toString().substring(5,7).equals("10")){date += " Oct ";}
		else if(outDate.getValue().toString().substring(5,7).equals("11")){date += " Nov ";}
		else if(outDate.getValue().toString().substring(5,7).equals("12")){date += " Dec ";}

		date += outDate.getValue().toString().substring(0,4);
		outgoingLbl = makeWhiteLabel("" + outbound.getValue() + " To " + inbound.getValue() + " on " + date, "Arial", 25, 20, y);

		outgoingLblTempA = outbound.getValue() + " to " + inbound.getValue() + "\t\t\t\t" + "Dept. " + outboundLeavingA + "\tArv. " + outboundArrivingA;
		if(out == true)
			outgoingLblTempB = outbound.getValue() + " to " + inbound.getValue() + "\t\t\t\t" + "Dept. " + outboundLeavingB + "\tArv. " + outboundArrivingB;
		ingoingLblTempA = inbound.getValue().toString() + " to " + outbound.getValue().toString() + "\t\t\t\t" + "Dept. " + inboundLeavingA + "\tArv. " + inboundArrivingA;
		if(out == true)
			ingoingLblTempB = inbound.getValue().toString() + " to " + outbound.getValue().toString() + "\t\t\t\t" + "Dept. " + inboundLeavingB + "\tArv. " + inboundArrivingB;
		Label outgoingLblA = makeBlackLabel(outgoingLblTempA,"Arial",20,60,15);
		if(out == true){
			outgoingLblB = makeBlackLabel(outgoingLblTempB,"Arial",20,60,15);
		}
		Label ingoingLblA = makeBlackLabel(ingoingLblTempA,"Arial",20,60,15);
		if(out == true){
			ingoingLblB = makeBlackLabel(ingoingLblTempB,"Arial",20,60,15);
		}


		outgoingSelectionA = new RadioButton("");
		outgoingSelectionA.setStyle("-fx-font-size: 20px;");
		outgoingSelectionA.setTranslateX(15);
		outgoingSelectionA.setTranslateY(10);
		outgoingSelectionA.setSelected(true);

		if(out == true){
			outgoingSelectionB = new RadioButton("");
			outgoingSelectionB.setStyle("-fx-font-size: 20px;");
			outgoingSelectionB.setTranslateX(15);
			outgoingSelectionB.setTranslateY(10);
		}

		outgoing = new ToggleGroup();
		outgoingSelectionA.setToggleGroup(outgoing);
		if(out == true)
			outgoingSelectionB.setToggleGroup(outgoing);

		outgoingTimeA = new AnchorPane();
		outgoingTimeA.setPrefSize(760, 50);
		outgoingTimeA.setTranslateX(20);
		outgoingTimeA.setTranslateY(70);
		outgoingTimeA.setStyle("-fx-background-color: #FFFFCC;");


		infoPane.setStyle("-fx-background-color:#19198c;");

		y += 50;
		outgoingTimeA = new AnchorPane();
		outgoingTimeA.setPrefSize(760, 50);
		outgoingTimeA.setTranslateX(20);
		outgoingTimeA.setTranslateY(y);
		outgoingTimeA.setStyle("-fx-background-color: #FFFFCC;");
		outgoingTimeA.getChildren().addAll(outgoingLblA, outgoingSelectionA);
		if(out == true){
			y += 60;
			outgoingTimeB = new AnchorPane();
			outgoingTimeB.setPrefSize(760, 50);
			outgoingTimeB.setTranslateX(20);
			outgoingTimeB.setTranslateY(y);
			outgoingTimeB.setStyle("-fx-background-color: #FFFFCC;");
			outgoingTimeB.getChildren().addAll(outgoingLblB, outgoingSelectionB);
		}
		y += 60;
		if(out == true){
			if(!returnFlight.isSelected()){
				contButton = makeButton("Continue with Booking", "Arial", 20, 500, y, 280);
				contButton.setStyle("-fx-background-radius: 30px;");
				infoPane.getChildren().addAll(outgoingLbl, outgoingTimeA, outgoingTimeB, contButton);
			}
			else
				infoPane.getChildren().addAll(outgoingLbl, outgoingTimeA, outgoingTimeB);
		}
		else{
			if(!returnFlight.isSelected()){
				backButton = makeButton("Back", "Arial", 20, 20, y, 100);
				backButton.setStyle("-fx-background-radius: 30px;");
				contButton = makeButton("Continue with Booking", "Arial", 20, 500, y, 280);
				contButton.setStyle("-fx-background-radius: 30px;");
				y+=50;
				infoPane.getChildren().addAll(outgoingLbl, outgoingTimeA, contButton, backButton);
			}
			else
				infoPane.getChildren().addAll(outgoingLbl, outgoingTimeA);
		}

		if(returnFlight.isSelected()){
			y += 20;
			dateA = inDate.getValue().toString().substring(8,10);
			if(inDate.getValue().toString().substring(5,7).equals("01")){dateA += " Jan ";}
			else if(inDate.getValue().toString().substring(5,7).equals("02")){dateA += " Feb ";}
			else if(inDate.getValue().toString().substring(5,7).equals("03")){dateA += " Mar ";}
			else if(inDate.getValue().toString().substring(5,7).equals("04")){dateA += " Apr ";}
			else if(inDate.getValue().toString().substring(5,7).equals("05")){dateA += " May ";}
			else if(inDate.getValue().toString().substring(5,7).equals("06")){dateA += " Jun ";}
			else if(inDate.getValue().toString().substring(5,7).equals("07")){dateA += " Jul ";}
			else if(inDate.getValue().toString().substring(5,7).equals("08")){dateA += " Aug ";}
			else if(inDate.getValue().toString().substring(5,7).equals("09")){dateA += " Sept ";}
			else if(inDate.getValue().toString().substring(5,7).equals("10")){dateA += " Oct ";}
			else if(inDate.getValue().toString().substring(5,7).equals("11")){dateA += " Nov ";}
			else if(inDate.getValue().toString().substring(5,7).equals("12")){dateA += " Dec ";}

			dateA += inDate.getValue().toString().substring(0,4);
			ingoingLbl = makeWhiteLabel("" + inbound.getValue() + " To " + outbound.getValue() + " on " + dateA, "Arial", 25, 20, y);


			ingoingSelectionA = new RadioButton("");
			ingoingSelectionA.setStyle("-fx-font-size: 20px;");
			ingoingSelectionA.setTranslateX(15);
			ingoingSelectionA.setTranslateY(10);

			ingoingSelectionA.setSelected(true);
			if(in == true){
				ingoingSelectionB = new RadioButton("");
				ingoingSelectionB.setStyle("-fx-font-size: 20px;");
				ingoingSelectionB.setTranslateX(15);
				ingoingSelectionB.setTranslateY(10);				
			}

			ingoing = new ToggleGroup();
			ingoingSelectionA.setToggleGroup(ingoing);
			if(in == true)
				ingoingSelectionB.setToggleGroup(ingoing);	
			y += 50;
			ingoingTimeA = new AnchorPane();
			ingoingTimeA.setPrefSize(760, 50);
			ingoingTimeA.setTranslateX(20);
			ingoingTimeA.setTranslateY(y);
			ingoingTimeA.setStyle("-fx-background-color: #FFFFCC;");
			ingoingTimeA.getChildren().addAll(ingoingLblA, ingoingSelectionA);

			if(in == true){
				y += 60;
				ingoingTimeB = new AnchorPane();
				ingoingTimeB.setPrefSize(760, 50);
				ingoingTimeB.setTranslateX(20);
				ingoingTimeB.setTranslateY(y);
				ingoingTimeB.setStyle("-fx-background-color: #FFFFCC;");
				ingoingTimeB.getChildren().addAll(ingoingLblB, ingoingSelectionB);
			}

			if(in == true){
				y+=60;
				contButton = makeButton("Continue with Booking", "Arial", 20, 500, y, 280);
				contButton.setStyle("-fx-background-radius: 30px;");
				backButton = makeButton("Back", "Arial", 20, 20, y, 100);
				backButton.setStyle("-fx-background-radius: 30px;");
				y+=50;
				infoPane.getChildren().addAll(ingoingLbl, ingoingTimeA, ingoingTimeB, contButton, backButton);
			}
			else{
				y+=60;
				contButton = makeButton("Continue with Booking", "Arial", 20, 500, y, 280);
				contButton.setStyle("-fx-background-radius: 30px;");
				backButton = makeButton("Back", "Arial", 20, 20, y, 100);
				backButton.setStyle("-fx-background-radius: 30px;");
				y+=50;
				infoPane.getChildren().addAll(ingoingLbl, ingoingTimeA, contButton, backButton);
			}
		}

		backButton.setOnAction(e -> {
			stage.setScene(scene);

		});

		contButton.setOnAction(e -> {
			Boolean flag = false;
			leavingDate = date;
			backDate = dateA;
			if(out == true){
				if(outgoingSelectionB.isSelected()){
					from = outgoingLblB.getText().substring(0, 35);
					flag = true;
					outboundLeaving = outboundLeavingB;
				}

			}
			if(outgoingSelectionA.isSelected()){
				from = outgoingLblA.getText().substring(0, 35);
				flag = true;
				outboundLeaving = outboundLeavingA;
			}
			if(returnFlight.isSelected()){
				if(in == true){
					if(ingoingSelectionB.isSelected()){
						to = ingoingLblB.getText().substring(0, 35);
						flag = true;
						inboundLeaving = inboundLeavingB;
					}
				}
				if(ingoingSelectionA.isSelected()){
					to = ingoingLblA.getText().substring(0, 35);
					flag = true;
					inboundLeaving = inboundLeavingA;
				}
			}

			if(flag == true){
				Scene passenger = EnteringPassengers(stage);
				stage.setScene(passenger);
			}
		});

		infoScene = new Scene(infoPane, 800, y);
		return infoScene;
	}
	public Scene EnteringPassengers(Stage primaryStage){
		totalAdult = adult;
		totalBaby = baby;
		totalToddler = toddlerChild;
		totalChild = child;

		Stage stage = primaryStage;
		passengerFirstName = new ArrayList<String>();
		passengerLastName = new ArrayList<String>();
		passengerDateOfBirth = new ArrayList<String>();
		passengerDNI = new ArrayList<String>();

		buttonLbl = "";

		if(passNum < numPeople){
			buttonLbl = "Next Passenger";
		}
		else{
			buttonLbl = "Continue to Payment";
		}
		passInfo = new AnchorPane();
		passInfo.setStyle("-fx-background-color:#19198c;");
		passengerLabel = makeWhiteLabel("Passenger #" + passNum, "Arial", 30, 20, 20);

		DatePicker dateOfBirth = new DatePicker();
		dateOfBirth.setValue(LocalDate.now());
		dateOfBirth.setPrefWidth(170);
		dateOfBirth.setTranslateX(160);
		dateOfBirth.setTranslateY(100);

		tickType = new ComboBox<String>();
		tickType.setPromptText("---");
		tickType.setValue("Ticket Type");
		tickType.setTranslateY(20);
		tickType.setTranslateX(250);
		tickType.setStyle("-fx-font-family: Arial; -fx-font-size: 15px; -fx-background-radius: 15px;");
		tickType.getItems().add("Adult");
		if(child != 0)
			tickType.getItems().add("Child");
		if(toddlerChild != 0)
			tickType.getItems().add("Toddler");
		if(baby != 0)
			tickType.getItems().add("Baby");

		dniCheck = makeButton("Check", "Arial", 15, 670, 100, 100);
		nextPass = makeButton("Next Passenger","Arial", 20, 50, 50, 200);
		fNameLabel = makeYellowLabel("First Name: ", "Arial", 20, 20, 60);
		firstName = MakeTextField(160, 60, 170);
		lNameLabel = makeYellowLabel("Last Name: ", "Arial", 20, 350, 60);
		lastName = MakeTextField(480, 60, 170);
		dobLabel = makeYellowLabel("Date Of Birth: ", "Arial", 20, 20, 100);

		firstName.setOnMouseClicked(e -> {
			firstName.setText("");
		});

		lastName.setOnMouseClicked(e -> {
			lastName.setText("");

		});

		dniLabel = makeYellowLabel("DNI No:", "Arial", 20, 350, 100);
		dniNo = MakeTextField(480, 100, 170);

		dniCheck.setOnMouseClicked(e -> {
			Boolean flag = false;
			if(dniNo.getLength()> 9)
				flag = verifyDni(dniNo.getText(), dniNo.getLength());

			if(flag == false){
				dniNo.setStyle("-fx-text-box-border: red;");
			}
			else{
				dniNo.setStyle("-fx-text-box-border: green;");
			}
		});

		CheckBox checkedBaggage = new CheckBox();
		checkedBaggage.setText("Checked Baggage (Extra fee of €15)");
		checkedBaggage.setFont(Font.font ("Arial", 20));	
		checkedBaggage.setTextFill(Color.YELLOW);
		checkedBaggage.setTranslateY(140);
		checkedBaggage.setTranslateX(20);

		Callback<DatePicker, DateCell> babyFac = new Callback<DatePicker, DateCell>()
		{
			public DateCell call(final DatePicker datePicker)
			{
				return new DateCell()
				{
					@Override
					public void updateItem(LocalDate item, boolean empty)
					{
						super.updateItem(item, empty);

						long p = ChronoUnit.DAYS.between(dateOfBirth.getValue(), item);
						if(item.isAfter(LocalDate.now())){
							setDisable(true);
							setStyle("-fx-background-color: #DC143C;");
						}
						if(item.isBefore(LocalDate.now().minusYears(1))){
							setDisable(true);
							setStyle("-fx-background-color: #DC143C;");
						}
					}
				};
			}
		};
		Callback<DatePicker, DateCell> toddlerFac = new Callback<DatePicker, DateCell>()
		{
			public DateCell call(final DatePicker datePicker)
			{
				return new DateCell()
				{
					@Override
					public void updateItem(LocalDate item, boolean empty)
					{
						super.updateItem(item, empty);

						long p = ChronoUnit.DAYS.between(dateOfBirth.getValue(), item);

						if(item.isAfter(LocalDate.now().minusYears(1))){
							setDisable(true);
							setStyle("-fx-background-color: #DC143C;");
						}
						if(item.isBefore(LocalDate.now().minusYears(5))){
							setDisable(true);
							setStyle("-fx-background-color: #DC143C;");
						}
					}
				};
			}
		};
		Callback<DatePicker, DateCell> childFac = new Callback<DatePicker, DateCell>()
		{
			public DateCell call(final DatePicker datePicker)
			{
				return new DateCell()
				{
					@Override
					public void updateItem(LocalDate item, boolean empty)
					{
						super.updateItem(item, empty);

						long p = ChronoUnit.DAYS.between(dateOfBirth.getValue(), item);

						if(item.isAfter(LocalDate.now().minusYears(6))){
							setDisable(true);
							setStyle("-fx-background-color: #DC143C;");
						}
						if(item.isBefore(LocalDate.now().minusYears(18))){
							setDisable(true);
							setStyle("-fx-background-color: #DC143C;");
						}
					}
				};
			}
		};


		firstName.setDisable(true);
		lastName.setDisable(true);
		dateOfBirth.setDisable(true);
		dniNo.setDisable(true);
		dniCheck.setVisible(true);
		nextPass.setDisable(true);
		dniCheck.setDisable(true);
		checkedBaggage.setDisable(true);

		tickType.setOnAction(e -> {
			checkedBaggage.setDisable(false);
			firstName.setDisable(false);
			lastName.setDisable(false);
			dateOfBirth.setDisable(false);
			dniNo.setDisable(false);
			dniCheck.setDisable(false);
			nextPass.setDisable(false);

			try{
				if(tickType.getValue().equals("Adult")){
					dateOfBirth.setDisable(true);
				}
			}catch(NullPointerException ex){

			}
			try{
				if(tickType.getValue().equals("Baby")){
					checkedBaggage.setDisable(true);
					dateOfBirth.setDayCellFactory(babyFac);
					dateOfBirth.setValue(LocalDate.now());
					dniNo.setDisable(true);
					dniLabel.setDisable(true);
					dniCheck.setDisable(true);
				}
			}catch(NullPointerException ex){

			}
			try{
				if(tickType.getValue().equals("Child")){
					dateOfBirth.setDayCellFactory(childFac);
					dateOfBirth.setValue(LocalDate.now().minusYears(6));
				}
			}catch(NullPointerException ex){

			}
			try{
				if(tickType.getValue().equals("Toddler")){
					dateOfBirth.setValue(LocalDate.now().minusYears(1));
					dateOfBirth.setDayCellFactory(toddlerFac);
				}
			}catch(NullPointerException ex){

			}
		});


		if(!outbound.getValue().equals(destinations[1]) && !outbound.getValue().equals(destinations[6]) 
				&& !inbound.getValue().equals(destinations[1]) && !inbound.getValue().equals(destinations[6])){
			dniLabel.setVisible(false);
			dniNo.setVisible(false);
			dniCheck.setVisible(false);
		}


		nextPass = makeButton(buttonLbl, "Arial", 20, 500, 150, 280);
		nextPass.setStyle("-fx-background-radius: 30px;");

		nextPass.setOnMouseClicked(e -> {

			if(checkedBaggage.isSelected()){
				if(returnFlight.isSelected())
					checkedBagCount += 2;
				else
					checkedBagCount++;
			}
			checkedBaggage.setSelected(false);
			try{
				if(tickType.getValue().equals("Adult")){
					totalAdult--;
					if(totalAdult == 0)
						tickType.getItems().remove("Adult");

					tickType.setValue("Ticket Type");
				}
			}catch(NullPointerException ex){
				System.out.println("");
			}
			try{
				if(tickType.getValue().equals("Child")){
					totalChild--;
					if(totalChild == 0)
						tickType.getItems().remove("Child");

					tickType.setValue("Ticket Type");
				}
			}catch(NullPointerException ex){
				System.out.println("");
			}
			try{
				if(tickType.getValue().equals("Toddler")){
					totalToddler--;
					if(totalToddler == 0)
						tickType.getItems().remove("Toddler");

					tickType.setValue("Ticket Type");
				}
			}catch(NullPointerException ex){
				System.out.println("");
			}
			try{
				if(tickType.getValue().equals("Baby")){
					totalBaby--;
					if(totalBaby == 0)
						tickType.getItems().remove("Baby");

					tickType.setValue("Ticket Type");
				}
			}catch(NullPointerException ex){
				System.out.println("");
			}
			Boolean dniFlag = true;
			Boolean validFlag = true;

			firstName.setStyle(null);
			lastName.setStyle(null);

			if(firstName.getText().equals("") || firstName.getText().equals("---")){
				validFlag = false;
				firstName.setText("---");
				firstName.setStyle("-fx-text-box-border: red ;");
			}
			else{
				validFlag = true;
				firstName.setStyle(null);
			}


			if(lastName.getText().equals("") || lastName.getText().equals("---")){
				validFlag = false;
				lastName.setText("---");
				lastName.setStyle("-fx-text-box-border: red ;");
			}
			else{
				validFlag = true;
				lastName.setStyle(null);
			}

			if(checkedBaggage.isSelected()){
				checkBaggageFees += 15;
			}

			if(dniNo.getText().length() == 9 || dniNo.getText().length() == 10){
				dniFlag = verifyDni(dniNo.getText(), dniNo.getLength());

				if(dniFlag == true){
					if(returnFlight.isSelected())
						dniCount+=2;
					else
						dniCount++;
				}
			}

			if(validFlag == true){				
				if(passNum+1 == numPeople){
					nextPass.setText("Continue to Payment");
				}

				passengerFirstName.add(firstName.getText());
				passengerLastName.add(lastName.getText());
				passengerDateOfBirth.add(dateOfBirth.getValue().toString());

				firstName.clear();
				lastName.clear();
				dniNo.clear();
				dateOfBirth.setValue(LocalDate.now());

				if(numPeople == passNum){
					Scene payment = Payment(stage);
					stage.setScene(payment);
				}
				passNum++;
				passengerLabel.setText("Passenger #" + passNum);
			}
			checkedBaggage.setDisable(true);
			firstName.setDisable(true);
			lastName.setDisable(true);
			dateOfBirth.setDisable(true);
			dniNo.setDisable(true);
			nextPass.setDisable(true);
		});

		passInfo.getChildren().addAll(passengerLabel, fNameLabel, lNameLabel,  firstName, lastName, dobLabel, dateOfBirth, nextPass, checkedBaggage,
				dniLabel, dniNo, tickType, dniCheck);
		passengerInfoScene = new Scene(passInfo, 800, 200);

		return passengerInfoScene;
	}
	public Scene Payment(Stage pStage){
		Stage stage = pStage;


		if(!returnFlight.isSelected()){
			totalFlightPriceOneWay = (price*adults) + (price*child) + (60*toddlerChild);
			if(weekendOut == true)
				totalFlightPriceOneWay = totalFlightPriceOneWay + (totalFlightPriceOneWay/100)*20;

			totalFlightCost = totalFlightPriceOneWay;
		}
		else{
			totalFlightPriceOneWay = (price*adult) + (price*child) + (60*toddlerChild);
			totalFlightPriceReturn = (price*adult) + (price*child) + (60*toddlerChild);
			if(weekendOut == true){
				totalFlightPriceOneWay = totalFlightPriceOneWay + (totalFlightPriceOneWay/100)*20;
			}
			if(weekendReturn == true){
				totalFlightPriceReturn = totalFlightPriceReturn + (totalFlightPriceReturn/100)*20;
			}

			totalFlightCost = totalFlightPriceOneWay + totalFlightPriceReturn;
		}

		totalFlightCost = totalFlightCost - (5 * dniCount);
		totalFlightCost = totalFlightCost + (15 * checkedBagCount);

		cardNumIn = MakeTextField(220, 140, 200);
		TextField cardCvvIn = MakeTextField(220, 170, 50);
		TextField cardNameIn = MakeTextField(220, 200, 250);
		AnchorPane paymentAnchor = new AnchorPane();
		paymentAnchor.setStyle("-fx-background-color:#19198c;");
		Label method = makeWhiteLabel("Payment Method", "Arial", 30, 20, 20);
		Label cardTypeLbl = makeYellowLabel("Card Type:", "Arial", 20, 20, 60);
		cardType = new ComboBox<String>();
		cardType.setPromptText("---");
		cardType.setValue("---");
		cardType.setTranslateY(60);
		cardType.setTranslateX(220);
		cardType.setStyle("-fx-font-family: Arial; -fx-font-size: 15px; -fx-background-radius: 15px;");
		cardType.getItems().add("Visa");
		cardType.getItems().add("Mastercard");
		cardType.getItems().add("American Express");

		Button completePay = makeButton("Complete Booking", "Arial", 20, 100, 320, 300);

		cardExpYear = new ComboBox<String>();
		cardExpYear.setValue("Year");
		cardExpYear.setTranslateY(230);
		cardExpYear.setTranslateX(220);
		cardExpYear.setStyle("-fx-font-family: Arial; -fx-font-size: 15px; -fx-background-radius: 15px;");
		cardExpYear.getItems().add("" + (LocalDate.now().getYear()));
		cardExpYear.getItems().add("" + (LocalDate.now().plusYears(1).getYear()));
		cardExpYear.getItems().add("" + (LocalDate.now().plusYears(2).getYear()));
		cardExpYear.getItems().add("" + (LocalDate.now().plusYears(3).getYear()));
		cardExpYear.getItems().add("" + (LocalDate.now().plusYears(4).getYear()));
		cardExpYear.getItems().add("" + (LocalDate.now().plusYears(5).getYear()));
		cardExpYear.getItems().add("" + (LocalDate.now().plusYears(6).getYear()));
		cardExpYear.getItems().add("" + (LocalDate.now().plusYears(7).getYear()));
		cardExpYear.getItems().add("" + (LocalDate.now().plusYears(8).getYear()));

		cardExpMonth = new ComboBox<String>();
		cardExpMonth.setValue("Month");
		cardExpMonth.setTranslateY(230);
		cardExpMonth.setTranslateX(320);
		cardExpMonth.setStyle("-fx-font-family: Arial; -fx-font-size: 15px; -fx-background-radius: 15px;");
		cardExpMonth.getItems().add("01 - January");
		cardExpMonth.getItems().add("02 - February");
		cardExpMonth.getItems().add("03 - March");
		cardExpMonth.getItems().add("04 - April");
		cardExpMonth.getItems().add("05 - May");
		cardExpMonth.getItems().add("06 - June");
		cardExpMonth.getItems().add("07 - July");
		cardExpMonth.getItems().add("08 - August");
		cardExpMonth.getItems().add("09 - September");
		cardExpMonth.getItems().add("10 - October");
		cardExpMonth.getItems().add("11 - November");
		cardExpMonth.getItems().add("12 - December");

		Label payment = makeWhiteLabel("Payment Details", "Arial", 30, 20, 100);
		Label cardNum = makeYellowLabel("Card Number:", "Arial", 20, 20, 140);
		Label cvv = makeYellowLabel("Security (CVV):", "Arial", 20, 20, 170);
		Label cardHolderName = makeYellowLabel("Cardholder Name:", "Arial", 20, 20, 200);
		Label expDate = makeYellowLabel("Expiry Date:", "Arial", 20, 20, 230);
		Label totalEndPrice = makeWhiteLabel("Total: €" + totalFlightCost, "Arial", 30, 50, 400);

		completePay.setOnAction(e -> {
			cardValid = true;
			Boolean flag = true;
			if(cardType.getValue().equals("---")){
				flag = false;
				cardType.setStyle("-fx-text-box-border: red ;");
			}
			else{
				cardType.setStyle(null);
			}
			if(cardNumIn.getText().length() == 16){
				cardValid = VerifyCreditCard(cardNumIn.getText());
				cardNumIn.setStyle(null);
			}
			else{
				flag = false;
				cardCvvIn.setStyle("-fx-text-box-border: red ;");
			}

			if(cardCvvIn.getText().length() != 3){
				cardCvvIn.setStyle("-fx-text-box-border: red ;");
				flag = false;
			}
			else{
				cardCvvIn.setStyle(null);
			}

			if(cardNameIn.getText().equals("")){
				cardNameIn.setStyle("-fx-text-box-border: red ;");
				flag = false;
			}
			else{
				cardNameIn.setStyle(null);
			}
			if(cardExpMonth.getValue().equals("Month")){
				cardValid = false;
			}
			if(cardExpMonth.getValue().equals("Year")){
				cardValid = false;
			}

			if(cardValid == false){
				flag = false;
			}

			if(flag == true){
				Scene statement = Statement();
				stage.setScene(statement);
			}
		});

		paymentAnchor.getChildren().addAll(method, cardTypeLbl, cardType, payment, cardNum, cardNumIn, cvv, cardCvvIn, cardHolderName, cardNameIn, 
				expDate, cardExpYear, cardExpMonth, completePay, totalEndPrice);
		return paymentScene = new Scene(paymentAnchor, 500, 450);	
	}
	public Scene Statement(){
		Random ran = new Random();
		String a = "";
		String jour;
		AnchorPane statementPane = new AnchorPane();
		ScrollPane scrollStatement = new ScrollPane();
		scrollStatement.setFitToWidth(true);
		int refNo = ran.nextInt(100000000) + 0;
		y = 20;
		Label conf = makeYellowLabel("Booking Confirmation", "Arial", 44, 20, y);
		y+=60;
		Label ref = makeWhiteLabel("Ref No: " + refNo, "Arial", 20, 20, y);
		if(returnFlight.isSelected())
			jour = "RETURN";
		else
			jour = "ONE-WAY";
		y+=30;
		Label type = makeWhiteLabel("Journey Type: " + jour, "Arial", 20, 20, y);
		y+=60;		
		Label leaving = makeWhiteLabel(from.substring(0, from.lastIndexOf("\t")), "Arial", 20, 20, y);
		y+=30;
		Label leavingDateLbl = makeWhiteLabel(date + " - " + outboundLeaving, "Arial", 20, 20, y);
		y+=60;
		if(returnFlight.isSelected()){
			Label returning = makeWhiteLabel(to.substring(0, from.lastIndexOf("\t")), "Arial", 20, 20, y);
			y+=30;
			Label retDateLbl = makeWhiteLabel(dateA + " - " + inboundLeaving, "Arial", 20, 20, y);
			y+=60;
			statementPane.getChildren().addAll(returning, retDateLbl);
		}

		Label passLbl = makeYellowLabel("Passengers", "Arial", 20, 20, y);
		Label dobLbl = makeYellowLabel("Date Of Birth", "Arial", 20, 300, y);

		y+=30;

		String dob = "";

		if(numPeople == 1 || numPeople > 1){
			if(passengerDateOfBirth.get(0).equals(LocalDate.now().toString()))
				dob = "---";
			else
				dob = "" + passengerDateOfBirth.get(0);

			Label passenger1 = makeWhiteLabel("1. " + passengerFirstName.get(0) + " " + passengerLastName.get(0), "Arial", 20, 20, y);
			Label pass1Dob = makeWhiteLabel(dob, "Arial", 20, 300, y);
			statementPane.getChildren().addAll(passenger1, pass1Dob);
		}
		if(numPeople == 2 || numPeople > 2){
			if(passengerDateOfBirth.get(1).equals(LocalDate.now().toString()))
				dob = "---";
			else
				dob = "" + passengerDateOfBirth.get(1);

			y+=30;
			Label passenger2 = makeWhiteLabel("2. " + passengerFirstName.get(1) + " " + passengerLastName.get(1), "Arial", 20, 20, y);
			Label pass2Dob = makeWhiteLabel(dob, "Arial", 20, 300, y);
			statementPane.getChildren().addAll(passenger2, pass2Dob);
		}
		if(numPeople == 3 || numPeople > 3){
			if(passengerDateOfBirth.get(2).equals(LocalDate.now().toString()))
				dob = "---";
			else
				dob = "" + passengerDateOfBirth.get(2);

			y+=30;
			Label passenger3 = makeWhiteLabel("3. " + passengerFirstName.get(2) + " " + passengerLastName.get(2), "Arial", 20, 20, y);
			Label pass3Dob = makeWhiteLabel(dob, "Arial", 20, 300, y);
			statementPane.getChildren().addAll(passenger3, pass3Dob);
		}
		if(numPeople == 4 || numPeople > 4){
			if(passengerDateOfBirth.get(3).equals(LocalDate.now().toString()))
				dob = "---";
			else
				dob = "" + passengerDateOfBirth.get(3);

			y+=30;
			Label passenger4 = makeWhiteLabel("4. " + passengerFirstName.get(3) + " " + passengerLastName.get(3), "Arial", 20, 20, y);
			Label pass4Dob = makeWhiteLabel(dob, "Arial", 20, 300, y);
			statementPane.getChildren().addAll(passenger4, pass4Dob);
		}
		if(numPeople == 5 || numPeople > 5){
			if(passengerDateOfBirth.get(4).equals(LocalDate.now().toString()))
				dob = "---";
			else
				dob = "" + passengerDateOfBirth.get(4);

			y+=30;
			Label passenger5 = makeWhiteLabel("5. " + passengerFirstName.get(4) + " " + passengerLastName.get(4), "Arial", 20, 20, y);
			Label pass5Dob = makeWhiteLabel(dob, "Arial", 20, 300, y);
			statementPane.getChildren().addAll(passenger5, pass5Dob);
		}
		if(numPeople == 6 || numPeople > 6){
			if(passengerDateOfBirth.get(5).equals(LocalDate.now().toString()))
				dob = "---";
			else
				dob = "" + passengerDateOfBirth.get(5);

			y+=30;
			Label passenger6 = makeWhiteLabel("6. " + passengerFirstName.get(5) + " " + passengerLastName.get(5), "Arial", 20, 20, y);
			Label pass6Dob = makeWhiteLabel(dob, "Arial", 20, 300, y);
			statementPane.getChildren().addAll(passenger6, pass6Dob);
		}
		if(numPeople == 7 || numPeople > 7){
			if(passengerDateOfBirth.get(6).equals(LocalDate.now().toString()))
				dob = "---";
			else
				dob = "" + passengerDateOfBirth.get(6);

			y+=30;
			Label passenger7 = makeWhiteLabel("7. " + passengerFirstName.get(6) + " " + passengerLastName.get(6), "Arial", 20, 20, y);
			Label pass7Dob = makeWhiteLabel(dob, "Arial", 20, 300, y);
			statementPane.getChildren().addAll(passenger7, pass7Dob);
		}
		if(numPeople == 8){
			if(passengerDateOfBirth.get(7).equals(LocalDate.now().toString()))
				dob = "---";
			else
				dob = "" + passengerDateOfBirth.get(7);

			y+=30;
			Label passenger8 = makeWhiteLabel("8. " + passengerFirstName.get(7) + " " + passengerLastName.get(7), "Arial", 20, 20, y);
			Label pass8Dob = makeWhiteLabel(dob, "Arial", 20, 300, y);
			statementPane.getChildren().addAll(passenger8, pass8Dob);
		}
		y+=60;
		int bags = 0;
		if(! returnFlight.isSelected() && checkedBagCount != 0)
			bags = checkedBagCount;
		else if(returnFlight.isSelected() && checkedBagCount != 0)
			bags = checkedBagCount/2;
		Label baggageLbl = makeWhiteLabel("Checked Baggage: " + bags + " Bags (Each Way)",  "Arial", 20, 20, y);
		y+=30;
		Label paidWith = makeWhiteLabel("Paid with " + cardType.getValue() + " ending in " + cardNumIn.getText().substring(12,16), "Arial", 20, 20, y);
		y+=30;
		Label finalTotal = makeWhiteLabel("Payment Received: €" + totalFlightCost, "Arial", 20, 20, y);
		y+=60;
		Label thankYou = makeWhiteLabel("Thank you for Travelling with JaviAir, Enjoy your trip", "Arial", 15, 20, y);
		y+=60;
		Button closeProg = makeButton("Close Statement", "Arial", 20, 100, y, 300);
		closeProg.setOnMouseClicked(e -> {
			System.exit(0);
		});
		y+=30;
		
		statementPane.setStyle("-fx-background-color:#19198c;");
		statementPane.getChildren().addAll(conf, ref, type, leaving, leavingDateLbl, passLbl, dobLbl, baggageLbl, thankYou, closeProg, 
				paidWith, finalTotal);

		statementPane.setPrefHeight(y+30);
		statementPane.setPrefWidth(500);
		int x = y;

		if(y > 500)
			x = 500;
		scrollStatement.setContent(statementPane);
		return statementScene = new Scene(scrollStatement, statementPane.getWidth(), x);
	}
	public Boolean[] GetTimes(){
		Boolean[] doubles = {false, false};
		if(outbound.getValue().equals(destinations[0]) && inbound.getValue().equals(destinations[1]) || outbound.getValue().equals(destinations[1]) && inbound.getValue().equals(destinations[0])){
			outboundLeavingA = "09:20";
			outboundArrivingA = "13:00";

			inboundLeavingA = "18:00";
			inboundArrivingA = "20:00";
		}
		else if(outbound.getValue().equals(destinations[0]) && inbound.getValue().equals(destinations[2]) || outbound.getValue().equals(destinations[2]) && inbound.getValue().equals(destinations[0])){
			outboundLeavingA = "10:30";
			outboundArrivingA = "14:00";

			inboundLeavingA = "19:00";
			inboundArrivingA = "20:20";
		}
		else if(outbound.getValue().equals(destinations[0]) && inbound.getValue().equals(destinations[3]) || outbound.getValue().equals(destinations[3]) && inbound.getValue().equals(destinations[0])){
			outboundLeavingA = "14:00";
			outboundArrivingA = "16:00";

			inboundLeavingA = "10:00";
			inboundArrivingA = "12:00";
		}
		else if(outbound.getValue().equals(destinations[0]) && inbound.getValue().equals(destinations[4]) || outbound.getValue().equals(destinations[4]) && inbound.getValue().equals(destinations[0])){
			outboundLeavingA = "09:00";
			outboundArrivingA = "12:15";
			outboundLeavingB = "18:20";
			outboundArrivingB = "21:05";
			doubles[0] = true;

			inboundLeavingA = "13:30";
			inboundArrivingA = "15:00";
			inboundLeavingB = "22:00";
			inboundArrivingB = "23:50";
			doubles[1] = true;
		}
		else if(outbound.getValue().equals(destinations[0]) && inbound.getValue().equals(destinations[5]) || outbound.getValue().equals(destinations[5]) && inbound.getValue().equals(destinations[0])){
			outboundLeavingA = "08:20";
			outboundArrivingA = "09:50";
			outboundLeavingB = "11:20";
			outboundArrivingB = "13:05";
			doubles[0] = true;

			inboundLeavingA = "11:00";
			inboundArrivingA = "12:20";
			inboundLeavingB = "18:00";
			inboundArrivingB = "19:20";
			doubles[1] = true;
		}
		else if(outbound.getValue().equals(destinations[0]) && inbound.getValue().equals(destinations[6]) || outbound.getValue().equals(destinations[6]) && inbound.getValue().equals(destinations[0])){
			outboundLeavingA = "08:00";
			outboundArrivingA = "11:30";

			inboundLeavingA = "13:00";
			inboundArrivingA = "14:20";
		}

		else if(outbound.getValue().equals(destinations[1]) && inbound.getValue().equals(destinations[2]) || outbound.getValue().equals(destinations[2]) && inbound.getValue().equals(destinations[1])){
			outboundLeavingA = "12:00";
			outboundArrivingA = "13:30";

			inboundLeavingA = "18:00";
			inboundArrivingA = "20:20";
		}

		else if(outbound.getValue().equals(destinations[1]) && inbound.getValue().equals(destinations[3]) || outbound.getValue().equals(destinations[3]) && inbound.getValue().equals(destinations[1])){
			outboundLeavingA = "06:20";
			outboundArrivingA = "08:00";

			inboundLeavingA = "18:00";
			inboundArrivingA = "21:20";
		}
		else if(outbound.getValue().equals(destinations[1]) && inbound.getValue().equals(destinations[4]) || outbound.getValue().equals(destinations[4]) && inbound.getValue().equals(destinations[1])){
			outboundLeavingA = "08:00";
			outboundArrivingA = "10:00";

			inboundLeavingA = "19:20";
			inboundArrivingA = "21:05";
		}
		else if(outbound.getValue().equals(destinations[1]) && inbound.getValue().equals(destinations[5]) || outbound.getValue().equals(destinations[5]) && inbound.getValue().equals(destinations[1])){
			outboundLeavingA = "14:00";
			outboundArrivingA = "15:20";
			outboundLeavingB = "19:05";
			outboundArrivingB = "21:20";
			doubles[0] = true;

			inboundLeavingA = "10:20";
			inboundArrivingA = "14:00";
		}
		else if(outbound.getValue().equals(destinations[1]) && inbound.getValue().equals(destinations[6]) || outbound.getValue().equals(destinations[6]) && inbound.getValue().equals(destinations[1])){
			outboundLeavingA = "08:00";
			outboundArrivingA = "09:05";

			inboundLeavingA = "20:00";
			inboundArrivingA = "21:05";
		}
		else if(outbound.getValue().equals(destinations[2]) && inbound.getValue().equals(destinations[4]) || outbound.getValue().equals(destinations[4]) && inbound.getValue().equals(destinations[2])){
			outboundLeavingA = "06:20";
			outboundArrivingA = "07:15";

			inboundLeavingA = "19:00";
			inboundArrivingA = "20:05";
		}
		else if(outbound.getValue().equals(destinations[2]) && inbound.getValue().equals(destinations[5]) || outbound.getValue().equals(destinations[5]) && inbound.getValue().equals(destinations[2])){
			outboundLeavingA = "08:05";
			outboundArrivingA = "08:30";

			inboundLeavingA = "18:00";
			inboundArrivingA = "20:00";
		}
		else if(outbound.getValue().equals(destinations[2]) && inbound.getValue().equals(destinations[6]) || outbound.getValue().equals(destinations[6]) && inbound.getValue().equals(destinations[2])){
			outboundLeavingA = "12:00";
			outboundArrivingA = "15:30";

			inboundLeavingA = "20:00";
			inboundArrivingA = "21:30";
		}
		else if(outbound.getValue().equals(destinations[3]) && inbound.getValue().equals(destinations[4]) || outbound.getValue().equals(destinations[4]) && inbound.getValue().equals(destinations[3])){
			outboundLeavingA = "08:00";
			outboundArrivingA = "10:15";

			inboundLeavingA = "20:00";
			inboundArrivingA = "20:15";
		}
		else if(outbound.getValue().equals(destinations[3]) && inbound.getValue().equals(destinations[5]) || outbound.getValue().equals(destinations[5]) && inbound.getValue().equals(destinations[3])){
			outboundLeavingA = "17:00";
			outboundArrivingA = "18:30";

			inboundLeavingA = "09:00";
			inboundArrivingA = "10:30";
		}
		else if(outbound.getValue().equals(destinations[3]) && inbound.getValue().equals(destinations[6]) || outbound.getValue().equals(destinations[6]) && inbound.getValue().equals(destinations[3])){
			outboundLeavingA = "08:00";
			outboundArrivingA = "11:30";

			inboundLeavingA = "09:00";
			inboundArrivingA = "10:30";
		}
		else if(outbound.getValue().equals(destinations[4]) && inbound.getValue().equals(destinations[5]) || outbound.getValue().equals(destinations[5]) && inbound.getValue().equals(destinations[4])){
			outboundLeavingA = "18:00";
			outboundArrivingA = "18:30";

			inboundLeavingA = "09:00";
			inboundArrivingA = "10:30";
		}
		else if(outbound.getValue().equals(destinations[5]) && inbound.getValue().equals(destinations[6]) || outbound.getValue().equals(destinations[6]) && inbound.getValue().equals(destinations[5])){
			outboundLeavingA = "08:00";
			outboundArrivingA = "11:00";
			outboundLeavingB = "13:30";
			outboundArrivingB = "16:20";
			doubles[0] = true;

			inboundLeavingA = "15:00";
			inboundArrivingA = "16:10";
			inboundLeavingB = "20:35";
			inboundArrivingB = "21:05";
			doubles[1] = true;
		}
		else if(outbound.getValue().equals(destinations[4]) && inbound.getValue().equals(destinations[6]) || outbound.getValue().equals(destinations[6]) && inbound.getValue().equals(destinations[4])){
			outboundLeavingA = "11:50";
			outboundArrivingA = "13:30";

			inboundLeavingA = "18:05";
			inboundArrivingA = "12:30";
		}
		return doubles;
	}
	public Boolean getFlightPrices() {
		Boolean flg = true;
		if(outbound.getValue().equals("---")){
			outboundText.setTextFill(Color.RED);
			flg = false;
		}
		else{
			outboundText.setTextFill(Color.YELLOW);
			if(inbound.getValue().equals("---")){
				inboundText.setTextFill(Color.RED);
				flg = false;
			}
			else{
				inboundText.setTextFill(Color.YELLOW);
				flg = true;
			}
		}
		return flg;
	}
	public void IncomingList() {
		inbound.getItems().clear();
		String value = outbound.getValue();
		for(int i = 0; i < destinations.length; i++){
			if(!(destinations[i].equals(value))){
				inbound.getItems().add(destinations[i]);
			}
		}
		if(outbound.getValue().equals("Jersey (JER)")){
			inbound.getItems().remove(destinations[2]);
		}
		if(outbound.getValue().equals("St Brieuc (SBK)")){
			inbound.getItems().remove(destinations[3]);
		}
	}
	public Label makeWhiteLabel(String text, String font, int fontSize, double xPos, double yPos){
		Label label = new Label(text);
		label.setTranslateX(xPos);
		label.setTranslateY(yPos);
		label.setTextFill(Color.WHITE);
		label.setStyle("-fx-font-weight: bold; -fx-font-size: " + fontSize + "px; -fx-font-family: " + font + ";");
		return label;
	}
	public TextField MakeTextField(double xPos, double yPos, double width){
		TextField textfield = new TextField();
		textfield.setTranslateX(xPos);
		textfield.setTranslateY(yPos);
		textfield.setPrefWidth(width);
		return textfield;
	}
	public Label makeYellowLabel(String text, String font, int fontSize, double xPos, double yPos){
		Label label = new Label(text);
		label.setTranslateX(xPos);
		label.setTranslateY(yPos);
		label.setFont(Font.font (font));	
		label.setTextFill(Color.YELLOW);
		label.setStyle("-fx-font-weight: bold; -fx-font-size: " + fontSize + "px; -fx-font-family: " + font + ";");
		return label;
	}
	public Label makeRedLabel(String text, String font, int fontSize, double xPos, double yPos){
		Label label = new Label(text);
		label.setTranslateX(xPos);
		label.setTranslateY(yPos);
		//label.setFont(Font.font (font, fontSize));	
		label.setTextFill(Color.RED);
		label.setStyle("-fx-font-weight: bold; -fx-font-size: " + fontSize + "px; -fx-font-family: " + font + ";");
		return label;
	}
	public Label makeBlackLabel(String text, String font, int fontSize, double xPos, double yPos){
		Label label = new Label(text);
		label.setTranslateX(xPos);
		label.setTranslateY(yPos);
		label.setFont(Font.font (font, fontSize));	
		label.setTextFill(Color.BLACK);
		label.setStyle("-fx-font-weight: bold; -fx-font-size: " + fontSize + "px; -fx-font-family: " + font + ";");
		return label;
	}
	public Button makeButton(String text, String font, int fontSize, double xPos, double yPos, double width){
		Button button = new Button(text);
		button.setTranslateX(xPos);
		button.setTranslateY(yPos);
		button.setFont(Font.font (font, fontSize));	
		button.setPrefWidth(width);
		return button;
	}
	public Boolean verifyDni(String dni, int length){
		Boolean isValid = false;
		dni = dni.toUpperCase();
		String dniNums = dni.substring(0,8);
		int dniNum = Integer.parseInt(dniNums);
		int dniMod = dniNum%23;
		int pos = 0;
		String ch = "";
		if(dniMod == 0)
			ch = "T";
		else if(dniMod == 1)
			ch = "R";
		else if(dniMod == 2)
			ch = "W";
		else if(dniMod == 3)
			ch = "A";
		else if(dniMod == 4)
			ch = "G";
		else if(dniMod == 5)
			ch = "M";
		else if(dniMod == 6)
			ch = "Y";
		else if(dniMod == 7)
			ch = "F";
		else if(dniMod == 8)
			ch = "P";
		else if(dniMod == 9)
			ch = "D";
		else if(dniMod == 10)
			ch = "X";
		else if(dniMod == 11)
			ch = "B";
		else if(dniMod == 12)
			ch = "N";
		else if(dniMod == 13)
			ch = "J";
		else if(dniMod == 14)
			ch = "Z";
		else if(dniMod == 15)
			ch = "S";
		else if(dniMod == 16)
			ch = "Q";
		else if(dniMod == 17)
			ch = "V";
		else if(dniMod == 18)
			ch = "H";
		else if(dniMod == 19)
			ch = "L";
		else if(dniMod == 20)
			ch = "C";
		else if(dniMod == 21)
			ch = "K";
		else if(dniMod == 22)
			ch = "E";

		if(length == 9){
			pos = 8;
		}
		else if (length == 10){
			if(dni.charAt(8) == '-')
				pos = 9;
		}
		String check = "" +  dni.charAt(pos);

		if(check.equals(ch)){
			isValid = true;
		}
		return isValid;
	}
	public Boolean VerifyCreditCard(String cardNo){
		Boolean isValid = true;
		int runningTotal = 0;
		int sub = 0, f2 = 0;

		for(int i = 0; i < cardNo.length(); i++){
			String num = cardNo.substring(i,i+1);
			int f1 = Integer.parseInt(num);
			if(i%2 == 0){
				f2 = f1*2;
				if(f2 >= 10){
					String op = "" + f2;
					sub = Integer.parseInt(op.substring(0,1));
					sub += Integer.parseInt(op.substring(1,2));
				}
				else
					sub = f2;
			}
			else if(i%2 != 0){
				sub = f1;
			}
			runningTotal += sub;
		}

		if(runningTotal%10 != 0){
			isValid = false;
		}
		return isValid;
	}
}
