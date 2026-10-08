# ☕ Café dos Padrões: Design Patterns com Java Puro

Projeto final do desafio de **Padrões de Projeto**: um sistema de pedidos de cafeteria que aplica **6 padrões de projeto** em um fluxo real, sem frameworks e sem dependências externas.

Em vez de exemplos isolados, os padrões trabalham juntos: um pedido é **montado** (Builder), **validado** por uma corrente de regras (Chain of Responsibility), recebe um **desconto intercambiável** (Strategy), **notifica** cliente, cozinha e auditoria (Observer), tudo orquestrado por uma **fachada** simples (Facade) que lê a configuração única da loja (Singleton).

## Padrões aplicados

| Padrão | Tipo | Onde | Problema que resolve |
|---|---|---|---|
| **Singleton** | Criacional | [`ConfiguracaoLoja`](src/main/java/br/com/cafeteria/singleton/ConfiguracaoLoja.java) | Uma única configuração (horário, valor mínimo) compartilhada. Usa *Lazy Holder*, que é thread-safe sem `synchronized`. |
| **Builder** | Criacional | [`PedidoBuilder`](src/main/java/br/com/cafeteria/builder/PedidoBuilder.java) | Montar pedidos com API fluente e validação na construção. |
| **Strategy** | Comportamental | [`strategy/`](src/main/java/br/com/cafeteria/strategy) | Regras de desconto trocáveis em tempo de execução: percentual, cupom, "leve 3 pague 2", sem desconto. |
| **Chain of Responsibility** | Comportamental | [`chain/`](src/main/java/br/com/cafeteria/chain) | Validações independentes encadeadas: itens → horário → valor mínimo → estoque. A primeira que falhar interrompe a corrente. |
| **Observer** | Comportamental | [`observer/`](src/main/java/br/com/cafeteria/observer) | Cliente, cozinha e auditoria reagem às mudanças de status sem que o `Pedido` conheça cada um. |
| **Facade** | Estrutural | [`CafeteriaFacade`](src/main/java/br/com/cafeteria/facade/CafeteriaFacade.java) | Esconde a orquestração inteira atrás de `finalizarPedido(pedido, desconto)`. |

## Fluxo

```
PedidoBuilder ──► CafeteriaFacade.finalizarPedido()
                     │
                     ├─► Chain: ValidadorItens → ValidadorHorario → ValidadorValorMinimo → ValidadorEstoque
                     │        (lê ConfiguracaoLoja, o Singleton)
                     │
                     ├─► Strategy: EstrategiaDesconto.calcular()
                     │
                     └─► Pedido.alterarStatus() ──► Observers: NotificadorCliente, PainelCozinha, RegistroAuditoria
```

## Como estender

- **Novo desconto?** Crie uma classe que implemente `EstrategiaDesconto`. Nada mais muda.
- **Nova regra de validação?** Estenda `ValidadorPedido` e encadeie-a na `CafeteriaFacade`.
- **Nova notificação (e-mail, WhatsApp)?** Implemente `ObservadorPedido` e registre-a.

Esse é o ganho real dos padrões: o código fica **aberto para extensão e fechado para modificação** (princípio Open/Closed).

## Como executar

Requer **JDK 17+**. Não precisa de Maven nem Gradle.

```bash
# Linux / macOS / Git Bash
./run.sh          # demonstração
./run.sh test     # testes

# Windows PowerShell
.\run.ps1
.\run.ps1 test
```

### Saída da demonstração (trecho)

```
1) Pedido com 10% de desconto:
  [SMS para Ana] Pedido #1: seu pedido foi confirmado! Total: R$ 19.80
  [Cozinha] Novo pedido #1:
     - 1x Cappuccino
     - 2x Pão de queijo
     Obs: Cappuccino sem canela
  [SMS para Ana] Pedido #1: estamos preparando seu pedido.
  [SMS para Ana] Pedido #1: seu pedido está pronto para retirada!

3) Pedido abaixo do valor mínimo:
  [SMS para Carla] Pedido #3: não foi possível aceitar seu pedido: Valor mínimo do pedido é R$ 10.00
```

## Decisões técnicas

- **`BigDecimal`** para valores monetários, porque `double` gera erros de arredondamento.
- **`Clock` injetado** no validador de horário, para que os testes não dependam da hora real.
- **Records** (`Produto`, `ItemPedido`, `ResultadoValidacao`) para modelos imutáveis.
- **Testes sem framework** ([`Testes.java`](src/test/java/br/com/cafeteria/Testes.java)) cobrindo cada padrão, para que o projeto rode só com o JDK.

## Referências

- [Padrões de Projeto com Java Puro (DIO)](https://github.com/digitalinnovationone/lab-padroes-projeto-java)
- [Padrões de Projeto com Spring (DIO)](https://github.com/digitalinnovationone/lab-padroes-projeto-spring)
- [java-design-patterns (iluwatar)](https://github.com/iluwatar/java-design-patterns)
- [Refactoring Guru: Design Patterns](https://refactoring.guru/pt-br/design-patterns)
