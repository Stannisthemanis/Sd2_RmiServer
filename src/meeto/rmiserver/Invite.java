package meeto.rmiserver;

import java.io.Serializable;

/**
 * Created by Ricardo on 15/10/2014.
 */
public class Invite implements Serializable {
	protected Meeting meeting;

	protected String invitedUser;

	public Invite(Meeting meeting, int decision, String invitedUser) {
		this.meeting = meeting;
		this.invitedUser = invitedUser;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public String getInvitedUser() {
		return invitedUser;
	}

	public void setInvitedUser(String invitedUser) {
		this.invitedUser = invitedUser;
	}

	@Override
	public String toString() {
		return "Invite: " + '\n' + "Meeting: " + meeting + '\n'
				+ "Invited User: " + invitedUser;
	}
}
