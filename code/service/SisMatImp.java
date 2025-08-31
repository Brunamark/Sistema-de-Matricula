package service;

import java.util.List;
import java.util.stream.Collectors;

import entity.Aluno;
import entity.Curso;
import entity.Disciplina;
import entity.Professor;
import entity.Secretario;
import entity.Usuario;
import enums.Codigo;
import enums.Cursos;
import exceptions.ExceptionHandler;
import exceptions.ResultadoOperacao;
import interfaces.SisMat;

public class SisMatImp implements SisMat {

    private Secretario secretario;
    private Professor professor;
    private Aluno aluno;
    private Disciplina disciplina;
    private Cursos curso;

    @Override
    public ResultadoOperacao criarDisciplina(String nome, int quantidadeCreditos, boolean optativa, boolean ativa,
            Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("criar disciplina", "Você não tem permissão para criar uma nova disciplina");
        }

        Disciplina novaDisciplina = Disciplina.builder()
                .nome(nome)
                .quantidadeCreditos(quantidadeCreditos)
                .optativa(optativa)
                .ativa(ativa)
                .build();

        if (secretario.criarDisciplina(novaDisciplina) != Codigo.CRIADO_201) {
            return handler.erroInterno("Erro ao criar disciplina");
        }

        return handler.sucesso("Disciplina criada com sucesso");
    }

    @Override
    public ResultadoOperacao editarDisciplina(Long id, String nome, int quantidadeCreditos, boolean optativa,
            boolean ativa, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();
        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("editar disciplina", "Você não tem permissão para editar esta disciplina");
        }

        ResultadoOperacao resultadoBusca = buscarDisciplinaPorId(id, idUsuario);
        if (!resultadoBusca.isSucesso()) {
            return handler.disciplinaNaoEncontrada(nome);
        }

        Disciplina disciplinaExistente = (Disciplina) resultadoBusca.getDados();

        Disciplina resultadoCriacao = Disciplina.builder()
                .id(id)
                .nome(nome)
                .quantidadeCreditos(quantidadeCreditos)
                .optativa(optativa)
                .ativa(ativa)
                .cursos(disciplinaExistente.getCursos())
                .alunos(disciplinaExistente.getAlunos())
                .professor(disciplinaExistente.getProfessor())
                .build();

        if (secretario.editarDisciplina(resultadoCriacao) != Codigo.ATUALIZADO_202) {
            return handler.erroInterno("Erro ao editar disciplina");
        }

        return handler.sucesso("Disciplina editada com sucesso");
    }

    @Override
    public ResultadoOperacao excluirDisciplina(String nome, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("excluir disciplina", "Você não tem permissão para excluir esta disciplina");
        }

        ResultadoOperacao resultadoBusca = buscarDisciplinaPorNome(nome, idUsuario);
        if (!resultadoBusca.isSucesso()) {
            return handler.disciplinaNaoEncontrada(nome);
        }

        Disciplina disciplinaExistente = (Disciplina) resultadoBusca.getDados();

        if (secretario.excluirDisciplinaPorNome(disciplinaExistente.getNome()) != Codigo.EXCLUIDO_204) {
            return handler.erroInterno("Erro ao excluir disciplina");
        }

        return handler.sucesso("Disciplina excluída com sucesso");
    }

    @Override
    public ResultadoOperacao buscarDisciplinaPorId(Long id, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("buscar disciplina", "Você não tem permissão para buscar esta disciplina");
        }

        Disciplina disciplina = secretario.buscarDisciplinaPorId(id);
        if (disciplina == null) {
            return handler.disciplinaNaoEncontrada("ID: " + id);
        }

        return handler.sucesso("Disciplina encontrada com sucesso", disciplina);
    }

    @Override
    public ResultadoOperacao buscarDisciplinaPorNome(String nome, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("buscar disciplina", "Você não tem permissão para buscar esta disciplina");
        }

        Disciplina disciplina = secretario.buscarDisciplinaPorNome(nome);
        if (disciplina == null) {
            return handler.disciplinaNaoEncontrada("Nome: " + nome);
        }

        return handler.sucesso("Disciplina encontrada com sucesso", disciplina);
    }

    @Override
    public ResultadoOperacao buscarDisciplinaPorNomeDoCurso(String nomeCurso, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("buscar disciplina", "Você não tem permissão para buscar esta disciplina");
        }

        Disciplina disciplina = secretario.buscarDisciplinaPorNomeDoCurso(nomeCurso);
        if (disciplina == null) {
            return handler.disciplinaNaoEncontrada("Nome do curso: " + nomeCurso);
        }

        return handler.sucesso("Disciplina encontrada com sucesso", disciplina);
    }

    @Override
    public ResultadoOperacao listarDisciplinas(Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("listar disciplinas", "Você não tem permissão para listar as disciplinas");
        }

        List<Disciplina> disciplinas = secretario.listarDisciplinas();
        return handler.sucesso("Disciplinas listadas com sucesso", disciplinas);
    }

    @Override
    public ResultadoOperacao verificarAlunosMatriculadosPorDisciplina(String nomeDisciplina, Long professorId) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarProfessorPorId(professorId, professorId).isSucesso()) {
            return handler.acessoNegado("verificar alunos matriculados",
                    "Você não tem permissão para verificar alunos matriculados nesta disciplina");
        }

        List<Aluno> alunosMatriculados = professor.verificarAlunosMatriculadosPorDisciplina(nomeDisciplina);
        return handler.sucesso("Alunos matriculados encontrados com sucesso", alunosMatriculados);
    }

    @Override
    public ResultadoOperacao criarProfessor(String nome, String email, String senha, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("criar professor", "Você não tem permissão para criar um professor");
        }

        Professor professor = Professor.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .build();

        if (secretario.criarProfessor(professor).isSucesso()) {
            return handler.sucesso("Professor criado com sucesso", professor);
        }
        return handler.erroInterno("Erro ao criar professor");
    }

    @Override
    public ResultadoOperacao editarProfessor(Long id, String nome, String email, String senha, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("editar professor", "Você não tem permissão para editar este professor");
        }

        Professor professor = Professor.builder()
                .id(id)
                .nome(nome)
                .email(email)
                .senha(senha)
                .build();

        if (secretario.editarProfessor(professor).isSucesso()) {
            return handler.sucesso("Professor editado com sucesso", professor);
        }
        return handler.erroInterno("Erro ao editar professor");
    }

    @Override
    public ResultadoOperacao excluirProfessor(Long id, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("excluir professor", "Você não tem permissão para excluir este professor");
        }

        ResultadoOperacao professorBuscado = buscarProfessorPorId(id, idUsuario);
        String email = ((Professor) professorBuscado.getDados()).getEmail();
        if (secretario.excluirProfessorPorEmail(email).isSucesso()) {
            return handler.sucesso("Professor excluído com sucesso", id);
        }
        return handler.erroInterno("Erro ao excluir professor");
    }

    @Override
    public ResultadoOperacao excluirProfessorPorEmail(String email, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("excluir professor", "Você não tem permissão para excluir este professor");
        }

        if (secretario.excluirProfessorPorEmail(email).isSucesso()) {
            return handler.sucesso("Professor excluído com sucesso", email);
        }
        return handler.erroInterno("Erro ao excluir professor");
    }

    @Override
    public ResultadoOperacao buscarProfessorPorId(Long id, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("buscar professor", "Você não tem permissão para buscar este professor");
        }

        Professor professor = secretario.buscarProfessorPorId(id);
        if (professor != null) {
            return handler.sucesso("Professor encontrado com sucesso", professor);
        }
        return handler.erroInterno("Erro ao buscar professor");
    }

    @Override
    public ResultadoOperacao buscarProfessorPorNome(String nome, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("buscar professor", "Você não tem permissão para buscar este professor");
        }

        List<Professor> professores = secretario.buscarProfessorPorNome(nome);
        if (professores != null && !professores.isEmpty()) {
            return handler.sucesso("Professores encontrados com sucesso", professores);
        }
        return handler.erroInterno("Erro ao buscar professor");
    }

    @Override
    public ResultadoOperacao listarProfessores(Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("listar professores", "Você não tem permissão para listar os professores");
        }

        List<Professor> professores = secretario.listarProfessores();
        if (professores == null || professores.isEmpty()) {
            return handler.erroInterno("Nenhum professor encontrado");
        }

        return handler.sucesso("Professores listados com sucesso", professores);
    }

    @Override
    public ResultadoOperacao matricularDisciplinasObrigatorias(List<String> nomesDisciplinas, Long alunoId) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarAlunoPorId(alunoId, alunoId).isSucesso()) {
            return handler.alunoNaoEncontrado("Aluno não encontrado");
        }

        List<Disciplina> disciplinas = nomesDisciplinas.stream()
                .map(nome -> {
                    List<Disciplina> encontradas = secretario.buscarDisciplinasPorNome(nome);
                    if (encontradas == null || encontradas.isEmpty()) {
                        handler.disciplinaNaoEncontrada(nome);
                        return null;
                    }
                    return encontradas.get(0);
                })
                .filter(disciplina -> disciplina != null)
                .collect(Collectors.toList());

        aluno.matricularDisciplinasObrigatorias(disciplinas);
        return handler.sucesso("Disciplinas obrigatórias matriculadas com sucesso", null);
    }

    @Override
    public ResultadoOperacao buscarDisciplinasPorNome(String nome, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("buscar disciplinas", "Você não tem permissão para buscar estas disciplinas");
        }

        List<Disciplina> disciplinas = secretario.buscarDisciplinasPorNome(nome);
        if (disciplinas != null && !disciplinas.isEmpty()) {
            return handler.sucesso("Disciplinas encontradas com sucesso", disciplinas);
        }
        return handler.erroInterno("Erro ao buscar disciplinas");
    }

    @Override
    public ResultadoOperacao matricularDisciplinasOptativas(List<String> nomesDisciplinas, Long alunoId) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarAlunoPorId(alunoId, alunoId).isSucesso()) {
            return handler.alunoNaoEncontrado("Aluno não encontrado");
        }

        List<Disciplina> disciplinas = nomesDisciplinas.stream()
                .map(nome -> {
                    List<Disciplina> encontradas = secretario.buscarDisciplinasPorNome(nome);
                    if (encontradas == null || encontradas.isEmpty()) {
                        handler.disciplinaNaoEncontrada(nome);
                        return null;
                    }
                    return encontradas.get(0);
                })
                .filter(disciplina -> disciplina != null)
                .collect(Collectors.toList());

        if (disciplinas.isEmpty()) {
            return handler.erroInterno("Nenhuma disciplina encontrada");
        }

        aluno.matricularDisciplinasOptativas(disciplinas);
        return handler.sucesso("Disciplinas optativas matriculadas com sucesso", null);
    }

    @Override
    public ResultadoOperacao verificarAtributosDoCurso(Long alunoId) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarAlunoPorId(alunoId, alunoId).isSucesso()) {
            return handler.alunoNaoEncontrado("Aluno não encontrado");
        }

        Curso cursoVerificado = aluno.verificarAtributosDoCurso();
        if (cursoVerificado != null) {
            return handler.sucesso("Atributos do curso verificados com sucesso", cursoVerificado);
        }
        return handler.erroInterno("Erro ao verificar atributos do curso");
    }

    @Override
    public ResultadoOperacao cancelarMatricula(Long alunoId) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarAlunoPorId(alunoId, alunoId).isSucesso()) {
            return handler.alunoNaoEncontrado("Aluno não encontrado");
        }

        aluno.cancelarMatricula();
        return handler.sucesso("Matrícula cancelada com sucesso", null);
    }

    @Override
    public ResultadoOperacao criarAluno(String nome, String email, String senha, Curso curso, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("criar aluno", "Você não tem permissão para criar um aluno");
        }

        Aluno novoAluno = Aluno.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .curso(curso)
                .build();

        if (secretario.criarAluno(novoAluno).isSucesso()) {
            return handler.sucesso("Aluno criado com sucesso", novoAluno);
        }
        return handler.erroInterno("Erro ao criar aluno");
    }

    @Override
    public ResultadoOperacao editarAluno(Long id, String nome, String email, String senha, Curso curso,
            Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarAlunoPorId(id, idUsuario).isSucesso()) {
            return handler.alunoNaoEncontrado("Aluno não encontrado");
        }

        Aluno alunoEditado = Aluno.builder()
                .id(id)
                .nome(nome)
                .email(email)
                .senha(senha)
                .curso(curso)
                .build();

        if (secretario.editarAluno(alunoEditado).isSucesso()) {
            return handler.sucesso("Aluno editado com sucesso", alunoEditado);
        }
        return handler.erroInterno("Erro ao editar aluno");
    }

    @Override
    public ResultadoOperacao excluirAluno(Long id, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();

        if (!buscarAlunoPorId(id, idUsuario).isSucesso()) {
            return handler.alunoNaoEncontrado("Aluno não encontrado");
        }

        if (secretario.excluirAlunoPorId(id).isSucesso()) {
            return handler.sucesso("Aluno excluído com sucesso", null);
        }
        return handler.erroInterno("Erro ao excluir aluno");
    }

    @Override
    public ResultadoOperacao buscarAlunoPorId(Long id, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();
        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("buscar aluno", "Você não tem permissão para buscar um aluno");
        }

        Aluno aluno = secretario.buscarAlunoPorId(id);
        if (aluno != null) {
            return handler.sucesso("Aluno encontrado", aluno);
        }
        return handler.alunoNaoEncontrado("Aluno não encontrado");
    }

    @Override
    public ResultadoOperacao buscarAlunoPorNome(String nome, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();
        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("buscar aluno", "Você não tem permissão para buscar um aluno");
        }

        List<Aluno> alunos = secretario.buscarAlunoPorNome(nome);
        if (alunos != null && !alunos.isEmpty()) {
            return handler.sucesso("Alunos encontrados", alunos);
        }
        return handler.alunoNaoEncontrado("Aluno não encontrado");
    }

    @Override
    public ResultadoOperacao buscarAlunoPorEmail(String email, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();
        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("buscar aluno", "Você não tem permissão para buscar um aluno");
        }

        Aluno aluno = secretario.buscarAlunoPorEmail(email);
        if (aluno != null) {
            return handler.sucesso("Aluno encontrado", aluno);
        }
        return handler.alunoNaoEncontrado("Aluno não encontrado");
    }

    @Override
    public ResultadoOperacao listarAlunos(Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();
        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("listar alunos", "Você não tem permissão para listar alunos");
        }

        List<Aluno> alunos = secretario.listarAlunos();
        if (alunos != null && !alunos.isEmpty()) {
            return handler.sucesso("Alunos encontrados", alunos);
        }
        return handler.alunoNaoEncontrado("Nenhum aluno encontrado");
    }

    @Override
    public ResultadoOperacao buscarCursoPorId(Long id, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();
        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("buscar curso", "Você não tem permissão para buscar um curso");
        }

        Curso curso = secretario.buscarCursoPorId(id);
        if (curso != null) {
            return handler.sucesso("Curso encontrado", curso);
        }
        return handler.erroInterno("Erro ao buscar curso");
    }

    @Override
    public ResultadoOperacao listarAlunosCurso(Long cursoId, Long idUsuario) {
        ExceptionHandler handler = new ExceptionHandler();
        if (!buscarSecretarioPorId(idUsuario).isSucesso()) {
            return handler.acessoNegado("listar alunos do curso", "Você não tem permissão para listar alunos do curso");
        }

        Curso cursoEncontrado = Curso.builder()
                .id(cursoId)
                .build();

        List<Aluno> alunos = secretario.listarAlunosCurso(cursoEncontrado);
        if (alunos != null && !alunos.isEmpty()) {
            return handler.sucesso("Alunos encontrados", alunos);
        }
        return handler.alunoNaoEncontrado("Nenhum aluno encontrado");
    }

    public ResultadoOperacao buscarSecretarioPorId(Long id) {
        ExceptionHandler handler = new ExceptionHandler();
        Usuario usuario = UsuarioUtil.buscarSecretarioPorId(id);
        if (usuario != null && usuario instanceof Secretario) {
            return handler.sucesso("Secretário encontrado", usuario);
        }
        return handler.acessoNegado("ação restrita", "Usuário não autorizado");
    }

    @Override
    public ResultadoOperacao efetuarLogin(String email, String senha) {
        ExceptionHandler handler = new ExceptionHandler();
        if (email == null || email.trim().isEmpty()) {
            return handler.campoObrigatorio("email");
        }

        if (senha == null || senha.trim().isEmpty()) {
            return handler.campoObrigatorio("senha");
        }

        Usuario usuario = UsuarioUtil.buscarUsuarioPorEmail(email);
        if (usuario == null || !usuario.getSenha().equals(senha)) {
            return handler.credenciaisInvalidas();
        }

        Usuario usuarioLogado = usuario.efetuarLogin(email, senha);

        if (usuarioLogado != null) {
            return handler.loginSucesso(usuarioLogado);
        }

        return handler.acessoNegado("login", "Email ou senha incorretos");
    }

    @Override
    public ResultadoOperacao gerarCurriculoSemestral(Long cursoId) {
        ExceptionHandler handler = new ExceptionHandler();
        Curso cursoEncontrado = Curso.builder()
                .id(cursoId)
                .build();

        String curriculo = secretario.gerarCurriculoSemestral(cursoEncontrado);
        if (curriculo != null && !curriculo.isEmpty()) {
            return handler.sucesso("Currículo semestral gerado com sucesso", curriculo);
        }
        return handler.erroInterno("Erro ao gerar currículo semestral");
    }

    @Override
    public ResultadoOperacao buscarCursoPorNome(String nome) {
        ExceptionHandler handler = new ExceptionHandler();

        Curso curso = UsuarioUtil.buscarCursoPorNome(nome);
        if (curso != null) {
            return handler.sucesso("Curso encontrado", curso);
        }
        return handler.erroInterno("Erro ao buscar curso");
    }

}
