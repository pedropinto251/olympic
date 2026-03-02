# Oporto Olympics (Olympic+)

A multi-module project with two main components:
- `LP_API`: an ASP.NET Core MVC web app that integrates with the Oporto Olympics external API for login, games, and ticketing.
- `Projeto/Lp3_Grupo5`: a JavaFX desktop application for managing Olympic data and interacting with the same external services and a local SQL Server database.

## Features
- Client login and registration
- Games listing, filtering, and details
- Ticket creation and ticket history
- JavaFX desktop UI with multiple screens (login, admin, athletes, events, etc.)
- SQL Server persistence and HikariCP connection pooling (desktop app)
- Email notifications via Mailjet

## Tech Stack
- ASP.NET Core 8 (MVC + Razor Pages)
- Java 17 (Maven) + JavaFX 21
- SQL Server
- HikariCP
- Mailjet API

## Project Structure
- `LP_API/` ASP.NET Core MVC web app
- `Projeto/Lp3_Grupo5/` JavaFX desktop application
- `Projeto/Base de Dados/` SQL Server schema scripts
- `JavaDocs/` generated documentation
- `Templates/` design templates and assets

## Database
SQL Server schema is available at:
- `Projeto/Base de Dados/base-dados-lp3.sql`

The JavaFX app reads DB credentials from `.env`.

### Environment Variables (JavaFX app)
Create a `.env` file inside `Projeto/Lp3_Grupo5/`:

```env
DB_URL=jdbc:sqlserver://localhost:1433;databaseName=2024_LP3_G5_FEIRA;encrypt=true;trustServerCertificate=true
DB_USER=sa
DB_PASSWORD=your_password

API_BASE_URL=https://services.inapa.com/opo/api/
API_USERNAME=your_api_user
API_PASSWORD=your_api_password

MAILJET_API_KEY=your_mailjet_key
MAILJET_API_SECRET=your_mailjet_secret
```

## Run the ASP.NET App (`LP_API`)
Prerequisites:
- .NET 8 SDK

From the repo root:

```bash
cd LP_API/LP_API

dotnet run
```

Notes:
- The app redirects `/` to the login page.
- External API calls use basic auth. Credentials are currently hardcoded in `LP_API/LP_API/Services/ApiAuthService.cs` and `LP_API/LP_API/Controller/GameController.cs`. Consider moving them to configuration.
- Mailjet keys are loaded from `appsettings.json`. Replace them with your own.

## Run the JavaFX App (`Projeto/Lp3_Grupo5`)
Prerequisites:
- JDK 17+
- SQL Server running with the schema loaded

From the repo root:

```bash
cd Projeto/Lp3_Grupo5
./mvnw clean javafx:run
```

On Windows:

```bat
cd Projeto\Lp3_Grupo5
mvnw.cmd clean javafx:run
```

## Notes
- The JavaFX app starts at `Login.fxml` and loads the main dashboard after authentication.
- The system depends on the external Oporto Olympics API for some operations.

## License
Not specified.
