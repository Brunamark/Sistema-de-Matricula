package entity;

import enums.Codigo;
import exceptions.ExceptionHandler;

import java.util.List;
import java.util.ArrayList;

public class Aluno extends Usuario {
    private Curso curso;
    private List<Disciplina> disciplinas;

    public static final int QUANTIDADE_MAXIMA_DISCIPLINA_OBRIGATORIA = 2;
    public static final int QUANTIDADE_MAXIMA_DISCIPLINA_OPTATIVA = 2;

    private Aluno() {
        super();
        this.disciplinas = new ArrayList<>();
    }

    private Aluno(Builder builder) {
        super(builder.id, builder.email, builder.senha, builder.nome);
        this.curso = builder.curso;
        this.disciplinas = builder.disciplinas != null ? builder.disciplinas : new ArrayList<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public static class Builder {
        private Long id;
        private String email;
        private String senha;
        private String nome;
        private Curso curso;
        private List<Disciplina> disciplinas;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder senha(String senha) {
            this.senha = senha;
            return this;
        }

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder curso(Curso curso) {
            this.curso = curso;
            return this;
        }

        public Builder disciplinas(List<Disciplina> disciplinas) {
            this.disciplinas = disciplinas;
            return this;
        }

        public Builder adicionarDisciplina(Disciplina disciplina) {
            if (this.disciplinas == null) {
                this.disciplinas = new ArrayList<>();
            }
            this.disciplinas.add(disciplina);
            return this;
        }

        public Aluno build() {
            ExceptionHandler handler = new ExceptionHandler();
            if (nome == null || nome.trim().isEmpty()) {
                handler.campoObrigatorio("Nome do aluno");
            }
            if (email == null || email.trim().isEmpty()) {
                handler.campoObrigatorio("Email do aluno");
            }
            if (senha == null || senha.trim().isEmpty()) {
                handler.campoObrigatorio("Senha do aluno");
            }

            return new Aluno(this);
        }
    }

    public Codigo matricularDisciplinasObrigatorias(List<Disciplina> disciplinas) {
        // TODO
        return null;
    }

    public Codigo matricularDisciplinasOptativas(List<Disciplina> disciplinas) {
        // TODO
        return null;
    }

    public Curso verificarAtributosDoCurso() {
        // TODO
        return null;
    }

    public Codigo cancelarMatricula() {
        // TODO
        return null;
    }
}
