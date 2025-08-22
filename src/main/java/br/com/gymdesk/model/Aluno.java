package br.com.gymdesk.model;

public class Aluno {
    public Integer id;
    public String nome;
    public String email;
    public String telefone;
    public Integer planoId;
    public boolean ativo;

    public Aluno() {}

    public Aluno(Integer id, String nome, String email, String telefone, Integer planoId, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.planoId = planoId;
        this.ativo = ativo;
    }
}