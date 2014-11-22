package meeto.rmiserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

// TODO: Auto-generated Javadoc
/**
 * Created by Diogo on 16/10/2014.
 */
/**
 * @author Diogo
 *
 */
public interface RmiServerInterface extends Remote {
	
	public boolean userExists(String username) throws RemoteException;
	
	public boolean tryLogin(String username, String password) throws RemoteException;
	
	public String registerNewUser(String newUserInformation) throws RemoteException;
	
	public void removeUserFromAllChats(String user) throws RemoteException;
	
	public void addNewMeeting(String newMeetingInformation) throws RemoteException;
	
	public String getListUpcumingMeetings(String user) throws RemoteException;
	
	public String getListPassedMeetings(String user) throws RemoteException;
	
	public String getListCurrentMeetings(String user) throws RemoteException;
	
	public String getMeetingResume(int id_meeting) throws RemoteException;
	
	public String getListOfAgendaItensFromMeeting(int id_meeting) throws RemoteException;
	
	public String getListOfInvitesByUser(String user) throws RemoteException;
	
	public String getResumeOfInvite(int id_invite) throws RemoteException;
	
	public boolean setReplyOfInvite(int id_invite, boolean reply) throws RemoteException;
	
	public int getNumberOfInvites(String user) throws RemoteException;
	
	public boolean addAgendaItemToMeeting(int id_meeting, String newItem) throws RemoteException;
	
	public boolean removeAgendaItemFromMeeting(int id_agenda_item) throws RemoteException;
	
	public boolean modifyTitleAgendaItem(int id_agenda_item, String newAgendaItem) throws RemoteException;
	
	public boolean addKeyDecisionToAgendaItem(int id_agenda_item, String newKeyDecision) throws RemoteException;
	
	public boolean addActionItemToMeeting(int id_meeting, String newAction, String user) throws RemoteException;
	
	public int getSizeOfTodo(String user) throws RemoteException;
	
	public String getListOfActionItensFromUser(String user) throws RemoteException;
	
	public boolean setActionAsCompleted(int id_action_item) throws RemoteException;
	
	public String getListActionItensFromMeeting(int id_meeting) throws RemoteException;
	
	public String getChatHistoryFromAgendaItem(int id_agenda_item) throws RemoteException;
	
	public void addClientToChat(int id_agenda_item, String user) throws RemoteException;
	
	public boolean testIfUserIsOnChat(int id_agenda_item, String user) throws RemoteException;
	
	public boolean addMessageToChat(int id_agenda_item, String user, String message) throws RemoteException;
		
	public boolean inviteUserToMeeting(String user, int id_meeting) throws RemoteException;
	
}
