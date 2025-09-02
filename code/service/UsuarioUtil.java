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
        if (email == null || senha == null) {
            return null;
        }
        
        // Buscar usuário por email em todos os repositories
        Usuario usuario = buscarUsuarioPorEmail(email);
        
        if (usuario == null) {
            return null;
        }
        
        // Validar credenciais usando o método da classe Usuario
        Usuario usuarioLogado = usuario.efetuarLogin(email, senha);
        
        return usuarioLogado;
    }

    public static Usuario buscarUsuarioPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        
        Secretario secretario = secretarioRepository.buscarPorEmail(email);
        if (secretario != null) {
            return secretario;
        }
        
        Professor professor = professorRepository.buscarPorEmail(email);
        if (professor != null) {
            return professor;
        }
        
        Aluno aluno = alunoRepository.buscarPorEmail(email);
        if (aluno != null) {
            return aluno;
        }
        
        return null; 
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
