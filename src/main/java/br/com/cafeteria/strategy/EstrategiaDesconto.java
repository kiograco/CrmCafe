package br.com.cafeteria.strategy;

import br.com.cafeteria.modelo.Pedido;

import java.math.BigDecimal;

/**
 * Strategy: cada regra de desconto é uma implementação intercambiável em tempo de execução.
 */
public interface EstrategiaDesconto {

    BigDecimal calcular(Pedido pedido);

    String descricao();
}
