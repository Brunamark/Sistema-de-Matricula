package service;

import entity.Aluno;
import entity.Professor;
import entity.Secretario;
import entity.Usuario;

public class UsuarioUtil {

    private static java.util.List<Secretario> secretarios = new java.util.ArrayList<>();
    private static java.util.List<Professor> professores = new java.util.ArrayList<>();
    private static java.util.List<Aluno> alunos = new java.util.ArrayList<>();



    public static Usuario efetuarLogin(String email, String senha) {
        if (email == null || senha == null) {
            return null;
        }
        // TODO: Implementar com repository
        return null;
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
    
    public static Usuario buscarUsuarioPorEmail(String email) {
       return null; // TODO: Implementar com repository
    }
    
    public static Secretario buscarSecretarioPorId(Long id) {
       return null; // TODO: Implementar com repository
    }
    

}
