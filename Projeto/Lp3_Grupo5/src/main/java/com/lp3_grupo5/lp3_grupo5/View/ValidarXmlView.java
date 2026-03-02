package com.lp3_grupo5.lp3_grupo5.View;

import com.lp3_grupo5.lp3_grupo5.Controller.XML.XmlValidator;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Classe responsável por fornecer uma interface gráfica para validar um arquivo XML em relação a um arquivo XSD.
 */
public class ValidarXmlView {

    private Stage primaryStage;

    /**
     * Construtor que recebe o palco principal da aplicação.
     *
     * @param primaryStage O palco principal da aplicação.
     */
    public ValidarXmlView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Método que inicia a interface gráfica para a validação de XML com XSD.
     * <p>
     * Este método permite ao utilizador:
     * <ul>
     *     <li>Inserir o caminho do arquivo XML.</li>
     *     <li>Inserir o caminho do arquivo XSD.</li>
     *     <li>Validar o XML em relação ao XSD.</li>
     *     <li>Exibir o resultado da validação.</li>
     * </ul>
     */
    public void start() {
        primaryStage.setTitle("Validação de XML com XSD");

        // Labels e campos de entrada para os caminhos dos arquivos XML e XSD
        Label xmlPathLabel = new Label("Caminho do arquivo XML:");
        TextField xmlPathField = new TextField();
        xmlPathField.setPromptText("Digite o caminho do arquivo XML");

        Label xsdPathLabel = new Label("Caminho do arquivo XSD:");
        TextField xsdPathField = new TextField();
        xsdPathField.setPromptText("Digite o caminho do arquivo XSD");

        // Botão para validar o XML
        Button validarButton = new Button("Validar XML");

        // Botão para voltar ao menu principal
        Button voltarButton = new Button("Voltar ao Menu Principal");

        // Área de texto para mostrar mensagens de status
        TextArea statusArea = new TextArea();
        statusArea.setEditable(false);
        statusArea.setWrapText(true);

        /**
         * Ação do botão "Validar XML".
         *
         * Este botão, ao ser clicado, realiza as seguintes operações:
         * <ul>
         *     <li>Obtém os caminhos do XML e XSD fornecidos pelo utilizador.</li>
         *     <li>Valida o XML com o XSD.</li>
         *     <li>Exibe mensagens de sucesso ou erro na área de status.</li>
         * </ul>
         */
        validarButton.setOnAction(e -> {
            String xmlPath = xmlPathField.getText().trim();
            String xsdPath = xsdPathField.getText().trim();

            // Chama a classe de validação de XML
            if (XmlValidator.validateXmlSchema(xmlPath, xsdPath)) {
                statusArea.appendText("XML é válido de acordo com o XSD.\n");
            } else {
                statusArea.appendText("XML inválido.\n");
            }
        });

        /**
         * Ação do botão "Voltar ao Menu Principal".
         *
         * Este botão, ao ser clicado, retorna à interface do menu principal.
         */
        voltarButton.setOnAction(e -> {
            LoaderFXML loader = new LoaderFXML(primaryStage);
            loader.loadMenu();
        });

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Adicionando componentes ao layout GridPane
        grid.add(xmlPathLabel, 0, 0);
        grid.add(xmlPathField, 1, 0);
        grid.add(xsdPathLabel, 0, 1);
        grid.add(xsdPathField, 1, 1);

        // Adicionando botões ao layout HBox
        HBox buttonBox = new HBox(10, validarButton, voltarButton);

        // Configuração do layout VBox
        VBox vbox = new VBox(10, grid, buttonBox, statusArea);
        vbox.setPadding(new Insets(10));

        // Cena e inicialização da janela
        Scene scene = new Scene(vbox, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
