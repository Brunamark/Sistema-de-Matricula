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

    // Métodos
    public boolean efetuarLogin(String email, String senha) {
        if(this.email.equals(email) && this.senha.equals(senha)) {
            return true;
        }
        return false;
    }

    public List<Disciplina> listarDisciplinas() {

        return null;
    }

    public Disciplina buscarDisciplinaPorNome(String nome) {
        //TODO
        return null;
    }

    public List<Disciplina> buscarDisciplinasPorNomeCurso(String nome) {
        //TODO
        return null;
    }
}
