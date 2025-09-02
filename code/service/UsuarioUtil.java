package service;

import entity.Aluno;
import entity.Curso;
import entity.Professor;
import entity.Secretario;
import entity.Usuario;
import repository.SecretarioRepository;
import repository.ProfessorRepository;
import repository.AlunoRepository;

public class UsuarioUtil {

    private static SecretarioRepository secretarioRepository = new SecretarioRepository();
    private static ProfessorRepository professorRepository = new ProfessorRepository();
    private static AlunoRepository alunoRepository = new AlunoRepository();

    public static Usuario efetuarLogin(String email, String senha) {
        if (email == null || senha == null || email.trim().isEmpty() || senha.trim().isEmpty()) {
            return null;
        }

        Secretario secretario = secretarioRepository.buscarPorEmailSenha(email, senha);
        if (secretario != null) {
            return secretario;
        } else {
        }

        Aluno aluno = alunoRepository.buscarPorEmailSenha(email, senha);
        if (aluno != null) {
            return aluno;
        } 

        Professor professor = professorRepository.buscarPorEmailSenha(email, senha);
        if (professor != null) {
            return professor;
        } 

        return null;
    }

    public static boolean validarCredenciais(String email, String senha) {
        return efetuarLogin(email, senha) != null;
    }

    public static Usuario buscarUsuarioPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        // Buscar em todos os repositories
        Secretario secretario = secretarioRepository.buscarPorEmail(email);
        if (secretario != null)
            return secretario;

        Aluno aluno = alunoRepository.buscarPorEmail(email);
        if (aluno != null)
            return aluno;

        Professor professor = professorRepository.buscarPorEmail(email);
        if (professor != null)
            return professor;

        return null;
    }

    public static boolean emailJaExiste(String email) {
        return buscarUsuarioPorEmail(email) != null;
    }

    public static boolean alterarSenha(String email, String senhaAtual, String novaSenha) {
        Usuario usuario = efetuarLogin(email, senhaAtual);
        if (usuario == null) {
            return false; // Credenciais inválidas
        }

        usuario.setSenha(novaSenha);

        // Salvar no repository correspondente
        if (usuario instanceof Secretario) {
            return secretarioRepository.salvar((Secretario) usuario);
        } else if (usuario instanceof Aluno) {
            return alunoRepository.salvar((Aluno) usuario);
        } else if (usuario instanceof Professor) {
            return professorRepository.salvar((Professor) usuario);
        }

        return false;
    }

    public static Usuario buscarUsuarioPorId(Long id) {
        if (id == null) {
            return null;
        }

        Secretario secretario = secretarioRepository.buscarPorId(id);
        if (secretario != null) {
            return secretario;
        }

        Professor professor = professorRepository.buscarPorId(id);
        if (professor != null) {
            return professor;
        }

        Aluno aluno = alunoRepository.buscarPorId(id);
        if (aluno != null) {
            return aluno;
        }

        return null;
    }

    public static Secretario buscarSecretarioPorId(Long id) {
        return secretarioRepository.buscarPorId(id);
    }

    public static Professor buscarProfessorPorId(Long id) {
        return professorRepository.buscarPorId(id);
    }

    public static Aluno buscarAlunoPorId(Long id) {
        return alunoRepository.buscarPorId(id);
    }

    public static Secretario buscarSecretarioPorEmail(String email) {
        return secretarioRepository.buscarPorEmail(email);
    }

    public static Professor buscarProfessorPorEmail(String email) {
        return professorRepository.buscarPorEmail(email);
    }

    public static Aluno buscarAlunoPorEmail(String email) {
        return alunoRepository.buscarPorEmail(email);
    }

    public static boolean isSecretario(Usuario usuario) {
        return usuario instanceof Secretario;
    }

    public static boolean isProfessor(Usuario usuario) {
        return usuario instanceof Professor;
    }

    public static boolean isAluno(Usuario usuario) {
        return usuario instanceof Aluno;
    }

    public static boolean existeEmail(String email) {
        return buscarUsuarioPorEmail(email) != null;
    }

    public static Long gerarProximoIdUsuario() {
        Long maxIdSecretario = secretarioRepository.listarTodos().stream()
                .mapToLong(Secretario::getId)
                .max()
                .orElse(0L);

        Long maxIdProfessor = professorRepository.listarTodos().stream()
                .mapToLong(Professor::getId)
                .max()
                .orElse(0L);

        Long maxIdAluno = alunoRepository.listarTodos().stream()
                .mapToLong(Aluno::getId)
                .max()
                .orElse(0L);

        return Math.max(Math.max(maxIdSecretario, maxIdProfessor), maxIdAluno) + 1;
    }

    public static String getTipoUsuario(Usuario usuario) {
        if (usuario instanceof Secretario) {
            return "Secretário";
        } else if (usuario instanceof Professor) {
            return "Professor";
        } else if (usuario instanceof Aluno) {
            return "Aluno";
        }
        return "Desconhecido";
    }

    public static Curso buscarCursoPorNome(String nome) {
        return null;
    }

}
