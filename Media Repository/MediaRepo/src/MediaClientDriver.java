import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class MediaClientDriver {
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException{
		String MediaServerURL = "rmi://localhost.RmiMediaServer";
		MediaServerInt mediaServer = (MediaServerInt) Naming.lookup(MediaServerURL);
		new MediaClient(mediaServer);
	}
}
