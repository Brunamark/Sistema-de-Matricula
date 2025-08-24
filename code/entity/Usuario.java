package entity;

import java.util.List;

public abstract class Usuario {
    private Long id;
    private String email;
    private String senha;
    private String nome;

    public boolean efetuarLogin(String email, String senha) {
        //TODO
        return false;
    }

    public List<Disciplina> listarDisciplinas() {
        //TODO
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
