package br.com.cafeteria.chain;

import br.com.cafeteria.modelo.ItemPedido;
import br.com.cafeteria.modelo.Pedido;

import java.util.Map;

public class ValidadorEstoque extends ValidadorPedido {

    private final Map<String, Integer> estoque;

    public ValidadorEstoque(Map<String, Integer> estoque) {
        this.estoque = estoque;
    }

    @Override
    protected ResultadoValidacao verificar(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            int disponivel = estoque.getOrDefault(item.produto().codigo(), 0);
            if (disponivel < item.quantidade()) {
                return ResultadoValidacao.falha("Estoque insuficiente de " + item.produto().nome()
                        + " (disponível: " + disponivel + ")");
            }
        }
        return ResultadoValidacao.ok();
    }
}
