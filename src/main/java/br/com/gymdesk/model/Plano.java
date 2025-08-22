package br.com.gymdesk.model;

public class Plano {
    public Integer id;
    public String nome;
    public double mensalidade;

    public Plano() {}

    public Plano(Integer id, String nome, double mensalidade) {
        this.id = id;
        this.nome = nome;
        this.mensalidade = mensalidade;
    }

    @Override
    public String toString() {
        return nome + " (R$ " + String.format("%.2f", mensalidade) + ")";
    }
}