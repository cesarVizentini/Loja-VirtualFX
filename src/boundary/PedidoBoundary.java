package boundary;

import control.PedidoControl;
import entity.Cliente;
import entity.Produto;
import entity.ProdutoCarrinho;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.converter.NumberStringConverter;

import java.util.Optional;

public class PedidoBoundary implements StrategyBoundary {

    private TextField txtQuantidade = new TextField();
    private TextField txtValorTotalPedido = new TextField();

    private Button btnAdicionarProduto = new Button("Adicionar");
    private Button btnSalvar = new Button("Salvar");

    private ComboBox<Produto> cboxProduto = new ComboBox();
    private ComboBox<Cliente> cboxCliente = new ComboBox();

    private PedidoControl pedidoControl = new PedidoControl();

    private TableView<ProdutoCarrinho> table = new TableView<>();

    private Boolean criouTabela = false;

    private void criarTabela() {

        TableColumn<ProdutoCarrinho, Long> col1 = new TableColumn<>("Id");
        col1.setCellValueFactory( new PropertyValueFactory<>("id") );

        TableColumn<ProdutoCarrinho, String> col2 = new TableColumn<>("Nome do Produto");
        col2.setCellValueFactory( new PropertyValueFactory<>("nome") );

        TableColumn<ProdutoCarrinho, Long> col3 = new TableColumn<>("Quantidade");
        col3.setCellValueFactory( new PropertyValueFactory<>("quantidade") );

        TableColumn<ProdutoCarrinho, Double> col4 = new TableColumn<>("Valor Unitário");
        col4.setCellValueFactory( new PropertyValueFactory<>("valorUni") );

        TableColumn<ProdutoCarrinho, Double> col5 = new TableColumn<>("Valor Total");
        col5.setCellValueFactory( new PropertyValueFactory<>("valorTotal") );

        TableColumn<ProdutoCarrinho, String> col6 = new TableColumn<>("Ações");
        col6.setCellValueFactory( new PropertyValueFactory<>("DUMMY") );
        col6.setCellFactory( (tbCol) ->
                new TableCell<ProdutoCarrinho, String>() {
                    final Button btn = new Button("Remover");

                    public void updateItem(String item, boolean empty) {
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction( (e) -> {
                                ProdutoCarrinho produto = getTableView().getItems().get(getIndex());
                                System.out.println(getIndex());
                                Alert alert = new Alert(Alert.AlertType.WARNING,
                                        "Você confirma a remoção do Produto: " +
                                                produto.getNome(), ButtonType.OK, ButtonType.CANCEL);
                                Optional<ButtonType> clicado = alert.showAndWait();
                                if (clicado.isPresent() &&
                                        clicado.get().equals(ButtonType.OK)) {
                                    pedidoControl.remover(produto);
                                }
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                }
        );

        table.getColumns().addAll(col1, col2, col3, col4, col5, col6);

        table.setItems(pedidoControl.getListaView());
        cboxProduto.setItems(pedidoControl.populaComboboxProduto());
        cboxProduto.setPromptText("Produto");
        cboxCliente.setItems(pedidoControl.populaComboboxCliente());
        cboxCliente.setPromptText("Cliente");

        table
                .getSelectionModel()
                .selectedItemProperty()
                .addListener( (obs, antigo, novo) -> {
                        }
                );
    }

    @Override
    public Pane render(String roleUsuario) {
        BorderPane panPrincipal = new BorderPane();
        GridPane panCamposTop = new GridPane();
        GridPane panCamposBottom = new GridPane();
        txtQuantidade.setPromptText("Quantidade");
        txtValorTotalPedido.setEditable(false);
        txtValorTotalPedido.setDisable(true);
        Bindings.bindBidirectional(cboxCliente.valueProperty(), pedidoControl.cliente);
        Bindings.bindBidirectional(txtValorTotalPedido.textProperty(), pedidoControl.valorTotalPedido, new NumberStringConverter());

        if (!roleUsuario.equals("cliente")) {
            panCamposTop.add(cboxCliente, 0, 0);

        }
        panCamposTop.add(cboxProduto, 1, 0);
        panCamposTop.add(txtQuantidade, 2, 0);
        panCamposTop.add(btnAdicionarProduto, 3, 0);

        panCamposBottom.add(new Label("TOTAL DO PEDIDO:"), 0, 1);
        panCamposBottom.add(txtValorTotalPedido, 1, 1);
        panCamposBottom.add(btnSalvar, 2, 1);

        btnAdicionarProduto.setOnAction(e -> {
            pedidoControl.addProduto(cboxProduto.getValue(), Long.parseLong(txtQuantidade.getText()));
        });

        btnSalvar.setOnAction(e -> {
            pedidoControl.salvar();
        });

        panPrincipal.setTop(panCamposTop);
        panPrincipal.setCenter(table);
        panPrincipal.setBottom(panCamposBottom);
        if (!criouTabela) {
            this.criarTabela();
            criouTabela = true;
        }
        return (panPrincipal);
    }

}
