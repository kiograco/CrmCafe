package br.com.cafeteria.chain;

import br.com.cafeteria.modelo.Pedido;
import br.com.cafeteria.singleton.ConfiguracaoLoja;

import java.time.Clock;
import java.time.LocalTime;

public class ValidadorHorario extends ValidadorPedido {

    private final Clock relogio;

    public ValidadorHorario(Clock relogio) {
        this.relogio = relogio;
    }

    @Override
    protected ResultadoValidacao verificar(Pedido pedido) {
        ConfiguracaoLoja config = ConfiguracaoLoja.getInstancia();
        LocalTime agora = LocalTime.now(relogio);
        return config.estaAberta(agora)
                ? ResultadoValidacao.ok()
                : ResultadoValidacao.falha("Loja fechada (funcionamento: "
                        + config.getAbertura() + " às " + config.getFechamento() + ")");
    }
}
