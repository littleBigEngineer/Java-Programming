import java.io.*;
public class FileObserve extends MediaRepo
{
	MediaRepo mediaRepo = new MediaRepo();
	String folderString;

	public static void main(String[] args) {
		Subject subject = new Subject();
		new Music(subject);
		new Video(subject);
		new Images(subject);
		
	}
	public void Observe(String folderName){
		folderString = folderName;
		Subject subject = new Subject();
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();
		String ext;

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				int length = listOfFiles[i].toString().length();
				ext = listOfFiles[i].toString().substring(length-4, length);
				subject.setState(ext);
			}

		}
		GetFolder();
	}
	public String GetFolder(){
		return folderString;
	}
	
}
