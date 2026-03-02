package com.lp3_grupo5.lp3_grupo5.Controller;

import com.lp3_grupo5.lp3_grupo5.DAO.TeamDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {

    private TeamDAO teamDAO;

    @BeforeEach
    public void setUp() {
        teamDAO = new TeamDAO();
    }

    @Test
    public void testInsertTeam() throws SQLException {
        Team team = new Team(0, "Dream Team", "USA", "Men", "Basketball", 1, 1992, false);
        boolean result = teamDAO.insertTeam(team);
        assertTrue(result, "Falha ao adicionar a equipa: " + team);
        assertNotEquals(0, team.getId(), "O ID da equipa não foi gerado corretamente.");
        System.out.println("Equipa adicionada com sucesso: " + team);
        teamDAO.realDeleteTeam(team.getId());
    }

    @Test
    public void testUpdateTeam() throws SQLException {
        Team team = new Team(0, "Dream Team", "USA", "Men", "Basketball", 1, 1992, false);
        boolean addResult = teamDAO.insertTeam(team);
        assertTrue(addResult, "Falha ao adicionar a equipa: " + team);
        System.out.println("Equipa adicionada com sucesso: " + team);

        Team addedTeam = teamDAO.findById(team.getId());
        addedTeam.setCountry("CAN");
        addedTeam.setSport("Hockey");
        addedTeam.setFoundationYear(1980);
        addedTeam.setSportsId(2);

        boolean updateResult = teamDAO.updateTeam(addedTeam);
        assertTrue(updateResult, "Falha ao atualizar a equipa: " + addedTeam);
        System.out.println("Equipa atualizada com sucesso: " + addedTeam);

        Team updatedTeam = teamDAO.findById(team.getId());
        Team expectedTeam = new Team(team.getId(), "Dream Team", "CAN", "Men", "Hockey", 2, 1980, false);

        assertEquals(expectedTeam, updatedTeam, "A equipa atualizada não corresponde ao esperado");
        System.out.println("A equipa atualizada corresponde ao esperado: " + updatedTeam);

        teamDAO.realDeleteTeam(team.getId());
    }

    @Test
    public void testDeleteTeam() throws SQLException {
        Team team = new Team(0, "Dream Team", "USA", "Men", "Basketball", 1, 1992, false);
        teamDAO.insertTeam(team);

        boolean result = teamDAO.deleteTeam(team.getId());
        assertTrue(result, "Falha ao apagar a equipa: " + team);
        System.out.println("Equipa inativada com sucesso: " + team);

        Team deletedTeam = teamDAO.findById(team.getId());
        assertTrue(deletedTeam.isInative(), "A equipa não foi marcada como inativa: " + deletedTeam);
        System.out.println("Equipa marcada como inativa: " + deletedTeam);
        teamDAO.realDeleteTeam(team.getId());
    }

    @Test
    public void testCompareTeams() {
        Team team1 = new Team(1, "Dream Team", "USA", "Men", "Basketball", 1, 1992, false);
        Team team2 = new Team(1, "Dream Team", "USA", "Men", "Basketball", 1, 1992, false);

        assertEquals(team1, team2, "As equipas não são iguais: " + team1 + " e " + team2);
        System.out.println("As equipas são iguais: " + team1 + " e " + team2);
    }

    @Test
    public void testFindById() throws SQLException {
        Team team = new Team(0, "Dream Team", "USA", "Men", "Basketball", 1, 1992, false);
        boolean addResult = teamDAO.insertTeam(team);
        assertTrue(addResult, "Falha ao adicionar a equipa: " + team);
        System.out.println("Equipa adicionada com sucesso: " + team);

        Team fetchedTeam = teamDAO.findById(team.getId());
        assertNotNull(fetchedTeam, "Falha ao buscar a equipa por ID: " + team.getId());
        assertEquals(team, fetchedTeam, "A equipa buscada não corresponde ao esperado");
        System.out.println("Equipa buscada com sucesso: " + fetchedTeam);

        teamDAO.realDeleteTeam(team.getId());
    }
}