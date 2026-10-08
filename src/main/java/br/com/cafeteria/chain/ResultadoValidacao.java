package br.com.cafeteria.chain;

public record ResultadoValidacao(boolean aprovado, String mensagem) {

    public static ResultadoValidacao ok() {
        return new ResultadoValidacao(true, "OK");
    }

    public static ResultadoValidacao falha(String mensagem) {
        return new ResultadoValidacao(false, mensagem);
    }
}
