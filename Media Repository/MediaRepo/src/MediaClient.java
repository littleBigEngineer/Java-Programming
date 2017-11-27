import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MediaClient extends UnicastRemoteObject implements MediaClientInt{
	private MediaServerInt mediaServer;
	protected MediaClient(MediaServerInt mediaServer) throws RemoteException {
		this.mediaServer = mediaServer;
		mediaServer.retrieveFiles();
	}

	public void recieveFiles(File files) throws RemoteException {
		System.out.println("Files Here");
		
	}

}
