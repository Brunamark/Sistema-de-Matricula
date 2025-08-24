package entity;

import enumation.Cursos;

import java.util.List;

public class Curso {
    private Long id;
    private Cursos nome;
    private int credito;
    private List<Disciplina> disciplinas;
    private List<Aluno> alunos;
}
