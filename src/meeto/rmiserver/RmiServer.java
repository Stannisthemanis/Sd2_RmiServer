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
				// createDataBase();
			}
		} catch (RemoteException e) {
			System.out.println("*** RmiServer: " + e.getMessage());
		}
		
	}
	
	private static Connection getConnectionToDataBase() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			SQLException {
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
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery("SHOW TABLES");
			if (!queryResult.next()) {
				connection.close();
				return true;
			}
		} catch (Exception e) {
			System.out.println("RmiServer.checkIfDatabaseEmpty()  " + e.getMessage());
		}
		return false;
	}
	
	public boolean userExists(String username) throws RemoteException {
		String query = "SELECT id_user FROM user1 WHERE username = '" + username + "'";
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(query);
			if (queryResult.next()) {
				return true;
			}
		} catch (Exception e) {
			System.out.println("RmiServer.userExists() " + e.getMessage());
		}
		return false;
	}
	
	public boolean tryLogin(String username, String password) throws RemoteException {
		String query = "SELECT id_user FROM user1 WHERE username = '" + username + "' AND password = '" + password + "'";
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(query);
			if (queryResult.next()) {
				return true;
			}
		} catch (Exception e) {
			System.out.println("RmiServer.tryLogin() " + e.getMessage());
		}
		return false;
	}
	
	public String registerNewUser(String newUserInformation) throws RemoteException {
		String query = "INSERT INTO user1 (username,password,dob) VALUES (" + buildNewUserString(newUserInformation) + ")";
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			return newUserInformation.split(",")[0];
		} catch (Exception e) {
			System.out.println("RmiServer.registerNewUser() " + e.getMessage());
		}
		return null;
	}
	
	private String buildNewUserString(String newUserInformation) {
		String username = newUserInformation.split(",")[0];
		String password = newUserInformation.split(",")[1];
		String[] dateOfBirth = newUserInformation.split(",")[2].split("/");
		String dob = dateOfBirth[2] + "-" + dateOfBirth[1] + "-" + dateOfBirth[0];
		return String.format("'%s','%s',TIMESTAMP('%s')", username, password, dob);
	}
	
	private int getUserId(String user) {
		String query = "SELECT id_user FROM user1 WHERE username = '" + user + "'";
		int id_user = -1;
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(query);
			while (queryResult.next()) {
				id_user = queryResult.getInt("id_user");
			}
		} catch (Exception e) {
			System.out.println("RmiServer.removeUserFromAllChats() " + e.getMessage());
		}
		return id_user;
	}
	
	public void removeUserFromAllChats(String user) throws RemoteException {
		String query = "DELETE FROM users_on_chat WHERE id_user = ";
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			query += getUserId(user) + "";
			statement.execute(query);
		} catch (Exception e) {
			System.out.println("RmiServer.removeUserFromAllChats() " + e.getMessage());
		}
		
	}
	
	public void addNewMeeting(String newMeetingInformation) throws RemoteException {
		String query1 = "INSERT INTO meeting (title,outcome,id_user,local,s_date,e_date,duration)" + " VALUES ("
				+ buildNewMeetingString(newMeetingInformation) + ")";
		String query2 = "SELECT id_meeting FROM meeting ORDER BY 1";
		int id_meeting = 0;
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query1);
			ResultSet queryResult = statement.executeQuery(query2);
			while (queryResult.next()) {
				id_meeting = queryResult.getInt("id_meeting");
			}
			addNewMeetingAgendaItens(newMeetingInformation, id_meeting);
			addNewMeetingInvites(newMeetingInformation, id_meeting);
			System.out.println("Meeting inserted with sucess");
		} catch (Exception e) {
			System.out.println("RmiServer.addNewMeeting() " + e.getMessage());
		}
	}
	
	private void addNewMeetingAgendaItens(String newMeetingInformation, int id_meeting) throws RemoteException {
		String[] tokenizer = newMeetingInformation.split("-");
		for (String s : tokenizer[6].split(",")) {
			if (s.length() > 1) {
				addAgendaItemToMeeting(id_meeting, s);
			}
		}
		addAgendaItemToMeeting(id_meeting, "Any Other Business");
	}
	
	private void addNewMeetingInvites(String newMeetingInformation, int id_meeting) throws RemoteException {
		String[] tokenizer = newMeetingInformation.split("-");
		if (!tokenizer[5].equalsIgnoreCase("none")) {
			for (String s : tokenizer[5].split(",")) {
				inviteUserToMeeting(getUserId(s), id_meeting);
			}
		}
	}
	
	private String buildNewMeetingString(String newMeetingInformation) {
		String[] tokenizer = newMeetingInformation.split("-");
		int id_user = getUserId(tokenizer[0]);
		String outcome = tokenizer[1];
		String local = tokenizer[2];
		String title = tokenizer[3];
		
		String day = tokenizer[4].split(",")[0].split("/")[0];
		String month = tokenizer[4].split(",")[0].split("/")[1];
		String year = tokenizer[4].split(",")[0].split("/")[2];
		String hour = tokenizer[4].split(",")[1].split(":")[0];
		String minutes = tokenizer[4].split(",")[1].split(":")[1];
		
		String initDate = year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":00";
		int duration = Integer.parseInt(tokenizer[7]);
		return String.format("'%s' , '%s' ,%d , '%s' , TIMESTAMP('%s') , " + "DATE_ADD('%s', INTERVAL %d MINUTE),%d", title, outcome,
				id_user, local, initDate, initDate, duration, duration);
	}
	
	public String getListUpcumingMeetings(String user) throws RemoteException {
		String query1 = String.format("SELECT m.id_meeting , m.title " + "FROM meeting m " + "WHERE m.id_user = %d "
				+ "AND (m.s_date > current_timestamp() AND m.e_date > current_timestamp())", getUserId(user));
		
		String query2 = String.format("SELECT m.id_meeting, m.title " + "FROM meeting m, invite i "
				+ "WHERE (m.id_meeting = i.id_meeting AND i.id_user = %d AND i.decision = 'yes') "
				+ "AND (m.s_date > current_timestamp() AND m.e_date > current_timestamp()) ORDER by 1;", getUserId(user));
		int id_meeting;
		String title;
		String finalList = "";
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(String.format("%s UNION %s", query1, query2));
			while (queryResult.next()) {
				id_meeting = queryResult.getInt("id_meeting");
				title = queryResult.getString("title");
				finalList += String.format("%d - %s\n", id_meeting, title);
			}
		} catch (Exception e) {
			System.out.println("RmiServer.getListUpcumingMeetings() " + e.getMessage());
		}
		return finalList;
	}
	
	public String getListPassedMeetings(String user) throws RemoteException {
		String query1 = String.format("SELECT m.id_meeting , m.title " + "FROM meeting m " + "WHERE m.id_user = %d "
				+ "AND (m.s_date < current_timestamp() AND m.e_date < current_timestamp())", getUserId(user));
		
		String query2 = String.format("SELECT m.id_meeting, m.title " + "FROM meeting m, invite i "
				+ "WHERE (m.id_meeting = i.id_meeting AND i.id_user = %d AND i.decision = 'yes') "
				+ "AND (m.s_date < current_timestamp() AND m.e_date < current_timestamp()) ORDER by 1;", getUserId(user));
		int id_meeting;
		String title;
		String finalList = "";
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(String.format("%s UNION %s", query1, query2));
			while (queryResult.next()) {
				id_meeting = queryResult.getInt("id_meeting");
				title = queryResult.getString("title");
				finalList += String.format("%d - %s\n", id_meeting, title);
			}
		} catch (Exception e) {
			System.out.println("RmiServer.getListPassedMeetings() " + e.getMessage());
		}
		return finalList;
	}
	
	public String getListCurrentMeetings(String user) throws RemoteException {
		String query1 = String.format("SELECT m.id_meeting , m.title " + "FROM meeting m " + "WHERE m.id_user = %d "
				+ "AND (m.s_date < current_timestamp() AND m.e_date > current_timestamp())", getUserId(user));
		
		String query2 = String.format("SELECT m.id_meeting, m.title " + "FROM meeting m, invite i "
				+ "WHERE (m.id_meeting = i.id_meeting AND i.id_user = %d AND i.decision = 'yes') "
				+ "AND (m.s_date < current_timestamp() AND m.e_date > current_timestamp()) ORDER by 1;", getUserId(user));
		int id_meeting;
		String title;
		String finalList = "";
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(String.format("%s UNION %s", query1, query2));
			while (queryResult.next()) {
				id_meeting = queryResult.getInt("id_meeting");
				title = queryResult.getString("title");
				finalList += String.format("%d - %s\n", id_meeting, title);
			}
		} catch (Exception e) {
			System.out.println("RmiServer.getListCurrentMeetings() " + e.getMessage());
		}
		return finalList;
	}
	
	public String getMeetingResume(int id_meeting) throws RemoteException {
		String query1 = String.format("SELECT m.* , u.username FROM meeting m, user1 u WHERE id_meeting = %d AND m.id_user = u.id_user",
				id_meeting);
		String query2 = String
				.format("SELECT u.username FROM user1 u, invite i WHERE u.id_user = i.id_user AND i.decision = 'yes' AND i.id_meeting = %d ORDER BY 1;",
						id_meeting);
		String title = "";
		String username = "";
		String outcome = "";
		String startDate = "";
		String endDate = "";
		String listOfUsers = "Users Attending:\n";
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(query1);
			while (queryResult.next()) {
				title = " Title: " + queryResult.getString("title") + "\n";
				username = "Created By: " + queryResult.getString("username") + "\n";
				outcome = "Expected outcome: " + queryResult.getString("outcome") + "\n";
				startDate = "Starts at: " + queryResult.getTimestamp("s_date").toString() + "\n";
				endDate = "Ends at: " + queryResult.getTimestamp("e_date").toString() + "\n";
			}
			queryResult = statement.executeQuery(query2);
			if (!queryResult.next()) {
				listOfUsers += "      - No user have confirmed their presence yet\n";
			} else
				queryResult.previous();
			while (queryResult.next()) {
				System.out.println();
				listOfUsers += "      - " + queryResult.getString("username") + "\n";
			}
		} catch (Exception e) {
			System.out.println("RmiServer.getMeetingResume() " + e.getMessage());
		}
		
		return String.format("%s %s %s %s %s %s %s %s", title, username, outcome, startDate, endDate, listOfUsers,
				getListOfAgendaItensFromMeeting(id_meeting), getListActionItensFromMeeting(id_meeting));
	}
	
	public String getListOfAgendaItensFromMeeting(int id_meeting) throws RemoteException {
		String query = String.format(
				"SELECT id_agenda_item, item_to_discuss, key_decision FROM agenda_item WHERE id_meeting = %d ORDER BY 1", id_meeting);
		String finalList = "List of agenda itens: \n";
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(query);
			while (queryResult.next()) {
				finalList += "      - " + queryResult.getInt("id_agenda_item") + " - " + queryResult.getString("item_to_discuss");
				if (queryResult.getString("key_decision") != null) {
					finalList += queryResult.getString("key_decision");
				}
				finalList += "\n";
			}
		} catch (Exception e) {
			System.out.println("RmiServer.getListOfAgendaItensFromMeeting() " + e.getMessage());
		}
		return finalList;
	}
	
	public String getListOfInvitesByUser(String user) throws RemoteException {
		String query = String.format("SELECT i.id_invite , m.title FROM invite i, meeting m "
				+ "WHERE i.id_meeting = m.id_meeting AND i.id_user = %d AND i.decision = 'unread' ORDER BY 1;", getUserId(user));
		String finalList = "Invites unanswered:\n";
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(query);
			if (!queryResult.next()) {
				finalList += "      - You have no invites unanswered\n";
			} else
				queryResult.previous();
			while (queryResult.next()) {
				finalList += "      - " + queryResult.getInt("id_invite") + " - " + queryResult.getString("title") + "\n";
			}
		} catch (Exception e) {
			System.out.println("RmiServer.getListOfInvitesByUser() " + e.getMessage());
		}
		return finalList;
	}
	
	public String getResumeOfInvite(int id_invite) throws RemoteException {
		String query = String.format("SELECT id_meeting FROM invite WHERE id_invite = %d", id_invite);
		int id_meeting = -1;
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(query);
			while (queryResult.next()) {
				id_meeting = queryResult.getInt("id_meeting");
			}
		} catch (Exception e) {
			System.out.println("RmiServer.getResumeOfInvite() " + e.getMessage());
		}
		return String.format("Resume of Invite %d:\n%s", id_invite, getMeetingResume(id_meeting));
	}
	
	public boolean setReplyOfInvite(int id_invite, boolean reply) throws RemoteException {
		String answer;
		if (reply == true)
			answer = "yes";
		else
			answer = "no";
		String query = String.format("UPDATE invite SET decision = '%s' WHERE id_invite = %d;", answer, id_invite);
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("RmiServer.setReplyOfInvite() " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public int getNumberOfInvites(String user) throws RemoteException {
		String query = String.format(" SELECT count(*) AS \"nrInvites\" FROM invite WHERE id_user = %d AND decision = 'unread';",
				getUserId(user));
		int numberOfInvites = -1;
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(query);
			while (queryResult.next()) {
				numberOfInvites = queryResult.getInt("nrInvites");
			}
		} catch (Exception e) {
			System.out.println("RmiServer.getNumberOfInvites() " + e.getMessage());
		}
		return numberOfInvites;
	}
	
	public boolean addAgendaItemToMeeting(int id_meeting, String newItem) throws RemoteException {
		String query = String.format("INSERT INTO agenda_item (item_to_discuss,id_meeting) VALUES ('%s', %d)", newItem, id_meeting);
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("RmiServer.addAgendaItemToMeeting() " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean removeAgendaItemFromMeeting(int id_agenda_item) throws RemoteException {
		String query = String.format("DELETE FROM agenda_item WHERE id_agenda_item = %d", id_agenda_item);
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("RmiServer.removeAgendaItemFromMeeting() " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean modifyTitleAgendaItem(int id_agenda_item, String newAgendaItem) throws RemoteException {
		String query = String.format("UPDATE agenda_item SET item_to_discuss = '%s' WHERE id_agenda_item = %d;", newAgendaItem,
				id_agenda_item);
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("RmiServer.setReplyOfInvite() " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean addKeyDecisionToAgendaItem(int id_agenda_item, String newKeyDecision) throws RemoteException {
		String query = String.format("UPDATE agenda_item SET key_decision = '%s' " + "WHERE id_agenda_item = %d;", newKeyDecision,
				id_agenda_item);
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("RmiServer.addKeyDecisionToAgendaItem() " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean addActionItemToMeeting(int id_meeting, String newAction, String user) throws RemoteException {
		String query = String.format("INSERT INTO action_item (name, id_user, id_meeting) VALUES ('%s', %d, %d)", newAction,
				getUserId(user), id_meeting);
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("RmiServer.addActionItemToMeeting() " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public int getSizeOfTodo(String user) throws RemoteException {
		String query = String.format(" SELECT count(*) AS \"sizeOfTodo\" FROM action_item WHERE id_user = %d;", getUserId(user));
		int sizeOfTodo = -1;
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(query);
			while (queryResult.next()) {
				sizeOfTodo = queryResult.getInt("sizeOfTodo");
			}
		} catch (Exception e) {
			System.out.println("RmiServer.getSizeOfTodo() " + e.getMessage());
		}
		return sizeOfTodo;
	}
	
	public String getListOfActionItensFromUser(String user) throws RemoteException {
		String query = String.format("SELECT id_action_item, name, completed FROM action_item "
				+ "WHERE id_user = %d AND completed = 'To Be Done'", getUserId(user));
		String listOfAction = "List of actions:\n";
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(query);
			if (!queryResult.next()) {
				listOfAction += "      - You have no action to be done\n";
			} else
				queryResult.previous();
			while (queryResult.next()) {
				listOfAction += "      - " + queryResult.getInt("id_action_item") + " - " + queryResult.getString("name") + " -> "
						+ queryResult.getString("completed") + "\n";
			}
		} catch (Exception e) {
			System.out.println("RmiServer.getListOfActionItensFromUser() " + e.getMessage());
		}
		return listOfAction;
	}
	
	public boolean setActionAsCompleted(int id_action_item) throws RemoteException {
		String query = String.format("UPDATE action_item SET completed = 'Done' WHERE id_action_item = %d;", id_action_item);
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("RmiServer.setActionAsCompleted() " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public String getListActionItensFromMeeting(int id_meeting) throws RemoteException {
		String query = String.format("SELECT ac.id_action_item, ac.name, u.username, ac.completed FROM action_item ac, user1 u "
				+ "WHERE ac.id_meeting = %d AND ac.id_user = u.id_user ORDER BY 1", id_meeting);
		String finalList = "List of actions:\n";
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(query);
			
			if (!queryResult.next()) {
				finalList += "      - No action as been defined yet\n";
			} else
				queryResult.previous();
			while (queryResult.next()) {
				System.out.println("ac " + queryResult.getInt("id_action_item"));
				finalList += "      - " + queryResult.getInt("id_action_item") + " - " + queryResult.getString("name") + " -> "
						+ queryResult.getString("username") + " : " + queryResult.getString("completed") + "\n";
			}
		} catch (Exception e) {
			System.out.println("RmiServer.getListActionItensFromMeeting() " + e.getMessage());
		}
		return finalList;
	}
	
	public String getChatHistoryFromAgendaItem(int id_agenda_item) throws RemoteException {
		String query = String.format(
				"SELECT m.mdate, u.username, m.messages FROM message m, user1 u WHERE m.id_agenda_item = %d AND u.id_user = m.id_user;",
				id_agenda_item);
		String messageHistory = "Chat History:\n";
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(query);
			if (!queryResult.next()) {
				messageHistory += "      - This agenda item as not chat history\n";
			} else
				queryResult.previous();
			while (queryResult.next()) {
				messageHistory += queryResult.getTimestamp("mdate") + " - " + queryResult.getString("username") + ": "
						+ queryResult.getString("messages") + "\n";
			}
		} catch (Exception e) {
			System.out.println("RmiServer.getChatHistoryFromAgendaItem() " + e.getMessage());
		}
		return messageHistory;
	}
	
	public void addClientToChat(int id_agenda_item, String user) throws RemoteException {
		String query = String.format("INSERT INTO users_on_chat (id_agenda_item, id_user) VALUES(%d, %d)", id_agenda_item, getUserId(user));
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("RmiServer.addClientToChat() " + e.getMessage());
		}
	}
	
	public boolean testIfUserIsOnChat(int id_agenda_item, String user) throws RemoteException {
		String query = String.format("SELECT id_user FROM agenda_item WHERE id_user = %d AND id_agenda_item = %d", getUserId(user),
				id_agenda_item);
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			ResultSet queryResult = statement.executeQuery(query);
			if (queryResult.next())
				return true;
		} catch (Exception e) {
			System.out.println("RmiServer.getChatHistoryFromAgendaItem() " + e.getMessage());
		}
		return false;
	}
	
	public boolean addMessageToChat(int id_agenda_item, String user, String message) throws RemoteException {
		String query = String.format("INSERT INTO message (id_agenda_item, id_user, mdate ,messages) "
				+ "values(%d, %d, current_timestamp(),'%s');", id_agenda_item, getUserId(user), message);
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("RmiServer.addClientToChat() " + e.getMessage());
		}
		return false;
	}
	
	public boolean inviteUserToMeeting(int id_user, int id_meeting) throws RemoteException {
		String query = String.format("INSERT INTO invite (id_meeting, id_user) VALUES (%d, %d)", id_meeting, id_user);
		try {
			Connection connection = getConnectionToDataBase();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (Exception e) {
			System.out.println("RmiServer.addAgendaItemToMeeting() " + e.getMessage());
		}
		return false;
	}
	
}
