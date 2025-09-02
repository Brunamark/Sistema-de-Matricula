package entity;

import java.util.List;

import exceptions.ExceptionHandler;
import repository.DisciplinaRepository;

public class Professor extends Usuario {
    private Disciplina disciplina;
    private DisciplinaRepository disciplinaRepository = new DisciplinaRepository();

    private Professor() {
        super();
    }

    private Professor(Builder builder) {
        super(builder.id, builder.email, builder.senha, builder.nome);
        this.disciplina = builder.disciplina;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public static class Builder {
        private Long id;
        private String email;
        private String senha;
        private String nome;
        private Disciplina disciplina;

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

        public Builder disciplina(Disciplina disciplina) {
            this.disciplina = disciplina;
            return this;
        }

        public Professor build() {
            ExceptionHandler handler = new ExceptionHandler();
            if (nome == null || nome.trim().isEmpty()) {
                handler.campoObrigatorio("Nome do professor");
            }
            if (email == null || email.trim().isEmpty()) {
                handler.campoObrigatorio("Email do professor");
            }
            if (senha == null || senha.trim().isEmpty()) {
                handler.campoObrigatorio("Senha do professor");
            }
            
            return new Professor(this);
        }
    }

    public List<Aluno> verificarAlunosMatriculadosPorDisciplina(String nomeDisciplina) {
        Disciplina disciplina = disciplinaRepository.buscarPorNome(nomeDisciplina);
        if (disciplina != null) {
            return disciplina.getAlunos();
        }
        return null;
    }
}
