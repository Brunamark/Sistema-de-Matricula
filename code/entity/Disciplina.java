package entity;

import java.util.List;
import enums.Codigo;
import exceptions.ExceptionHandler;

public class Disciplina {
    public static final int QUANTIDADE_MAXIMA_ALUNO = 60;
    public static final int QUANTIDADE_MINIMA_ALUNO = 3;
    public static final int CREDITOS_MINIMO = 1; 
    public static final int CREDITOS_MAXIMO = 12; 


    private Long id;
    private String nome;
    private int quantidadeCreditos;
    private List<Curso> cursos;
    private boolean isOptativa;
    private boolean isAtiva;
    private List<Aluno> alunos;
    private Professor professor;

    private Disciplina() {
    }

    private Disciplina(Builder builder) {
        this.id = builder.id;
        this.nome = builder.nome;
        this.quantidadeCreditos = builder.quantidadeCreditos;
        this.cursos = builder.cursos;
        this.isOptativa = builder.isOptativa;
        this.isAtiva = builder.isAtiva;
        this.alunos = builder.alunos;
        this.professor = builder.professor;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String nome;
        private int quantidadeCreditos;
        private List<Curso> cursos;
        private boolean isOptativa = false;
        private boolean isAtiva = true;
        private List<Aluno> alunos;
        private Professor professor;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder quantidadeCreditos(int quantidadeCreditos) {
            this.quantidadeCreditos = quantidadeCreditos;
            return this;
        }

        public Builder cursos(List<Curso> cursos) {
            this.cursos = cursos;
            return this;
        }

        public Builder optativa(boolean isOptativa) {
            this.isOptativa = isOptativa;
            return this;
        }

        public Builder ativa(boolean isAtiva) {
            this.isAtiva = isAtiva;
            return this;
        }

        public Builder alunos(List<Aluno> alunos) {
            this.alunos = alunos;
            return this;
        }

        public Builder professor(Professor professor) {
            this.professor = professor;
            return this;
        }

        public Disciplina build() {
            ExceptionHandler handler = new ExceptionHandler();
            if (nome == null || nome.trim().isEmpty()) {
                handler.campoObrigatorio("Nome da disciplina");
            }
            if (quantidadeCreditos <= CREDITOS_MINIMO) {
                handler.dadosInvalidos("Quantidade de créditos não deve ser menor ou igual a " + CREDITOS_MINIMO);
            }

            if (quantidadeCreditos > CREDITOS_MAXIMO) {
                handler.dadosInvalidos("Quantidade de créditos não deve ser maior que " + CREDITOS_MAXIMO);
            }
            return new Disciplina(this);
        }
    }


    public Codigo adicionarAluno(Aluno aluno) {
        if (aluno == null) {
            return Codigo.DADOS_NULOS_400;
        }

        if (!isAtiva) {
            return Codigo.DISCIPLINA_INATIVA_409;
        }

        if (alunos != null && alunos.contains(aluno)) {
            return Codigo.ALUNO_JA_MATRICULADO_409;
        }

        if (alunos != null && alunos.size() >= QUANTIDADE_MAXIMA_ALUNO) {
            return Codigo.LIMITE_MATRICULAS_EXCEDIDO_422;
        }

        if (alunos == null) {
            alunos = new java.util.ArrayList<>();
        }

        alunos.add(aluno);
        return Codigo.MATRICULA_REALIZADA_201;
    }

    public Codigo removerAluno(Aluno aluno) {
        if (aluno == null) {
            return Codigo.DADOS_NULOS_400;
        }

        if (alunos == null || !alunos.contains(aluno)) {
            return Codigo.ALUNO_NAO_MATRICULADO_404;
        }

        alunos.remove(aluno);
        return Codigo.MATRICULA_CANCELADA_200;
    }

    public Codigo definirProfessor(Professor professor) {
        if (professor == null) {
            return Codigo.DADOS_NULOS_400;
        }

        this.professor = professor;
        return Codigo.ATUALIZADO_202;
    }

    public Codigo ativar() {
        if (isAtiva) {
            return Codigo.OPERACAO_NAO_PERMITIDA_405;
        }

        this.isAtiva = true;
        return Codigo.ATUALIZADO_202;
    }

    public Codigo desativar() {
        if (!isAtiva) {
            return Codigo.OPERACAO_NAO_PERMITIDA_405;
        }

        if (alunos != null && !alunos.isEmpty()) {
            return Codigo.CONFLITO_409; 
        }

        this.isAtiva = false;
        return Codigo.ATUALIZADO_202;
    }

    public Codigo validarCapacidadeMinima() {
        if (alunos == null || alunos.size() < QUANTIDADE_MINIMA_ALUNO) {
            return Codigo.CONFLITO_409; 
        }

        return Codigo.OK_200;
    }

    public Codigo validarPreRequisitos(Aluno aluno) {
        if (aluno == null) {
            return Codigo.DADOS_NULOS_400;
        }

       
        return Codigo.OK_200; 
    }

    public Codigo verificarConflitosHorario(Aluno aluno) {
        if (aluno == null) {
            return Codigo.DADOS_NULOS_400;
        }

        return Codigo.OK_200; 
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


    public int getQuantidadeAlunosMatriculados() {
        return alunos != null ? alunos.size() : 0;
    }

    public boolean temVagasDisponiveis() {
        return getQuantidadeAlunosMatriculados() < QUANTIDADE_MAXIMA_ALUNO;
    }

    public boolean temCapacidadeMinima() {
        return getQuantidadeAlunosMatriculados() >= QUANTIDADE_MINIMA_ALUNO;
    }

    public boolean temProfessorDefinido() {
        return professor != null;
    }
}
