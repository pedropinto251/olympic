package com.lp3_grupo5.lp3_grupo5.Controller.Login;

import com.lp3_grupo5.lp3_grupo5.Model.User;
import com.lp3_grupo5.lp3_grupo5.DAO.IDAO.IUserDAO;
import com.lp3_grupo5.lp3_grupo5.DAO.UserDAO;
import com.lp3_grupo5.lp3_grupo5.Session.Session;
import com.lp3_grupo5.lp3_grupo5.Util.LoaderFXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

/**
 * Controlador para a tela de alteração de senha. Este controlador permite que o usuário altere sua senha,
 * realizando as validações necessárias e atualizando a senha no banco de dados se todas as condições forem atendidas.
 */
public class PasswordChangeController {

    @FXML
    private AnchorPane anchorPane;  // Painel de ancoragem da tela

    @FXML
    private PasswordField confirmPasswordField;  // Campo para confirmação da nova senha

    @FXML
    private PasswordField currentPasswordField;  // Campo para a senha atual

    @FXML
    private PasswordField newPasswordField;  // Campo para a nova senha

    @FXML
    private Button saveButton;  // Botão para salvar as alterações

    @FXML
    private Button menuBtn;  // Botão para voltar ao menu

    // DAO para atualizar a senha no banco de dados
    private final IUserDAO userDAO = new UserDAO();

    /**
     * Método chamado quando o botão de salvar é clicado.
     * Realiza a validação dos campos e a atualização da senha do usuário.
     *
     * @param event O evento de clique no botão.
     */
    @FXML
    void onSaveButtonClicked(ActionEvent event) {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Verificação dos campos
        if (newPassword.isEmpty() || confirmPassword.isEmpty() || currentPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campos obrigatórios vazios", "Por favor, preencha todos os campos.");
        } else if (newPassword.equals(currentPassword)) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Nova senha inválida", "A nova senha não pode ser igual à senha atual.");
        } else if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Confirmação incorreta", "A nova senha e a confirmação não coincidem.");
        } else {
            // Obtém o ID do usuário logado através da sessão
            int loggedUserId = Session.getLoggedUserId();
            // Gera o hash da nova senha


            // Se o ID for válido, busca o usuário no banco
            if (loggedUserId != -1) {
                Optional<User> loggedUserOptional = userDAO.findById(loggedUserId);

                if (loggedUserOptional.isPresent()) {
                    User user = loggedUserOptional.get();

                    // Verifica se a senha atual fornecida é a mesma que a armazenada no banco de dados
                    String currentPasswordHash = hashPassword(currentPassword);
                    if (!currentPasswordHash.equals(user.getPasswordHash())) {  // Use currentPasswordStored aqui
                        showAlert(Alert.AlertType.ERROR, "Erro", "Senha atual incorreta", "A senha atual fornecida está incorreta.");
                        return;
                    }

                    // Gera o hash da nova senha
                    String newPasswordHash = hashPassword(newPassword);

                    // Atualiza a senha no banco de dados
                    boolean success = userDAO.updatePassword(loggedUserId, newPasswordHash);


                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Senha alterada com sucesso", "Sua senha foi atualizada.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar senha", "Não foi possível atualizar sua senha.");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao encontrar usuário", "Não foi possível encontrar o usuário no banco de dados.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Usuário não logado", "Não foi possível encontrar o usuário logado.");
            }
        }
    }

    /**
     * Método chamado quando o botão de voltar ao menu é clicado.
     * Carrega a tela de login novamente.
     *
     * @param actionEvent O evento de clique do botão.
     */
    public void handleMenubtn(ActionEvent actionEvent) {
        Stage currentStage = getStage();
        LoaderFXML loader = new LoaderFXML(currentStage);
        loader.loadLogin();
    }

    /**
     * Método para gerar o hash de uma senha usando SHA-256.
     *
     * @param password A senha em texto simples.
     * @return O hash da senha em Base64.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao encriptar a senha: " + e.getMessage(), e);
        }
    }

    /**
     * Método para obter o estágio (janela) atual da aplicação.
     *
     * @return O estágio atual da aplicação.
     */
    private Stage getStage() {
        return (Stage) menuBtn.getScene().getWindow();
    }

    /**
     * Método para exibir um alerta com o tipo, título, cabeçalho e mensagem fornecidos.
     *
     * @param alertType O tipo de alerta (informação, erro, aviso, etc.).
     * @param title     O título do alerta.
     * @param header    O cabeçalho do alerta.
     * @param message   A mensagem a ser exibida no alerta.
     */
    private void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Método chamado para inicializar o controlador. Não realiza nenhuma ação adicional,
     * pois o ID do usuário logado já está disponível na sessão.
     */
    public void initialize() {
        // Não é necessário buscar o usuário diretamente da sessão,
        // pois o ID do usuário logado já está disponível.
    }
}