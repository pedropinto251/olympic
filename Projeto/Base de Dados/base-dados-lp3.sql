USE 2024_LP3_G5_FEIRA;
GO

-- Tabela de Atletas (Athletes)
CREATE TABLE Athletes (
    id INT IDENTITY(1120001,1) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(3) NOT NULL,  -- Código ISO de 3 letras (ISOAlpha3CountryCode)
    genre VARCHAR(10) NOT NULL CHECK (genre IN ('Men', 'Women')),
    height INT NOT NULL,
    weight INT NOT NULL,
    dateOfBirth DATE NOT NULL,
	inative BIT 
);
GO


-- Tabela de utilizadores (Users)
CREATE TABLE Users (
    id INT PRIMARY KEY,  -- O ID é o número mecanográfico do atleta
    username VARCHAR(255) NOT NULL UNIQUE,  -- Nome de usuário será o ID do atleta
    password_hash VARCHAR(255) NOT NULL,  -- Senha criptografada
    role VARCHAR(50) NOT NULL CHECK (role IN ('athlete', 'manager')),  -- Papel (atleta ou gestor)
    athlete_id INT NULL,  -- Referência ao atleta, se for um atleta
    inative BIT,
	FOREIGN KEY (athlete_id) REFERENCES Athletes(id)
);
GO

--Tabela de Lacalizações (locations)
CREATE TABLE Locations (
    id INT IDENTITY(1,1) PRIMARY KEY,
    address VARCHAR(255),  -- Morada do local
    city VARCHAR(255) NOT NULL,  -- Cidade onde o local está
    capacity INT,  -- Capacidade (opcional)
    year_built INT,-- Ano de construção (opcional)
	event_id INT,
	inative BIT,
	[type] VARCHAR(50) CHECK ([type] IN ('interior', 'outdoor')),
	FOREIGN KEY (event_id) REFERENCES Events(id)
);
Go

--Tabela de eventos (events)
CREATE TABLE Events (
    id INT IDENTITY(1,1) PRIMARY KEY,
    year INT NOT NULL,  -- Ano do evento
    country VARCHAR(255) NOT NULL,  -- País anfitrião
    mascot VARCHAR(255),  -- Mascote ou logo do evento
	inative BIT,
   
);
GO
-- Tabela de Participações Olímpicas de Atletas (OlympicParticipations)
CREATE TABLE OlympicParticipations (
    id INT IDENTITY(1,1) PRIMARY KEY,
    year INT NOT NULL,
	gold INT,
	silver INT,
	bronze INT,
	diploma INT,
	result VARCHAR(10),
    athlete_id INT,
	event_id INT,
	inative BIT,
    FOREIGN KEY (athlete_id) REFERENCES Athletes(id),
	FOREIGN KEY (event_id) REFERENCES Events(id)
);
GO


-- Tabela de desportos (Sports)
CREATE TABLE Sports (
    id INT IDENTITY(1,1) PRIMARY KEY,
    type VARCHAR(50) NOT NULL CHECK (type IN ('Individual', 'Collective')),
    genre VARCHAR(10) NOT NULL CHECK (genre IN ('Men', 'Women')),
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    minParticipants INT NOT NULL,
    scoringMeasure VARCHAR(50) NOT NULL CHECK (scoringMeasure IN ('Time', 'Points', 'Distance')),
    oneGame VARCHAR(10) NOT NULL CHECK (oneGame IN ('One', 'Multiple')),
    olympicRecord_time VARCHAR(50),
    olympicRecord_year INT,
    olympicRecord_holder VARCHAR(255),
    winnerOlympic_time VARCHAR(50),
    winnerOlympic_year INT,
    winnerOlympic_holder VARCHAR(255),
	sport_id INT,
	inative BIT ,
	FOREIGN KEY (sport_id) REFERENCES Sports(id)
);
GO



