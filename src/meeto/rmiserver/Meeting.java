package meeto.rmiserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Ricardo on 15/10/2014.
 */
public class Meeting implements Serializable {
    protected String meetingTitle;
    protected String local;
    protected String responsibleUser;
    protected String desireOutcome;
    protected Calendar startDate;
    protected Calendar endDate;
    protected int duration; //hours
    protected ArrayList<String> usersAccepted;
    protected ArrayList<AgendaItem> agendaItems;
    protected ArrayList<ActionItem> actionItems;

    public Meeting(String meetingTitle, String local, String responsibleUser, String desireOutcome, Calendar startDate, int duration,
                   ArrayList<AgendaItem> agendaItems) {
        this.meetingTitle = meetingTitle;
        this.local = local;
        this.responsibleUser = responsibleUser;
        this.desireOutcome = desireOutcome;
        this.startDate = startDate;
        this.duration = duration;
        this.endDate = (Calendar) startDate.clone();
        this.endDate.add(Calendar.MINUTE, duration);
        this.usersAccepted = new ArrayList<String>();
        this.agendaItems = agendaItems;
        this.actionItems = new ArrayList<ActionItem>();
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public String getLocal() {
        return local;
    }

    public String getResponsibleUser() {
        return responsibleUser;
    }

    public String getDesireOutcome() {
        return desireOutcome;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public int getDuration() {
        return duration;
    }

    public ArrayList<String> getUsersInvited() {
        return usersAccepted;
    }

    public ArrayList<AgendaItem> getAgendaItems() {
        return agendaItems;
    }


    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setResponsibleUser(String responsibleUser) {
        this.responsibleUser = responsibleUser;
    }

    public void setDesireOutcome(String desireOutcome) {
        this.desireOutcome = desireOutcome;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void addAgendaItem(AgendaItem agendaItem) {
        this.agendaItems.add(agendaItem);
    }

    public void addAgendaItem(AgendaItem agendaItem, boolean flag) {
        this.agendaItems.add(this.agendaItems.size() - 1, agendaItem);
    }

    public void removerAgendaItem(int nAgendaItem) {
        this.agendaItems.remove(nAgendaItem);
    }

    public void addActionItem(ActionItem actionItem) {
        this.actionItems.add(actionItem);
    }

    public void addUser(String userAccepted) {
        this.usersAccepted.add(userAccepted);
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }


    public boolean isInvited(String username) {
        for (String i : usersAccepted) {
            if (i.equals(username))
                return true;
        }
        return false;
    }

    private String printAcceptedUsers() {
        String outPut = "| ";
        for (String user : usersAccepted) {
            outPut += user + " | ";

        }
        return outPut;
    }

    public String printAgendaItems() {
        String outPut = "";
        for (int i = 0; i < this.agendaItems.size(); i++) {
            outPut += (i + 1) + "- " + this.agendaItems.get(i).getItemToDiscuss();
            if (this.agendaItems.get(i).getKeyDecision() != null)
                outPut += "-> Decision: " + this.agendaItems.get(i).getKeyDecision();
            outPut += "\n";
        }
        return outPut;
    }

    private String printDate(Calendar date) {
        return date.get(Calendar.DAY_OF_MONTH) + "/" +
                date.get(Calendar.MONTH) + "/" +
                date.get(Calendar.YEAR) + " " +
                date.get(Calendar.HOUR) + ":" +
                date.get(Calendar.MINUTE) + "\n";
    }

    public String printActionItens() {
        String outPut = "";
        int i = 1;
        if (this.actionItems.size() > 0) {
            outPut += "ACTION ITENS:\n";
            for (ActionItem aItem : actionItems) {
                outPut += (i++) + aItem.toString();
            }
            return outPut;
        } else {
            return "This meeting have no agenda itens";
        }
    }

    @Override
    public String toString() {
        return "TITLE: " + meetingTitle + "\n" +
                "LOCAL: " + local + '\n' +
                "RESPONSIBLE USER: " + responsibleUser + '\n' +
                "DESIRE OUTCOME: " + desireOutcome + '\n' +
                "START DATE: " + printDate(startDate) +
                "END DATE: " + printDate(endDate) +
                "INVITATIONS: " + printAcceptedUsers() + "\n" +
                "AGENDA ITEMS:\n" + printAgendaItems() +
                printActionItens() + "\n\n\n";
    }
}
