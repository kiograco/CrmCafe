package br.com.cafeteria.modelo;

import java.math.BigDecimal;

public record ItemPedido(Produto produto, int quantidade) {

    public ItemPedido {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
    }

    public BigDecimal total() {
        return produto.preco().multiply(BigDecimal.valueOf(quantidade));
    }
}
