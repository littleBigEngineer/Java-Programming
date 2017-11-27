import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;

public class ObserveTask extends MediaRepo implements Runnable{

	FileObserve observer = new FileObserve();
	MediaRepo media;
	Long temp;
	int sec=0;

	String file = "repository/";

	public ObserveTask(MediaRepo r) {
		media = r;
		Observer();
	}

	public void Observer(){
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
			private long timeStamp;
			@Override
			public void run() {
				File fileOb = new File("repository/");
				long timeStamp = fileOb.lastModified();

				if(this.timeStamp != timeStamp){
					sec=0;
					this.timeStamp = timeStamp;
					observer.Observe(file); 
					media.Populate();
				}
				else{
					sec++;
				}
				
				Platform.runLater(new Runnable(){
					@Override
					public void run()
					{	if(sec % 5 == 0)
							MediaRepo.TimeUpdate(sec);
					}
				});
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