-- Tabela de Equipas (Teams)
CREATE TABLE Teams (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(3) NOT NULL,  -- Código ISO de 3 letras (ISOAlpha3CountryCode)
    genre VARCHAR(10) NOT NULL CHECK (genre IN ('Men', 'Women')),
    sport VARCHAR(255) NOT NULL,
	sports_id INT,
    foundationYear INT NOT NULL,
	inative BIT,
	FOREIGN KEY (sports_id) REFERENCES Sports(id)
);
GO

-- Tabela de Participações Olímpicas de Equipes (TeamOlympicParticipations)
CREATE TABLE TeamOlympicParticipations (
    id INT IDENTITY(1,1) PRIMARY KEY,
    year INT NOT NULL,
    result VARCHAR(10) NOT NULL CHECK (result IN ('Gold', 'Silver', 'Bronze', 'Diploma')),
	resultTDP VARCHAR(250),
    team_id INT,
	event_id INT,
	inative BIT,
    FOREIGN KEY (team_id) REFERENCES Teams(id),
	FOREIGN KEY (event_id) REFERENCES Events(id)
);
GO 

--Tabela de inscrições em modalidades (AthleteRegistrations)
CREATE TABLE AthleteRegistrations (
    id INT IDENTITY(1,1) PRIMARY KEY,
    athlete_id INT,  -- Atleta inscrito
    sport_id INT,  -- Modalidade em que o atleta se inscreveu
    event_id INT,  -- Evento associado à inscrição
    status VARCHAR(50) CHECK (status IN ('approved', 'pending', 'rejected')),  -- Estado da inscrição
    inative BIT,  -- Indica exclusão lógica
    FOREIGN KEY (athlete_id) REFERENCES Athletes(id),
    FOREIGN KEY (sport_id) REFERENCES Sports(id),
    FOREIGN KEY (event_id) REFERENCES Events(id)  -- Relaciona o evento à inscrição
);
GO

-- Tabela de participação na equipa (TeamMemberships)
CREATE TABLE TeamMemberships (
    id INT IDENTITY(1,1) PRIMARY KEY,
    athlete_id INT,
    team_id INT,
	inative BIT,
    FOREIGN KEY (athlete_id) REFERENCES Athletes(id),
    FOREIGN KEY (team_id) REFERENCES Teams(id),
);
GO


-- Tabela de inscrições para as equipas
CREATE TABLE TeamApplications (
    id INT IDENTITY(1,1) PRIMARY KEY,
    athlete_id INT NOT NULL,
    team_id INT,
    status VARCHAR(50) NOT NULL CHECK (status IN ('approved', 'pending', 'rejected')),
    application_date DATETIME DEFAULT GETDATE(),
	inative BIT,
	sport_id int,
	FOREIGN KEY (sport_id) REFERENCES Sports(id),
    FOREIGN KEY (athlete_id) REFERENCES Athletes(id),
    FOREIGN KEY (team_id) REFERENCES Teams(id)
);
GO

CREATE TABLE Countries (
    CountryCode CHAR(3) PRIMARY KEY, -- Código ISO 3166-1 Alpha-3
    CountryName NVARCHAR(100) NOT NULL -- Nome completo do país
);

CREATE TABLE ValueIntervals (
    id INT IDENTITY(1,1) PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    min_value FLOAT NOT NULL,
    max_value FLOAT NOT NULL
);
go

CREATE TABLE SportEvents (
    id INT IDENTITY(1,1) PRIMARY KEY,
    sport_id INT,
    start_time DATETIME,  -- Inclui data e hora
    end_time DATETIME,  -- Inclui data e hora
    local INT,
    event_id INT,
	inative BIT DEFAULT 0 ,
    FOREIGN KEY (sport_id) REFERENCES Sports(id),
    FOREIGN KEY (local) REFERENCES Locations(id),
    FOREIGN KEY (event_id) REFERENCES Events(id)  -- Referência à tabela Events
);
GO

-- TRIGGERS --

