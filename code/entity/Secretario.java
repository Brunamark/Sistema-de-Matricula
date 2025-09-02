package entity;

import enums.Codigo;
import exceptions.ExceptionHandler;
import repository.AlunoRepository;
import repository.CursoRepository;
import repository.DisciplinaRepository;
import repository.ProfessorRepository;
import repository.SecretarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Secretario extends Usuario {
    private Curso curso;

    private static AlunoRepository alunoRepository = new AlunoRepository();
    private static ProfessorRepository professorRepository = new ProfessorRepository();
    private static DisciplinaRepository disciplinaRepository = new DisciplinaRepository();
    private static CursoRepository cursoRepository = new CursoRepository();
    
    private Secretario() {
        super();
    }

    private Secretario(Builder builder) {
        super(builder.id, builder.email, builder.senha, builder.nome);
        this.curso = builder.curso;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String email;
        private String senha;
        private String nome;
        private Curso curso;

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

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
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
        
        disciplinaRepository.salvar(disciplina);
         
        return Codigo.DISCIPLINA_CRIADA_201;
    }

    public Codigo editarDisciplina(Disciplina disciplina) {
        Disciplina disciplinaExistente = buscarDisciplinaPorNome(disciplina.getNome());

        if (disciplinaExistente == null) {
            return Codigo.DISCIPLINA_NAO_ENCONTRADA_404;
        }

        disciplinaRepository.salvar(disciplina);

        return Codigo.ATUALIZADO_202;
    }

    public Disciplina buscarDisciplinaPorId(Long id) {
        if (curso == null || curso.getDisciplinas() == null) {
            return null;
        }

        return disciplinaRepository.buscarPorId(id);
    }

    public Codigo excluirDisciplinaPorNome(String nome) {
        Disciplina disciplinaExistente = buscarDisciplinaPorNome(nome);

        if (disciplinaExistente == null) {
            return Codigo.DISCIPLINA_NAO_ENCONTRADA_404;
        }

        disciplinaRepository.excluir(disciplinaExistente.getId());
        return Codigo.EXCLUIDO_204;
    }

    public List<Disciplina> buscarDisciplinaPorNomeDoCurso(String nomeCurso) {
        if (curso == null || curso.getDisciplinas() == null) {
            return null;
        }

        return disciplinaRepository.buscarPorNomeDoCurso(nomeCurso);
    }

    public List<Disciplina> buscarDisciplinasPorNome(String nome) {
        if (curso == null || curso.getDisciplinas() == null) {
            return null;
        }

        return disciplinaRepository.buscarDisciplinasPorNome(nome);
    }

    public Codigo criarProfessor(Professor professor) {
        Professor professorExistente = buscarProfessorPorEmail(professor.getEmail());

        if (professorExistente != null) {
            return Codigo.PROFESSOR_JA_EXISTE_406;
        }
        professorRepository.salvar(professor);
        return Codigo.PROFESSOR_CRIADO_201;
    }

    public Codigo editarProfessor(Professor professor) {
        Professor professorExistente = buscarProfessorPorEmail(professor.getEmail());

        if (professorExistente == null) {
            return Codigo.PROFESSOR_NAO_ENCONTRADO_404;
        }

        professorRepository.salvar(professor);

        return Codigo.ATUALIZADO_202;
    }

    public Codigo excluirProfessorPorEmail(String email) {
        Professor professorExistente = buscarProfessorPorEmail(email);

        if (professorExistente == null) {
            return Codigo.PROFESSOR_NAO_ENCONTRADO_404;
        }

        professorRepository.excluir(professorExistente.getId());
        return Codigo.EXCLUIDO_204;
    }

    public Professor buscarProfessorPorId(Long id) {
        if (id == null) return null;

        return professorRepository.buscarPorId(id);
    }

    public Codigo criarAluno(Aluno aluno) {
        Aluno alunoExistente = buscarAlunoPorEmail(aluno.getEmail());

        if (alunoExistente != null) {
            return Codigo.ALUNO_JA_EXISTE_406;
        }

        alunoRepository.salvar(aluno);

        return Codigo.ALUNO_CRIADO_201;
    }

    public Codigo editarAluno(Aluno aluno) {
        Aluno alunoExistente = buscarAlunoPorEmail(aluno.getEmail());

        if (alunoExistente == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        alunoRepository.salvar(aluno);

        return Codigo.ATUALIZADO_202;
    }

    public Codigo excluirAlunoPorEmail(String email) {
        Aluno alunoExistente = buscarAlunoPorEmail(email);

        if (alunoExistente == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        alunoRepository.excluir(alunoExistente.getId());
        return Codigo.EXCLUIDO_204;
    }

    public Codigo excluirAlunoPorId(Long id) {
        Aluno alunoExistente = buscarAlunoPorId(id);

        if (alunoExistente == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        alunoRepository.excluir(alunoExistente.getId());
        return Codigo.EXCLUIDO_204;
    }

    public Aluno buscarAlunoPorId(Long id) {
        if (curso == null || curso.getAlunos() == null) {
            return null;
        }

        return alunoRepository.buscarPorId(id);
    }

    public List<Disciplina> listarDisciplinas() {
        if (curso == null) {
            return null;
        }
        return disciplinaRepository.listarTodos();
    }

    public Disciplina buscarDisciplinaPorNome(String nome) {
        if (curso == null || curso.getDisciplinas() == null) {
            return null;
        }

       return disciplinaRepository.buscarPorNome(nome);
    }

    public List<Disciplina> buscarDisciplinasPorNomeCurso(String nome) {
        if (curso == null) {
            return null;
        }
        return disciplinaRepository.buscarPorNomeDoCurso(nome);
    }

    public List<Professor> listarProfessores() {
        return professorRepository.listarTodos();
    }

    public List<Professor> buscarProfessorPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }
        return professorRepository.buscarPorNome(nome);
    }

    public Professor buscarProfessorPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return professorRepository.buscarPorEmail(email);
    }

    public List<Aluno> listarAlunos() {
        return alunoRepository.listarTodos();
    }

    public List<Aluno> buscarAlunoPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }
        return alunoRepository.buscarPorNome(nome);
    }

    public Aluno buscarAlunoPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return alunoRepository.buscarPorEmail(email);
    }

    // === MÉTODOS ESPECÍFICOS DE MATRÍCULA ===

    public Codigo matricularAluno(Long alunoId, Long disciplinaId) {
        Aluno aluno = buscarAlunoPorId(alunoId);
        Disciplina disciplina = buscarDisciplinaPorId(disciplinaId);

        if (aluno == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        if (disciplina == null) {
            return Codigo.DISCIPLINA_NAO_ENCONTRADA_404;
        }

        if (verificarLimiteCreditos(alunoId, disciplinaId) == Codigo.LIMITE_CREDITOS_EXCEDIDO_422) {
            return Codigo.LIMITE_CREDITOS_EXCEDIDO_422;
        }

        disciplina.adicionarAluno(aluno);
        return Codigo.MATRICULA_REALIZADA_201;
    }

    public Codigo desmatricularAluno(Long alunoId, Long disciplinaId) {
        Aluno aluno = buscarAlunoPorId(alunoId);
        Disciplina disciplina = buscarDisciplinaPorId(disciplinaId);

        if (aluno == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        if (disciplina == null) {
            return Codigo.DISCIPLINA_NAO_ENCONTRADA_404;
        }

        disciplina.removerAluno(aluno);
        return Codigo.MATRICULA_CANCELADA_200;
    }

    public Codigo verificarLimiteCreditos(Long alunoId, Long disciplinaId) {
        Aluno aluno = buscarAlunoPorId(alunoId);
        Disciplina disciplina = buscarDisciplinaPorId(disciplinaId);

        if (aluno == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        if (disciplina == null) {
            return Codigo.DISCIPLINA_NAO_ENCONTRADA_404;
        }

        int creditosAtuais = aluno.calcularCreditosAtuais();
        int creditosNovos = disciplina.getQuantidadeCreditos();
        int limiteCreditos = Aluno.LIMITE_CREDITOS_POR_SEMESTRE;

        if (creditosAtuais + creditosNovos > limiteCreditos) {
            return Codigo.LIMITE_CREDITOS_EXCEDIDO_422;
        }

        return Codigo.OK_200; 
    }


   
    public List<Aluno> listarAlunosCurso(Curso curso) {
        if (curso == null) {
            return null;
        }
        return alunoRepository.listarAlunosPorCurso(curso);
    }

    public Curso buscarCursoPorId(Long id) {
        if (id == null) {
            return null;
        }
        return cursoRepository.buscarPorId(id);
    }
}
