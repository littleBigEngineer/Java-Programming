import javafx.application.Platform;

public class VideoTask implements Runnable{

	MediaRepo media = new MediaRepo();

	int numIndex;

	public VideoTask(int index)
	{
		numIndex = index;

		Platform.runLater(new Runnable(){
			@Override
			public void run()
			{	
				try {
					media.PlayVideo(numIndex);
					Thread.sleep(500);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void run() {

	}
}
