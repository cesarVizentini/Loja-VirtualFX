package boundary;

import control.ClienteControl;
import control.LoginControl;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.swing.*;

public class LoginBoundary extends Application implements CommandExecution {

    private BorderPane panePrincipal = new BorderPane();

    private LojaBoundary lojaBoundary = new LojaBoundary();

    private TextField txtEmail = new TextField();
    private PasswordField txtSenha = new PasswordField();

    private Button btnLogin = new Button("Login");
    private Button btnNovoCliente = new Button("Cliente novo?");

    private TextField txtNomeCad = new TextField();
    private  TextField txtEmailCad = new TextField();
    private  TextField txtEnderecoCad = new TextField();
    private  PasswordField txtSenhaCad = new PasswordField();

    private Button btnCadastrar = new Button("Cadastrar");

    private LoginControl control = new LoginControl();
    private ClienteControl clienteControl = new ClienteControl();

    public LoginBoundary() {
        lojaBoundary.addExecution(this);
    }

    @Override
    public void start(Stage stage) throws Exception {

        GridPane panCampos = new GridPane();

        Scene scn = new Scene(panePrincipal, 1024, 768);

        Bindings.bindBidirectional(txtEmail.textProperty(), control.email);
        Bindings.bindBidirectional(txtSenha.textProperty(), control.senha);
        panCampos.add(new Label("EMAIL"), 0, 0);
        panCampos.add(txtEmail, 1, 0);

        panCampos.add(new Label("SENHA"), 0, 1);
        panCampos.add(txtSenha, 1, 1);

        panCampos.add(btnLogin, 0, 2);
        panCampos.add(btnNovoCliente, 1, 2);

        panePrincipal.setCenter(panCampos);


        btnLogin.setOnAction(e -> {

            switch (control.buscaCadastro()) {
                case "":
                    JOptionPane.showMessageDialog(null, "Usuario nao encontrado" );
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Bem vindo " + control.roleUsuario);
                    execute("BOUNDARY-LOJA");
            }

        });

        btnNovoCliente.setOnAction(e -> {
            Bindings.bindBidirectional(txtNomeCad.textProperty(), clienteControl.nome);
            Bindings.bindBidirectional(txtEmailCad.textProperty(), clienteControl.email);
            Bindings.bindBidirectional(txtSenhaCad.textProperty(), clienteControl.senha);
            Bindings.bindBidirectional(txtEnderecoCad.textProperty(), clienteControl.endereco);

            panCampos.add(new Label("NOME"), 0, 3);
            panCampos.add(txtNomeCad, 1, 3);

            panCampos.add(new Label("EMAIL"), 0, 4);
            panCampos.add(txtEmailCad, 1, 4);

            panCampos.add(new Label("SENHA"), 0, 5);
            panCampos.add(txtSenhaCad, 1, 5);

            panCampos.add(new Label("ENDERECO"), 0, 6);
            panCampos.add(txtEnderecoCad, 1, 6);

            panCampos.add(btnCadastrar, 0, 7);

            btnCadastrar.setOnAction(ev -> {
                clienteControl.salvar();
                JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso, realize seu login!");
            });
        });

        panePrincipal.setCenter(panCampos);

        stage.setScene(scn);
        stage.setTitle("Loja Virtual");
        stage.show();

    }

    @Override
    public void execute(String command) {
        if ("BOUNDARY-LOJA".equals(command)) {
            panePrincipal.setCenter(lojaBoundary.render(control.roleUsuario));
        }
    }

}
