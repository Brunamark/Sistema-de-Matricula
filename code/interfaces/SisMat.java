package interfaces;

import exceptions.ResultadoOperacao;

import java.util.List;

import entity.Curso;


public interface SisMat {
    // === DISCIPLINA ===
    ResultadoOperacao criarDisciplina(String nome, int quantidadeCreditos, boolean optativa, boolean ativa, Long idUsuario);
    ResultadoOperacao editarDisciplina(Long id, String nome, int quantidadeCreditos, boolean optativa, boolean ativa, Long idUsuario);
    ResultadoOperacao excluirDisciplina(String nome, Long idUsuario);
    ResultadoOperacao buscarDisciplinaPorId(Long id, Long idUsuario);
    ResultadoOperacao buscarDisciplinaPorNome(String nome, Long idUsuario);
    ResultadoOperacao buscarDisciplinaPorNomeDoCurso(String nomeCurso, Long idUsuario);
    ResultadoOperacao listarDisciplinas(Long idUsuario);
    ResultadoOperacao buscarDisciplinasPorNome(String nome, Long idUsuario);

    // === PROFESSOR ===
    ResultadoOperacao verificarAlunosMatriculadosPorDisciplina(String nomeDisciplina, Long professorId);
    ResultadoOperacao criarProfessor(String nome, String email, String senha, Long idUsuario);
    ResultadoOperacao editarProfessor(Long id, String nome, String email, String senha, Long idUsuario);
    ResultadoOperacao excluirProfessor(Long id, Long idUsuario);
    ResultadoOperacao excluirProfessorPorEmail(String email, Long idUsuario);
    ResultadoOperacao buscarProfessorPorId(Long id, Long idUsuario);
    ResultadoOperacao buscarProfessorPorNome(String nome, Long idUsuario);
    ResultadoOperacao listarProfessores(Long idUsuario);

    // === ALUNO ===
    ResultadoOperacao matricularDisciplinasObrigatorias(List<String> nomesDisciplinas, Long alunoId);
    ResultadoOperacao matricularDisciplinasOptativas(List<String> nomesDisciplinas, Long alunoId);
    ResultadoOperacao verificarAtributosDoCurso(Long alunoId);
    ResultadoOperacao cancelarMatricula(Long alunoId);
    ResultadoOperacao criarAluno(String nome, String email, String senha, Curso curso, Long idUsuario);
    ResultadoOperacao editarAluno(Long id, String nome, String email, String senha, Curso curso, Long idUsuario);
    ResultadoOperacao excluirAluno(Long id, Long idUsuario);
    ResultadoOperacao buscarAlunoPorId(Long id, Long idUsuario);
    ResultadoOperacao buscarAlunoPorNome(String nome, Long idUsuario);
    ResultadoOperacao buscarAlunoPorEmail(String email, Long idUsuario);
    ResultadoOperacao listarAlunos(Long idUsuario);

    // === CURSO ===
    ResultadoOperacao buscarCursoPorId(Long id, Long idUsuario);
    ResultadoOperacao listarAlunosCurso(Long cursoId, Long idUsuario);

    // === SECRETÁRIO ===
    ResultadoOperacao buscarSecretarioPorId(Long id);
 

    // === USUÁRIO (Operações genéricas baseadas na herança) ===
    ResultadoOperacao efetuarLogin(String email, String senha);

    // === RELATÓRIOS E CONSULTAS ===
    ResultadoOperacao gerarCurriculoSemestral(Long cursoId);
}

