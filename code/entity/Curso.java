package entity;

import enums.Cursos;
import exceptions.ExceptionHandler;

import java.util.List;
import java.util.ArrayList;

public class Curso {
    private Long id;
    private Cursos nome;
    private int credito;
    private List<Disciplina> disciplinas;
    private List<Aluno> alunos;

    private Curso() {
        this.disciplinas = new ArrayList<>();
        this.alunos = new ArrayList<>();
    }

    private Curso(Builder builder) {
        this.id = builder.id;
        this.nome = builder.nome;
        this.credito = builder.credito;
        this.disciplinas = builder.disciplinas != null ? builder.disciplinas : new ArrayList<>();
        this.alunos = builder.alunos != null ? builder.alunos : new ArrayList<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cursos getNome() {
        return nome;
    }

    public void setNome(Cursos nome) {
        this.nome = nome;
    }

    public int getCredito() {
        return credito;
    }

    public void setCredito(int credito) {
        this.credito = credito;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        if (this.disciplinas == null) {
            this.disciplinas = new ArrayList<>();
        }
        this.disciplinas.add(disciplina);
    }

    public void adicionarAluno(Aluno aluno) {
        if (this.alunos == null) {
            this.alunos = new ArrayList<>();
        }
        this.alunos.add(aluno);
    }

    public static class Builder {
        private Long id;
        private Cursos nome;
        private int credito;
        private List<Disciplina> disciplinas;
        private List<Aluno> alunos;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nome(Cursos nome) {
            this.nome = nome;
            return this;
        }

        public Builder credito(int credito) {
            this.credito = credito;
            return this;
        }

        public Builder disciplinas(List<Disciplina> disciplinas) {
            this.disciplinas = disciplinas;
            return this;
        }

        public Builder alunos(List<Aluno> alunos) {
            this.alunos = alunos;
            return this;
        }

        public Builder adicionarDisciplina(Disciplina disciplina) {
            if (this.disciplinas == null) {
                this.disciplinas = new ArrayList<>();
            }
            this.disciplinas.add(disciplina);
            return this;
        }

        public Builder adicionarAluno(Aluno aluno) {
            if (this.alunos == null) {
                this.alunos = new ArrayList<>();
            }
            this.alunos.add(aluno);
            return this;
        }

        public Curso build() { 
            ExceptionHandler handler = new ExceptionHandler();
            if (nome == null) {
                handler.campoObrigatorio("Nome");
            }
            if (credito <= 0) {
                handler.dadosNulos("Crédito");
            }

            return new Curso(this);
        }
    }
}
