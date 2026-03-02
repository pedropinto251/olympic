package com.lp3_grupo5.lp3_grupo5.Infrastructure.Connection;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DBConnectionTest {

    @Test
    void getConnection() {
        // Cria uma instância da classe DBConnection
        DBConnection dbConnection = new DBConnection();

        try {
            // Tenta obter uma conexão
            Connection connection = dbConnection.getConnection();

            // Verifica se a conexão não é null (significa que a conexão foi bem-sucedida)
            assertNotNull(connection, "A conexão com o banco de dados falhou.");

            // Verifica se a conexão está válida
            assertTrue(connection.isValid(2), "A conexão não é válida.");

            // Fecha a conexão após o teste
            connection.close();
        } catch (SQLException e) {
            // Caso ocorra um erro de SQL, falha o teste
            fail("Erro ao tentar conectar ao banco de dados: " + e.getMessage());
        }
    }

    @Test
    void testCloseConnection() {
        // Cria uma instância da classe DBConnection
        DBConnection dbConnection = new DBConnection();

        try {
            // Tenta obter uma conexão
            Connection connection = DBConnection.getConnection();

            // Verifica se a conexão foi estabelecida corretamente
            assertNotNull(connection, "A conexão não foi estabelecida.");

            // Verifica que a conexão não está fechada no início
            assertFalse(connection.isClosed(), "A conexão não deveria estar fechada antes de chamar closeConnection.");

            // Fecha a conexão
           // DBConnection.closeConnection();

            // Verifica se a conexão foi fechada (isClosed retorna true se a conexão estiver fechada)
            assertTrue(connection.isClosed(), "A conexão não foi fechada corretamente.");
        } catch (SQLException e) {
            // Caso ocorra um erro de SQL, falha o teste
            fail("Erro ao tentar fechar a conexão: " + e.getMessage());
        }
    }
}

