package br.com.cafeteria.observer;

import br.com.cafeteria.modelo.ItemPedido;
import br.com.cafeteria.modelo.Pedido;
import br.com.cafeteria.modelo.StatusPedido;

public class PainelCozinha implements ObservadorPedido {

    @Override
    public void statusAlterado(Pedido pedido, StatusPedido anterior, String motivo) {
        if (pedido.getStatus() != StatusPedido.CONFIRMADO) {
            return; // a cozinha só se interessa por pedidos recém-confirmados
        }
        System.out.printf("  [Cozinha] Novo pedido #%d:%n", pedido.getNumero());
        for (ItemPedido item : pedido.getItens()) {
            System.out.printf("     - %dx %s%n", item.quantidade(), item.produto().nome());
        }
        if (!pedido.getObservacao().isBlank()) {
            System.out.printf("     Obs: %s%n", pedido.getObservacao());
        }
    }
}
