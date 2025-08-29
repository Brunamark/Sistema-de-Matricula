package entity;

import java.util.List;

public class Professor extends Usuario {
    private Disciplina disciplina;

    public Professor() {
        super();
    }

    public Professor(Long id, String email, String senha, String nome, Disciplina disciplina) {
        super(id, email, senha, nome);
        this.disciplina = disciplina;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    // Métodos
    public List<Aluno> verificarAlunosMatriculadosPorDisciplina(String nomeDisciplina) {
        //TODO
        return null;
    }
}
