package meeto.rmiserver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings("deprecation")
public class RmiServer extends UnicastRemoteObject implements RmiServerInterface {
	
	private static final long	serialVersionUID	= 1L;
	
	protected RmiServer() throws RemoteException {
		super();
	}
	
	public static void main(String[] args) {
		
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());
		try {
			RmiServerInterface rmiServer = new RmiServer();
			LocateRegistry.createRegistry(1099).rebind("DataBase", rmiServer);
			System.out.println("RmiServer Ready");
			if (isDatabaseEmpty()) {
				//createDataBase();
			}
			System.exit(0); // NAO ESQUECER DE REMOVER ISTO
		} catch (RemoteException e) {
			System.out.println("*** RmiServer: " + e.getMessage());
		}
		
	}
	
	private static Connection getConnectionToDataBase() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "sd2_bd";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";
		String password = "Roxkax77";
		try {
			if (InetAddress.getLocalHost().equals(InetAddress.getByName("ricardo"))) {
				userName = "userDoRicardo";
				password = "passDoRicardo";
			}
		} catch (UnknownHostException e) {
			
		}
		Class.forName(driver).newInstance();
		return DriverManager.getConnection(url + dbName, userName, password);
	}
	
	public static boolean isDatabaseEmpty() {
		try {
			Connection connection = getConnectionToDataBase();
			Statement statment = connection.createStatement();
			ResultSet queryResult = statment.executeQuery("SHOW TABLES");
			if(!queryResult.next()){
				connection.close();	
				return true;
			}
		} catch (Exception e) {
			System.out.println("RmiServer.checkIfDatabaseEmpty()  " + e.getMessage());
		}
		return false;
	}
	
}
