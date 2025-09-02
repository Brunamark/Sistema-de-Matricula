package repository;

import entity.Disciplina;
import entity.Curso;
import entity.Professor;
import entity.Aluno;
import enums.Cursos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaRepository {
    

    private static final String ARQUIVO_DISCIPLINAS = System.getProperty("user.dir") + "/code/file/disciplinas.txt";

    private static final String SEPARADOR = ";";
    
    public boolean salvar(Disciplina disciplina) {
        try {
            criarDiretorioSeNaoExistir();
            
            List<Disciplina> disciplinas = listarTodos();
            
            boolean existe = disciplinas.removeIf(d -> d.getId().equals(disciplina.getId()));
            
            disciplinas.add(disciplina);
            
            salvarTodos(disciplinas);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Erro ao salvar disciplina: " + e.getMessage());
            return false;
        }
    }
    
    public Disciplina buscarPorId(Long id) {
        if (id == null) return null;
        
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public Disciplina buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) return null;
        
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> d.getNome() != null && d.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }
    
    public List<Disciplina> listarTodos() {
        List<Disciplina> disciplinas = new ArrayList<>();
        
        try {
            if (!Files.exists(Paths.get(ARQUIVO_DISCIPLINAS))) {
                return disciplinas;
            }
            
            List<String> linhas = Files.readAllLines(Paths.get(ARQUIVO_DISCIPLINAS));
            
            for (String linha : linhas) {
                if (linha.trim().isEmpty()) continue;
                
                Disciplina disciplina = parsearLinha(linha);
                if (disciplina != null) {
                    disciplinas.add(disciplina);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar disciplinas: " + e.getMessage());
        }
        
        return disciplinas;
    }
    
    public boolean excluir(Long id) {
        if (id == null) return false;
        
        try {
            List<Disciplina> disciplinas = listarTodos();
            
            boolean removido = disciplinas.removeIf(d -> d.getId().equals(id));
            
            if (removido) {
                salvarTodos(disciplinas);
            }
            
            return removido;
            
        } catch (Exception e) {
            System.err.println("Erro ao excluir disciplina: " + e.getMessage());
            return false;
        }
    }
    
    public boolean existeNome(String nome) {
        return buscarPorNome(nome) != null;
    }
    
    public Long gerarProximoId() {
        List<Disciplina> disciplinas = listarTodos();
        
        if (disciplinas.isEmpty()) {
            return 1L;
        }
        
        return disciplinas.stream()
                .mapToLong(Disciplina::getId)
                .max()
                .orElse(0L) + 1;
    }
    
    public List<Disciplina> buscarPorNomeContendo(String nome) {
        if (nome == null || nome.trim().isEmpty()) return new ArrayList<>();
        
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> d.getNome() != null && 
                           d.getNome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
    }
    
    public List<Disciplina> buscarPorCurso(Long cursoId) {
        if (cursoId == null) return new ArrayList<>();
        
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> d.getCursos() != null && 
                           d.getCursos().stream().anyMatch(c -> c.getId().equals(cursoId)))
                .toList();
    }
    
    public List<Disciplina> buscarPorProfessor(Long professorId) {
        if (professorId == null) return new ArrayList<>();
        
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> d.getProfessor() != null && 
                           d.getProfessor().getId().equals(professorId))
                .toList();
    }
    
    public List<Disciplina> buscarPorCreditos(int creditos) {
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> d.getQuantidadeCreditos() == creditos)
                .toList();
    }
    
    public List<Disciplina> buscarOptativas() {
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(Disciplina::isOptativa)
                .toList();
    }
    
    public List<Disciplina> buscarObrigatorias() {
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> !d.isOptativa())
                .toList();
    }
    
    public List<Disciplina> buscarAtivas() {
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(Disciplina::isAtiva)
                .toList();
    }
    
    public List<Disciplina> buscarInativas() {
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> !d.isAtiva())
                .toList();
    }
    
    public List<Disciplina> buscarSemProfessor() {
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> d.getProfessor() == null)
                .toList();
    }
    
    private void salvarTodos(List<Disciplina> disciplinas) throws IOException {
        criarDiretorioSeNaoExistir();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_DISCIPLINAS))) {
            for (Disciplina disciplina : disciplinas) {
                writer.println(formatarDisciplina(disciplina));
            }
        }
    }
    
    public boolean salvarTodas(List<Disciplina> disciplinas) {
        if (disciplinas == null || disciplinas.isEmpty()) {
            return false;
        }
        
        try {
            criarDiretorioSeNaoExistir();
            
            List<Disciplina> disciplinasExistentes = listarTodos();
            
            // Remover disciplinas que serão atualizadas
            for (Disciplina novaDisciplina : disciplinas) {
                disciplinasExistentes.removeIf(d -> d.getId().equals(novaDisciplina.getId()));
            }
            
            // Adicionar as novas disciplinas
            disciplinasExistentes.addAll(disciplinas);
            
            salvarTodos(disciplinasExistentes);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Erro ao salvar lista de disciplinas: " + e.getMessage());
            return false;
        }
    }
    
    public boolean salvarDisciplinasObrigatorias(List<Disciplina> disciplinasObrigatorias) {
        if (disciplinasObrigatorias == null || disciplinasObrigatorias.isEmpty()) {
            return false;
        }
        
        // Verificar se todas são obrigatórias
        boolean todasObrigatorias = disciplinasObrigatorias.stream()
                .allMatch(d -> !d.isOptativa());
        
        if (!todasObrigatorias) {
            System.err.println("Erro: Lista contém disciplinas optativas!");
            return false;
        }
        
        return salvarTodas(disciplinasObrigatorias);
    }
    
    public boolean salvarDisciplinasOptativas(List<Disciplina> disciplinasOptativas) {
        if (disciplinasOptativas == null || disciplinasOptativas.isEmpty()) {
            return false;
        }
        
        // Verificar se todas são optativas
        boolean todasOptativas = disciplinasOptativas.stream()
                .allMatch(Disciplina::isOptativa);
        
        if (!todasOptativas) {
            System.err.println("Erro: Lista contém disciplinas obrigatórias!");
            return false;
        }
        
        return salvarTodas(disciplinasOptativas);
    }
    
    public boolean salvarDisciplinasPorTipo(List<Disciplina> obrigatorias, List<Disciplina> optativas) {
        try {
            boolean sucessoObrigatorias = true;
            boolean sucessoOptativas = true;
            
            if (obrigatorias != null && !obrigatorias.isEmpty()) {
                sucessoObrigatorias = salvarDisciplinasObrigatorias(obrigatorias);
            }
            
            if (optativas != null && !optativas.isEmpty()) {
                sucessoOptativas = salvarDisciplinasOptativas(optativas);
            }
            
            return sucessoObrigatorias && sucessoOptativas;
            
        } catch (Exception e) {
            System.err.println("Erro ao salvar disciplinas por tipo: " + e.getMessage());
            return false;
        }
    }
    
    public boolean adicionarDisciplinas(List<Disciplina> novasDisciplinas) {
        if (novasDisciplinas == null || novasDisciplinas.isEmpty()) {
            return false;
        }
        
        try {
            // Gerar IDs para disciplinas sem ID
            for (Disciplina disciplina : novasDisciplinas) {
                if (disciplina.getId() == null) {
                    disciplina.setId(gerarProximoId());
                }
            }
            
            return salvarTodas(novasDisciplinas);
            
        } catch (Exception e) {
            System.err.println("Erro ao adicionar disciplinas: " + e.getMessage());
            return false;
        }
    }
    
    public boolean substituirTodasDisciplinas(List<Disciplina> novasDisciplinas) {
        try {
            // Limpar arquivo existente
            limparArquivo();
            
            // Salvar as novas disciplinas
            if (novasDisciplinas != null && !novasDisciplinas.isEmpty()) {
                salvarTodos(novasDisciplinas);
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Erro ao substituir todas as disciplinas: " + e.getMessage());
            return false;
        }
    }
    
    public int salvarDisciplinasComValidacao(List<Disciplina> disciplinas) {
        if (disciplinas == null || disciplinas.isEmpty()) {
            return 0;
        }
        
        int salvas = 0;
        
        for (Disciplina disciplina : disciplinas) {
            try {
                // Validações básicas
                if (disciplina.getNome() == null || disciplina.getNome().trim().isEmpty()) {
                    System.err.println("Disciplina sem nome ignorada: " + disciplina.getId());
                    continue;
                }
                
                if (disciplina.getQuantidadeCreditos() < Disciplina.CREDITOS_MINIMO || 
                    disciplina.getQuantidadeCreditos() > Disciplina.CREDITOS_MAXIMO) {
                    System.err.println("Disciplina com créditos inválidos ignorada: " + disciplina.getNome());
                    continue;
                }
                
                // Salvar disciplina individual
                if (salvar(disciplina)) {
                    salvas++;
                }
                
            } catch (Exception e) {
                System.err.println("Erro ao salvar disciplina " + disciplina.getNome() + ": " + e.getMessage());
            }
        }
        
        return salvas;
    }
    
    // Método auxiliar para separar disciplinas por tipo
    public void separarDisciplinasPorTipo(List<Disciplina> disciplinas, 
                                         List<Disciplina> obrigatorias, 
                                         List<Disciplina> optativas) {
        if (disciplinas == null) return;
        
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.isOptativa()) {
                optativas.add(disciplina);
            } else {
                obrigatorias.add(disciplina);
            }
        }
    }
    
    // Correção no método criarDiretorioSeNaoExistir (estava "src" em vez de "file")
    private void criarDiretorioSeNaoExistir() {
        try {
            Files.createDirectories(Paths.get("file"));
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório file: " + e.getMessage());
        }
    }
    
    private String formatarDisciplina(Disciplina disciplina) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(disciplina.getId()).append(SEPARADOR);
        sb.append(disciplina.getNome()).append(SEPARADOR);
        sb.append(disciplina.getQuantidadeCreditos()).append(SEPARADOR);
        sb.append(disciplina.isOptativa()).append(SEPARADOR);
        sb.append(disciplina.isAtiva()).append(SEPARADOR);
        
        // Professor
        if (disciplina.getProfessor() != null) {
            sb.append(disciplina.getProfessor().getId()).append(SEPARADOR);
        } else {
            sb.append("").append(SEPARADOR);
        }
        
        // Cursos (IDs separados por vírgula)
        if (disciplina.getCursos() != null && !disciplina.getCursos().isEmpty()) {
            StringBuilder cursosIds = new StringBuilder();
            for (int i = 0; i < disciplina.getCursos().size(); i++) {
                if (i > 0) cursosIds.append(",");
                cursosIds.append(disciplina.getCursos().get(i).getId());
            }
            sb.append(cursosIds.toString()).append(SEPARADOR);
        } else {
            sb.append("").append(SEPARADOR);
        }
        
        // Alunos (IDs separados por vírgula)
        if (disciplina.getAlunos() != null && !disciplina.getAlunos().isEmpty()) {
            StringBuilder alunosIds = new StringBuilder();
            for (int i = 0; i < disciplina.getAlunos().size(); i++) {
                if (i > 0) alunosIds.append(",");
                alunosIds.append(disciplina.getAlunos().get(i).getId());
            }
            sb.append(alunosIds.toString());
        } else {
            sb.append("");
        }
        
        return sb.toString();
    }
    
    private Disciplina parsearLinha(String linha) {
        try {
            String[] partes = linha.split(SEPARADOR, -1);
            
            if (partes.length < 5) {
                System.err.println("Linha inválida no arquivo: " + linha);
                return null;
            }
            
            Long id = Long.parseLong(partes[0]);
            String nome = partes[1];
            int quantidadeCreditos = Integer.parseInt(partes[2]);
            boolean optativa = Boolean.parseBoolean(partes[3]);
            boolean ativa = Boolean.parseBoolean(partes[4]);
            
            // Professor
            Professor professor = null;
            if (partes.length >= 6 && !partes[5].isEmpty()) {
                try {
                    Long professorId = Long.parseLong(partes[5]);
                    // TODO: Buscar professor no ProfessorRepository quando necessário
                    professor = Professor.builder()
                            .id(professorId)
                            .nome("Professor " + professorId)
                            .email("prof" + professorId + "@uni.br")
                            .senha("123456")
                            .build();
                } catch (Exception e) {
                    System.err.println("Erro ao parsear professor na linha: " + linha);
                }
            }
            
            // Cursos
            List<Curso> cursos = new ArrayList<>();
            if (partes.length >= 7 && !partes[6].isEmpty()) {
                try {
                    String[] cursosIds = partes[6].split(",");
                    for (String cursoIdStr : cursosIds) {
                        Long cursoId = Long.parseLong(cursoIdStr.trim());
                        // TODO: Buscar curso no CursoRepository quando necessário
                        Curso curso = Curso.builder()
                                .id(cursoId)
                                .nome(Cursos.ENGENHARIA_DE_SOFTWARE) // Valor padrão
                                .credito(240)
                                .build();
                        cursos.add(curso);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao parsear cursos na linha: " + linha);
                }
            }
            
            // Alunos
            List<Aluno> alunos = new ArrayList<>();
            if (partes.length >= 8 && !partes[7].isEmpty()) {
                try {
                    String[] alunosIds = partes[7].split(",");
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
            
            Disciplina disciplina = Disciplina.builder()
                    .id(id)
                    .nome(nome)
                    .quantidadeCreditos(quantidadeCreditos)
                    .optativa(optativa)
                    .ativa(ativa)
                    .professor(professor)
                    .cursos(cursos)
                    .alunos(alunos)
                    .build();
                    
            return disciplina;
            
        } catch (Exception e) {
            System.err.println("Erro ao parsear linha: " + linha + " - " + e.getMessage());
            return null;
        }
    }
    
    public void limparArquivo() {
        try {
            if (Files.exists(Paths.get(ARQUIVO_DISCIPLINAS))) {
                Files.delete(Paths.get(ARQUIVO_DISCIPLINAS));
            }
        } catch (IOException e) {
            System.err.println("Erro ao limpar arquivo: " + e.getMessage());
        }
    }
    
    public int contarDisciplinas() {
        return listarTodos().size();
    }
    
    public int contarDisciplinasAtivas() {
        return buscarAtivas().size();
    }
    
    public int contarDisciplinasOptativas() {
        return buscarOptativas().size();
    }
    
    public int contarDisciplinasObrigatorias() {
        return buscarObrigatorias().size();
    }
    
    public int contarDisciplinasPorCurso(Long cursoId) {
        return buscarPorCurso(cursoId).size();
    }
    
    public int contarDisciplinasPorProfessor(Long professorId) {
        return buscarPorProfessor(professorId).size();
    }
    
    public int contarDisciplinasSemProfessor() {
        return buscarSemProfessor().size();
    }
    
    public boolean atribuirProfessor(Long disciplinaId, Professor professor) {
        Disciplina disciplina = buscarPorId(disciplinaId);
        if (disciplina == null) return false;
        
        disciplina.setProfessor(professor);
        return salvar(disciplina);
    }
    
    public boolean removerProfessor(Long disciplinaId) {
        Disciplina disciplina = buscarPorId(disciplinaId);
        if (disciplina == null) return false;
        
        disciplina.setProfessor(null);
        return salvar(disciplina);
    }
    
    public boolean adicionarAluno(Long disciplinaId, Aluno aluno) {
        Disciplina disciplina = buscarPorId(disciplinaId);
        if (disciplina == null) return false;
        
        if (disciplina.getAlunos() == null) {
            disciplina.setAlunos(new ArrayList<>());
        }
        
        // Verificar se aluno já está matriculado
        if (disciplina.getAlunos().stream().anyMatch(a -> a.getId().equals(aluno.getId()))) {
            return false;
        }
        
        disciplina.getAlunos().add(aluno);
        return salvar(disciplina);
    }
    
    public boolean removerAluno(Long disciplinaId, Long alunoId) {
        Disciplina disciplina = buscarPorId(disciplinaId);
        if (disciplina == null || disciplina.getAlunos() == null) return false;
        
        boolean removido = disciplina.getAlunos().removeIf(a -> a.getId().equals(alunoId));
        
        if (removido) {
            return salvar(disciplina);
        }
        
        return false;
    }
    
    public boolean temVagas(Long disciplinaId) {
        Disciplina disciplina = buscarPorId(disciplinaId);
        if (disciplina == null) return false;
        
        int alunosMatriculados = disciplina.getQuantidadeAlunosMatriculados();
        return alunosMatriculados < Disciplina.QUANTIDADE_MAXIMA_ALUNO;
    }
    
    public int contarVagasDisponiveis(Long disciplinaId) {
        Disciplina disciplina = buscarPorId(disciplinaId);
        if (disciplina == null) return 0;
        
        int alunosMatriculados = disciplina.getQuantidadeAlunosMatriculados();
        return Math.max(0, Disciplina.QUANTIDADE_MAXIMA_ALUNO - alunosMatriculados);
    }
    
    public List<Disciplina> buscarPorNomeDoCurso(String nomeCurso) {
        if (nomeCurso == null || nomeCurso.trim().isEmpty()) return new ArrayList<>();
        
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> d.getCursos() != null && 
                           d.getCursos().stream().anyMatch(c -> 
                               c.getNome() != null && 
                               c.getNome().getNomeCurso().equalsIgnoreCase(nomeCurso)))
                .toList();
    }
    
    public Disciplina buscarDisciplinaPorNomeDoCurso(String nomeCurso) {
        if (nomeCurso == null || nomeCurso.trim().isEmpty()) return null;
        
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> d.getCursos() != null && 
                           d.getCursos().stream().anyMatch(c -> 
                               c.getNome() != null && 
                               c.getNome().getNomeCurso().equalsIgnoreCase(nomeCurso)))
                .findFirst()
                .orElse(null);
    }
    
    public List<Disciplina> buscarPorNomeCursoEnum(Cursos nomeCurso) {
        if (nomeCurso == null) return new ArrayList<>();
        
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> d.getCursos() != null && 
                           d.getCursos().stream().anyMatch(c -> 
                               c.getNome() != null && 
                               c.getNome().equals(nomeCurso)))
                .toList();
    }
    
    public List<Disciplina> buscarDisciplinasPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) return new ArrayList<>();
        
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> d.getNome() != null && 
                           d.getNome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
    }
    
    public List<Disciplina> buscarDisciplinasPorNomeParcial(String nome) {
        if (nome == null || nome.trim().isEmpty()) return new ArrayList<>();
        
        List<Disciplina> disciplinas = listarTodos();
        
        return disciplinas.stream()
                .filter(d -> d.getNome() != null && 
                           d.getNome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
    }
}