-- Trigger para gerar automaticamente login e senha em texto simples quando um atleta é inserido
CREATE TRIGGER CreateUserForAthlete
ON Athletes
AFTER INSERT
AS
BEGIN
    DECLARE @athlete_id INT;
    DECLARE @password_hash VARCHAR(255);  -- Senha em texto simples

    -- Obter o ID do atleta recém-inserido 
    SELECT @athlete_id = id FROM inserted;

    -- Gerar a senha inicial como o ID do atleta em formato de texto
    SET @password_hash = CAST(@athlete_id AS VARCHAR);

    -- Inserir automaticamente o utilizador na tabela Users
    INSERT INTO Users (id, username, password_hash, role, athlete_id)
    VALUES (@athlete_id, CAST(@athlete_id AS VARCHAR), @password_hash, 'athlete', @athlete_id);
END;
GO


-- Trigger para VERIFICAR automaticamente se o pais do atleta e da equipa sao iguais.
CREATE TRIGGER CheckCountryMatch
ON TeamMemberships  
AFTER INSERT
AS
BEGIN
    DECLARE @athlete_country VARCHAR(3);
    DECLARE @team_country VARCHAR(3);
    DECLARE @athlete_id INT;
    DECLARE @team_id INT;

    -- Obtem os IDs do atleta e da equipa inseridos
    SELECT @athlete_id = athlete_id, @team_id = team_id
    FROM inserted;

    -- Obtem o país do atleta
    SELECT @athlete_country = country
    FROM Athletes
    WHERE id = @athlete_id;

    -- Obtem o país da equipa
    SELECT @team_country = country
    FROM Teams
    WHERE id = @team_id;

    -- Verifica se os países são diferentes
    IF @athlete_country <> @team_country
    BEGIN
        -- Caso os países não coincidam, lançar um erro e impedir a inserção
        RAISERROR ('O país do atleta e da equipa não coincidem.', 16, 1);
        ROLLBACK TRANSACTION;
    END
END;
GO

-- Trigger para atualizar o status inativo quando um atleta é marcado como inativo
CREATE  TRIGGER TRG_UpdateInactiveStatus
ON Athletes
AFTER UPDATE
AS
BEGIN
    -- Verifica se o campo 'inative' foi alterado para TRUE
    IF EXISTS (
        SELECT 1
        FROM inserted i
        WHERE i.inative = 1
        AND EXISTS (SELECT 1 FROM deleted d WHERE d.id = i.id AND d.inative = 0)
    )
    BEGIN
        -- Atualiza o campo 'inative' nas tabelas relacionadas quando o atleta é marcado como inativo
        UPDATE Users
        SET inative = 1
        FROM Users u
        INNER JOIN inserted i ON u.athlete_id = i.id
        WHERE i.inative = 1;

        UPDATE OlympicParticipations
        SET inative = 1
        FROM OlympicParticipations op
        INNER JOIN inserted i ON op.athlete_id = i.id
        WHERE i.inative = 1;

        UPDATE TeamMemberships
        SET inative = 1
        FROM TeamMemberships tm
        INNER JOIN inserted i ON tm.athlete_id = i.id
        WHERE i.inative = 1;

        UPDATE AthleteRegistrations
        SET inative = 1
        FROM AthleteRegistrations ar
        INNER JOIN inserted i ON ar.athlete_id = i.id
        WHERE i.inative = 1;
    END
END;
GO

-- Trigger para restaurar o status inativo quando um atleta é restaurado
CREATE TRIGGER TRG_UpdateRestoredStatus
ON Athletes
AFTER UPDATE
AS
BEGIN
    -- Verifica se o campo 'inative' foi alterado de TRUE para FALSE
    IF EXISTS (
        SELECT 1
        FROM inserted i
        WHERE i.inative = 0
        AND EXISTS (SELECT 1 FROM deleted d WHERE d.id = i.id AND d.inative = 1)
    )
    BEGIN
        -- Atualiza o campo 'inative' nas tabelas relacionadas quando o atleta é restaurado (de TRUE para FALSE)
        UPDATE Users
        SET inative = 0
        FROM Users u
        INNER JOIN inserted i ON u.athlete_id = i.id
        WHERE i.inative = 0;

        UPDATE OlympicParticipations
        SET inative = 0
        FROM OlympicParticipations op
        INNER JOIN inserted i ON op.athlete_id = i.id
        WHERE i.inative = 0;
    END
