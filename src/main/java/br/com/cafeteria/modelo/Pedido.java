package br.com.cafeteria.modelo;

import br.com.cafeteria.observer.ObservadorPedido;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Pedido é o "sujeito" do padrão Observer: avisa os observadores a cada mudança de status.
 * É construído pelo {@link br.com.cafeteria.builder.PedidoBuilder}.
 */
public class Pedido {

    private final int numero;
    private final String cliente;
    private final List<ItemPedido> itens;
    private final String observacao;
    private final List<ObservadorPedido> observadores = new ArrayList<>();
    private StatusPedido status = StatusPedido.CRIADO;
    private BigDecimal desconto = BigDecimal.ZERO;

    public Pedido(int numero, String cliente, List<ItemPedido> itens, String observacao) {
        this.numero = numero;
        this.cliente = cliente;
        this.itens = List.copyOf(itens);
        this.observacao = observacao;
    }

    public void adicionarObservador(ObservadorPedido observador) {
        observadores.add(observador);
    }

    public void removerObservador(ObservadorPedido observador) {
        observadores.remove(observador);
    }

    public void alterarStatus(StatusPedido novoStatus, String motivo) {
        StatusPedido anterior = this.status;
        this.status = novoStatus;
        for (ObservadorPedido observador : List.copyOf(observadores)) {
            observador.statusAlterado(this, anterior, motivo);
        }
    }

    public BigDecimal subtotal() {
        return itens.stream().map(ItemPedido::total).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal total() {
        return subtotal().subtract(desconto).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_EVEN);
    }

    public void aplicarDesconto(BigDecimal desconto) {
        this.desconto = desconto.setScale(2, RoundingMode.HALF_EVEN);
    }

    public int getNumero() { return numero; }
    public String getCliente() { return cliente; }
    public List<ItemPedido> getItens() { return itens; }
    public String getObservacao() { return observacao; }
    public StatusPedido getStatus() { return status; }
    public BigDecimal getDesconto() { return desconto; }
}
