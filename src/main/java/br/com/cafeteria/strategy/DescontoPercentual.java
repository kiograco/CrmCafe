package br.com.cafeteria.strategy;

import br.com.cafeteria.modelo.Pedido;

import java.math.BigDecimal;

public class DescontoPercentual implements EstrategiaDesconto {

    private final BigDecimal percentual;

    public DescontoPercentual(int percentual) {
        if (percentual < 0 || percentual > 100) {
            throw new IllegalArgumentException("Percentual deve estar entre 0 e 100");
        }
        this.percentual = BigDecimal.valueOf(percentual);
    }

    @Override
    public BigDecimal calcular(Pedido pedido) {
        return pedido.subtotal().multiply(percentual).divide(BigDecimal.valueOf(100));
    }

    @Override
    public String descricao() {
        return percentual + "% de desconto";
    }
}
