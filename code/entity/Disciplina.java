package entity;

import java.util.List;

public class Disciplina {
    public static final int QUANTIDADE_MAXIMA_ALUNO = 60;
    public static final int QUANTIDADE_MINIMA_ALUNO = 3;
    private Long id;
    private String nome;
    private int quantidadeCreditos;
    private List<Curso> cursos;
    private boolean isOptativa;
    private boolean isAtiva;
    private List<Aluno> alunos;
    private Professor professor;
}
