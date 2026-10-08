package br.com.cafeteria;

import br.com.cafeteria.builder.PedidoBuilder;
import br.com.cafeteria.facade.CafeteriaFacade;
import br.com.cafeteria.modelo.Pedido;
import br.com.cafeteria.modelo.Produto;
import br.com.cafeteria.modelo.StatusPedido;
import br.com.cafeteria.observer.RegistroAuditoria;
import br.com.cafeteria.singleton.ConfiguracaoLoja;
import br.com.cafeteria.strategy.CupomValorFixo;
import br.com.cafeteria.strategy.DescontoPercentual;
import br.com.cafeteria.strategy.LeveTresPagueDois;
import br.com.cafeteria.strategy.SemDesconto;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

/** Mini executor de testes sem dependências externas (roda só com o JDK). */
public class Testes {

    private static final Produto CAFE = new Produto("CAF", "Café", new BigDecimal("8.00"));
    private static final Produto BOLO = new Produto("BOL", "Bolo", new BigDecimal("10.00"));

    private static int aprovados;
    private static int falhas;

    public static void main(String[] args) {
        teste("Singleton retorna sempre a mesma instância", () ->
                verificar(ConfiguracaoLoja.getInstancia() == ConfiguracaoLoja.getInstancia(), "instâncias diferentes"));

        teste("Builder exige cliente", () -> {
            try {
                PedidoBuilder.novo().item(CAFE, 1).construir();
                verificar(false, "deveria lançar exceção");
            } catch (IllegalStateException esperado) {
                // ok
            }
        });

        teste("Builder calcula subtotal", () -> {
            Pedido p = PedidoBuilder.novo().cliente("X").item(CAFE, 2).item(BOLO, 1).construir();
            igual(new BigDecimal("26.00"), p.subtotal());
        });

        teste("Strategy: desconto percentual", () -> {
            Pedido p = PedidoBuilder.novo().cliente("X").item(BOLO, 2).construir();
            igual(new BigDecimal("3.00"), new DescontoPercentual(15).calcular(p));
        });

        teste("Strategy: cupom não ultrapassa o subtotal", () -> {
            Pedido p = PedidoBuilder.novo().cliente("X").item(CAFE, 1).construir();
            igual(new BigDecimal("8.00"), new CupomValorFixo("X", new BigDecimal("50")).calcular(p));
        });

        teste("Strategy: leve 3 pague 2", () -> {
            Pedido p = PedidoBuilder.novo().cliente("X").item(CAFE, 7).construir();
            igual(new BigDecimal("16.00"), new LeveTresPagueDois().calcular(p));
        });

        teste("Facade confirma pedido válido, aplica desconto e baixa estoque", () -> {
            CafeteriaFacade f = facadeAs(LocalTime.of(10, 0), new RegistroAuditoria());
            Pedido p = PedidoBuilder.novo().cliente("X").item(BOLO, 2).construir();
            verificar(f.finalizarPedido(p, new DescontoPercentual(10)), "deveria aprovar");
            igual(StatusPedido.CONFIRMADO, p.getStatus());
            igual(new BigDecimal("18.00"), p.total());
            igual(3, f.estoqueDe("BOL"));
        });

        teste("Chain: recusa pedido fora do horário", () -> {
            CafeteriaFacade f = facadeAs(LocalTime.of(23, 30), new RegistroAuditoria());
            Pedido p = PedidoBuilder.novo().cliente("X").item(BOLO, 2).construir();
            verificar(!f.finalizarPedido(p, new SemDesconto()), "deveria recusar");
            igual(StatusPedido.RECUSADO, p.getStatus());
            igual(5, f.estoqueDe("BOL"));
        });

        teste("Chain: recusa pedido abaixo do valor mínimo", () -> {
            CafeteriaFacade f = facadeAs(LocalTime.of(10, 0), new RegistroAuditoria());
            Pedido p = PedidoBuilder.novo().cliente("X").item(CAFE, 1).construir();
            verificar(!f.finalizarPedido(p, new SemDesconto()), "deveria recusar");
        });

        teste("Chain: recusa pedido sem estoque", () -> {
            CafeteriaFacade f = facadeAs(LocalTime.of(10, 0), new RegistroAuditoria());
            Pedido p = PedidoBuilder.novo().cliente("X").item(BOLO, 6).construir();
            verificar(!f.finalizarPedido(p, new SemDesconto()), "deveria recusar");
        });

        teste("Observer: auditoria registra todo o ciclo do pedido", () -> {
            RegistroAuditoria auditoria = new RegistroAuditoria();
            CafeteriaFacade f = facadeAs(LocalTime.of(10, 0), auditoria);
            Pedido p = PedidoBuilder.novo().cliente("X").item(BOLO, 1).construir();
            f.finalizarPedido(p, new SemDesconto());
            f.iniciarPreparo(p);
            f.marcarPronto(p);
            igual(3, auditoria.getEventos().size());
            verificar(auditoria.getEventos().get(2).endsWith("EM_PREPARO -> PRONTO"), auditoria.getEventos().toString());
        });

        System.out.printf("%n%d aprovado(s), %d falha(s)%n", aprovados, falhas);
        if (falhas > 0) {
            System.exit(1);
        }
    }

    private static CafeteriaFacade facadeAs(LocalTime horario, RegistroAuditoria auditoria) {
        ZoneId zona = ZoneId.systemDefault();
        Clock relogio = Clock.fixed(LocalDate.now().atTime(horario).atZone(zona).toInstant(), zona);
        CafeteriaFacade facade = new CafeteriaFacade(relogio, List.of(auditoria));
        facade.abastecer("CAF", 10);
        facade.abastecer("BOL", 5);
        return facade;
    }

    private static void teste(String nome, Runnable corpo) {
        try {
            corpo.run();
            aprovados++;
            System.out.println("[OK]    " + nome);
        } catch (AssertionError | RuntimeException e) {
            falhas++;
            System.out.println("[FALHA] " + nome + " -> " + e.getMessage());
        }
    }

    private static void verificar(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new AssertionError(mensagem);
        }
    }

    private static void igual(Object esperado, Object atual) {
        boolean ok = esperado instanceof BigDecimal e && atual instanceof BigDecimal a
                ? e.compareTo(a) == 0
                : esperado.equals(atual);
        verificar(ok, "esperado " + esperado + " mas foi " + atual);
    }
}
