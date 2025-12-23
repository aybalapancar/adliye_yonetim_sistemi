package com.adliye;

public class CaseParty {

    private int id;
    private int caseId;
    private String partyName;     // party_name
    private String partyRole;     // davaci / davali
    private Integer lawyerId;     // opsiyonel

    // UI için gösterim alanı (avukat adı vs istersen sonra eklenir)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCaseId() { return caseId; }
    public void setCaseId(int caseId) { this.caseId = caseId; }

    public String getPartyName() { return partyName; }
    public void setPartyName(String partyName) { this.partyName = partyName; }

    public String getPartyRole() { return partyRole; }
    public void setPartyRole(String partyRole) { this.partyRole = partyRole; }

    public Integer getLawyerId() { return lawyerId; }
    public void setLawyerId(Integer lawyerId) { this.lawyerId = lawyerId; }
}
