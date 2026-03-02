package com.lp3_grupo5.lp3_grupo5.Util;

import com.lp3_grupo5.lp3_grupo5.Controller.Sports.SetScheduleController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A classe LoaderFXML é responsável por carregar e exibir diferentes telas (views) em uma aplicação JavaFX.
 * Utiliza o Stage fornecido para exibir as telas de forma dinâmica. Cada método carrega uma tela específica
 * com um título definido.
 * <p>
 * O método {@link #loadView(String, String)} é usado internamente para carregar qualquer tela,
 * independentemente do tipo de layout (como AnchorPane, VBox, HBox, etc.).
 */
public class LoaderFXML {

    /**
     * Referência ao Stage principal onde as telas serão carregadas
     */
    private Stage primaryStage;

    /**
     * Construtor que inicializa o LoaderFXML com um Stage específico.
     * Este Stage será utilizado para exibir as telas carregadas.
     *
     * @param primaryStage o Stage principal onde as views serão exibidas
     */
    public LoaderFXML(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**-------------------------------------------------------------------------------MENUS----------------------------------------------------------------------------------*/

    /**
     * Carrega e exibe o menu principal da aplicação.
     * O arquivo FXML para o menu principal é carregado, e o título da janela é definido como "Menu Principal".
     * Esse método é utilizado para exibir a tela inicial de navegação.
     */
    public void loadMainMenu() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/MenusFXML/Menu.fxml", "Menu Principal");
    }

    public void loadMenu() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/MenusFXML/Menu.fxml", "Menu Atletas");
    }

    public void loadXMLMenu() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/MenusFXML/XML_Menu.fxml", "Menu XML");
    }

    public void loadLocalMenu() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/LocalFXML/Local_Menu.fxml", "Menu Local");
    }

    public void loadLEventMenu() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/EventFXML/Menu_Eventos.fxml", "Menu Evento");
    }

    public void loadLAthleteMenu() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/AtletaFXML/MenuAtleta.fxml", "Menu Atletal");
    }

    public void loadLSportsMenu() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/SportFXML/Menu_Modalidades.fxml", "Menu Modalidades");
    }

    public void loadViewMenuInicialAtleta() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/AtletaFXML/Menu_Atleta.fxml", "Menu");
    }

    public void loadTeamMenu() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/TamFXML/Menu_Equipas.fxml", "Menu Equipas");
    }

    public void loadGestaoMenu() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/MenusFXML/GestaoEvento.fxml", "Menu Equipas");
    }

    public void loadHistoricoXML() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/MenusFXML/HistoricoXML.fxml", "Historico XML");
    }



    /**------------------------------------------------------------------------LOCAIS---------------------------------------------------------------------------- */


    /**
     * Carrega e exibe a tela de listagem de locais.
     * O arquivo FXML correspondente é carregado e o título da janela é definido como "Lista de Locais".
     */
    public void loadLocalList() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/LocalFXML/Dashboard_Local_Read.fxml", "Lista de Locais");
    }

    /**
     * Carrega e exibe a tela de criação de um novo local.
     * O arquivo FXML correspondente é carregado e o título da janela é definido como "Criar Local".
     */
    // Supondo que você tenha um método semelhante no seu LoaderFXML
    public void loadLocalCreate() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/LocalFXML/CreateLocal.fxml", "Adicionar Locais");
    }

    public void loadLocalEdit() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/LocalFXML/Dashboard_Edit_Local.fxml", "Editar Locais");
    }


    /**------------------------------------------------------------------------EVENTOS---------------------------------------------------------------------------- */


    /**
     * Carrega e exibe a tela de edição de eventos.
     * O arquivo FXML correspondente é carregado e o título da janela é definido como "Editar Eventos".
     */
    public void loadEventEdit() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/EventFXML/Dashboard_Edit_Event.fxml", "Editar Eventos");
    }

    /**
     * Carrega e exibe a tela de listagem de eventos.
     * O arquivo FXML correspondente é carregado e o título da janela é definido como "Lista de Eventos".
     */
    public void loadEventsList() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/EventFXML/ReadEvent.fxml", "Lista de Eventos");
    }

    public void loadEventCreate() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/EventFXML/Dashboard_Create_Event.fxml", "Adicionar Evento");
    }


    /**------------------------------------------------------------------------ATLETAS---------------------------------------------------------------------------- */


    /**
     * Carrega e exibe a tela de edição de atletas.
     * O arquivo FXML correspondente é carregado e o título da janela é definido como "Editar Atletas".
     */
    public void loadAtheleteEdit() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/AtletaFXML/Dashboard_Edit_Athlete.fxml", "Editar Atletas");
    }

    /**
     * Carrega e exibe a tela de listagem de eventos.
     * O arquivo FXML correspondente é carregado e o título da janela é definido como "Lista de Eventos".
     */

    public void loadAthletesList() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/AtletaFXML/ReadAthlete.fxml", "Lista de Atletas");
    }

    public void loadAproveAthleteList() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/TamFXML/Dashboard_Aprove_Insciption.fxml", "Lista de Inscições Pendentes");
    }

    public void loadFXMLAthleteList() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/AtletaFXML/AthleteFXML.fxml", "Lista de Inscições Pendentes");
    }

    public void loadAthleteRegistration() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/AtletaFXML/Dashboard_Athlete_Registration.fxml", "Inscrição em Modalidades");
    }

    public void loadAddAthlete() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/AtletaFXML/Dahsboard_Create_Athlete.fxml", "Adicionar Atleta");
    }

    public void loadViewParticipationIdAtleta() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/AtletaFXML/OlympicParticipations.fxml", "Participações por id");
    }

    public void loadReadIndividual(){
        loadView("/com/lp3_grupo5/lp3_grupo5/View/AtletaFXML/ReadIndividualInscription.fxml", "gerar resultados");
    }

    public void loadReadCollective(){
        loadView("/com/lp3_grupo5/lp3_grupo5/View/AtletaFXML/ReadCollectiveInscription.fxml", "gerar resultados");
    }
    public void loadIndividualParticipation(){
        loadView("/com/lp3_grupo5/lp3_grupo5/View/AtletaFXML/AthleteIndividualParticipation.fxml", "Participações Individuais");    }

    public void loadCalendarView() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/AtletaFXML/ReadAthleteCalendar.fxml", "Ver calendário");
    }
    public void loadCalendarAllView() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/EventFXML/AllCalendar.fxml", "Ver calendário");
    }

    public void loadEditPhoto() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/AtletaFXML/EditPhotoAthlete.fxml", "Editar foto");
    }

    /**
     * ------------------------------------------------------------------------SPORT----------------------------------------------------------------------------
     */

    public void loadSportRead() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/SportFXML/Dashboard_Sport_Read.fxml", "Listar Modalidades");
    }

    public void loadSportList() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/SportFXML/Dashboard_Sport_Read.fxml", "Lista de Eventos");
    }

    public void loadSportCreate() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/SportFXML/Dashboard_Sport_Create.fxml", "Criar Modalidades");
    }

    public void loadSportEdit() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/SportFXML/Dashboard_Sport_Edit.fxml", "Editar Modalidades");
    }

    public void loadSportXML() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/SportFXML/DesportoXML.fxml", "Carregar Modalidades");
    }

    public void loadSportReadEvent() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/SportFXML/SportReadEvent.fxml", "Listar Modalidades do Evento");
    }

    public void loadSetSchedule(int sportId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lp3_grupo5/lp3_grupo5/View/SportFXML/SetSchedule.fxml"));
            Parent root = loader.load();

            // Passa o ID da modalidade e o ID do local para o controlador da nova janela
            SetScheduleController controller = loader.getController();
            controller.setSportId(sportId);
            //controller.setLocationId(locationId); // Novo método para definir o ID do local
            controller.loadActiveEvents(); // Carrega os eventos ativos

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Definir Horário e Local");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo FXML: /com/lp3_grupo5/lp3_grupo5/View/SportFXML/SetSchedule.fxml");
            e.printStackTrace();
        }
    }

    /**
     * ------------------------------------------------------------------------TEAM----------------------------------------------------------------------------
     */

    public void loadTeamInscription() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/TamFXML/Inscition_Team.fxml", "Mudar Password");
    }

    public void loadViewParticipationTeamIdAtleta() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/TamFXML/TeamPaticipationViewByid.fxml", "Participaçoes Equipa");
    }

    public void loadTeamCreate(){
        loadView("/com/lp3_grupo5/lp3_grupo5/View/TamFXML/Dashboard_Team_Create.fxml", "Participaçoes Equipa");
    }

    public void loadTeamEdit(){
        loadView("/com/lp3_grupo5/lp3_grupo5/View/TamFXML/Dashboard_Team_Edit.fxml", "Participaçoes Equipa");
    }

    public void loadTeamList(){
        loadView("/com/lp3_grupo5/lp3_grupo5/View/TamFXML/Dashboard_Team_Read.fxml", "Participaçoes Equipa");
    }

    public void loadAllTeamParticipations(){
        loadView("/com/lp3_grupo5/lp3_grupo5/View/TamFXML/AllTeamParticipations.fxml", "Participaçoes Equipa");
    }
    public void loadTeamXML(){
        loadView("/com/lp3_grupo5/lp3_grupo5/View/TamFXML/TeamXML.fxml", "Carregar Equipas");
    }

    /**
     * ------------------------------------------------------------------------LOGIN----------------------------------------------------------------------------
     */

    public void loadPasswordView() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/LoginFXML/Password_Change.fxml", "Mudar Password");
    }

    public void loadLogin() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/LoginFXML/Login.fxml", "Login");
    }


    /**
     * ------------------------------------------------------------------------GERAR RESULTADOS----------------------------------------------------------------------------
     */
    public void loadGenerateResult() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/GenerateResultFXML/Generate_Result.fxml", "gerar resultados");
    }
    public void loadAltIntervalo() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/GenerateResultFXML/AltIntervalos.fxml", "gerar resultados");
    }


    /** ********************************************************************************************API************************************************************************* */

    public void loadAltClientAPI() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/EventFXML/ClientAPIView.fxml", "Clientes API");
    }
    public void loadAllGamesAPI() {
        loadView("/com/lp3_grupo5/lp3_grupo5/View/EventFXML/GamesApi.fxml", "Jogos API");
    }



