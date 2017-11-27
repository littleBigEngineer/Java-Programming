import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MediaServerInt extends Remote {
	String retrieveFiles() throws RemoteException;
}
