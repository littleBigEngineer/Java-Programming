import javafx.application.Platform;

public class DownloadTask implements Runnable{

	int numIndex;
	MediaRepo media = new MediaRepo();
	public DownloadTask(){
		numIndex = media.index;

		Platform.runLater(new Runnable(){
			@Override
			public void run()
			{	
				media.DownloadFile(numIndex);
			}
		});
	}

	@Override
	public void run() {

	}

}
