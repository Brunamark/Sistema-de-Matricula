package repository;

import entity.Curso;
import entity.Disciplina;
import entity.Aluno;
import enums.Cursos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CursoRepository {
    
    private static final String ARQUIVO_CURSOS = "file/cursos.txt";
    private static final String SEPARADOR = ";";
    
    public boolean salvar(Curso curso) {
        try {
            criarDiretorioSeNaoExistir();
            
            List<Curso> cursos = listarTodos();
            
            boolean existe = cursos.removeIf(c -> c.getId().equals(curso.getId()));
            
            cursos.add(curso);
            
            salvarTodos(cursos);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Erro ao salvar curso: " + e.getMessage());
            return false;
        }
    }
    
    public Curso buscarPorId(Long id) {
        if (id == null) return null;
        
        List<Curso> cursos = listarTodos();
        
        return cursos.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public Curso buscarPorNome(Cursos nome) {
        if (nome == null) return null;
        
        List<Curso> cursos = listarTodos();
        
        return cursos.stream()
                .filter(c -> c.getNome() != null && c.getNome().equals(nome))
                .findFirst()
                .orElse(null);
    }
    
    public Curso buscarPorNomeString(String nome) {
        if (nome == null || nome.trim().isEmpty()) return null;
        
        try {
            Cursos cursosEnum = Cursos.valueOf(nome.toUpperCase());
            return buscarPorNome(cursosEnum);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    public List<Curso> listarTodos() {
        List<Curso> cursos = new ArrayList<>();
        
        try {
            if (!Files.exists(Paths.get(ARQUIVO_CURSOS))) {
                return cursos;
            }
            
            List<String> linhas = Files.readAllLines(Paths.get(ARQUIVO_CURSOS));
            
            for (String linha : linhas) {
                if (linha.trim().isEmpty()) continue;
                
                Curso curso = parsearLinha(linha);
                if (curso != null) {
                    cursos.add(curso);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar cursos: " + e.getMessage());
        }
        
        return cursos;
    }
    
    public boolean excluir(Long id) {
        if (id == null) return false;
        
        try {
            List<Curso> cursos = listarTodos();
            
            boolean removido = cursos.removeIf(c -> c.getId().equals(id));
            
            if (removido) {
                salvarTodos(cursos);
            }
            
            return removido;
            
        } catch (Exception e) {
            System.err.println("Erro ao excluir curso: " + e.getMessage());
            return false;
        }
    }
    
    public boolean existeNome(Cursos nome) {
        return buscarPorNome(nome) != null;
    }
    
    public Long gerarProximoId() {
        List<Curso> cursos = listarTodos();
        
        if (cursos.isEmpty()) {
            return 1L;
        }
        
        return cursos.stream()
                .mapToLong(Curso::getId)
                .max()
                .orElse(0L) + 1;
    }
    
    public List<Curso> buscarPorCredito(int credito) {
        List<Curso> cursos = listarTodos();
        
        return cursos.stream()
                .filter(c -> c.getCredito() == credito)
                .toList();
    }
    
    public List<Curso> buscarPorCreditoMinimo(int creditoMinimo) {
        List<Curso> cursos = listarTodos();
        
        return cursos.stream()
                .filter(c -> c.getCredito() >= creditoMinimo)
                .toList();
    }
    
    public List<Curso> buscarPorCreditoMaximo(int creditoMaximo) {
        List<Curso> cursos = listarTodos();
        
        return cursos.stream()
                .filter(c -> c.getCredito() <= creditoMaximo)
                .toList();
    }
    
    public List<Curso> buscarPorDisciplina(Long disciplinaId) {
        if (disciplinaId == null) return new ArrayList<>();
        
        List<Curso> cursos = listarTodos();
        
        return cursos.stream()
                .filter(c -> c.getDisciplinas() != null && 
                           c.getDisciplinas().stream().anyMatch(d -> d.getId().equals(disciplinaId)))
                .toList();
    }
    
    public List<Curso> buscarComAlunos() {
        List<Curso> cursos = listarTodos();
        
        return cursos.stream()
                .filter(c -> c.getAlunos() != null && !c.getAlunos().isEmpty())
                .toList();
    }
    
    public List<Curso> buscarSemAlunos() {
        List<Curso> cursos = listarTodos();
        
        return cursos.stream()
                .filter(c -> c.getAlunos() == null || c.getAlunos().isEmpty())
                .toList();
    }
    
    private void salvarTodos(List<Curso> cursos) throws IOException {
        criarDiretorioSeNaoExistir();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_CURSOS))) {
            for (Curso curso : cursos) {
                writer.println(formatarCurso(curso));
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
    
    private String formatarCurso(Curso curso) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(curso.getId()).append(SEPARADOR);
        sb.append(curso.getNome().name()).append(SEPARADOR);
        sb.append(curso.getCredito()).append(SEPARADOR);
        
        // Disciplinas (IDs separados por vírgula)
        if (curso.getDisciplinas() != null && !curso.getDisciplinas().isEmpty()) {
            StringBuilder disciplinasIds = new StringBuilder();
            for (int i = 0; i < curso.getDisciplinas().size(); i++) {
                if (i > 0) disciplinasIds.append(",");
                disciplinasIds.append(curso.getDisciplinas().get(i).getId());
            }
            sb.append(disciplinasIds.toString()).append(SEPARADOR);
        } else {
            sb.append("").append(SEPARADOR);
        }
        
        // Alunos (IDs separados por vírgula)
        if (curso.getAlunos() != null && !curso.getAlunos().isEmpty()) {
            StringBuilder alunosIds = new StringBuilder();
            for (int i = 0; i < curso.getAlunos().size(); i++) {
                if (i > 0) alunosIds.append(",");
                alunosIds.append(curso.getAlunos().get(i).getId());
            }
            sb.append(alunosIds.toString());
        } else {
            sb.append("");
        }
        
        return sb.toString();
    }
    
    private Curso parsearLinha(String linha) {
        try {
            String[] partes = linha.split(SEPARADOR, -1);
            
            if (partes.length < 3) {
                System.err.println("Linha inválida no arquivo: " + linha);
                return null;
            }
            
            Long id = Long.parseLong(partes[0]);
            Cursos nome = Cursos.valueOf(partes[1]);
            int credito = Integer.parseInt(partes[2]);
            
            // Disciplinas
            List<Disciplina> disciplinas = new ArrayList<>();
            if (partes.length >= 4 && !partes[3].isEmpty()) {
                try {
                    String[] disciplinasIds = partes[3].split(",");
                    for (String disciplinaIdStr : disciplinasIds) {
                        Long disciplinaId = Long.parseLong(disciplinaIdStr.trim());
                        // TODO: Buscar disciplina no DisciplinaRepository quando necessário
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
            
            // Alunos
            List<Aluno> alunos = new ArrayList<>();
            if (partes.length >= 5 && !partes[4].isEmpty()) {
                try {
                    String[] alunosIds = partes[4].split(",");
                    for (String alunoIdStr : alunosIds) {
                        Long alunoId = Long.parseLong(alunoIdStr.trim());
                        // TODO: Buscar aluno no AlunoRepository quando necessário
                        Aluno aluno = Aluno.builder()
                                .id(alunoId)
                                .nome("Aluno " + alunoId)
                                .email("aluno" + alunoId + "@uni.br")
                                .senha("123456")
                                .build();
                        alunos.add(aluno);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao parsear alunos na linha: " + linha);
                }
            }
            
            Curso curso = Curso.builder()
                    .id(id)
                    .nome(nome)
                    .credito(credito)
                    .disciplinas(disciplinas)
                    .alunos(alunos)
                    .build();
                    
            return curso;
            
        } catch (Exception e) {
            System.err.println("Erro ao parsear linha: " + linha + " - " + e.getMessage());
            return null;
        }
    }
    
    public void limparArquivo() {
        try {
            if (Files.exists(Paths.get(ARQUIVO_CURSOS))) {
                Files.delete(Paths.get(ARQUIVO_CURSOS));
            }
        } catch (IOException e) {
            System.err.println("Erro ao limpar arquivo: " + e.getMessage());
        }
    }
    
    public int contarCursos() {
        return listarTodos().size();
    }
    
    public int contarCursosPorCredito(int credito) {
        return buscarPorCredito(credito).size();
    }
    
    public int contarCursosComAlunos() {
        return buscarComAlunos().size();
    }
    
    public int contarCursosSemAlunos() {
        return buscarSemAlunos().size();
    }
    
    public int contarAlunosCurso(Long cursoId) {
        Curso curso = buscarPorId(cursoId);
        if (curso == null || curso.getAlunos() == null) {
            return 0;
        }
        return curso.getAlunos().size();
    }
    
    public int contarDisciplinasCurso(Long cursoId) {
        Curso curso = buscarPorId(cursoId);
        if (curso == null || curso.getDisciplinas() == null) {
            return 0;
        }
        return curso.getDisciplinas().size();
    }
    
    public boolean adicionarDisciplina(Long cursoId, Disciplina disciplina) {
        Curso curso = buscarPorId(cursoId);
        if (curso == null || disciplina == null) return false;
        
        if (curso.getDisciplinas() == null) {
            curso.setDisciplinas(new ArrayList<>());
        }
        
        // Verificar se disciplina já está no curso
        if (curso.getDisciplinas().stream().anyMatch(d -> d.getId().equals(disciplina.getId()))) {
            return false;
        }
        
        curso.getDisciplinas().add(disciplina);
        return salvar(curso);
    }
    
    public boolean removerDisciplina(Long cursoId, Long disciplinaId) {
        Curso curso = buscarPorId(cursoId);
        if (curso == null || curso.getDisciplinas() == null) return false;
        
        boolean removido = curso.getDisciplinas().removeIf(d -> d.getId().equals(disciplinaId));
        
        if (removido) {
            return salvar(curso);
        }
        
        return false;
    }
    
    public boolean adicionarAluno(Long cursoId, Aluno aluno) {
        Curso curso = buscarPorId(cursoId);
        if (curso == null || aluno == null) return false;
        
        if (curso.getAlunos() == null) {
            curso.setAlunos(new ArrayList<>());
        }
        
        // Verificar se aluno já está no curso
        if (curso.getAlunos().stream().anyMatch(a -> a.getId().equals(aluno.getId()))) {
            return false;
        }
        
        curso.getAlunos().add(aluno);
        return salvar(curso);
    }
    
    public boolean removerAluno(Long cursoId, Long alunoId) {
        Curso curso = buscarPorId(cursoId);
        if (curso == null || curso.getAlunos() == null) return false;
        
        boolean removido = curso.getAlunos().removeIf(a -> a.getId().equals(alunoId));
        
        if (removido) {
            return salvar(curso);
        }
        
        return false;
    }
    
    public List<Aluno> listarAlunosCurso(Long cursoId) {
        Curso curso = buscarPorId(cursoId);
        if (curso == null || curso.getAlunos() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(curso.getAlunos());
    }
    
    public List<Disciplina> listarDisciplinasCurso(Long cursoId) {
        Curso curso = buscarPorId(cursoId);
        if (curso == null || curso.getDisciplinas() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(curso.getDisciplinas());
    }
    
    public boolean temDisciplina(Long cursoId, Long disciplinaId) {
        Curso curso = buscarPorId(cursoId);
        if (curso == null || curso.getDisciplinas() == null) return false;
        
        return curso.getDisciplinas().stream()
                .anyMatch(d -> d.getId().equals(disciplinaId));
    }
    
    public boolean temAluno(Long cursoId, Long alunoId) {
        Curso curso = buscarPorId(cursoId);
        if (curso == null || curso.getAlunos() == null) return false;
        
        return curso.getAlunos().stream()
                .anyMatch(a -> a.getId().equals(alunoId));
    }
    
    public List<Cursos> listarNomesCursosDisponiveis() {
        return List.of(Cursos.values());
    }
    
    public boolean podeAdicionarDisciplina(Long cursoId) {
        int disciplinasAtuais = contarDisciplinasCurso(cursoId);
        return disciplinasAtuais < Curso.LIMITE_DISCIPLINAS_POR_CURSO;
    }
    
    public int calcularCreditosObrigatorios(Long cursoId) {
        List<Disciplina> disciplinas = listarDisciplinasCurso(cursoId);
        
        return disciplinas.stream()
                .filter(d -> !d.isOptativa())
                .mapToInt(Disciplina::getQuantidadeCreditos)
                .sum();
    }
    
    public int calcularCreditosOptativos(Long cursoId) {
        List<Disciplina> disciplinas = listarDisciplinasCurso(cursoId);
        
        return disciplinas.stream()
                .filter(Disciplina::isOptativa)
                .mapToInt(Disciplina::getQuantidadeCreditos)
                .sum();
    }
    
    public void inicializarCursosPadrao() {
        if (listarTodos().isEmpty()) {
            // Criar cursos padrão
            for (Cursos cursoEnum : Cursos.values()) {
                Curso curso = Curso.builder()
                        .id(gerarProximoId())
                        .nome(cursoEnum)
                        .credito(240) // Valor padrão
                        .build();
                salvar(curso);
            }
        }
    }
}