END;
GO

-- Trigger para atualizar o status inativo quando um desporto é marcado como inativo
CREATE  TRIGGER TRG_UpdateSportsDeletedStatus
ON Sports
AFTER UPDATE
AS
BEGIN
    -- Verifica se o campo 'inative' foi alterado para TRUE
    IF EXISTS (
        SELECT 1
        FROM inserted i
        WHERE i.inative = 1
        AND EXISTS (SELECT 1 FROM deleted d WHERE d.id = i.id AND d.inative = 0)
    )
    BEGIN
        -- Atualiza o campo 'inative' na tabela AthleteRegistrations
        UPDATE AthleteRegistrations
        SET inative = 1
        FROM AthleteRegistrations ar
        INNER JOIN inserted i ON ar.sport_id = i.id
        WHERE i.inative = 1;

		   -- Atualiza o campo 'inative' na tabela TeamApplications
        UPDATE TeamApplications
        SET inative = 1
        FROM TeamApplications ar
        INNER JOIN inserted i ON ar.sport_id = i.id
        WHERE i.inative = 1;

		 -- Atualiza o campo 'inative' na tabela TeamApplications
        UPDATE SportEvents
        SET inative = 1
        FROM SportEvents se
        INNER JOIN inserted i ON se.sport_id = i.id
        WHERE i.inative = 1;

        -- Atualiza o campo 'inative' na tabela Rules
        UPDATE Rules
        SET inative = 1
        FROM Rules r
        INNER JOIN inserted i ON r.sport_id = i.id
        WHERE i.inative = 1;
    END
END;
GO

-- Trigger para restaurar o status inativo quando um desporto é restaurado
CREATE TRIGGER TRG_UpdateSportsRestoredStatus
ON Sports
AFTER UPDATE
AS
BEGIN
    -- Verifica se o campo 'inative' foi alterado de TRUE para FALSE
    IF EXISTS (
        SELECT 1
        FROM inserted i
        WHERE i.inative = 0
        AND EXISTS (SELECT 1 FROM deleted d WHERE d.id = i.id AND d.inative = 1)
    )
    BEGIN
    
        -- Atualiza o campo 'inative' na tabela Rules
        UPDATE Rules
        SET inative = 0
        FROM Rules r
        INNER JOIN inserted i ON r.sport_id = i.id
        WHERE i.inative = 0;
    END
END;
GO

-- Trigger para atualizar o status inativo quando uma localização é marcada como inativa
CREATE TRIGGER TRG_MarkEventAsDeletedOnLocationDeleted
ON Locations
AFTER UPDATE
AS
BEGIN
    -- Verifica se o campo 'inative' foi alterado para TRUE
    IF EXISTS (
        SELECT 1
        FROM inserted i
        WHERE i.inative = 1
        AND EXISTS (SELECT 1 FROM deleted d WHERE d.id = i.id AND d.inative = 0)
    )
    BEGIN
        -- Atualiza o campo 'inative' na tabela Events, marcando o evento como inativo
        UPDATE Events
        SET inative = 1
        WHERE location_id IN (
            SELECT i.id
            FROM inserted i
            WHERE i.inative = 1
        );
    END
END;
GO

-- Trigger para restaurar o status inativo quando uma localização é restaurada
CREATE TRIGGER TRG_RestoreEventsOnLocationRestored
ON Locations
AFTER UPDATE
AS
BEGIN
    -- Verifica se o campo 'inative' foi alterado de TRUE para FALSE
    IF EXISTS (
        SELECT 1
        FROM inserted i
        WHERE i.inative = 0
        AND EXISTS (SELECT 1 FROM deleted d WHERE d.id = i.id AND d.inative = 1)
    )
    BEGIN
        -- Atualiza o campo 'inative' na tabela Events, restaurando o evento (marcando como FALSE)
        UPDATE Events
        SET inative = 0
        WHERE location_id IN (
            SELECT i.id
            FROM inserted i
            WHERE i.inative = 0
        );
    END
