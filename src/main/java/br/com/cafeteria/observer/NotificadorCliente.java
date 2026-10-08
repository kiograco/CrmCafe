package br.com.cafeteria.observer;

import br.com.cafeteria.modelo.Pedido;
import br.com.cafeteria.modelo.StatusPedido;

public class NotificadorCliente implements ObservadorPedido {

    @Override
    public void statusAlterado(Pedido pedido, StatusPedido anterior, String motivo) {
        String mensagem = switch (pedido.getStatus()) {
            case CONFIRMADO -> "seu pedido foi confirmado! Total: R$ " + pedido.total();
            case EM_PREPARO -> "estamos preparando seu pedido.";
            case PRONTO -> "seu pedido está pronto para retirada!";
            case RECUSADO -> "não foi possível aceitar seu pedido: " + motivo;
            case CRIADO -> "pedido registrado.";
        };
        System.out.printf("  [SMS para %s] Pedido #%d: %s%n", pedido.getCliente(), pedido.getNumero(), mensagem);
    }
}
