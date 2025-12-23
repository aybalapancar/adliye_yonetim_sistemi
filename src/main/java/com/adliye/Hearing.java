package com.adliye;

import java.sql.Timestamp;

public class Hearing {

    private int hearingId;
    private int caseId;
    private Timestamp date;
    private String room;
    private String description;

    public int getHearingId() { return hearingId; }
    public void setHearingId(int hearingId) { this.hearingId = hearingId; }

    public int getCaseId() { return caseId; }
    public void setCaseId(int caseId) { this.caseId = caseId; }

    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
