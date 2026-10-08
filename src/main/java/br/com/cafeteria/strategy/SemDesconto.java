package br.com.cafeteria.strategy;

import br.com.cafeteria.modelo.Pedido;

import java.math.BigDecimal;

public class SemDesconto implements EstrategiaDesconto {

    @Override
    public BigDecimal calcular(Pedido pedido) {
        return BigDecimal.ZERO;
    }

    @Override
    public String descricao() {
        return "Sem desconto";
    }
}
