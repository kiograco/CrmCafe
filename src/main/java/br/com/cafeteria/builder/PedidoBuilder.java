package br.com.cafeteria.builder;

import br.com.cafeteria.modelo.ItemPedido;
import br.com.cafeteria.modelo.Pedido;
import br.com.cafeteria.modelo.Produto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Builder: monta um Pedido passo a passo com uma API fluente, evitando construtores enormes.
 */
public class PedidoBuilder {

    private static final AtomicInteger SEQUENCIA = new AtomicInteger(1);

    private String cliente;
    private final List<ItemPedido> itens = new ArrayList<>();
    private String observacao = "";

    public static PedidoBuilder novo() {
        return new PedidoBuilder();
    }

    public PedidoBuilder cliente(String cliente) {
        this.cliente = cliente;
        return this;
    }

    public PedidoBuilder item(Produto produto, int quantidade) {
        itens.add(new ItemPedido(produto, quantidade));
        return this;
    }

    public PedidoBuilder observacao(String observacao) {
        this.observacao = observacao;
        return this;
    }

    public Pedido construir() {
        if (cliente == null || cliente.isBlank()) {
            throw new IllegalStateException("Pedido precisa de um cliente");
        }
        return new Pedido(SEQUENCIA.getAndIncrement(), cliente, itens, observacao);
    }
}
