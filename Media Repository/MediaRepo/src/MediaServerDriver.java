import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class MediaServerDriver {

	public static void main(String[] args) throws RemoteException, MalformedURLException{
		Naming.rebind("RmiMediaServer", new MediaServer());
	}
}
