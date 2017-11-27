import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RepositoryData extends Remote {
	String GetFilePath(String path) throws RemoteException;
}
