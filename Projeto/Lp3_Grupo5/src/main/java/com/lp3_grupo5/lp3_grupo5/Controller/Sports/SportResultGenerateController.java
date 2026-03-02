package com.lp3_grupo5.lp3_grupo5.Controller.Sports;

import com.lp3_grupo5.lp3_grupo5.DAO.SportDAO;
import com.lp3_grupo5.lp3_grupo5.Model.*;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador responsável pela geração dos resultados dos eventos desportivos.
 */
public class SportResultGenerateController {

    private SportDAO sportsDAO;
    private ObservableList<Result> resultsList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Sport, String> sportDescriptionTC;

    @FXML
    private TableColumn<Sport, String> sportGameTC;

    @FXML
    private TableColumn<Sport, String> sportGenderTC;

    @FXML
    private TableColumn<Sport, Integer> sportIdTC;

    @FXML
    private TableColumn<Sport, String> sportMeasureTC;

    @FXML
    private TableColumn<Sport, Integer> sportMinParticipantsTC;

    @FXML
    private TableColumn<Sport, String> sportNameTC;
    @FXML
    private TableColumn<Sport, String> sportTypeTC;
    @FXML
    private TableColumn<Sport, String> inativeTC;
    @FXML
    private TableView<Sport> sportTV;
    @FXML
    private TableView<Result> resultTV;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnMenu;

