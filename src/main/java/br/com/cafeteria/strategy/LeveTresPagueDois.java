package br.com.cafeteria.strategy;

import br.com.cafeteria.modelo.ItemPedido;
import br.com.cafeteria.modelo.Pedido;

import java.math.BigDecimal;

/** A cada 3 unidades do mesmo produto, 1 sai de graça. */
public class LeveTresPagueDois implements EstrategiaDesconto {

    @Override
    public BigDecimal calcular(Pedido pedido) {
        BigDecimal desconto = BigDecimal.ZERO;
        for (ItemPedido item : pedido.getItens()) {
            int gratis = item.quantidade() / 3;
            desconto = desconto.add(item.produto().preco().multiply(BigDecimal.valueOf(gratis)));
        }
        return desconto;
    }

    @Override
    public String descricao() {
        return "Leve 3, pague 2";
    }
}
