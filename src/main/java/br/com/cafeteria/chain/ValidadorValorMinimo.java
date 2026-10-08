package br.com.cafeteria.chain;

import br.com.cafeteria.modelo.Pedido;
import br.com.cafeteria.singleton.ConfiguracaoLoja;

import java.math.BigDecimal;

public class ValidadorValorMinimo extends ValidadorPedido {

    @Override
    protected ResultadoValidacao verificar(Pedido pedido) {
        BigDecimal minimo = ConfiguracaoLoja.getInstancia().getValorMinimoPedido();
        return pedido.subtotal().compareTo(minimo) >= 0
                ? ResultadoValidacao.ok()
                : ResultadoValidacao.falha("Valor mínimo do pedido é R$ " + minimo);
    }
}
