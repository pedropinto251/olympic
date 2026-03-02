package com.lp3_grupo5.lp3_grupo5.Controller;

import com.lp3_grupo5.lp3_grupo5.DAO.EventDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    private EventDAO eventDAO;

    @BeforeEach
    public void setUp() {
        eventDAO = new EventDAO();
    }

    @Test
    public void testAddEvent() throws SQLException {
        Event event = new Event(2024, "Portugal", "Mascot1", false);
        boolean result = eventDAO.addEvent(event);
        assertTrue(result, "Falha ao adicionar o evento: " + event);
        System.out.println("Evento adicionado com sucesso: " + event);
        eventDAO.deleteEvent(event.getId());
    }

    @Test
    public void testUpdateEvent() throws SQLException {
        Event event = new Event(2024, "Portugal", "Mascot1", false);
        boolean addResult = eventDAO.addEvent(event);
        assertTrue(addResult, "Falha ao adicionar o evento: " + event);
        System.out.println("Evento adicionado com sucesso: " + event);

        Event addedEvent = eventDAO.getEventById(event.getId());
        addedEvent.setYear(2025);
        addedEvent.setCountry("Spain");
        addedEvent.setMascot("Mascot2");

        boolean updateResult = eventDAO.updateEvent(addedEvent);
        assertTrue(updateResult, "Falha ao atualizar o evento: " + addedEvent);
        System.out.println("Evento atualizado com sucesso: " + addedEvent);

        Event updatedEvent = eventDAO.getEventById(event.getId());
        Event expectedEvent = new Event(event.getId(), 2025, "Spain", "Mascot2", false);

        assertEquals(expectedEvent, updatedEvent, "O evento atualizado não corresponde ao esperado");
        System.out.println("O evento atualizado corresponde ao esperado: " + updatedEvent);

        eventDAO.deleteEvent(event.getId());
    }

    @Test
    public void testCompareEvents() {
        Event event1 = new Event(1, 2024, "Portugal", "Mascot1", false);
        Event event2 = new Event(1, 2024, "Portugal", "Mascot1", false);

        assertEquals(event1, event2, "Os eventos não são iguais: " + event1 + " e " + event2);
        System.out.println("Os eventos são iguais: " + event1 + " e " + event2);
    }

    @Test
    public void testGetEventById() throws SQLException {
        Event event = new Event(2024, "Portugal", "Mascot1", false);
        boolean addResult = eventDAO.addEvent(event);
        assertTrue(addResult, "Falha ao adicionar o evento: " + event);
        System.out.println("Evento adicionado com sucesso: " + event);

        Event fetchedEvent = eventDAO.getEventById(event.getId());
        assertNotNull(fetchedEvent, "Falha ao buscar o evento por ID: " + event.getId());
        assertEquals(event, fetchedEvent, "O evento buscado não corresponde ao esperado");
        System.out.println("Evento buscado com sucesso: " + fetchedEvent);

        eventDAO.deleteEvent(event.getId());
    }

    @Test
    public void testMarkEventAsInative() throws SQLException {
        Event event = new Event(2024, "Portugal", "Mascot1", false);
        boolean addResult = eventDAO.addEvent(event);
        assertTrue(addResult, "Falha ao adicionar o evento: " + event);
        System.out.println("Evento adicionado com sucesso: " + event);

        boolean markResult = eventDAO.markEventAsinative(event.getId());
        assertTrue(markResult, "Falha ao marcar o evento como inativo: " + event);
        System.out.println("Evento marcado como inativo com sucesso: " + event);

        Event markedEvent = eventDAO.getEventById(event.getId());
        assertTrue(markedEvent.isInative(), "O evento não foi marcado como inativo: " + markedEvent);
        System.out.println("Evento marcado como inativo: " + markedEvent);

        eventDAO.deleteEvent(event.getId());
    }
}