    @FXML
    private TableColumn<Sport, String> resultValueTC;
    @FXML
    private TableColumn<Sport, String> resultMedalTC;
    @FXML
    private TableColumn<Result, String> resultSportNameTC;
    @FXML
    private TableColumn<Result, String> resultAthleteNameTC;
    @FXML
    private Label statusLabel; // Rótulo para mostrar o status da operação
    private SportDAO sportDAO = new SportDAO();
    /**
     * Construtor do controlador que inicializa a instância do SportsDAO.
     */
    @FXML
    private void initialize() {
        sportIdTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        sportNameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        sportTypeTC.setCellValueFactory(new PropertyValueFactory<>("type"));
        sportGenderTC.setCellValueFactory(new PropertyValueFactory<>("genre"));
        sportDescriptionTC.setCellValueFactory(new PropertyValueFactory<>("description"));
        sportMinParticipantsTC.setCellValueFactory(new PropertyValueFactory<>("minParticipants"));
        sportMeasureTC.setCellValueFactory(new PropertyValueFactory<>("scoringMeasure"));
        sportGameTC.setCellValueFactory(new PropertyValueFactory<>("oneGame"));

        inativeTC.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isInative() ? "Desativado" : "Ativo"));
        resultValueTC.setCellValueFactory(new PropertyValueFactory<>("result"));
        resultMedalTC.setCellValueFactory(new PropertyValueFactory<>("medal"));
        resultSportNameTC.setCellValueFactory(new PropertyValueFactory<>("sportName"));
        resultAthleteNameTC.setCellValueFactory(new PropertyValueFactory<>("athleteName"));
        // Carrega os dados da tabela usando a DAO
        loadDataFromDatabase();
        resultTV.setItems(resultsList);
    }
    public SportResultGenerateController() {
        // Inicializa o SportDAO
        sportsDAO = new SportDAO();
    }
    private void loadDataFromDatabase() {
        List<Sport> sports = sportDAO.findAllForGenerator();
        ObservableList<Sport> observableSports = FXCollections.observableArrayList(sports);
        sportTV.setItems(observableSports);
    }

    /**
     * Método acionado pelo botão para gerar os resultados dos eventos desportivos.
     */
    @FXML
    private void handleGenerateResults() {
        // Obtém o desporto selecionado na tabela
        Sport selectedSport = sportTV.getSelectionModel().getSelectedItem();

        if (selectedSport != null) {
            // Exibe uma mensagem de status
            statusLabel.setText("Verificando se o número mínimo de participantes foi atingido...");

            try {
                // Crie uma instância do SportDAO (ou utilize um já existente)

                // Verifica se o esporte selecionado é coletivo
                if ("Collective".equals(selectedSport.getType())) {
                    // Verificar se o número mínimo de equipes foi alcançado, passando o id do esporte
                    boolean minParticipantsMet = sportDAO.checkMinParticipants(selectedSport.getId());

                    if (minParticipantsMet) {
                        // Número mínimo de equipes foi alcançado, gerar resultados
                        statusLabel.setText("Gerando resultados para o desporto selecionado...");
                        generateResultsForSport(selectedSport);
                        showSuccessAlert("Sucesso" , "Resultados gerados com sucesso para o desporto: " + selectedSport.getName());
                    } else {
                        // Número mínimo de equipes não foi alcançado
                        showErrorAlert("Error","Número mínimo de equipes não alcançado. Não é possível gerar os resultados.");
                    }
                } else if ("Individual".equals(selectedSport.getType())) {
                    // Se o desporto for individual, verifica o número mínimo de atletas
                    boolean minParticipantsMet = sportDAO.checkMinParticipantsForIndividual(selectedSport.getId());

                    if (minParticipantsMet) {
                        // Número mínimo de atletas foi alcançado, gerar resultados
                        statusLabel.setText("Gerando resultados para o desporto selecionado...");
                        generateResultsForSport(selectedSport);
                        showSuccessAlert("Sucesso","Resultados gerados com sucesso para o desporto: " + selectedSport.getName());
                    } else {
                        // Número mínimo de atletas não foi alcançado
                        showErrorAlert("Error","Número mínimo de atletas não alcançado. Não é possível gerar os resultados.");
                    }
                } else {
                    // Caso o tipo de desporto não seja nem coletivo nem individual
                    showErrorAlert("Erro","Tipo de desporto inválido. Não é possível gerar resultados.");
                }

            } catch (SQLException e) {
                showErrorAlert("Error","Erro ao gerar resultados: " + e.getMessage());
                e.printStackTrace();
            } catch (IllegalStateException e) {
                showErrorAlert("Error","Erro: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showErrorAlert("Error","Por favor, selecione um desporto para gerar os resultados.");
        }
    }






    /**
     * Gera os resultados para os eventos desportivos ativos.
     *
     * @throws SQLException se ocorrer um erro na consulta ao banco de dados.
     */
    private void generateResultsForSport(Sport sport) throws SQLException {
        // Obtém o evento ativo
        List<Event> activeEvents = sportsDAO.getActiveEvents();
        if (activeEvents.size() != 1) {
            showErrorAlert("Error", "Deve existir unicamente um evento ativo");
            throw new IllegalStateException("Deve existir exatamente um evento ativo. Eventos ativos encontrados: " + activeEvents.size());
        }

        Event activeEvent = activeEvents.get(0);

        if (sport.getType().equalsIgnoreCase("Individual")) {
            // Gera resultados para atletas individuais
            List<Athlete> athletes = sportsDAO.getAthletesBySport(sport.getId());
            List<OlympicParticipation> athleteParticipations = new ArrayList<>();

            for (Athlete athlete : athletes) {
                String result = generateRandomResult(sport.getScoringMeasure());
                OlympicParticipation participation = new OlympicParticipation(
                        activeEvent.getYear(),
                        result,
                        athlete.getId(),
                        activeEvent.getId(),
                        sport.getId()
                );
                athleteParticipations.add(participation);
            }

            // Ordena e atribui medalhas
            athleteParticipations.sort((p1, p2) -> compareResults(p1.getResult(), p2.getResult(), sport.getScoringMeasure()));

            if (!athleteParticipations.isEmpty()) {
                OlympicParticipation winner = athleteParticipations.get(0);
                sportsDAO.saveIndividualParticipation(winner, "Gold");
                resultsList.add(new Result(sport.getName(), winner.getResult(), "Gold", winner.getAthlete().getName()));
                System.out.println(winner.getAthlete().getName());
                if (athleteParticipations.size() > 1) {
                    sportsDAO.saveIndividualParticipation(athleteParticipations.get(1), "Silver");
                    resultsList.add(new Result(sport.getName(), athleteParticipations.get(1).getResult(), "Silver", athleteParticipations.get(1).getAthlete().getName()));
                }
                if (athleteParticipations.size() > 2) {
                    sportsDAO.saveIndividualParticipation(athleteParticipations.get(2), "Bronze");
                    resultsList.add(new Result(sport.getName(), athleteParticipations.get(2).getResult(), "Bronze", athleteParticipations.get(2).getAthlete().getName()));
                }
                for (int i = 3; i < athleteParticipations.size(); i++) {
                    sportsDAO.saveIndividualParticipation(athleteParticipations.get(i), "Diploma");
                    resultsList.add(new Result(sport.getName(), athleteParticipations.get(i).getResult(), "Diploma", athleteParticipations.get(i).getAthlete().getName()));
                }

                // Atualizar vencedor olímpico
                sportDAO.updateWinnerOlympic(sport.getId(), winner.getResult(), activeEvent.getYear(), winner.getAthlete().getName());

                // Verificar se o recorde foi batido
                if (sport.getOlympicRecordTime() == null || compareResults(winner.getResult(), sport.getOlympicRecordTime(), sport.getScoringMeasure()) < 0) {
                    sportDAO.updateOlympicRecord(sport.getId(), winner.getResult(), activeEvent.getYear(), winner.getAthlete().getName());
                }
            }

        } else if (sport.getType().equalsIgnoreCase("Collective")) {
            // Gera resultados para equipas
            List<Team> teams = sportsDAO.getTeamsBySport(sport.getId());
            List<TeamOlympicParticipation> teamParticipations = new ArrayList<>();

            for (Team team : teams) {
                String result = generateRandomResult(sport.getScoringMeasure());
                String resultTPD = generateResultTPD(sport.getScoringMeasure());

                TeamOlympicParticipation participation = new TeamOlympicParticipation(
                        activeEvent.getYear(),
                        result,
                        resultTPD,
                        team.getId(),
                        activeEvent.getId()
                );
                teamParticipations.add(participation);
            }

            // Ordena e atribui medalhas
            teamParticipations.sort((p1, p2) -> compareResults(p1.getResultTPD(), p2.getResultTPD(), sport.getScoringMeasure()));

            if (!teamParticipations.isEmpty()) {
                TeamOlympicParticipation winner = teamParticipations.get(0);
                sportsDAO.saveTeamParticipation(winner, activeEvent, "Gold");
                resultsList.add(new Result(sport.getName(), winner.getResult(), "Gold", winner.getTeam().getName()));
                if (teamParticipations.size() > 1) {
                    sportsDAO.saveTeamParticipation(teamParticipations.get(1), activeEvent, "Silver");
                    resultsList.add(new Result(sport.getName(), teamParticipations.get(1).getResult(), "Silver", teamParticipations.get(1).getTeam().getName()));
                }
                if (teamParticipations.size() > 2) {
                    sportsDAO.saveTeamParticipation(teamParticipations.get(2), activeEvent, "Bronze");
                    resultsList.add(new Result(sport.getName(), teamParticipations.get(2).getResult(), "Bronze", teamParticipations.get(2).getTeam().getName()));
                }
                for (int i = 3; i < teamParticipations.size(); i++) {
                    sportsDAO.saveTeamParticipation(teamParticipations.get(i), activeEvent, "Diploma");
                    resultsList.add(new Result(sport.getName(), teamParticipations.get(i).getResult(), "Diploma", teamParticipations.get(i).getTeam().getName()));
                }

                // Atualizar vencedor olímpico
                sportDAO.updateWinnerOlympic(sport.getId(), winner.getResult(), activeEvent.getYear(), winner.getTeam().getName());

                // Verificar se o recorde foi batido
                if (sport.getOlympicRecordTime() == null || compareResults(winner.getResult(), sport.getOlympicRecordTime(), sport.getScoringMeasure()) < 0) {
                    sportDAO.updateOlympicRecord(sport.getId(), winner.getResult(), activeEvent.getYear(), winner.getTeam().getName());
                }
            }
        } else {
            throw new IllegalArgumentException("Tipo de desporto desconhecido: " + sport.getType());
        }

        // Marca o desporto como inativo após gerar os resultados
        if (sportsDAO.markSportAsInactive(sport.getId())) {
            showSuccessAlert("Sucesso", "Resultados gerados e desporto marcado como inativo.");
            resultTV.refresh();
        } else {
            showErrorAlert("Erro", "Erro ao marcar o desporto como inativo.");
        }
    }




    /**
     * Compara dois resultados, dependendo da medida de pontuação (Tempo, Distância, ou Pontos).
     *
     * @param result1       Primeiro resultado.
     * @param result2       Segundo resultado.
     * @param scoringMeasure A medida de pontuação do desporto.
     * @return O valor comparativo entre os dois resultados.
     */
    private int compareResults(String result1, String result2, String scoringMeasure) {
        switch (scoringMeasure) {
            case "Time":
                return compareTimeResults(result1, result2);
            case "Distance":
                return compareDistanceResults(result1, result2);
            case "Points":
                return comparePointsResults(result1, result2);
            default:
                throw new IllegalArgumentException("Medida de pontuação desconhecida: " + scoringMeasure);
        }
    }

    /**
     * Compara dois resultados no formato de tempo ("hh:mm:ss").
     *
     * @param result1 Primeiro resultado (formato de tempo).
     * @param result2 Segundo resultado (formato de tempo).
     * @return Valor comparativo entre os dois resultados.
     */
    private int compareTimeResults(String result1, String result2) {
        return Integer.compare(parseTime(result1), parseTime(result2));
    }


    /**
     * Converte uma string de tempo no formato "hh:mm:ss" ou "ss.SSS" para um valor numérico de segundos.
     *
     * @param time O tempo no formato "hh:mm:ss" ou "ss.SSS".
     * @return O tempo em segundos.
     */
    private int parseTime(String time) {
        // Verifica se a string de tempo é nula
        if (time == null) {
            throw new IllegalArgumentException("Time string cannot be null");
        }

        // Divide a string de tempo em partes usando ":" como delimitador
        String[] parts = time.split(":");

        // Se a string de tempo estiver no formato "hh:mm:ss"
        if (parts.length == 3) {
            // Converte a parte das horas para inteiro
            int hours = Integer.parseInt(parts[0]);
            // Converte a parte dos minutos para inteiro
            int minutes = Integer.parseInt(parts[1]);
            // Converte a parte dos segundos para double (para lidar com frações de segundo)
            double seconds = Double.parseDouble(parts[2]);
            // Calcula o tempo total em segundos e retorna como inteiro
            return (int) (hours * 3600 + minutes * 60 + seconds);
        }
        // Se a string de tempo estiver no formato "ss.SSS"
        else if (parts.length == 1) {
            // Converte a string de segundos para double
            double seconds = Double.parseDouble(parts[0]);
            // Retorna o valor dos segundos como inteiro
            return (int) seconds;
        }
        // Se a string de tempo não estiver em um formato válido
        else {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }
    }

    /**
     * Compara dois resultados no formato de distância.
     *
     * @param result1 Primeiro resultado (distância).
     * @param result2 Segundo resultado (distância).
     * @return Valor comparativo entre os dois resultados.
     */
    private int compareDistanceResults(String result1, String result2) {
        return Double.compare(parseDistance(result2), parseDistance(result1));
    }

    /**
     * Converte uma string de distância (em metros) para um valor numérico.
     *
     * @param distance A distância no formato "XX.XX m".
     * @return A distância como número.
     */
    private double parseDistance(String distance) {
        // Verifica se a string de distância é nula
        if (distance == null) {
            throw new IllegalArgumentException("Distance string cannot be null");
        }
        // Substitui a vírgula por um ponto para garantir o formato correto
        String formattedDistance = distance.replace(",", ".");
        return Double.parseDouble(formattedDistance.replace(" m", ""));
    }

    /**
     * Compara dois resultados no formato de pontos.
     *
     * @param result1 Primeiro resultado (pontos).
     * @param result2 Segundo resultado (pontos).
     * @return Valor comparativo entre os dois resultados.
     */
    private int comparePointsResults(String result1, String result2) {
        return Integer.compare(parsePoints(result2), parsePoints(result1));
    }

    /**
     * Converte uma string de pontos para um valor numérico.
     *
     * @param points Os pontos no formato "XX pts".
     * @return Os pontos como número inteiro.
     */
    private int parsePoints(String points) {
        if (points == null) {
            throw new IllegalArgumentException("Points string cannot be null");
        }
        return Integer.parseInt(points.replace(" pts", ""));
    }

    /**
     * Gera um resultado aleatório com base na medida de pontuação do desporto.
     *
     * @param scoringMeasure A medida de pontuação do desporto (Tempo, Distância, Pontos).
     * @return O resultado gerado como uma string.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
    private String generateRandomResult(String scoringMeasure) throws SQLException {
        SportDAO sportDAO = new SportDAO();

        switch (scoringMeasure) {
            case "Time":
                double[] timeInterval = sportDAO.getTimeInterval();
                int totalSeconds = (int) (Math.random() * (timeInterval[1] - timeInterval[0]) + timeInterval[0]);
                int hours = totalSeconds / 3600;
                int minutes = (totalSeconds % 3600) / 60;
                int seconds = totalSeconds % 60;
                return String.format("%d:%02d:%02d", hours, minutes, seconds);
            case "Distance":
                double[] distanceInterval = sportDAO.getDistanceInterval();
                double distance = Math.random() * (distanceInterval[1] - distanceInterval[0]) + distanceInterval[0];
                return String.format("%.2f m", distance).replace(".", ",");
            case "Points":
                double[] pointsInterval = sportDAO.getPointsInterval();
                int points = (int) (Math.random() * (pointsInterval[1] - pointsInterval[0]) + pointsInterval[0]);
                return String.format("%d pts", points);
            default:
                throw new IllegalArgumentException("Tipo de pontuação desconhecido: " + scoringMeasure);
        }
    }

    /**
     * Gera o resultado TPD (Tempo, Distância ou Pontos) para a equipa, com base na medida de pontuação.
     *
     * @param scoringMeasure A medida de pontuação do desporto (Tempo, Distância, Pontos).
     * @return O resultado TPD gerado como uma string.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     */
    private String generateResultTPD(String scoringMeasure) throws SQLException {
        SportDAO sportDAO = new SportDAO();

        switch (scoringMeasure) {
            case "Time":
                double[] timeInterval = sportDAO.getTimeInterval();
                int totalSeconds = (int) (Math.random() * (timeInterval[1] - timeInterval[0]) + timeInterval[0]);
                int hours = totalSeconds / 3600;
                int minutes = (totalSeconds % 3600) / 60;
                int seconds = totalSeconds % 60;
                return String.format("%d:%02d:%02d", hours, minutes, seconds);
            case "Distance":
                double[] distanceInterval = sportDAO.getDistanceInterval();
                double distance = Math.random() * (distanceInterval[1] - distanceInterval[0]) + distanceInterval[0];
                return String.format("%.2f m", distance).replace(".", ",");
            case "Points":
                double[] pointsInterval = sportDAO.getPointsInterval();
                int points = (int) (Math.random() * (pointsInterval[1] - pointsInterval[0]) + pointsInterval[0]);
                return String.format("%d pts", points);
            default:
                return "N/A"; // Caso o tipo de medida não seja reconhecido, retorna "N/A"
        }
    }

    /**
     * Método acionado pelo botão de voltar para o menu principal.
     *
     * @param actionEvent O evento de ação do botão de voltar.
     */
    public void handleBackToMenu(javafx.event.ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnMenu.getScene().getWindow();
        // Cria o LoaderFXML e passa o currentStage para ele
        LoaderFXML loader = new LoaderFXML(currentStage); // Passa o currentStage ao invés de criar um novo
        loader.loadMenu(); // Método para carregar o menu principal
    }

    public void handleBtnBack(javafx.event.ActionEvent actionEvent) {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadGestaoMenu();
    }

    // Método para capturar o ID do esporte selecionado
    private int getSelectedSportId() {
        Sport selectedSport = sportTV.getSelectionModel().getSelectedItem();
        return (selectedSport != null) ? selectedSport.getId() : -1;
    }

    @FXML
    public void handleGenerateAthletes(javafx.event.ActionEvent event) {
        int selectedSportId = getSelectedSportId(); // Captura o ID do esporte selecionado
        if (selectedSportId == -1) {
            statusLabel.setText("Por favor, selecione um esporte.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lp3_grupo5/lp3_grupo5/View/GenerateResultFXML/AthletesAssoc.fxml"));
            Parent root = loader.load();
            AthletesListController controller = loader.getController();
            List<Athlete> athletes = sportsDAO.getAthletesBySport(selectedSportId); // Obtém atletas do esporte selecionado
            controller.setAthletes(athletes); // Passar lista de atletas ao controlador.
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            statusLabel.setText("Erro ao carregar a lista de atletas.");
            e.printStackTrace();
        }
    }



    /**
     * Exibe uma mensagem de erro em formato de alerta.
     *
     * @param title   Título do alerta.
     * @param message Mensagem de erro.
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro"); // Título da janela
        alert.setHeaderText(title); // Cabeçalho do alerta
        alert.setContentText(message); // Mensagem detalhada do erro
        alert.showAndWait(); // Exibe o alerta e espera interação do usuário
    }



    /**
     * Exibe um alerta de sucesso.
     *
     * @param title O título do alerta.
     * @param message A mensagem do alerta.
     */
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Exibe um alerta de erro.
     *
     * @param title O título do alerta.
     * @param message A mensagem do alerta.
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
