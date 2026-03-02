package com.lp3_grupo5.lp3_grupo5.Model;

import javafx.scene.image.ImageView;

public class Ticket {
    private String Id;
    private String StartDate;
    private String EndDate;
    private String Location;
    private String Seat;
    private String TicketQR; // Armazenar a string base64
    private transient ImageView TicketQRImage; // Campo transitório para o ImageView

    // Getters and setters
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getSeat() {
        return Seat;
    }

    public void setSeat(String seat) {
        Seat = seat;
    }

    public String getTicketQR() {
        return TicketQR;
    }

    public void setTicketQR(String ticketQR) {
        TicketQR = ticketQR;
    }

    public ImageView getTicketQRImage() {
        return TicketQRImage;
    }

    public void setTicketQRImage(ImageView ticketQRImage) {
        TicketQRImage = ticketQRImage;
    }
}