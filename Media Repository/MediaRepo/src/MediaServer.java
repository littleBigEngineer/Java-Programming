import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import com.sun.media.jfxmedia.Media;

public class MediaServer extends UnicastRemoteObject implements MediaServerInt{
	private static final long serialVersionUID = 1L;
	protected MediaServer() throws RemoteException {
		System.out.println("Server Started");
	}
	//public synchronized void runInterface throws RemoteException {
	//	MediaRepo.launch(MediaRepo.class);
	//}
	
	public synchronized String retrieveFiles() throws RemoteException{
		return "File";
	}

}
