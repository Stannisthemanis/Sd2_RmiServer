package meeto.rmiserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer extends UnicastRemoteObject implements RmiServerInterface{

	protected RmiServer() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

}
