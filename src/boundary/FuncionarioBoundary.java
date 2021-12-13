package boundary;

import control.FuncionarioControl;
import entity.Funcionario;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.converter.NumberStringConverter;

import java.util.Optional;

public class FuncionarioBoundary implements StrategyBoundary {

    private String roleUsuario;

    private TextField txtId = new TextField();
    private TextField txtNome = new TextField();
    private TextField txtEmail = new TextField();
    private TextField txtSenha = new TextField();

    private Button btnNovoFuncionario = new Button("Novo Funcionario");
    private Button btnSalvar = new Button("Salvar");
    private Button btnPesquisar = new Button("Pesquisar");

    private FuncionarioControl control = new FuncionarioControl();

    private TableView<Funcionario> table = new TableView<>();

    private Boolean criouTabela = false;

    private void criarTabela() {

        TableColumn<Funcionario, Long> col1 = new TableColumn<>("Id");
        col1.setCellValueFactory( new PropertyValueFactory<>("id") );

        TableColumn<Funcionario, String> col2 = new TableColumn<>("Nome");
        col2.setCellValueFactory( new PropertyValueFactory<>("nome") );

        TableColumn<Funcionario, String> col3 = new TableColumn<>("Email");
        col3.setCellValueFactory( new PropertyValueFactory<>("email") );

        TableColumn<Funcionario, String> col4 = new TableColumn<>("Senha");

        TableColumn<Funcionario, String> col5 = new TableColumn<>("Ações");

        if (roleUsuario.equals("admin")) {
            col4.setCellValueFactory( new PropertyValueFactory<>("senha") );

            col5.setCellValueFactory( new PropertyValueFactory<>("DUMMY") );
            col5.setCellFactory( (tbCol) ->
                    new TableCell<Funcionario, String>() {
                        final Button btn = new Button("Remover");

                        public void updateItem(String item, boolean empty) {
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                btn.setOnAction( (e) -> {
                                    Funcionario funcionario = getTableView().getItems().get(getIndex());
                                    Alert alert = new Alert(Alert.AlertType.WARNING,
                                            "Você confirma a remoção do Funcionario Id " +
                                                    funcionario.getId(), ButtonType.OK, ButtonType.CANCEL);
                                    Optional<ButtonType> clicado = alert.showAndWait();
                                    if (clicado.isPresent() &&
                                            clicado.get().equals(ButtonType.OK)) {
                                        control.remover(funcionario.getId());
                                    }
                                });
                                setGraphic(btn);
                                setText(null);
                            }
                        }
                    }
            );
        }

        table.getColumns().addAll(col1, col2, col3, col4, col5);

        table.setItems(control.getListaView());

        table
                .getSelectionModel()
                .selectedItemProperty()
                .addListener( (obs, antigo, novo) -> {
                            if (novo != null) {
                                control.setEntity(novo);
                            }
                        }
                );
        control.pesquisar();
    }

    @Override
    public Pane render(String roleUsuario) {
        this.roleUsuario = roleUsuario;

        BorderPane panPrincipal = new BorderPane();
        GridPane panCampos = new GridPane();
        txtId.setEditable(false);
        txtId.setDisable(true);
        Bindings.bindBidirectional(txtId.textProperty(), control.id, new NumberStringConverter());
        Bindings.bindBidirectional(txtNome.textProperty(), control.nome);
        Bindings.bindBidirectional(txtEmail.textProperty(), control.email);
        Bindings.bindBidirectional(txtSenha.textProperty(), control.senha);

        panCampos.add(new Label("Id"), 0, 0);
        panCampos.add(txtId, 1, 0);


        panCampos.add(new Label("Nome"), 0, 1);
        panCampos.add(txtNome, 1, 1);

        panCampos.add(new Label("Email"), 0, 2);
        panCampos.add(txtEmail, 1, 2);

        panCampos.add(btnPesquisar, 1, 4);

        if (roleUsuario.equals("admin")) {
            panCampos.add(new Label("Senha"), 0, 3);
            panCampos.add(txtSenha, 1, 3);

            panCampos.add(btnNovoFuncionario, 2, 0);
            panCampos.add(btnSalvar, 0, 4);
        }

        btnSalvar.setOnAction(e -> {
            control.salvar();
        });

        btnPesquisar.setOnAction( e -> {
            control.pesquisar();
        });

        btnNovoFuncionario.setOnAction( e -> {
            control.novoFuncionario();
        });

        panPrincipal.setTop(panCampos);
        panPrincipal.setCenter(table);
        if (!criouTabela) {
            this.criarTabela();
            criouTabela = true;
        }
        return (panPrincipal);
    }
}
