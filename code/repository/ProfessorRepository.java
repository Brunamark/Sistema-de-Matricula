package repository;

import entity.Professor;
import entity.Disciplina;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProfessorRepository {

    private static final String ARQUIVO_PROFESSORES = System.getProperty("user.dir") + "/codefile/professores.txt";
    private static final String SEPARADOR = ";";

    public boolean salvar(Professor professor) {
        try {
            criarDiretorioSeNaoExistir();

            List<Professor> professores = listarTodos();

            boolean existe = professores.removeIf(p -> p.getId().equals(professor.getId()));

            professores.add(professor);

            salvarTodos(professores);

            return true;

        } catch (Exception e) {
            System.err.println("Erro ao salvar professor: " + e.getMessage());
            return false;
        }
    }

    public Professor buscarPorId(Long id) {
        if (id == null)
            return null;

        List<Professor> professores = listarTodos();

        return professores.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Professor buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty())
            return null;

        List<Professor> professores = listarTodos();

        return professores.stream()
                .filter(p -> p.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public List<Professor> listarTodos() {
        List<Professor> professores = new ArrayList<>();

        try {
            if (!Files.exists(Paths.get(ARQUIVO_PROFESSORES))) {
                return professores;
            }

            List<String> linhas = Files.readAllLines(Paths.get(ARQUIVO_PROFESSORES));

            for (String linha : linhas) {
                if (linha.trim().isEmpty())
                    continue;

                Professor professor = parsearLinha(linha);
                if (professor != null) {
                    professores.add(professor);
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao carregar professores: " + e.getMessage());
        }

        return professores;
    }

    public boolean excluir(Long id) {
        if (id == null)
            return false;

        try {
            List<Professor> professores = listarTodos();

            boolean removido = professores.removeIf(p -> p.getId().equals(id));

            if (removido) {
                salvarTodos(professores);
            }

            return removido;

        } catch (Exception e) {
            System.err.println("Erro ao excluir professor: " + e.getMessage());
            return false;
        }
    }

    public boolean existeEmail(String email) {
        return buscarPorEmail(email) != null;
    }

    public Long gerarProximoId() {
        List<Professor> professores = listarTodos();

        if (professores.isEmpty()) {
            return 1L;
        }

        return professores.stream()
                .mapToLong(Professor::getId)
                .max()
                .orElse(0L) + 1;
    }

    public List<Professor> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty())
            return new ArrayList<>();

        List<Professor> professores = listarTodos();

        return professores.stream()
                .filter(p -> p.getNome() != null &&
                        p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
    }

    public List<Professor> buscarPorDisciplina(Long disciplinaId) {
        if (disciplinaId == null)
            return new ArrayList<>();

        List<Professor> professores = listarTodos();

        return professores.stream()
                .filter(p -> p.getDisciplina() != null &&
                        p.getDisciplina().getId().equals(disciplinaId))
                .toList();
    }

    public Professor buscarProfessorDisciplina(Long disciplinaId) {
        if (disciplinaId == null)
            return null;

        List<Professor> professores = listarTodos();

        return professores.stream()
                .filter(p -> p.getDisciplina() != null &&
                        p.getDisciplina().getId().equals(disciplinaId))
                .findFirst()
                .orElse(null);
    }

    private void salvarTodos(List<Professor> professores) throws IOException {
        criarDiretorioSeNaoExistir();

        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PROFESSORES))) {
            for (Professor professor : professores) {
                writer.println(formatarProfessor(professor));
            }
        }
    }

    private void criarDiretorioSeNaoExistir() {
        try {
            Files.createDirectories(Paths.get("src"));
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório src: " + e.getMessage());
        }
    }

    private String formatarProfessor(Professor professor) {
        StringBuilder sb = new StringBuilder();

        sb.append(professor.getId()).append(SEPARADOR);
        sb.append(professor.getNome()).append(SEPARADOR);
        sb.append(professor.getEmail()).append(SEPARADOR);
        sb.append(professor.getSenha()).append(SEPARADOR);

        // Disciplina (apenas um ID)
        if (professor.getDisciplina() != null) {
            sb.append(professor.getDisciplina().getId());
        } else {
            sb.append("");
        }

        return sb.toString();
    }

    private Professor parsearLinha(String linha) {
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

            // Disciplina (apenas uma)
            Disciplina disciplina = null;
            if (partes.length >= 5 && !partes[4].isEmpty()) {
                try {
                    Long disciplinaId = Long.parseLong(partes[4].trim());
                    // TODO: Buscar disciplina no DisciplinaRepository quando estiver pronto
                    // Por enquanto, criar um objeto básico
                    disciplina = Disciplina.builder()
                            .id(disciplinaId)
                            .nome("Disciplina " + disciplinaId)
                            .quantidadeCreditos(4)
                            .optativa(false)
                            .ativa(true)
                            .build();
                } catch (Exception e) {
                    System.err.println("Erro ao parsear disciplina na linha: " + linha);
                }
            }

            Professor professor = Professor.builder()
                    .id(id)
                    .nome(nome)
                    .email(email)
                    .senha(senha)
                    .disciplina(disciplina)
                    .build();

            return professor;

        } catch (Exception e) {
            System.err.println("Erro ao parsear linha: " + linha + " - " + e.getMessage());
            return null;
        }
    }

    public void limparArquivo() {
        try {
            if (Files.exists(Paths.get(ARQUIVO_PROFESSORES))) {
                Files.delete(Paths.get(ARQUIVO_PROFESSORES));
            }
        } catch (IOException e) {
            System.err.println("Erro ao limpar arquivo: " + e.getMessage());
        }
    }

    public int contarProfessores() {
        return listarTodos().size();
    }

    public int contarProfessoresPorDisciplina(Long disciplinaId) {
        return buscarPorDisciplina(disciplinaId).size();
    }

    public boolean lecionaDisciplina(Long professorId, Long disciplinaId) {
        if (professorId == null || disciplinaId == null)
            return false;

        Professor professor = buscarPorId(professorId);
        if (professor == null || professor.getDisciplina() == null)
            return false;

        return professor.getDisciplina().getId().equals(disciplinaId);
    }

    public boolean atribuirDisciplina(Long professorId, Disciplina disciplina) {
        if (professorId == null || disciplina == null)
            return false;

        Professor professor = buscarPorId(professorId);
        if (professor == null)
            return false;

        // Verificar se já leciona uma disciplina
        if (professor.getDisciplina() != null) {
            return false; // Professor já tem uma disciplina
        }

        professor.setDisciplina(disciplina);
        return salvar(professor);
    }

    public boolean removerDisciplina(Long professorId) {
        if (professorId == null)
            return false;

        Professor professor = buscarPorId(professorId);
        if (professor == null || professor.getDisciplina() == null)
            return false;

        professor.setDisciplina(null);
        return salvar(professor);
    }

    public List<Professor> buscarProfessoresDisponiveis() {
        List<Professor> professores = listarTodos();

        return professores.stream()
                .filter(p -> p.getDisciplina() == null)
                .toList();
    }

    public List<Professor> buscarProfessoresComDisciplina() {
        List<Professor> professores = listarTodos();

        return professores.stream()
                .filter(p -> p.getDisciplina() != null)
                .toList();
    }

    public boolean temDisciplina(Long professorId) {
        Professor professor = buscarPorId(professorId);
        return professor != null && professor.getDisciplina() != null;
    }

    public boolean podeReceberDisciplina(Long professorId) {
        Professor professor = buscarPorId(professorId);
        return professor != null && professor.getDisciplina() == null;
    }

    public Disciplina getDisciplinaProfessor(Long professorId) {
        Professor professor = buscarPorId(professorId);
        if (professor == null) {
            return null;
        }
        return professor.getDisciplina();
    }

    public boolean substituirDisciplina(Long professorId, Disciplina novaDisciplina) {
        if (professorId == null || novaDisciplina == null)
            return false;

        Professor professor = buscarPorId(professorId);
        if (professor == null)
            return false;

        professor.setDisciplina(novaDisciplina);
        return salvar(professor);
    }

    public Professor buscarPorEmailSenha(String email, String senha) {
        if (email == null || senha == null || email.trim().isEmpty() || senha.trim().isEmpty()) {
            return null;
        }

        List<Professor> professores = listarTodos();

        return professores.stream()
                .filter(p -> p.getEmail() != null && p.getSenha() != null)
                .filter(p -> p.getEmail().equalsIgnoreCase(email) && p.getSenha().equals(senha))
                .findFirst()
                .orElse(null);
    }

}
