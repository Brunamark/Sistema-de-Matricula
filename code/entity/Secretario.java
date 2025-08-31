package entity;

import enums.Codigo;
import exceptions.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

public class Secretario extends Usuario {
    Curso curso;

    public Secretario() {
        super();
    }

    private Secretario(Builder builder) {
        super(builder.id, builder.email, builder.senha, builder.nome);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String email;
        private String senha;
        private String nome;

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

        public Secretario build() {
            ExceptionHandler handler = new ExceptionHandler();
            if (nome == null || nome.trim().isEmpty()) {
                handler.campoObrigatorio("Nome do secretário");
            }
            if (email == null || email.trim().isEmpty()) {
                handler.campoObrigatorio("Email do secretário");
            }
            if (senha == null || senha.trim().isEmpty()) {
                handler.campoObrigatorio("Senha do secretário");
            }

            return new Secretario(this);
        }
    }

    public String gerarCurriculoSemestral(Curso curso) {
        // TODO:
        return "Currículo semestral gerado com sucesso";
    }

    public Codigo criarDisciplina(Disciplina disciplina) {
        Disciplina disciplinaExistente = buscarDisciplinaPorNome(disciplina.getNome());

        if (disciplinaExistente != null) {
            return Codigo.DISCIPLINA_JA_EXISTE_406;
        }

        Disciplina novaDisciplina = new Disciplina(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getQuantidadeCreditos(),
                disciplina.getCursos(),
                disciplina.isOptativa(),
                disciplina.isAtiva(),
                disciplina.getAlunos(),
                disciplina.getProfessor());

        return Codigo.DISCIPLINA_CRIADA_201;
    }

    public Codigo editarDisciplina(Disciplina disciplina) {
        Disciplina disciplinaExistente = buscarDisciplinaPorNome(disciplina.getNome());

        if (disciplinaExistente == null) {
            return Codigo.DISCIPLINA_NAO_ENCONTRADA_404;
        }

        disciplinaExistente.setNome(disciplina.getNome());
        disciplinaExistente.setQuantidadeCreditos(disciplina.getQuantidadeCreditos());
        disciplinaExistente.setCursos(disciplina.getCursos());
        disciplinaExistente.setOptativa(disciplina.isOptativa());
        disciplinaExistente.setAtiva(disciplina.isAtiva());
        disciplinaExistente.setAlunos(disciplina.getAlunos());
        disciplinaExistente.setProfessor(disciplina.getProfessor());

        // chamar repository para salvar

        return Codigo.ATUALIZADO_202;
    }

    public Disciplina buscarDisciplinaPorId(Long id) {
        List<Disciplina> disciplinas = curso.getDisciplinas();
        Disciplina disciplina = disciplinas.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);

        if (disciplina == null) {
            return null;
        }
        // chama repository

