package br.com.cafeteria;

import br.com.cafeteria.builder.PedidoBuilder;
import br.com.cafeteria.facade.CafeteriaFacade;
import br.com.cafeteria.modelo.Pedido;
import br.com.cafeteria.modelo.Produto;
import br.com.cafeteria.observer.NotificadorCliente;
import br.com.cafeteria.observer.PainelCozinha;
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

public class Main {

    public static void main(String[] args) {
        ConfiguracaoLoja config = ConfiguracaoLoja.getInstancia();
        config.setNome("Café dos Padrões");

        // relógio fixo às 15h para a demonstração ser sempre reproduzível
        ZoneId zona = ZoneId.systemDefault();
        Clock relogio = Clock.fixed(LocalDate.now().atTime(LocalTime.of(15, 0)).atZone(zona).toInstant(), zona);

        RegistroAuditoria auditoria = new RegistroAuditoria();
        CafeteriaFacade cafeteria = new CafeteriaFacade(relogio,
                List.of(new NotificadorCliente(), new PainelCozinha(), auditoria));

        Produto espresso = new Produto("ESP", "Espresso", new BigDecimal("6.50"));
        Produto cappuccino = new Produto("CAP", "Cappuccino", new BigDecimal("12.00"));
        Produto paoDeQueijo = new Produto("PDQ", "Pão de queijo", new BigDecimal("5.00"));

        cafeteria.abastecer("ESP", 20);
        cafeteria.abastecer("CAP", 2);
        cafeteria.abastecer("PDQ", 30);

        System.out.println("=== " + config.getNome() + " ===\n");

        System.out.println("1) Pedido com 10% de desconto:");
        Pedido p1 = PedidoBuilder.novo()
                .cliente("Ana")
                .item(cappuccino, 1)
                .item(paoDeQueijo, 2)
                .observacao("Cappuccino sem canela")
                .construir();
        if (cafeteria.finalizarPedido(p1, new DescontoPercentual(10))) {
            cafeteria.iniciarPreparo(p1);
            cafeteria.marcarPronto(p1);
        }

        System.out.println("\n2) Pedido com 'leve 3, pague 2':");
        Pedido p2 = PedidoBuilder.novo().cliente("Bruno").item(paoDeQueijo, 6).construir();
        cafeteria.finalizarPedido(p2, new LeveTresPagueDois());

        System.out.println("\n3) Pedido abaixo do valor mínimo:");
        Pedido p3 = PedidoBuilder.novo().cliente("Carla").item(espresso, 1).construir();
        cafeteria.finalizarPedido(p3, new SemDesconto());

        System.out.println("\n4) Pedido sem estoque suficiente:");
        Pedido p4 = PedidoBuilder.novo().cliente("Diego").item(cappuccino, 3).construir();
        cafeteria.finalizarPedido(p4, new CupomValorFixo("BEMVINDO", new BigDecimal("5.00")));

        System.out.println("\n--- Auditoria ---");
        auditoria.getEventos().forEach(e -> System.out.println("  " + e));
    }
}
