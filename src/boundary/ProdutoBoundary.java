package boundary;

import control.ProdutoControl;
import entity.Produto;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.converter.NumberStringConverter;

import java.util.Optional;

public class ProdutoBoundary implements StrategyBoundary {

    private TextField txtId = new TextField();
    private TextField txtNome = new TextField();
    private TextField txtValor = new TextField();
    private TextField txtCusto = new TextField();
    private TextField txtQuantidadeEmEstoque = new TextField();

    private Button btnNovoProduto = new Button("Novo Produto");
    private Button btnSalvar = new Button("Salvar");
    private Button btnPesquisar = new Button("Pesquisar");

    private ProdutoControl control = new ProdutoControl();

    private TableView<Produto> table = new TableView<>();

    private Boolean criouTabela = false;

    private void criarTabela() {

        TableColumn<Produto, Long> col1 = new TableColumn<>("Id");
        col1.setCellValueFactory( new PropertyValueFactory<>("id") );

        TableColumn<Produto, String> col2 = new TableColumn<>("Nome");
        col2.setCellValueFactory( new PropertyValueFactory<>("nome") );

        TableColumn<Produto, String> col3 = new TableColumn<>("Valor");
        col3.setCellValueFactory( new PropertyValueFactory<>("valor") );

        TableColumn<Produto, Double> col4 = new TableColumn<>("Custo");
        col4.setCellValueFactory( new PropertyValueFactory<>("custo") );

        TableColumn<Produto, Integer> col5 = new TableColumn<>("Quantidade em Estoque");
        col5.setCellValueFactory( new PropertyValueFactory<>("quantidadeEmEstoque") );

        TableColumn<Produto, String> col6 = new TableColumn<>("Ações");
        col6.setCellValueFactory( new PropertyValueFactory<>("DUMMY") );
        col6.setCellFactory( (tbCol) ->
                new TableCell<Produto, String>() {
                    final Button btn = new Button("Remover");

                    public void updateItem(String item, boolean empty) {
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction( (e) -> {
                                Produto produto = getTableView().getItems().get(getIndex());
                                Alert alert = new Alert(Alert.AlertType.WARNING,
                                        "Você confirma a remoção do Produto Id " +
                                                produto.getId(), ButtonType.OK, ButtonType.CANCEL);
                                Optional<ButtonType> clicado = alert.showAndWait();
                                if (clicado.isPresent() &&
                                        clicado.get().equals(ButtonType.OK)) {
                                    control.remover(produto.getId());
                                }
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                }
        );

        table.getColumns().addAll(col1, col2, col3, col4, col5, col6);

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
        BorderPane panPrincipal = new BorderPane();
        GridPane panCampos = new GridPane();
        txtId.setEditable(false);
        txtId.setDisable(true);
        Bindings.bindBidirectional(txtId.textProperty(), control.id, new NumberStringConverter());
        Bindings.bindBidirectional(txtNome.textProperty(), control.nome);
        Bindings.bindBidirectional(txtValor.textProperty(), control.valor, new NumberStringConverter());
        Bindings.bindBidirectional(txtCusto.textProperty(), control.custo, new NumberStringConverter());
        Bindings.bindBidirectional(txtQuantidadeEmEstoque.textProperty(), control.quantidadeEmEstoque, new NumberStringConverter());

        panCampos.add(new Label("Id"), 0, 0);
        panCampos.add(txtId, 1, 0);
        panCampos.add(btnNovoProduto, 2, 0);

        panCampos.add(new Label("Nome"), 0, 1);
        panCampos.add(txtNome, 1, 1);

        panCampos.add(new Label("Valor"), 0, 2);
        panCampos.add(txtValor, 1, 2);

        panCampos.add(new Label("Custo"), 0, 3);
        panCampos.add(txtCusto, 1, 3);

        panCampos.add(new Label("Quantidade em Estoque"), 0, 4);
        panCampos.add(txtQuantidadeEmEstoque, 1, 4);

        panCampos.add(btnSalvar, 0, 5);
        panCampos.add(btnPesquisar, 1, 5);

        btnSalvar.setOnAction(e -> {
            control.salvar();
        });

        btnPesquisar.setOnAction( e -> {
            control.pesquisar();
        });

        btnNovoProduto.setOnAction( e -> {
            control.novoProduto();
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
