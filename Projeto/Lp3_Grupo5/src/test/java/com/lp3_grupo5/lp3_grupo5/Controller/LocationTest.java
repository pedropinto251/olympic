package com.lp3_grupo5.lp3_grupo5.Controller;

import com.lp3_grupo5.lp3_grupo5.DAO.LocalDAO;
import com.lp3_grupo5.lp3_grupo5.Model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class LocationTest {

    private LocalDAO locationDAO;

    @BeforeEach
    public void setUp() {
        locationDAO = new LocalDAO();
    }

    @Test
    public void testInsertLocation() throws SQLException {
        Location location = new Location(0, "123 Main St", "Springfield", 5000, 1990, false, "interior", 1);
        boolean result = locationDAO.insert(location);
        assertTrue(result, "Falha ao adicionar o local: " + location);
        assertNotEquals(0, location.getId(), "O ID do local não foi gerado corretamente.");
        System.out.println("Local adicionado com sucesso: " + location);
        locationDAO.realDeleteLocation(location.getId());
    }

    @Test
    public void testUpdateLocation() throws SQLException {
        Location location = new Location(0, "123 Main St", "Springfield", 5000, 1990, false, "interior", 1);
        boolean addResult = locationDAO.insert(location);
        assertTrue(addResult, "Falha ao adicionar o local: " + location);
        System.out.println("Local adicionado com sucesso: " + location);

        Location addedLocation = locationDAO.getLocationById(location.getId());
        addedLocation.setAddress("456 St");
        addedLocation.setCity("Santa Maria");
        addedLocation.setCapacity(6000);
        addedLocation.setYearBuilt(2000);
        addedLocation.setInative(true);
        addedLocation.setType("outdoor");
        addedLocation.setEventId(2);

        boolean updateResult = locationDAO.updateLocation(addedLocation);
        assertTrue(updateResult, "Falha ao atualizar o local: " + addedLocation);
        System.out.println("Local atualizado com sucesso: " + addedLocation);

        Location updatedLocation = locationDAO.getLocationById(location.getId());
        Location expectedLocation = new Location(location.getId(), "456 St", "Santa Maria", 6000, 2000, true, "outdoor", 2);

        assertEquals(expectedLocation, updatedLocation, "O local atualizado não corresponde ao esperado");
        System.out.println("O local atualizado corresponde ao esperado: " + updatedLocation);

        locationDAO.realDeleteLocation(location.getId());
    }

    @Test
    public void testDeleteLocation() throws SQLException {
        Location location = new Location(0, "123 Main St", "Springfield", 5000, 1990, false, "interior", 1);
        locationDAO.insert(location);

        boolean result = locationDAO.deleteLocation(location.getId());
        assertTrue(result, "Falha ao apagar o local: " + location);
        System.out.println("Local inativado com sucesso: " + location);

        Location deletedLocation = locationDAO.getLocationById(location.getId());
        assertTrue(deletedLocation.isInative(), "O local não foi marcado como inativo: " + deletedLocation);
        System.out.println("Local marcado como inativo: " + deletedLocation);
        locationDAO.realDeleteLocation(location.getId());
    }

    @Test
    public void testCompareLocations() {
        Location location1 = new Location(1, "123 Main St", "Springfield", 5000, 1990, false, "interior", 1);
        Location location2 = new Location(1, "123 Main St", "Springfield", 5000, 1990, false, "interior", 1);

        assertEquals(location1, location2, "Os locais não são iguais: " + location1 + " e " + location2);
        System.out.println("Os locais são iguais: " + location1 + " e " + location2);
    }

    @Test
    public void testGetLocationById() throws SQLException {
        Location location = new Location(0, "123 Main St", "Springfield", 5000, 1990, false, "interior", 1);
        boolean addResult = locationDAO.insert(location);
        assertTrue(addResult, "Falha ao adicionar o local: " + location);
        System.out.println("Local adicionado com sucesso: " + location);

        Location fetchedLocation = locationDAO.getLocationById(location.getId());
        assertNotNull(fetchedLocation, "Falha ao buscar o local por ID: " + location.getId());
        assertEquals(location, fetchedLocation, "O local buscado não corresponde ao esperado");
        System.out.println("Local buscado com sucesso: " + fetchedLocation);

        locationDAO.realDeleteLocation(location.getId());
    }
}