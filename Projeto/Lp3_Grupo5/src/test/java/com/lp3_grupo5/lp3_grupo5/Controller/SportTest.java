package com.lp3_grupo5.lp3_grupo5.Controller;

import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Sport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SportTest {

    private SportDAO sportDAO;

    @BeforeEach
    public void setUp() {
        sportDAO = new SportDAO();
    }

    @Test
    public void testInsertSport() throws SQLException {
        Sport sport = new Sport(0, "Basketball", "Individual", "Men", "A team sport", 5, "Points", "One", "00:00:00", 2020, "John Doe", 2024, "00:00:00", "Jane Doe", false);
        String rule = "No double dribbling";
        boolean result = sportDAO.insertSport(sport, rule);
        assertTrue(result, "Falha ao adicionar o desporto: " + sport);
        assertNotEquals(0, sport.getId(), "O ID do desporto não foi gerado corretamente.");
        System.out.println("Desporto adicionado com sucesso: " + sport);
        sportDAO.realDeleteSport(sport.getId());
    }

    @Test
    public void testUpdateSport() throws SQLException {
        Sport sport = new Sport(0, "Basketball", "Collective", "Men", "A team sport", 5, "Points", "One", "00:00:00", 2020, "John Doe", 2024, "00:00:00", "Jane Doe", false);
        String rule = "No double dribbling";
        boolean addResult = sportDAO.insertSport(sport, rule);
        assertTrue(addResult, "Falha ao adicionar o desporto: " + sport);
        System.out.println("Desporto adicionado com sucesso: " + sport);

        Sport addedSport = sportDAO.getSportById(sport.getId());
        addedSport.setType("Individual");
        addedSport.setDescription("An individual sport");
        addedSport.setMinParticipants(1);
        addedSport.setScoringMeasure("Time");
        addedSport.setOneGame("Multiple"); // Corrigido para "No"
        addedSport.setOlympicRecordTime("00:00:01");
        addedSport.setOlympicRecordYear(2021);
        addedSport.setOlympicRecordHolder("Jane Doe");
        addedSport.setWinnerOlympicYear(2025);
        addedSport.setWinnerOlympicTime("00:00:01");
        addedSport.setWinnerOlympicHolder("John Doe");

        boolean updateResult = sportDAO.updateSport(addedSport, "No fouls");
        assertTrue(updateResult, "Falha ao atualizar o desporto: " + addedSport);
        System.out.println("Desporto atualizado com sucesso: " + addedSport);

        Sport updatedSport = sportDAO.getSportById(sport.getId());
        Sport expectedSport = new Sport(sport.getId(), "Basketball", "Individual", "Men", "An individual sport", 1, "Time", "Multiple", "00:00:01", 2021, "Jane Doe", 2025, "00:00:01", "John Doe", false);

        assertEquals(expectedSport, updatedSport, "O desporto atualizado não corresponde ao esperado");
        System.out.println("O desporto atualizado corresponde ao esperado: " + updatedSport);

        sportDAO.realDeleteSport(sport.getId());
    }

    @Test
    public void testDeleteSport() throws SQLException {
          Sport sport = new Sport(0, "Basketball", "Individual", "Men", "A team sport", 5, "Points", "One", "00:00:00", 2020, "John Doe", 2024, "00:00:00", "Jane Doe", false);
        String rule = "No double dribbling";
        sportDAO.insertSport(sport, rule);

        boolean result = sportDAO.deleteSport(sport.getId());
        assertTrue(result, "Falha ao apagar o desporto: " + sport);
        System.out.println("Desporto inativado com sucesso: " + sport);

        Sport deletedSport = sportDAO.getSportById(sport.getId());
        assertTrue(deletedSport.isInative(), "O desporto não foi marcado como inativo: " + deletedSport);
        System.out.println("Desporto marcado como inativo: " + deletedSport);
        sportDAO.realDeleteSport(sport.getId());
    }

    @Test
    public void testCompareSports() {
        Sport sport1 = new Sport(1, "Basketball", "Individual", "Men", "A team sport", 5, "Points", "One", "00:00:00", 2020, "John Doe", 2024, "00:00:00", "Jane Doe", false);
        Sport sport2 = new Sport(1, "Basketball", "Individual", "Men", "A team sport", 5, "Points", "One", "00:00:00", 2020, "John Doe", 2024, "00:00:00", "Jane Doe", false);

        assertEquals(sport1, sport2, "Os desportos não são iguais: " + sport1 + " e " + sport2);
        System.out.println("Os desportos são iguais: " + sport1 + " e " + sport2);
    }

    @Test
    public void testGetSportById() throws SQLException {
          Sport sport = new Sport(0, "Basketball", "Individual", "Men", "A team sport", 5, "Points", "One", "00:00:00", 2020, "John Doe", 2024, "00:00:00", "Jane Doe", false);
        String rule = "No double dribbling";
        boolean addResult = sportDAO.insertSport(sport, rule);
        assertTrue(addResult, "Falha ao adicionar o desporto: " + sport);
        System.out.println("Desporto adicionado com sucesso: " + sport);

        Sport fetchedSport = sportDAO.getSportById(sport.getId());
        assertNotNull(fetchedSport, "Falha ao buscar o desporto por ID: " + sport.getId());
        assertEquals(sport, fetchedSport, "O desporto buscado não corresponde ao esperado");
        System.out.println("Desporto buscado com sucesso: " + fetchedSport);

        sportDAO.realDeleteSport(sport.getId());
    }
}