package br.com.cafeteria.strategy;

import br.com.cafeteria.modelo.Pedido;

import java.math.BigDecimal;

public class CupomValorFixo implements EstrategiaDesconto {

    private final String codigo;
    private final BigDecimal valor;

    public CupomValorFixo(String codigo, BigDecimal valor) {
        this.codigo = codigo;
        this.valor = valor;
    }

    @Override
    public BigDecimal calcular(Pedido pedido) {
        // o cupom nunca pode deixar o pedido negativo
        return valor.min(pedido.subtotal());
    }

    @Override
    public String descricao() {
        return "Cupom " + codigo + " (R$ " + valor + ")";
    }
}
