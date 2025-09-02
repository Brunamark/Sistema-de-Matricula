package repository;

import entity.Aluno;
import entity.Curso;
import entity.Disciplina;
import enums.Cursos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AlunoRepository {
    
    private static final String ARQUIVO_ALUNOS = "file/alunos.txt";
    private static final String SEPARADOR = ";";
    
    public boolean salvar(Aluno aluno) {
        try {
            criarDiretorioSeNaoExistir();
            
            List<Aluno> alunos = listarTodos();
            
            boolean existe = alunos.removeIf(a -> a.getId().equals(aluno.getId()));
            
            alunos.add(aluno);
            
            salvarTodos(alunos);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Erro ao salvar aluno: " + e.getMessage());
            return false;
        }
    }
    
    public Aluno buscarPorId(Long id) {
        if (id == null) return null;
        
        List<Aluno> alunos = listarTodos();
        
        return alunos.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public Aluno buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) return null;
        
        List<Aluno> alunos = listarTodos();
        
        return alunos.stream()
                .filter(a -> a.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
    
    public List<Aluno> listarTodos() {
        List<Aluno> alunos = new ArrayList<>();
        
        try {
            if (!Files.exists(Paths.get(ARQUIVO_ALUNOS))) {
                return alunos;
            }
            
            List<String> linhas = Files.readAllLines(Paths.get(ARQUIVO_ALUNOS));
            
            for (String linha : linhas) {
                if (linha.trim().isEmpty()) continue;
                
                Aluno aluno = parsearLinha(linha);
                if (aluno != null) {
                    alunos.add(aluno);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar alunos: " + e.getMessage());
        }
        
        return alunos;
    }
    
    public boolean excluir(Long id) {
        if (id == null) return false;
        
        try {
            List<Aluno> alunos = listarTodos();
            
            boolean removido = alunos.removeIf(a -> a.getId().equals(id));
            
            if (removido) {
                salvarTodos(alunos);
            }
            
            return removido;
            
        } catch (Exception e) {
            System.err.println("Erro ao excluir aluno: " + e.getMessage());
            return false;
        }
    }
    
    public boolean existeEmail(String email) {
        return buscarPorEmail(email) != null;
    }
    
    public Long gerarProximoId() {
        List<Aluno> alunos = listarTodos();
        
        if (alunos.isEmpty()) {
            return 1L;
        }
        
        return alunos.stream()
                .mapToLong(Aluno::getId)
                .max()
                .orElse(0L) + 1;
    }
    
    public List<Aluno> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) return new ArrayList<>();
        
        List<Aluno> alunos = listarTodos();
        
        return alunos.stream()
                .filter(a -> a.getNome() != null && 
                           a.getNome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
    }
    
    public List<Aluno> buscarPorCurso(Long cursoId) {
        if (cursoId == null) return new ArrayList<>();
        
        List<Aluno> alunos = listarTodos();
        
        return alunos.stream()
                .filter(a -> a.getCurso() != null && a.getCurso().getId().equals(cursoId))
                .toList();
    }
    
    public List<Aluno> buscarPorDisciplina(Long disciplinaId) {
        if (disciplinaId == null) return new ArrayList<>();
        
        List<Aluno> alunos = listarTodos();
        
        return alunos.stream()
                .filter(a -> a.getDisciplinas() != null && 
                           a.getDisciplinas().stream()
                            .anyMatch(d -> d.getId().equals(disciplinaId)))
                .toList();
    }
    
    private void salvarTodos(List<Aluno> alunos) throws IOException {
        criarDiretorioSeNaoExistir();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_ALUNOS))) {
            for (Aluno aluno : alunos) {
                writer.println(formatarAluno(aluno));
            }
        }
    }
    
    private void criarDiretorioSeNaoExistir() {
        try {
            Files.createDirectories(Paths.get("file"));
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório file: " + e.getMessage());
        }
    }
    
    private String formatarAluno(Aluno aluno) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(aluno.getId()).append(SEPARADOR);
        sb.append(aluno.getNome()).append(SEPARADOR);
        sb.append(aluno.getEmail()).append(SEPARADOR);
        sb.append(aluno.getSenha()).append(SEPARADOR);
        
        // Curso
        if (aluno.getCurso() != null) {
            sb.append(aluno.getCurso().getId()).append(SEPARADOR);
            sb.append(aluno.getCurso().getNome().name()).append(SEPARADOR);
            sb.append(aluno.getCurso().getCredito()).append(SEPARADOR);
        } else {
            sb.append("").append(SEPARADOR);
            sb.append("").append(SEPARADOR);
            sb.append("").append(SEPARADOR);
        }
        
        // Disciplinas (IDs separados por vírgula)
        if (aluno.getDisciplinas() != null && !aluno.getDisciplinas().isEmpty()) {
            StringBuilder disciplinasIds = new StringBuilder();
            for (int i = 0; i < aluno.getDisciplinas().size(); i++) {
                if (i > 0) disciplinasIds.append(",");
                disciplinasIds.append(aluno.getDisciplinas().get(i).getId());
            }
            sb.append(disciplinasIds.toString());
        } else {
            sb.append("");
        }
        
        return sb.toString();
    }
    
    private Aluno parsearLinha(String linha) {
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
            
            // Curso
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
            
            // Disciplinas
            List<Disciplina> disciplinas = new ArrayList<>();
            if (partes.length >= 8 && !partes[7].isEmpty()) {
                try {
                    String[] disciplinasIds = partes[7].split(",");
                    for (String disciplinaIdStr : disciplinasIds) {
                        Long disciplinaId = Long.parseLong(disciplinaIdStr.trim());
                        // TODO: Buscar disciplina no DisciplinaRepository quando estiver pronto
                        // Por enquanto, criar um objeto básico
                        Disciplina disciplina = Disciplina.builder()
                                .id(disciplinaId)
                                .nome("Disciplina " + disciplinaId)
                                .quantidadeCreditos(4)
                                .optativa(false)
                                .ativa(true)
                                .build();
                        disciplinas.add(disciplina);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao parsear disciplinas na linha: " + linha);
                }
            }
            
            Aluno aluno = Aluno.builder()
                    .id(id)
                    .nome(nome)
                    .email(email)
                    .senha(senha)
                    .curso(curso)
                    .disciplinas(disciplinas)
                    .build();
                    
            return aluno;
            
        } catch (Exception e) {
            System.err.println("Erro ao parsear linha: " + linha + " - " + e.getMessage());
            return null;
        }
    }
    
    public void limparArquivo() {
        try {
            if (Files.exists(Paths.get(ARQUIVO_ALUNOS))) {
                Files.delete(Paths.get(ARQUIVO_ALUNOS));
            }
        } catch (IOException e) {
            System.err.println("Erro ao limpar arquivo: " + e.getMessage());
        }
    }
    
    public int contarAlunos() {
        return listarTodos().size();
    }
    
    public int contarAlunosPorCurso(Long cursoId) {
        return buscarPorCurso(cursoId).size();
    }
    
    public int contarAlunosPorDisciplina(Long disciplinaId) {
        return buscarPorDisciplina(disciplinaId).size();
    }
    
    public List<Aluno> buscarAlunosMatriculadosEmDisciplina(Long disciplinaId) {
        return buscarPorDisciplina(disciplinaId);
    }
    
    public boolean estaMatriculadoEmDisciplina(Long alunoId, Long disciplinaId) {
        if (alunoId == null || disciplinaId == null) return false;
        
        Aluno aluno = buscarPorId(alunoId);
        if (aluno == null || aluno.getDisciplinas() == null) return false;
        
        return aluno.getDisciplinas().stream()
                .anyMatch(d -> d.getId().equals(disciplinaId));
    }
    
    public boolean adicionarDisciplina(Long alunoId, Disciplina disciplina) {
        if (alunoId == null || disciplina == null) return false;
        
        Aluno aluno = buscarPorId(alunoId);
        if (aluno == null) return false;
        
        if (aluno.getDisciplinas() == null) {
            aluno.setDisciplinas(new ArrayList<>());
        }
        
        // Verificar se já está matriculado
        if (estaMatriculadoEmDisciplina(alunoId, disciplina.getId())) {
            return false;
        }
        
        aluno.getDisciplinas().add(disciplina);
        return salvar(aluno);
    }
    
    public boolean removerDisciplina(Long alunoId, Long disciplinaId) {
        if (alunoId == null || disciplinaId == null) return false;
        
        Aluno aluno = buscarPorId(alunoId);
        if (aluno == null || aluno.getDisciplinas() == null) return false;
        
        boolean removido = aluno.getDisciplinas().removeIf(d -> d.getId().equals(disciplinaId));
        
        if (removido) {
            return salvar(aluno);
        }
        
        return false;
    }
    
    public List<Aluno> listarAlunosPorCurso(Long cursoId) {
        return buscarPorCurso(cursoId);
    }

    public List<Aluno> listarAlunosPorCurso(Curso curso) {
        if (curso == null) return new ArrayList<>();
        return buscarPorCurso(curso.getId());
    }

    public List<Aluno> listarAlunosPorNomeCurso(String nomeCurso) {
        if (nomeCurso == null || nomeCurso.trim().isEmpty()) return new ArrayList<>();
        
        List<Aluno> alunos = listarTodos();
        
        return alunos.stream()
                .filter(a -> a.getCurso() != null && 
                           a.getCurso().getNome() != null &&
                           a.getCurso().getNome().getNomeCurso().equalsIgnoreCase(nomeCurso))
                .toList();
    }

    public List<Aluno> listarAlunosPorCursoEnum(Cursos cursoEnum) {
        if (cursoEnum == null) return new ArrayList<>();
        
        List<Aluno> alunos = listarTodos();
        
        return alunos.stream()
                .filter(a -> a.getCurso() != null && 
                           a.getCurso().getNome() != null &&
                           a.getCurso().getNome().equals(cursoEnum))
                .toList();
    }
    
    public boolean cancelarMatricula(Long alunoId) {
        if (alunoId == null) return false;
        
        Aluno aluno = buscarPorId(alunoId);
        if (aluno == null) return false;
        
        // Verificar se tem disciplinas para cancelar
        if (aluno.getDisciplinas() == null || aluno.getDisciplinas().isEmpty()) {
            return false;
        }
        
        // Limpar todas as disciplinas do aluno
        aluno.getDisciplinas().clear();
        
        // Recalcular créditos se o método existir
        if (aluno instanceof Aluno) {
            aluno.recalcularCreditos();
        }
        
        // Salvar aluno atualizado
        return salvar(aluno);
    }

    public boolean cancelarMatriculaDisciplina(Long alunoId, Long disciplinaId) {
        if (alunoId == null || disciplinaId == null) return false;
        
        Aluno aluno = buscarPorId(alunoId);
        if (aluno == null || aluno.getDisciplinas() == null) return false;
        
        // Verificar se está matriculado na disciplina
        if (!estaMatriculadoEmDisciplina(alunoId, disciplinaId)) {
            return false;
        }
        
        // Remover disciplina específica
        boolean removido = aluno.getDisciplinas().removeIf(d -> d.getId().equals(disciplinaId));
        
        if (removido) {
            // Recalcular créditos
            aluno.recalcularCreditos();
            
            // Salvar aluno atualizado
            return salvar(aluno);
        }
        
        return false;
    }

    public boolean cancelarMatriculaMultiplas(Long alunoId, List<Long> disciplinasIds) {
        if (alunoId == null || disciplinasIds == null || disciplinasIds.isEmpty()) {
            return false;
        }
        
        Aluno aluno = buscarPorId(alunoId);
        if (aluno == null || aluno.getDisciplinas() == null) return false;
        
        int canceladas = 0;
        
        for (Long disciplinaId : disciplinasIds) {
            if (cancelarMatriculaDisciplina(alunoId, disciplinaId)) {
                canceladas++;
            }
        }
        
        return canceladas > 0;
    }

    public int contarMatriculas(Long alunoId) {
        if (alunoId == null) return 0;
        
        Aluno aluno = buscarPorId(alunoId);
        if (aluno == null || aluno.getDisciplinas() == null) return 0;
        
        return aluno.getDisciplinas().size();
    }

    public List<Disciplina> listarDisciplinasMatriculadas(Long alunoId) {
        if (alunoId == null) return new ArrayList<>();
        
        Aluno aluno = buscarPorId(alunoId);
        if (aluno == null || aluno.getDisciplinas() == null) return new ArrayList<>();
        
        return new ArrayList<>(aluno.getDisciplinas());
    }

    public boolean temMatriculas(Long alunoId) {
        return contarMatriculas(alunoId) > 0;
    }

    public boolean podeMatricular(Long alunoId, Disciplina disciplina) {
        if (alunoId == null || disciplina == null) return false;
        
        Aluno aluno = buscarPorId(alunoId);
        if (aluno == null) return false;
        
        // Verificar se já está matriculado
        if (estaMatriculadoEmDisciplina(alunoId, disciplina.getId())) {
            return false;
        }
        
        // Usar método do aluno se disponível
        return aluno.podeMatricular(disciplina);
    }

    public boolean matricularAluno(Long alunoId, Disciplina disciplina) {
        if (!podeMatricular(alunoId, disciplina)) {
            return false;
        }
        
        return adicionarDisciplina(alunoId, disciplina);
    }

    public boolean transferirAluno(Long alunoId, Long novoCursoId) {
        if (alunoId == null || novoCursoId == null) return false;
        
        Aluno aluno = buscarPorId(alunoId);
        if (aluno == null) return false;
        
        // Cancelar todas as matrículas atuais
        boolean cancelou = cancelarMatricula(alunoId);
        if (!cancelou && temMatriculas(alunoId)) {
            return false; // Se tinha matrículas e não conseguiu cancelar
        }
        
        // TODO: Buscar novo curso no CursoRepository quando estiver disponível
        // Por enquanto, criar curso básico
        Curso novoCurso = Curso.builder()
                .id(novoCursoId)
                .nome(Cursos.ENGENHARIA_DE_SOFTWARE) // Valor padrão
                .credito(240)
                .build();
        
        aluno.setCurso(novoCurso);
        
        return salvar(aluno);
    }
}