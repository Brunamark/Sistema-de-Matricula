package entity;

import java.util.List;

public abstract class Usuario {
    private Long id;
    private String email;
    private String senha;
    private String nome;

    public Usuario() {
    }

    public Usuario(Long id, String email, String senha, String nome) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario efetuarLogin(String email, String senha) {
        if(email == null || senha == null) {
            return null;
        }
        if(this.email.equals(email) && this.senha.equals(senha)) {
            return this;
        }
        return null;
    }
}
