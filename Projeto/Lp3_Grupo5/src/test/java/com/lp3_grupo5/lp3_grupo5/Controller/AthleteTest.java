package com.lp3_grupo5.lp3_grupo5.Controller;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Athlete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AthleteTest {

    private AthleteDAO athleteDAO;

    @BeforeEach
    public void setUp() {
        athleteDAO = new AthleteDAO();
    }

    @Test
    public void testAddAthlete() throws SQLException {
        Athlete athlete = new Athlete("John Doe", "USA", "Men", 180, 75, "1990-01-01", "photo.jpg");
        boolean result = athleteDAO.addAthlete(athlete);
        assertTrue(result, "Falha ao adicionar o atleta: " + athlete);
        System.out.println("Atleta adicionado com sucesso: " + athlete);
        athleteDAO.realDeleteAthlete(athlete.getId());
    }

    @Test
    public void testUpdateAthlete() throws SQLException {
        Athlete athlete = new Athlete("John Doe", "USA", "Men", 180, 75, "1990-01-01", "photo.jpg");
        boolean addResult = athleteDAO.addAthlete(athlete);
        assertTrue(addResult, "Falha ao adicionar o atleta: " + athlete);
        System.out.println("Atleta adicionado com sucesso: " + athlete);

        Athlete addedAthlete = athleteDAO.getAthleteById(athlete.getId());
        addedAthlete.setName("Jane Doe");
        addedAthlete.setCountry("PRT");
        addedAthlete.setGenre("Women");
        addedAthlete.setHeight(170);
        addedAthlete.setWeight(65);
        addedAthlete.setDateOfBirth("1992-01-01");
        addedAthlete.setInative(false);
        addedAthlete.setPhoto("photo_updated.jpg");

        Athlete updatedAthlete = athleteDAO.updateAthlete(addedAthlete);
        assertNotNull(updatedAthlete, "Falha ao atualizar o atleta: " + addedAthlete);
        System.out.println("Atleta atualizado com sucesso: " + updatedAthlete);

        Athlete expectedAthlete = new Athlete("Jane Doe", "PRT", "Women", 170, 65, "1992-01-01", "photo_updated.jpg");
        expectedAthlete.setId(athlete.getId());
        expectedAthlete.setInative(false);

        assertEquals(expectedAthlete, updatedAthlete, "O atleta atualizado não corresponde ao esperado");
        System.out.println("O atleta atualizado corresponde ao esperado: " + updatedAthlete);

        athleteDAO.realDeleteAthlete(athlete.getId());

    }

    @Test
    public void testDeleteAthlete() throws SQLException {
        Athlete athlete = new Athlete("John Doe", "USA", "Men", 180, 75, "1990-01-01", "photo.jpg");
        athleteDAO.addAthlete(athlete);

        boolean result = athleteDAO.deleteAthlete(athlete.getId());
        assertTrue(result, "Falha ao apagar o atleta: " + athlete);
        System.out.println("Atleta inativado com sucesso: " + athlete);

        Athlete deletedAthlete = athleteDAO.getAthleteById(athlete.getId());
        assertTrue(deletedAthlete.isInative(), "O atleta não foi marcado como inativo: " + deletedAthlete);
        System.out.println("Atleta marcado como inativo: " + deletedAthlete);
        athleteDAO.realDeleteAthlete(athlete.getId());
    }

    @Test
    public void testCompareAthletes() {
        Athlete athlete1 = new Athlete("John Doe", "USA", "Men", 180, 75, "1990-01-01", "photo.jpg");
        Athlete athlete2 = new Athlete("John Doe", "USA", "Men", 180, 75, "1990-01-01", "photo.jpg");

        athlete1.setId(1);
        athlete2.setId(1);

        assertEquals(athlete1, athlete2, "Os atletas não são iguais: " + athlete1 + " e " + athlete2);
        System.out.println("Os atletas são iguais: " + athlete1 + " e " + athlete2);
    }
}