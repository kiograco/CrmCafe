package br.com.cafeteria.observer;

import br.com.cafeteria.modelo.Pedido;
import br.com.cafeteria.modelo.StatusPedido;

/** Observer: interessados em mudanças de status de um pedido. */
public interface ObservadorPedido {

    void statusAlterado(Pedido pedido, StatusPedido anterior, String motivo);
}