END;
GO

-- Trigger para atualizar o status inativo quando um evento é marcado como inativo
CREATE TRIGGER TRG_MarkParticipationsAsDeletedOnEventDeleted
ON Events
AFTER UPDATE
AS
BEGIN
    -- Verifica se o campo 'inative' foi alterado para TRUE
    IF EXISTS (
        SELECT 1
        FROM inserted i
        WHERE i.inative = 1
        AND EXISTS (SELECT 1 FROM deleted d WHERE d.id = i.id AND d.inative = 0)
    )
    BEGIN
        -- Atualiza o campo 'inative' na tabela TeamOlympicParticipations, marcando a participação como inativa
        UPDATE TeamOlympicParticipations
        SET inative = 1
        WHERE event_id IN (
            SELECT i.id
            FROM inserted i
            WHERE i.inative = 1
        );

        -- Atualiza o campo 'inative' na tabela OlympicParticipations, marcando a participação como inativa
        UPDATE OlympicParticipations
        SET inative = 1
        WHERE event_id IN (
            SELECT i.id
            FROM inserted i
            WHERE i.inative = 1
        );
    END
END;
GO

-- Trigger para restaurar o status inativo quando um evento é restaurado
CREATE TRIGGER TRG_RestoreParticipationsOnEventRestored
ON Events
AFTER UPDATE
AS
BEGIN
    -- Verifica se o campo 'inative' foi alterado para FALSE
    IF EXISTS (
        SELECT 1
        FROM inserted i
        WHERE i.inative = 0
        AND EXISTS (SELECT 1 FROM deleted d WHERE d.id = i.id AND d.inative = 1)
    )
    BEGIN
        -- Atualiza o campo 'inative' na tabela TeamOlympicParticipations, restaurando a participação
        UPDATE TeamOlympicParticipations
        SET inative = 0
        WHERE event_id IN (
            SELECT i.id
            FROM inserted i
            WHERE i.inative = 0
        );

        -- Atualiza o campo 'inative' na tabela OlympicParticipations, restaurando a participação
        UPDATE OlympicParticipations
        SET inative = 0
        WHERE event_id IN (
            SELECT i.id
            FROM inserted i
            WHERE i.inative = 0
        );
    END
END;
GO

-- Trigger para adicionar automaticamente registros à tabela TeamMemberships
CREATE TRIGGER trg_TeamApplications_Approved
ON TeamApplications
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    -- Inserir na tabela TeamMemberships sempre que uma inscrição for aprovada
    INSERT INTO TeamMemberships (athlete_id, team_id, inative)
    SELECT 
        inserted.athlete_id, 
        inserted.team_id, 
        0  -- Valor inicial de 'inative' como ativo
    FROM 
        inserted
    INNER JOIN 
        deleted ON inserted.id = deleted.id
    WHERE 
        inserted.status = 'approved' AND deleted.status <> 'approved';
END;
GO
-- Trigger para marcar todos os desportos como inativos quando um evento é marcado como inativo
CREATE TRIGGER trg_Events_Inactive
ON Events
AFTER UPDATE
AS
BEGIN
    -- Verifica se a coluna 'inactive' foi atualizada para 1 (inativo)
    IF UPDATE(inative)
    BEGIN
        DECLARE @Inactive BIT;

        -- Obtém o valor da coluna 'inactive'
        SELECT @Inactive = inative
        FROM inserted;

        -- Se o evento foi marcado como inativo
        IF @Inactive = 1
        BEGIN
            -- Atualiza todos os desportos para inativos
            UPDATE Sports
            SET inative = 1;
        END
    END
END;
GO

select * from Events

update Event
where id = 2

select * from Users