/** ************************************************************************************************************************************************************************ */
    /**
     * Carrega e exibe uma tela específica (view) definida pelo caminho do arquivo FXML.
     * Este método é genérico e permite a definição de qualquer tela e título,
     * utilizando o caminho FXML e o título fornecidos.
     * <p>
     * O layout carregado pode ser de qualquer tipo de container de layout do JavaFX (como {@link javafx.scene.layout.AnchorPane},
     * {@link javafx.scene.layout.VBox}, etc.). O método utiliza {@link Parent} para garantir a flexibilidade no tipo de layout.
     *
     * @param fxmlPath Caminho do arquivo FXML da tela a ser carregada
     * @param title    Título a ser exibido na janela para a tela carregada
     */
    public void loadView(String fxmlPath, String title) {
        try {
            // Depuração do caminho FXML
            System.out.println("Tentando carregar o FXML: " + fxmlPath);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            // Verifica se o arquivo FXML foi encontrado
            if (loader.getLocation() == null) {
                System.err.println("Erro: o arquivo FXML não foi encontrado no caminho: " + fxmlPath);
                return;  // Não continua se não encontrar o arquivo
            } else {
                System.out.println("Arquivo FXML encontrado: " + fxmlPath);
            }

            Parent view = loader.load();  // Usando Parent para permitir layouts variados
            Scene scene = new Scene(view);

            // Define a cena e o título do Stage
            primaryStage.setScene(scene);
            primaryStage.setTitle(title);

            // Maximizar a janela
            primaryStage.setMaximized(true);

            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}