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

    public Disciplina() {
    }

    public Disciplina(Long id, String nome, int quantidadeCreditos, List<Curso> cursos, 
                      boolean isOptativa, boolean isAtiva, List<Aluno> alunos, Professor professor) {
        this.id = id;
        this.nome = nome;
        this.quantidadeCreditos = quantidadeCreditos;
        this.cursos = cursos;
        this.isOptativa = isOptativa;
        this.isAtiva = isAtiva;
        this.alunos = alunos;
        this.professor = professor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidadeCreditos() {
        return quantidadeCreditos;
    }

    public void setQuantidadeCreditos(int quantidadeCreditos) {
        this.quantidadeCreditos = quantidadeCreditos;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public boolean isOptativa() {
        return isOptativa;
    }

    public void setOptativa(boolean optativa) {
        isOptativa = optativa;
    }

    public boolean isAtiva() {
        return isAtiva;
    }

    public void setAtiva(boolean ativa) {
        isAtiva = ativa;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
