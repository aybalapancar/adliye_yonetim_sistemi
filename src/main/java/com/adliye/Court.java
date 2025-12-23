package com.adliye;

public class Court {
    private int courtId;
    private String city;
    private String courtType;

    public int getCourtId() { return courtId; }
    public void setCourtId(int courtId) { this.courtId = courtId; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCourtType() { return courtType; }
    public void setCourtType(String courtType) { this.courtType = courtType; }

    @Override
    public String toString() {
        return city + " - " + courtType;
    }
}
