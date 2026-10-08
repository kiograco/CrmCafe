package br.com.cafeteria.facade;

import br.com.cafeteria.chain.ResultadoValidacao;
import br.com.cafeteria.chain.ValidadorEstoque;
import br.com.cafeteria.chain.ValidadorHorario;
import br.com.cafeteria.chain.ValidadorItens;
import br.com.cafeteria.chain.ValidadorPedido;
import br.com.cafeteria.chain.ValidadorValorMinimo;
import br.com.cafeteria.modelo.ItemPedido;
import br.com.cafeteria.modelo.Pedido;
import br.com.cafeteria.modelo.StatusPedido;
import br.com.cafeteria.observer.ObservadorPedido;
import br.com.cafeteria.strategy.EstrategiaDesconto;

import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Facade: esconde a orquestração de validação (Chain), desconto (Strategy),
 * baixa de estoque e notificações (Observer) atrás de uma interface simples.
 */
public class CafeteriaFacade {

    private final Map<String, Integer> estoque = new HashMap<>();
    private final ValidadorPedido validacao;
    private final List<ObservadorPedido> observadoresPadrao;

    public CafeteriaFacade(Clock relogio, List<ObservadorPedido> observadoresPadrao) {
        this.observadoresPadrao = List.copyOf(observadoresPadrao);
        this.validacao = new ValidadorItens();
        validacao.encadear(new ValidadorHorario(relogio))
                .encadear(new ValidadorValorMinimo())
                .encadear(new ValidadorEstoque(estoque));
    }

    public void abastecer(String codigoProduto, int quantidade) {
        estoque.merge(codigoProduto, quantidade, Integer::sum);
    }

    public int estoqueDe(String codigoProduto) {
        return estoque.getOrDefault(codigoProduto, 0);
    }

    public boolean finalizarPedido(Pedido pedido, EstrategiaDesconto desconto) {
        observadoresPadrao.forEach(pedido::adicionarObservador);

        ResultadoValidacao resultado = validacao.validar(pedido);
        if (!resultado.aprovado()) {
            pedido.alterarStatus(StatusPedido.RECUSADO, resultado.mensagem());
            return false;
        }

        pedido.aplicarDesconto(desconto.calcular(pedido));
        for (ItemPedido item : pedido.getItens()) {
            estoque.merge(item.produto().codigo(), -item.quantidade(), Integer::sum);
        }
        pedido.alterarStatus(StatusPedido.CONFIRMADO, desconto.descricao());
        return true;
    }

    public void iniciarPreparo(Pedido pedido) {
        pedido.alterarStatus(StatusPedido.EM_PREPARO, null);
    }

    public void marcarPronto(Pedido pedido) {
        pedido.alterarStatus(StatusPedido.PRONTO, null);
    }
}
