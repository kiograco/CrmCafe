package br.com.cafeteria.singleton;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Singleton (Lazy Holder): uma única configuração da loja compartilhada pela aplicação.
 * A JVM garante que a classe Holder só é carregada no primeiro acesso, de forma thread-safe.
 */
public final class ConfiguracaoLoja {

    private String nome = "Café Padrão";
    private LocalTime abertura = LocalTime.of(7, 0);
    private LocalTime fechamento = LocalTime.of(22, 0);
    private BigDecimal valorMinimoPedido = new BigDecimal("10.00");

    private ConfiguracaoLoja() {
    }

    private static final class Holder {
        private static final ConfiguracaoLoja INSTANCIA = new ConfiguracaoLoja();
    }

    public static ConfiguracaoLoja getInstancia() {
        return Holder.INSTANCIA;
    }

    public boolean estaAberta(LocalTime horario) {
        return !horario.isBefore(abertura) && horario.isBefore(fechamento);
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalTime getAbertura() { return abertura; }
    public LocalTime getFechamento() { return fechamento; }
    public void setHorario(LocalTime abertura, LocalTime fechamento) {
        this.abertura = abertura;
        this.fechamento = fechamento;
    }
    public BigDecimal getValorMinimoPedido() { return valorMinimoPedido; }
    public void setValorMinimoPedido(BigDecimal valorMinimoPedido) { this.valorMinimoPedido = valorMinimoPedido; }
}
