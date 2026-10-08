package br.com.cafeteria.modelo;

import java.math.BigDecimal;

public record Produto(String codigo, String nome, BigDecimal preco) {
}
