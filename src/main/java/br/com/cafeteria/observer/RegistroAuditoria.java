package br.com.cafeteria.observer;

import br.com.cafeteria.modelo.Pedido;
import br.com.cafeteria.modelo.StatusPedido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegistroAuditoria implements ObservadorPedido {

    private final List<String> eventos = new ArrayList<>();

    @Override
    public void statusAlterado(Pedido pedido, StatusPedido anterior, String motivo) {
        eventos.add("#" + pedido.getNumero() + " " + anterior + " -> " + pedido.getStatus()
                + (motivo == null || motivo.isBlank() ? "" : " (" + motivo + ")"));
    }

    public List<String> getEventos() {
        return Collections.unmodifiableList(eventos);
    }
}
