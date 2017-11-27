import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;

import java.lang.Thread;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MediaRepo extends Application implements Serializable{

	int index,length = 0;
	long prevMod;
	String date;
	File f;
	Stage primaryStage;
	String file;
	File folder = new File ("repository/");
	Scene sceneOne;
	static Label timeOb, downloadStatus;
	ArrayList<String> listFileLocation = new ArrayList<String> ();
	ImageView next, prev, play, full, musicImage;
	MediaView mview;
	AnchorPane view;
	Stage photoView, vidView, musicView;
	Scene vidScene, musicScene;
	File[] listOfFiles;
	Button executeBtn;
	MediaPlayer mp;
	Thread t, v;
	Boolean playing;
	ObservableList<MediaFile> data = FXCollections.observableArrayList(); 
	TableView<MediaFile> fileTable = new TableView<MediaFile>();

	public static void main(String[] args){
		launch(args);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {


		AnchorPane statusBar = new AnchorPane();
		statusBar.setPrefSize(1000, 30);
		timeOb = new Label();
		timeOb.setText("");
		timeOb.setFont(Font.font ("Arial", 17));
		timeOb.setTranslateY(5);
		timeOb.setTranslateX(835);
		
		downloadStatus = new Label();
		downloadStatus.setText("No recent downloads");
		downloadStatus.setFont(Font.font ("Arial", 17));
		downloadStatus.setTranslateY(5);
		downloadStatus.setTranslateX(10);
		statusBar.setTranslateY(570);
		statusBar.getChildren().addAll(timeOb, downloadStatus);


		AnchorPane controls = new AnchorPane();
		controls.setPrefSize(1000, 70);
		controls.setStyle("-fx-background-color: #fff;");

		ImageView open = new ImageView(new Image("file:play.jpg"));
		Tooltip openT = new Tooltip("Open");
		openT.setStyle("-fx-font-size: 1.3em;-fx-font-weight: bold;");
		Tooltip.install(open, openT);
		open.setFitHeight(50);
		open.setFitWidth(50);
		open.setTranslateX(10);
		open.setTranslateY(10);
		open.setOnMouseClicked(e -> {
			execute(index);
		});

		ImageView download = new ImageView(new Image("file:download.jpg"));
		Tooltip downloadT = new Tooltip("Download");
		downloadT.setStyle("-fx-font-size: 1.3em;-fx-font-weight: bold;");
		Tooltip.install(download, downloadT);
		download.setFitHeight(50);
		download.setFitWidth(50);
		download.setTranslateX(80);
		download.setTranslateY(10);
		download.setOnMouseClicked(e -> {
			DownloadFile(index);
		});


		ImageView change = new ImageView(new Image("file:change.jpg"));
		Tooltip changeT = new Tooltip("Change Repository");
		changeT.setStyle("-fx-font-size: 1.3em;-fx-font-weight: bold;");
		Tooltip.install(change, changeT);
		change.setFitHeight(50);
		change.setFitWidth(50);
		change.setTranslateX(150);
		change.setTranslateY(15);
		change.setOnMouseClicked(e -> {
			ChangeDirectory();
			Populate();
		});

		ImageView exit = new ImageView(new Image("file:exit.jpg"));
		Tooltip exitT = new Tooltip("Exit");
		exitT.setStyle("-fx-font-size: 1.3em;-fx-font-weight: bold;");
		Tooltip.install(exit, exitT);
		exit.setFitHeight(50);
		exit.setFitWidth(50);
		exit.setTranslateX(940);
		exit.setTranslateY(10);
		exit.setOnMouseClicked(e -> {
			System.exit(0);
		});


		controls.getChildren().addAll(open, download, change, exit);
		AnchorPane mainPane = new AnchorPane();			

		//Table
		fileTable.setStyle("-fx-font-size: 1.2em;");
		TableColumn<MediaFile, String> name = new TableColumn<MediaFile, String>("File Name");
		name.setCellValueFactory(new PropertyValueFactory<MediaFile, String>("Name"));
		name.setPrefWidth(300);

		TableColumn<MediaFile, String> format = new TableColumn<MediaFile, String>("Format");
		format.setCellValueFactory(new PropertyValueFactory<MediaFile, String>("Format"));
		format.setPrefWidth(100);

		TableColumn<MediaFile, String> size = new TableColumn<MediaFile, String>("Size");
		size.setCellValueFactory(new PropertyValueFactory<MediaFile, String>("Size"));
		size.setPrefWidth(50);

		TableColumn<MediaFile, String> type = new TableColumn<MediaFile, String>("Type");
		type.setCellValueFactory(new PropertyValueFactory<MediaFile, String>("Type"));
		type.setPrefWidth(100);

		TableColumn<MediaFile, String> date = new TableColumn<MediaFile, String>("Date Of Creation");
		date.setCellValueFactory(new PropertyValueFactory<MediaFile, String>("Date")); 
		date.setPrefWidth(150);

		fileTable.getColumns().addAll(name, format, size, type, date);
		name.setSortable(false);
		format.setSortable(false);
		size.setSortable(false);
		type.setSortable(false);
		date.setSortable(false);
		fileTable.setPrefSize(1000, 500);
		fileTable.setTranslateY(70);
		fileTable.setRowFactory(tv -> {
			TableRow<MediaFile> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!row.isEmpty())) {
					index = (fileTable.getSelectionModel().getSelectedIndex());
				}
			});
			return row;
		});

		mainPane.getChildren().addAll(fileTable,controls, statusBar);

		sceneOne = new Scene(mainPane, 1000, 600);
		primaryStage.setScene(sceneOne);
		primaryStage.setTitle("Media Repository");
		primaryStage.show();
		primaryStage.getIcons().add(new Image("file:mainIcon.png"));
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.setOnCloseRequest(a -> {
			System.exit(0);
		});
		Observe();
	}

	public void Download() {
		DownloadTask downloadTask = new DownloadTask();
		Thread d = new Thread(downloadTask);
		d.start();
	}
	public static void TimeUpdate(int time){
		if(time >= 60 && time < 3600){
			if(time > 120){
				timeOb.setText("Updated " + time/60 + " mins ago");
			}
			else{
				timeOb.setText("Updated " + time/60 + " min ago");
			}
		}
		else if( time >= 3600){
			if(time > 7200){
				timeOb.setText("Updated " + (time/60)/60 + " hours ago");
			}
			else{
				timeOb.setText("Updated " + (time/60)/60 + " hour ago");
			}
		}
		else{
			if(time > 1 && time < 60){
				timeOb.setText("Updated " + time + " secs ago");
			}
			else{
				timeOb.setText("Updated " + time + " sec ago");
			}
		}
	}
	public void Observe(){
		ObserveTask observe = new ObserveTask(this);
		Thread o = new Thread(observe);
		o.start();
	}
	public void execute(int index) {
		f = new File(listFileLocation.get(index));
		int length = listFileLocation.get(index).length();
		String address= listFileLocation.get(index).substring(length-4, length);

		if(address.equals(".mp3") || address.equals(".wav") || address.equals(".wma")){
			MusicTask musicTask = new MusicTask(index);
			Thread mus = new Thread(musicTask);
			mus.start();
		}
		if(address.equalsIgnoreCase(".jpg") || address.equalsIgnoreCase(".png") || address.equalsIgnoreCase(".gif")){
			ImageTask imageTask = new ImageTask(index);
			Thread t = new Thread(imageTask);
			t.start();
		}
		if(address.equals(".mp4")){
			VideoTask videoTask = new VideoTask(index);
			Thread v = new Thread(videoTask);
			v.start();
		}
	}
	public void Populate(){ 
		String type = null;
		String size = null;
		String temp;
		listOfFiles = folder.listFiles();

		if(data.size() > 0){
			data.removeAll(data);
		}

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				int length = listOfFiles[i].toString().length();
				String ext = listOfFiles[i].toString().substring(length-3, length).toUpperCase() + " file";
				if(listOfFiles[i].toString().substring(length-4, length).equals(".jpg") || 
						listOfFiles[i].toString().substring(length-4, length).equals(".png") || 
						listOfFiles[i].toString().substring(length-4, length).equals(".gif")){
					type = "Image";
				}
				else if(listOfFiles[i].toString().substring(length-4, length).equals(".mp3") || 
						listOfFiles[i].toString().substring(length-4, length).equals(".wav") || 
						listOfFiles[i].toString().substring(length-4, length).equals(".wma")){
					type = "Audio";
				}
				else if(listOfFiles[i].toString().substring(length-4, length).equals(".avi") || 
						listOfFiles[i].toString().substring(length-4, length).equals(".wnv") || 
						listOfFiles[i].toString().substring(length-4, length).equals(".mp4")){
					type = "Video";
				}
				else{
					type = null;
				}

				temp = "" + (listOfFiles[i].length())/1024;
				if(listOfFiles[i].length()/1024 < 976562){
					size = "" + temp.charAt(0) + "." + temp.charAt(1) + "MB";
				}
				else{
					size = "" + temp.charAt(0) + "." + temp.charAt(1) + "GB";
				}
				long dateTemp = listOfFiles[i].lastModified();
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMMMMMMMMMM yyyy");
				String date = (sdf.format(listOfFiles[i].lastModified()));
				listFileLocation.add(listOfFiles[i].getAbsolutePath());
				data.add(new MediaFile(listOfFiles[i].getName().toString(), ext, size, type, date));
			}
		}
		fileTable.setItems(data);
	}
	public void ChangeDirectory(){
		DirectoryChooser directoryChooser = new DirectoryChooser();
		folder = directoryChooser.showDialog(primaryStage);
	}
	public void PlayMusic(int m){
		Populate();
		AnchorPane musicViewer = new AnchorPane();
		musicViewer.setStyle("-fx-background-color: #fff;");

		Media med = new Media(listOfFiles[m].toURI().toString());
		mp = new MediaPlayer(med);
		
		
		ImageView placeholder = new ImageView(new Image("file:placeholder.png"));
		placeholder.setTranslateX(20);
		placeholder.setTranslateY(20);
		placeholder.setFitHeight(180);
		placeholder.setFitWidth(180);
		String fullFin;
		String full = "" + listOfFiles[m].toString().substring(listOfFiles[m].toString().lastIndexOf("\\")+1);
		if(full.length() > 20){
			fullFin = full.substring(0,17) + "...";
		}
		Label name = new Label(full.substring(0, full.length()-4));
		name.setFont(Font.font ("Arial", 15));	
		name.setTextFill(Color.BLACK);
		name.setStyle("-fx-font-weight: bold;");
		name.setTranslateX(20);
		name.setTranslateY(210);
		name.setPrefWidth(180);
		name.setAlignment(Pos.CENTER);
		
		ImageView control = new ImageView(new Image("file:pause.jpg"));
		playing = true;
		control.setFitHeight(40);
		control.setFitWidth(40);
		control.setTranslateX(85);
		control.setTranslateY(240);
		control.setOnMouseClicked(e -> {
			if(playing == true){
				control.setImage(new Image("file:play.jpg"));
				mp.pause();
				playing = false;
			}
			else{
				control.setImage(new Image("file:pause.jpg"));
				mp.play();
				playing = true;
			}
		});
		musicViewer.getChildren().addAll(name, placeholder, control);

		musicView = new Stage();

		musicScene = new Scene(musicViewer, 210, 290);
		musicView.setResizable(false);
		musicView.setScene(musicScene);
		musicView.getIcons().add(new Image("file:musicIcon.png"));
		musicView.setTitle(listOfFiles[m].toString().substring(listOfFiles[m].toString().lastIndexOf("\\")+1)+" - Music Player");
		musicView.show();
		mp.play();

		musicView.setOnCloseRequest(new EventHandler<WindowEvent>(){
			public void handle(WindowEvent we){
				mp.stop();
			}
		});
	}
	public void DisplayImage(int a){ 
		Populate();
		Image i = new Image(listOfFiles[a].toURI().toString());
		ImageView imgView = new ImageView(i);
		imgView.setFitWidth(650);
		imgView.setFitHeight(650);
		imgView.setPreserveRatio(true);
		imgView.toFront();

		photoView = new Stage();
		photoView.setResizable(false);

		AnchorPane imageViewer = new AnchorPane();
		imageViewer.getChildren().add(imgView);

		Scene imgScene = new Scene(imageViewer);

		photoView.sizeToScene();
		photoView.setScene(imgScene);
		photoView.setTitle(listOfFiles[a].toString().substring(listOfFiles[a].toString().lastIndexOf("\\")+1)+" - Image Viewer");
		photoView.getIcons().add(new Image("file:imageIcon.png"));
		photoView.show();
	}
	public void DownloadFile(int d){
		
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(primaryStage);

		Path from = Paths.get(listFileLocation.get(d));
		Path to = Paths.get(selectedDirectory.toString() + "\\" + listOfFiles[d].getName());
		CopyOption[] options = new CopyOption[]{
				StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES
		}; 
		try {
			Files.copy(from, to, options);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		downloadStatus.setText("Last Download: " + listOfFiles[d].getName());

	}
	public void PlayVideo(int b){
		Populate();
		AnchorPane vidViewer = new AnchorPane();
		vidViewer.setStyle("-fx-background-color: #000;");
		Media m = new Media(listOfFiles[b].toURI().toString());
		mp = new MediaPlayer(m);
		
		mp.setOnEndOfMedia(v);
		
		MediaView mv = new MediaView(mp);
		mv.setFitWidth(650);
		mv.setFitHeight(300);
		mv.setPreserveRatio(false);
		mv.setTranslateX(10);
		mv.setTranslateY(10);
		vidViewer.getChildren().addAll(mv);

		vidView = new Stage();

		vidScene = new Scene(vidViewer, 660, 310);
		//vidView.sizeToScene();
		vidView.setResizable(false);
		vidView.setScene(vidScene);
		vidView.setTitle(listOfFiles[b].toString().substring(listOfFiles[b].toString().lastIndexOf("\\")+1)+" - Video Player");
		vidView.getIcons().add(new Image("file:videoIcon.png"));
		vidView.show();
		mp.play();

		vidView.setOnCloseRequest(new EventHandler<WindowEvent>(){
			public void handle(WindowEvent we){
				mp.stop();
			}
		});
	}
}
