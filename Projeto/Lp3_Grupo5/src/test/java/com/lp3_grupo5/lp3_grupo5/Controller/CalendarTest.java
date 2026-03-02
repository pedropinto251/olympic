package com.lp3_grupo5.lp3_grupo5.Controller;

import com.lp3_grupo5.lp3_grupo5.DAO.AthleteRegistrationDAO;
import com.lp3_grupo5.lp3_grupo5.Model.AthleteRegistration;
import com.lp3_grupo5.lp3_grupo5.Model.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class CalendarTest {

    private AthleteRegistrationDAO calendarDAO;

    @BeforeEach
    public void setUp() {
        calendarDAO = new AthleteRegistrationDAO();
    }

    private Calendar createTestCalendar() {
        Calendar calendar = new Calendar("2025-01-01 10:00:00.0", "2025-01-01 12:00:00.0", 1, 1, 1, false);
        return calendar;
    }

    @Test
    public void testInsertCalendar() throws SQLException {
        Calendar calendar = createTestCalendar();

        boolean result = calendarDAO.insertCalendar(calendar);
        assertTrue(result, "Falha ao adicionar o evento no calendário: " + calendar);
        assertNotEquals(0, calendar.getId(), "O ID do evento no calendário não foi gerado corretamente.");
        System.out.println("Evento no calendário adicionado com sucesso: " + calendar);
        calendarDAO.deleteCalendar(calendar.getId());
    }

    @Test
    public void testUpdateCalendar() throws SQLException {
        Calendar calendar = createTestCalendar();

        boolean addResult = calendarDAO.insertCalendar(calendar);
        assertTrue(addResult, "Falha ao adicionar o evento no calendário: " + calendar);
        System.out.println("Evento no calendário adicionado com sucesso: " + calendar);

        Calendar addedCalendar = calendarDAO.findById(calendar.getId());
        addedCalendar.setStartTime("2025-01-01 11:00:00.0");
        addedCalendar.setEndTime("2025-01-01 13:00:00.0");
        addedCalendar.setSportId(2); // Atualizando para um sport_id diferente
        addedCalendar.setLocationId(2); // Atualizando para um local diferente
        addedCalendar.setEventId(2); // Atualizando para um event_id diferente
        addedCalendar.setInactive(true); // Atualizando o status de inativo

        boolean updateResult = calendarDAO.updateCalendar(addedCalendar);
        assertTrue(updateResult, "Falha ao atualizar o evento no calendário: " + addedCalendar);
        System.out.println("Evento no calendário atualizado com sucesso: " + addedCalendar);

        Calendar updatedCalendar = calendarDAO.findById(calendar.getId());
        Calendar expectedCalendar = new Calendar(calendar.getId(), "2025-01-01 11:00:00.0", "2025-01-01 13:00:00.0", 2, 2, 2, true);

        assertEquals(expectedCalendar, updatedCalendar, "O evento atualizado no calendário não corresponde ao esperado");
        System.out.println("O evento atualizado no calendário corresponde ao esperado: " + updatedCalendar);

        calendarDAO.deleteCalendar(calendar.getId());
    }

    @Test
    public void testDeleteCalendar() throws SQLException {
        Calendar calendar = createTestCalendar();

        calendarDAO.insertCalendar(calendar);

        boolean result = calendarDAO.deleteCalendar(calendar.getId());
        assertTrue(result, "Falha ao apagar o evento no calendário: " + calendar);
        System.out.println("Evento no calendário inativado com sucesso: " + calendar);

        Calendar deletedCalendar = calendarDAO.findById(calendar.getId());
        assertNull(deletedCalendar, "O evento no calendário não foi removido corretamente");
        System.out.println("Evento no calendário removido com sucesso: " + calendar);
    }

    @Test
    public void testCompareCalendars() {
        Calendar calendar1 = new Calendar(1, "2025-01-01 10:00:00", "2025-01-01 12:00:00", 1, 1, 1, false);
        Calendar calendar2 = new Calendar(1, "2025-01-01 10:00:00", "2025-01-01 12:00:00", 1, 1, 1, false);

        assertEquals(calendar1, calendar2, "Os eventos no calendário não são iguais: " + calendar1 + " e " + calendar2);
        System.out.println("Os eventos no calendário são iguais: " + calendar1 + " e " + calendar2);
    }

    @Test
    public void testFindById() throws SQLException {
        Calendar calendar = createTestCalendar();

        boolean addResult = calendarDAO.insertCalendar(calendar);
        assertTrue(addResult, "Falha ao adicionar o evento no calendário: " + calendar);
        System.out.println("Evento no calendário adicionado com sucesso: " + calendar);

        Calendar fetchedCalendar = calendarDAO.findById(calendar.getId());
        assertNotNull(fetchedCalendar, "Falha ao buscar o evento no calendário por ID: " + calendar.getId());
        assertEquals(calendar, fetchedCalendar, "O evento no calendário buscado não corresponde ao esperado");
        System.out.println("Evento no calendário buscado com sucesso: " + fetchedCalendar);

        calendarDAO.deleteCalendar(calendar.getId());
    }
}