        return disciplina;
    }

    public Codigo excluirDisciplinaPorNome(String nome) {
        Disciplina disciplinaExistente = buscarDisciplinaPorNome(nome);

        if (disciplinaExistente == null) {
            return Codigo.DISCIPLINA_NAO_ENCONTRADA_404;
        }

        // TODO: Implementar lógica de exclusão

        return Codigo.EXCLUIDO_204;
    }

    public Disciplina buscarDisciplinaPorNomeDoCurso(String nomeCurso) {
        List<Disciplina> disciplinas = curso.getDisciplinas();
        return disciplinas.stream()
                .filter(d -> d.getCursos().stream().anyMatch(
                        c -> c != null && c.getNome() != null && c.getNome().toString().equalsIgnoreCase(nomeCurso)))
                .findFirst()
                .orElse(null);
    }

    public List<Disciplina> buscarDisciplinasPorNome(String nome) {
        List<Disciplina> disciplinas = curso.getDisciplinas();
        return disciplinas.stream()
                .filter(d -> d.getNome() != null && d.getNome().equalsIgnoreCase(nome))
                .collect(Collectors.toList());
    }

    public Codigo criarProfessor(Professor professor) {
        Professor professorExistente = buscarProfessorPorEmail(professor.getEmail());

        if (professorExistente != null) {
            return Codigo.PROFESSOR_JA_EXISTE_406;
        }

        Professor novoProfessor = new Professor(
                professor.getId(),
                professor.getEmail(),
                professor.getSenha(),
                professor.getNome(),
                professor.getDisciplina());

        return Codigo.PROFESSOR_CRIADO_201;
    }

    public Codigo editarProfessor(Professor professor) {
        Professor professorExistente = buscarProfessorPorEmail(professor.getEmail());

        if (professorExistente == null) {
            return Codigo.PROFESSOR_NAO_ENCONTRADO_404;
        }

        // TODO: Implementar lógica de edição

        return Codigo.ATUALIZADO_202;
    }

    public Codigo excluirProfessorPorEmail(String email) {
        Professor professorExistente = buscarProfessorPorEmail(email);

        if (professorExistente == null) {
            return Codigo.PROFESSOR_NAO_ENCONTRADO_404;
        }

        // TODO: Implementar lógica de exclusão

        return Codigo.EXCLUIDO_204;
    }

    public Professor buscarProfessorPorId(Long id) {
        // TODO: Implementar busca por ID
        return null;
    }

    public Codigo criarAluno(Aluno aluno) {
        Aluno alunoExistente = buscarAlunoPorEmail(aluno.getEmail());

        if (alunoExistente != null) {
            return Codigo.ALUNO_JA_EXISTE_406;
        }

        Aluno novoAluno = Aluno.builder()
                .id(aluno.getId())
                .email(aluno.getEmail())
                .senha(aluno.getSenha())
                .nome(aluno.getNome())
                .curso(aluno.getCurso())
                .disciplinas(aluno.getDisciplinas())
                .build();

        return Codigo.ALUNO_CRIADO_201;
    }

    public Codigo editarAluno(Aluno aluno) {
        // TODO: Buscar aluno existente
        Aluno alunoExistente = null; // buscarAlunoPorEmail(aluno.getEmail());

        if (alunoExistente == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        // TODO: Implementar lógica de edição

        return Codigo.ATUALIZADO_202;
    }

    public Codigo excluirAlunoPorEmail(String email) {
        // TODO: Buscar aluno existente
        // Aluno alunoExistente = buscarAlunoPorEmail(email);

        // if (alunoExistente == null) {
        // return Codigo.ALUNO_NAO_ENCONTRADO_404;
        // }

        // TODO: Implementar lógica de exclusão

        return Codigo.EXCLUIDO_204;
    }

    public Codigo excluirAlunoPorId(Long id) {
        Aluno alunoExistente = buscarAlunoPorId(id);

        if (alunoExistente == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        // TODO: Implementar lógica de exclusão

        return Codigo.EXCLUIDO_204;
    }

    public Aluno buscarAlunoPorId(Long id) {
        List<Aluno> alunos = curso.getAlunos();
        return alunos.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Disciplina> listarDisciplinas() {
        // TODO: Implementar listagem de disciplinas
        return null;
    }

    public Disciplina buscarDisciplinaPorNome(String nome) {
        // TODO: Implementar busca por nome
        return null;
    }

    public List<Disciplina> buscarDisciplinasPorNomeCurso(String nome) {
        // TODO: Implementar busca por curso
        return null;
    }

    public List<Professor> listarProfessores() {
        // TODO: Implementar listagem de professores
        return null;
    }

    public List<Professor> buscarProfessorPorNome(String nome) {
        // TODO: Implementar busca por nome
        return null;
    }

    public Professor buscarProfessorPorEmail(String email) {
        // TODO: Implementar busca por email
        return null;
    }

    public List<Aluno> listarAlunos() {
        // TODO: Implementar listagem de alunos
        return null;
    }

    public List<Aluno> buscarAlunoPorNome(String nome) {
        // TODO: Implementar busca por nome
        return null;
    }

    public Aluno buscarAlunoPorEmail(String email) {
        // TODO: Implementar busca por email
        return null;
    }

    // === MÉTODOS ESPECÍFICOS DE MATRÍCULA ===

    public Codigo matricularAluno(Long alunoId, Long disciplinaId) {
        // TODO: Implementar lógica de matrícula
        // Verificar se aluno existe
        // Verificar se disciplina existe
        // Verificar se já está matriculado
        // Verificar pré-requisitos
        // Verificar conflitos de horário

        return Codigo.MATRICULA_REALIZADA_201;
    }

    public Codigo desmatricularAluno(Long alunoId, Long disciplinaId) {
        // TODO: Implementar lógica de desmatrícula

        return Codigo.MATRICULA_CANCELADA_200;
    }

    public Codigo verificarLimiteCreditos(Long alunoId, Long disciplinaId) {
        // TODO: Implementar verificação de limite de créditos

        return Codigo.LIMITE_CREDITOS_EXCEDIDO_422;
    }

    public Codigo verificarPreRequisitos(Long alunoId, Long disciplinaId) {
        // TODO: Implementar verificação de pré-requisitos

        return Codigo.PRE_REQUISITO_NAO_ATENDIDO_422;
    }

    public Codigo verificarConflitosHorario(Long alunoId, Long disciplinaId) {
        // TODO: Implementar verificação de conflitos

        return Codigo.HORARIO_CONFLITO_409;
    }

    public List<Aluno> listarAlunosCurso(Curso curso) {
        if (curso == null) {
            return null;
        }
        return curso.getAlunos();
    }

    public Curso buscarCursoPorId(Long id) {
        if (id == null) {
            return null;
        }
        // busca no repository
        return null;
    }
}
