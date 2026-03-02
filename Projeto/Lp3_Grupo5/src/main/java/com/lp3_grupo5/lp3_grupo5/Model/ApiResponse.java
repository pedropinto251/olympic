package com.lp3_grupo5.lp3_grupo5.Model;

import java.util.List;

public class ApiResponse {
    private String Status;
    private List<Client> Clients;
    private List<Game> Games;
    private List<Ticket> TicketInfo; // Adicionando a lista de bilhetes
    private int responseCode;
    private String responseMessage;

    // Getters e setters
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<Client> getClients() {
        return Clients;
    }

    public void setClients(List<Client> clients) {
        Clients = clients;
    }

    public List<Game> getGames() {
        return Games;
    }

    public void setGames(List<Game> games) {
        Games = games;
    }

    public List<Ticket> getTicketInfo() {
        return TicketInfo;
    }

    public void setTicketInfo(List<Ticket> ticketInfo) {
        TicketInfo = ticketInfo;
    }

    public ApiResponse(int responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}