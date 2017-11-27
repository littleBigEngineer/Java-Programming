

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MediaClientInt extends Remote {
	void recieveFiles(File files) throws RemoteException;
}
