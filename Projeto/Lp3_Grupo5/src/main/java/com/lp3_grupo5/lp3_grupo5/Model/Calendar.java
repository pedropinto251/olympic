package com.lp3_grupo5.lp3_grupo5.Model;

public class Calendar {
    private int id;
    private String startTime;
    private String endTime;
    private String sportName;
    private String location;
    private boolean inactive;
    private int year; // Novo campo para o ano do evento
    private String sportFullName; // Novo campo para o nome completo do desporto
    private int sportId; // Novo campo para o ID do esporte
    private int locationId; // Novo campo para o ID do local
    private int eventId; // Novo campo para o ID do evento

    public Calendar(String startTime, String endTime, String sportName, String location, boolean inactive, int year, String sportFullName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.sportName = sportName;
        this.location = location;
        this.inactive = inactive;
        this.year = year;
        this.sportFullName = sportFullName;
    }

    public Calendar(String startTime, String endTime, int sportId, int locationId, int eventId, boolean inactive) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.sportId = sportId;
        this.locationId = locationId;
        this.eventId = eventId;
        this.inactive = inactive;
    }

    public Calendar(int id, String startTime, String endTime, int sportId, int locationId, int eventId, boolean inactive) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sportId = sportId;
        this.locationId = locationId;
        this.eventId = eventId;
        this.inactive = inactive;
    }
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSportFullName() {
        return sportFullName;
    }

    public void setSportFullName(String sportFullName) {
        this.sportFullName = sportFullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Calendar calendar = (Calendar) o;

        if (id != calendar.id) return false;
        if (sportId != calendar.sportId) return false;
        if (locationId != calendar.locationId) return false;
        if (eventId != calendar.eventId) return false;
        if (inactive != calendar.inactive) return false;
        if (!startTime.equals(calendar.startTime)) return false;
        return endTime.equals(calendar.endTime);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        result = 31 * result + sportId;
        result = 31 * result + locationId;
        result = 31 * result + eventId;
        result = 31 * result + (inactive ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "id=" + id +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", sportId=" + sportId +
                ", locationId=" + locationId +
                ", eventId=" + eventId +
                ", inactive=" + inactive +
                '}';
    }
}