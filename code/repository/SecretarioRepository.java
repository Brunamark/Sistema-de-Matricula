package repository;

import entity.Secretario;
import entity.Curso;
import enums.Cursos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SecretarioRepository {

    private static final String ARQUIVO_SECRETARIOS =  System.getProperty("user.dir") + "/code/file/secretarios.txt";
    private static final String SEPARADOR = ";";

    public boolean salvar(Secretario secretario) {
        try {

            List<Secretario> secretarios = listarTodos();

            boolean existe = secretarios.removeIf(s -> s.getId().equals(secretario.getId()));

            secretarios.add(secretario);

            salvarTodos(secretarios);

            return true;

        } catch (Exception e) {
            System.err.println("Erro ao salvar secretário: " + e.getMessage());
            return false;
        }
    }

    public Secretario buscarPorId(Long id) {
        if (id == null)
            return null;

        List<Secretario> secretarios = listarTodos();

        return secretarios.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Secretario buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty())
            return null;

        List<Secretario> secretarios = listarTodos();

        return secretarios.stream()
                .filter(s -> s.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public List<Secretario> listarTodos() {
        List<Secretario> secretarios = new ArrayList<>();

        try {

            if (!Files.exists(Paths.get(ARQUIVO_SECRETARIOS))) {
                return secretarios;
            }

            List<String> linhas = Files.readAllLines(Paths.get(ARQUIVO_SECRETARIOS));

            for (String linha : linhas) {
                if (linha.trim().isEmpty())
                    continue;

                Secretario secretario = parsearLinha(linha);
                if (secretario != null) {
                    secretarios.add(secretario);
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao carregar secretários: " + e.getMessage());
        }

        return secretarios;
    }

    public boolean excluir(Long id) {
        if (id == null)
            return false;

        try {
            List<Secretario> secretarios = listarTodos();

            boolean removido = secretarios.removeIf(s -> s.getId().equals(id));

            if (removido) {
                salvarTodos(secretarios);
            }

            return removido;

        } catch (Exception e) {
            System.err.println("Erro ao excluir secretário: " + e.getMessage());
            return false;
        }
    }

    public boolean existeEmail(String email) {
        return buscarPorEmail(email) != null;
    }

    public Long gerarProximoId() {
        List<Secretario> secretarios = listarTodos();

        if (secretarios.isEmpty()) {
            return 1L;
        }

        return secretarios.stream()
                .mapToLong(Secretario::getId)
                .max()
                .orElse(0L) + 1;
    }

    private void salvarTodos(List<Secretario> secretarios) throws IOException {

        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_SECRETARIOS))) {
            for (Secretario secretario : secretarios) {
                writer.println(formatarSecretario(secretario));
            }
        }
    }

    private String formatarSecretario(Secretario secretario) {
        StringBuilder sb = new StringBuilder();

        sb.append(secretario.getId()).append(SEPARADOR);
        sb.append(secretario.getNome()).append(SEPARADOR);
        sb.append(secretario.getEmail()).append(SEPARADOR);
        sb.append(secretario.getSenha()).append(SEPARADOR);

        if (secretario.getCurso() != null) {
            sb.append(secretario.getCurso().getId()).append(SEPARADOR);
            sb.append(secretario.getCurso().getNome().name()).append(SEPARADOR);
            sb.append(secretario.getCurso().getCredito());
        } else {
            sb.append("").append(SEPARADOR);
            sb.append("").append(SEPARADOR);
            sb.append("");
        }

        return sb.toString();
    }

    private Secretario parsearLinha(String linha) {
        try {
            String[] partes = linha.split(SEPARADOR, -1);

            if (partes.length < 4) {
                System.err.println("Linha inválida no arquivo: " + linha);
                return null;
            }

            Long id = Long.parseLong(partes[0]);
            String nome = partes[1];
            String email = partes[2];
            String senha = partes[3];

            Curso curso = null;
            if (partes.length >= 7 && !partes[4].isEmpty()) {
                try {
                    Long cursoId = Long.parseLong(partes[4]);
                    Cursos cursosEnum = Cursos.valueOf(partes[5]);
                    int credito = Integer.parseInt(partes[6]);

                    curso = Curso.builder()
                            .id(cursoId)
                            .nome(cursosEnum)
                            .credito(credito)
                            .build();

                } catch (Exception e) {
                    System.err.println("Erro ao parsear curso na linha: " + linha);
                }
            }

            Secretario secretario = Secretario.builder()
                    .id(id)
                    .nome(nome)
                    .email(email)
                    .senha(senha)
                    .curso(curso)
                    .build();

            return secretario;

        } catch (Exception e) {
            System.err.println("Erro ao parsear linha: " + linha + " - " + e.getMessage());
            return null;
        }
    }

    public void limparArquivo() {
        try {
            if (Files.exists(Paths.get(ARQUIVO_SECRETARIOS))) {
                Files.delete(Paths.get(ARQUIVO_SECRETARIOS));
            }
        } catch (IOException e) {
            System.err.println("Erro ao limpar arquivo: " + e.getMessage());
        }
    }

    public int contarSecretarios() {
        return listarTodos().size();
    }

    public List<Secretario> buscarPorCurso(Long cursoId) {
        if (cursoId == null)
            return new ArrayList<>();

        List<Secretario> secretarios = listarTodos();

        return secretarios.stream()
                .filter(s -> s.getCurso() != null && s.getCurso().getId().equals(cursoId))
                .toList();
    }

    public Secretario buscarPorEmailSenha(String email, String senha) {
        if (email == null || senha == null || email.trim().isEmpty() || senha.trim().isEmpty()) {
            return null;
        }

        List<Secretario> secretarios = listarTodos();
        return secretarios.stream()
                .filter(s -> s.getEmail() != null && s.getSenha() != null)
                .filter(s -> s.getEmail().equalsIgnoreCase(email) && s.getSenha().equalsIgnoreCase(senha))
                .findFirst()
                .orElse(null);
    }

}
