import javafx.application.Platform;

public class ImageTask implements Runnable{

	MediaRepo media = new MediaRepo();

	int numIndex;

	public ImageTask(int index)
	{
		numIndex = index;

		Platform.runLater(new Runnable(){
			@Override
			public void run()
			{	
				try {
					media.DisplayImage(numIndex);
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	@Override
	public void run() {

	}
}
