package br.com.cafeteria.chain;

import br.com.cafeteria.modelo.Pedido;

public class ValidadorItens extends ValidadorPedido {

    @Override
    protected ResultadoValidacao verificar(Pedido pedido) {
        return pedido.getItens().isEmpty()
                ? ResultadoValidacao.falha("O pedido não possui itens")
                : ResultadoValidacao.ok();
    }
}
