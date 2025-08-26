package entity;

import enums.Codigo;

import java.util.List;

public class Aluno extends Usuario {
    private Curso curso;
    private List<Disciplina> disciplinas;

    public static final int QUANTIDADE_MAXIMA_DISCIPLINA_OBRIGATORIA = 2;
    public static final int QUANTIDADE_MAXIMA_DISCIPLINA_OPTATIVA = 2;

    public Codigo matricularDisciplinasObrigatorias(List<Disciplina> disciplinas) {
        //TODO
        return null;
    }

    public Codigo matricularDisciplinasOptativas(List<Disciplina> disciplinas) {
        //TODO
        return null;
    }

    public Curso verificarAtributosDoCurso() {
        //TODO
        return null;
    }

    public Codigo cancelarMatricula() {
        //TODO
        return null;
    }
}
