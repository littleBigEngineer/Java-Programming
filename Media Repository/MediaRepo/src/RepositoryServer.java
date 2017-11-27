import java.io.File;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Hashtable;

public class RepositoryServer extends UnicastRemoteObject implements RepositoryData {
	Hashtable<String, RepositoryDataFiles> RepoDbase = new Hashtable<String, RepositoryDataFiles>();

	RepositoryServer() throws RemoteException{
		FileObserve observe = new FileObserve();
		String folderName = observe.GetFolder();
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();

		for(int i = 0; i < listOfFiles.length; i++){
			RepoDbase.put("File", new RepositoryDataFiles(listOfFiles[i].toString()));
		}
	}

	public String GetFilePath(String path) throws RemoteException{
		RepositoryDataFiles file = (RepositoryDataFiles)RepoDbase.get(path);
		return file.GetPath();
	}

	public static void main (String args[]) {

		try {

			/* Create an instance of the EUStatsServer object */
			RepositoryServer repoServer = new RepositoryServer();
			Naming.rebind("REPOSITORY-SERVER", repoServer);

		}
		catch (java.net.MalformedURLException e) {
			System.out.println("Malformed URL for RepositoryServer name " + e.toString());
		}

		catch (RemoteException e) {
			System.out.println("Communication error " + e.toString());
		}
	}
}
class RepositoryDataFiles{
	private String path;

	RepositoryDataFiles(String filePath){
		path = filePath;
	}

	String GetPath() { return path; }
}
