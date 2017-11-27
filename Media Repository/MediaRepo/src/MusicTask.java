import javafx.application.Platform;

public class MusicTask implements Runnable{

	MediaRepo media = new MediaRepo();

	int numIndex;

	public MusicTask(int index)
	{
		numIndex = index;

		Platform.runLater(new Runnable(){
			@Override
			public void run()
			{	
				try {
					media.PlayMusic(numIndex);
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
