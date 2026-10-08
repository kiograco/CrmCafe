package br.com.cafeteria.chain;

import br.com.cafeteria.modelo.Pedido;

/**
 * Chain of Responsibility: cada validador verifica uma regra e, se passar,
 * delega para o próximo elo da corrente.
 */
public abstract class ValidadorPedido {

    private ValidadorPedido proximo;

    /** Encadeia o próximo validador e o retorna, permitindo a.encadear(b).encadear(c). */
    public ValidadorPedido encadear(ValidadorPedido proximo) {
        this.proximo = proximo;
        return proximo;
    }

    public ResultadoValidacao validar(Pedido pedido) {
        ResultadoValidacao resultado = verificar(pedido);
        if (!resultado.aprovado() || proximo == null) {
            return resultado;
        }
        return proximo.validar(pedido);
    }

    protected abstract ResultadoValidacao verificar(Pedido pedido);
}
