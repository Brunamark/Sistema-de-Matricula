package entity;

import enums.Codigo;
import exceptions.ExceptionHandler;
import repository.AlunoRepository;
import repository.CursoRepository;
import repository.DisciplinaRepository;
import repository.ProfessorRepository;
import repository.SecretarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Secretario extends Usuario {
    private Curso curso;

    private static AlunoRepository alunoRepository = new AlunoRepository();
    private static ProfessorRepository professorRepository = new ProfessorRepository();
    private static DisciplinaRepository disciplinaRepository = new DisciplinaRepository();
    private static CursoRepository cursoRepository = new CursoRepository();
    
    private Secretario() {
        super();
    }

    private Secretario(Builder builder) {
        super(builder.id, builder.email, builder.senha, builder.nome);
        this.curso = builder.curso;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String email;
        private String senha;
        private String nome;
        private Curso curso;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder senha(String senha) {
            this.senha = senha;
            return this;
        }

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder curso(Curso curso) {
            this.curso = curso;
            return this;
        }

        public Secretario build() {
            ExceptionHandler handler = new ExceptionHandler();
            if (nome == null || nome.trim().isEmpty()) {
                handler.campoObrigatorio("Nome do secretário");
            }
            if (email == null || email.trim().isEmpty()) {
                handler.campoObrigatorio("Email do secretário");
            }
            if (senha == null || senha.trim().isEmpty()) {
                handler.campoObrigatorio("Senha do secretário");
            }

            return new Secretario(this);
        }
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public String gerarCurriculoSemestral(Curso curso) {
        if (curso == null) {
            return "Erro: Curso não especificado para geração do currículo.";
        }

        StringBuilder curriculo = new StringBuilder();
        
        try {
            curriculo.append("═══════════════════════════════════════════════════════════════\n");
            curriculo.append("              CURRÍCULO SEMESTRAL - UNIVERSIDADE\n");
            curriculo.append("═══════════════════════════════════════════════════════════════\n\n");
            
            curriculo.append("CURSO: ").append(curso.getNome().getNomeCurso()).append("\n");
            curriculo.append("ID do Curso: ").append(curso.getId()).append("\n");
            curriculo.append("Créditos Totais do Curso: ").append(curso.getCredito()).append("\n");
            curriculo.append("Data de Geração: ").append(java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n\n");

            curriculo.append("ESTATÍSTICAS GERAIS\n");
            curriculo.append("─────────────────────────────────────────────────────────────────\n");
            
            int totalDisciplinasCurso = disciplinaRepository.contarDisciplinasPorCurso(curso.getId());
            int disciplinasAtivas = disciplinaRepository.contarDisciplinasAtivas();
            int alunosCurso = alunoRepository.contarAlunosPorCurso(curso.getId());
            int professoresDisponiveis = professorRepository.contarProfessores();
            
            curriculo.append("• Total de Disciplinas: ").append(totalDisciplinasCurso).append("\n");
            curriculo.append("• Disciplinas Ativas: ").append(disciplinasAtivas).append("\n");
            curriculo.append("• Alunos Matriculados: ").append(alunosCurso).append("\n");
            curriculo.append("• Professores Disponíveis: ").append(professoresDisponiveis).append("\n\n");

            curriculo.append("DISCIPLINAS OBRIGATÓRIAS\n");
            curriculo.append("─────────────────────────────────────────────────────────────────\n");
            
            List<Disciplina> obrigatorias = disciplinaRepository.buscarObrigatorias();
            int totalCreditosObrigatorios = calcularCreditosObrigatoriosCurso(curso.getId());
            
            if (obrigatorias.isEmpty()) {
                curriculo.append("Nenhuma disciplina obrigatória encontrada.\n\n");
            } else {
                for (Disciplina disciplina : obrigatorias) {
                    curriculo.append(formatarDisciplina(disciplina));
                }
                curriculo.append("Total de Créditos Obrigatórios: ").append(totalCreditosObrigatorios).append("\n\n");
            }

            curriculo.append("DISCIPLINAS OPTATIVAS\n");
            curriculo.append("─────────────────────────────────────────────────────────────────\n");
            
            List<Disciplina> optativas = disciplinaRepository.buscarOptativas();
            int totalCreditosOptativos = calcularCreditosOptativosCurso(curso.getId());
            
            if (optativas.isEmpty()) {
                curriculo.append("Nenhuma disciplina optativa encontrada.\n\n");
            } else {
                for (Disciplina disciplina : optativas) {
                    curriculo.append(formatarDisciplina(disciplina));
                }
                curriculo.append("Total de Créditos Optativos Disponíveis: ").append(totalCreditosOptativos).append("\n\n");
            }

            curriculo.append("PROFESSORES E DISCIPLINAS\n");
            curriculo.append("─────────────────────────────────────────────────────────────────\n");
            
            List<Professor> todosProfessores = professorRepository.listarTodos();
            
            if (todosProfessores.isEmpty()) {
                curriculo.append("Nenhum professor cadastrado.\n\n");
            } else {
                for (Professor professor : todosProfessores) {
                    curriculo.append("").append(professor.getNome()).append(" (").append(professor.getEmail()).append(")\n");
                    
                    List<Disciplina> disciplinasProfessor = disciplinaRepository.buscarPorProfessor(professor.getId());
                    if (disciplinasProfessor.isEmpty()) {
                        curriculo.append("   └─ Nenhuma disciplina atribuída\n");
                    } else {
                        for (Disciplina disc : disciplinasProfessor) {
                            curriculo.append("   └─ ").append(disc.getNome())
                                    .append(" (").append(disc.getQuantidadeCreditos()).append(" créditos)\n");
                        }
                    }
                    curriculo.append("\n");
                }
            }

            List<Disciplina> semProfessor = disciplinaRepository.buscarSemProfessor();
            int countSemProfessor = disciplinaRepository.contarDisciplinasSemProfessor();
            
            if (countSemProfessor > 0) {
                curriculo.append("DISCIPLINAS SEM PROFESSOR ATRIBUÍDO (").append(countSemProfessor).append(")\n");
                curriculo.append("─────────────────────────────────────────────────────────────────\n");
                for (Disciplina disc : semProfessor) {
                    curriculo.append("• ").append(disc.getNome())
                            .append(" (").append(disc.getQuantidadeCreditos()).append(" créditos)\n");
                }
                curriculo.append("\n");
            }

            curriculo.append("RELATÓRIO DE MATRÍCULAS\n");
            curriculo.append("─────────────────────────────────────────────────────────────────\n");
            
            curriculo.append("Distribuição por Disciplina:\n");
            List<Disciplina> disciplinasCurso = disciplinaRepository.buscarPorCurso(curso.getId());
            for (Disciplina disciplina : disciplinasCurso) {
                int vagasDisponiveis = disciplinaRepository.contarVagasDisponiveis(disciplina.getId());
                
                curriculo.append("  • ").append(disciplina.getNome())
                        .append(": ").append(disciplina.getQuantidadeAlunosMatriculados()).append(" alunos")
                        .append(" (").append(vagasDisponiveis).append(" vagas disponíveis)\n");
            }
            curriculo.append("\n");

            curriculo.append("ALUNOS MATRICULADOS NO CURSO\n");
            curriculo.append("─────────────────────────────────────────────────────────────────\n");
            
            List<Aluno> alunosListaCurso = alunoRepository.listarAlunosPorCurso(curso.getId());
            
            if (alunosListaCurso.isEmpty()) {
                curriculo.append("Nenhum aluno matriculado no curso.\n\n");
            } else {
                for (Aluno aluno : alunosListaCurso) {
                    curriculo.append(" ").append(aluno.getNome())
                            .append(" (").append(aluno.getEmail()).append(")\n");
                    
                    int creditosAtuais = aluno.calcularCreditosAtuais();
                    int creditosCompletos = aluno.calcularTotalCreditosCompletos();
                    double progresso = aluno.getProgressoGraduacao();
                    
                    curriculo.append("   └─ Créditos Atuais: ").append(creditosAtuais)
                            .append(" | Completos: ").append(creditosCompletos)
                            .append(" | Progresso: ").append(String.format("%.1f", progresso)).append("%\n");
                }
                curriculo.append("\n");
            }

            curriculo.append("RESUMO EXECUTIVO\n");
            curriculo.append("─────────────────────────────────────────────────────────────────\n");
            
            int totalObrigatoriasCurso = disciplinaRepository.contarDisciplinasObrigatorias();
            int totalOptativasCurso = disciplinaRepository.contarDisciplinasOptativas();
            int totalCreditosDisponiveis = calcularTotalCreditosDisponiveis(curso.getId());
            
            curriculo.append("• Disciplinas Obrigatórias: ").append(totalObrigatoriasCurso).append("\n");
            curriculo.append("• Disciplinas Optativas: ").append(totalOptativasCurso).append("\n");
            curriculo.append("• Total de Créditos Disponíveis: ").append(totalCreditosDisponiveis).append("\n");
            curriculo.append("• Disciplinas sem Professor: ").append(countSemProfessor).append("\n");
            curriculo.append("• Total de Alunos: ").append(alunosCurso).append("\n\n");

            curriculo.append("═══════════════════════════════════════════════════════════════\n");
            curriculo.append("           Currículo gerado automaticamente pelo sistema\n");
            curriculo.append("                    Secretaria Universitária\n");
            curriculo.append("═══════════════════════════════════════════════════════════════\n");

            salvarCurriculoEmArquivo(curriculo.toString(), curso);
            
            return curriculo.toString();
            
        } catch (Exception e) {
            return "Erro ao gerar currículo semestral: " + e.getMessage();
        }
    }


    private int calcularCreditosObrigatoriosCurso(Long cursoId) {
        List<Disciplina> obrigatorias = disciplinaRepository.buscarObrigatorias();
        return obrigatorias.stream()
                .filter(d -> temDisciplinaNoCurso(d.getId(), cursoId))
                .mapToInt(Disciplina::getQuantidadeCreditos)
                .sum();
    }

    private int calcularCreditosOptativosCurso(Long cursoId) {
        List<Disciplina> optativas = disciplinaRepository.buscarOptativas();
        return optativas.stream()
                .filter(d -> temDisciplinaNoCurso(d.getId(), cursoId))
                .mapToInt(Disciplina::getQuantidadeCreditos)
                .sum();
    }

    private int calcularTotalCreditosDisponiveis(Long cursoId) {
        List<Disciplina> disciplinasCurso = disciplinaRepository.buscarPorCurso(cursoId);
        return disciplinasCurso.stream()
                .mapToInt(Disciplina::getQuantidadeCreditos)
                .sum();
    }

    private boolean temDisciplinaNoCurso(Long disciplinaId, Long cursoId) {
        List<Disciplina> disciplinasCurso = disciplinaRepository.buscarPorCurso(cursoId);
        return disciplinasCurso.stream()
                .anyMatch(d -> d.getId().equals(disciplinaId));
    }

    private String formatarDisciplina(Disciplina disciplina) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("📖 ").append(disciplina.getNome()).append("\n");
        sb.append("   ├─ ID: ").append(disciplina.getId()).append("\n");
        sb.append("   ├─ Créditos: ").append(disciplina.getQuantidadeCreditos()).append("\n");
        sb.append("   ├─ Tipo: ").append(disciplina.isOptativa() ? "Optativa" : "Obrigatória").append("\n");
        sb.append("   ├─ Status: ").append(disciplina.isAtiva() ? "Ativa" : "Inativa").append("\n");
        
        if (disciplina.getProfessor() != null) {
            sb.append("   ├─ Professor: ").append(disciplina.getProfessor().getNome()).append("\n");
        } else {
            sb.append("   ├─ Professor: Não atribuído\n");
        }
        
        sb.append("   └─ Alunos Matriculados: ").append(disciplina.getQuantidadeAlunosMatriculados())
          .append("/").append(Disciplina.QUANTIDADE_MAXIMA_ALUNO).append("\n\n");
        
        return sb.toString();
    }

    private void salvarCurriculoEmArquivo(String curriculo, Curso curso) {
        try {
            String nomeArquivo = "file/curriculo_" + curso.getNome().name().toLowerCase() + 
                               "_" + java.time.LocalDate.now().format(
                                   java.time.format.DateTimeFormatter.ofPattern("yyyy_MM_dd")) + ".txt";
            
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("file"));
            java.nio.file.Files.write(java.nio.file.Paths.get(nomeArquivo), curriculo.getBytes());
            
            System.out.println("Currículo salvo em: " + nomeArquivo);
            
        } catch (Exception e) {
            System.err.println("Erro ao salvar currículo em arquivo: " + e.getMessage());
        }
    }

    // Método para gerar currículo de todos os cursos
    public String gerarCurriculoGeralTodosCursos() {
        StringBuilder curriculoGeral = new StringBuilder();
        
        List<Curso> todosCursos = cursoRepository.listarTodos();
        
        curriculoGeral.append("═══════════════════════════════════════════════════════════════\n");
        curriculoGeral.append("           CURRÍCULO GERAL - TODOS OS CURSOS\n");
        curriculoGeral.append("═══════════════════════════════════════════════════════════════\n\n");
        
        for (Curso curso : todosCursos) {
            curriculoGeral.append(gerarCurriculoSemestral(curso));
            curriculoGeral.append("\n" + "=".repeat(65) + "\n\n");
        }
        
        return curriculoGeral.toString();
    }

    public Codigo criarDisciplina(Disciplina disciplina) {
        Disciplina disciplinaExistente = buscarDisciplinaPorNome(disciplina.getNome());

        if (disciplinaExistente != null) {
            return Codigo.DISCIPLINA_JA_EXISTE_406;
        }
        
        disciplinaRepository.salvar(disciplina);
         
        return Codigo.DISCIPLINA_CRIADA_201;
    }

    public Codigo editarDisciplina(Disciplina disciplina) {
        Disciplina disciplinaExistente = buscarDisciplinaPorNome(disciplina.getNome());

        if (disciplinaExistente == null) {
            return Codigo.DISCIPLINA_NAO_ENCONTRADA_404;
        }

        disciplinaRepository.salvar(disciplina);

        return Codigo.ATUALIZADO_202;
    }

    public Disciplina buscarDisciplinaPorId(Long id) {
        if (curso == null || curso.getDisciplinas() == null) {
            return null;
        }

        return disciplinaRepository.buscarPorId(id);
    }

    public Codigo excluirDisciplinaPorNome(String nome) {
        Disciplina disciplinaExistente = buscarDisciplinaPorNome(nome);

        if (disciplinaExistente == null) {
            return Codigo.DISCIPLINA_NAO_ENCONTRADA_404;
        }

        disciplinaRepository.excluir(disciplinaExistente.getId());
        return Codigo.EXCLUIDO_204;
    }

    public List<Disciplina> buscarDisciplinaPorNomeDoCurso(String nomeCurso) {
        if (curso == null || curso.getDisciplinas() == null) {
            return null;
        }

        return disciplinaRepository.buscarPorNomeDoCurso(nomeCurso);
    }

    public List<Disciplina> buscarDisciplinasPorNome(String nome) {
        if (curso == null || curso.getDisciplinas() == null) {
            return null;
        }

        return disciplinaRepository.buscarDisciplinasPorNome(nome);
    }

    public Codigo criarProfessor(Professor professor) {
        Professor professorExistente = buscarProfessorPorEmail(professor.getEmail());

        if (professorExistente != null) {
            return Codigo.PROFESSOR_JA_EXISTE_406;
        }
        professorRepository.salvar(professor);
        return Codigo.PROFESSOR_CRIADO_201;
    }

    public Codigo editarProfessor(Professor professor) {
        Professor professorExistente = buscarProfessorPorEmail(professor.getEmail());

        if (professorExistente == null) {
            return Codigo.PROFESSOR_NAO_ENCONTRADO_404;
        }

        professorRepository.salvar(professor);

        return Codigo.ATUALIZADO_202;
    }

    public Codigo excluirProfessorPorEmail(String email) {
        Professor professorExistente = buscarProfessorPorEmail(email);

        if (professorExistente == null) {
            return Codigo.PROFESSOR_NAO_ENCONTRADO_404;
        }

        professorRepository.excluir(professorExistente.getId());
        return Codigo.EXCLUIDO_204;
    }

    public Professor buscarProfessorPorId(Long id) {
        if (id == null) return null;

        return professorRepository.buscarPorId(id);
    }

    public Codigo criarAluno(Aluno aluno) {
        Aluno alunoExistente = buscarAlunoPorEmail(aluno.getEmail());

        if (alunoExistente != null) {
            return Codigo.ALUNO_JA_EXISTE_406;
        }

        alunoRepository.salvar(aluno);

        return Codigo.ALUNO_CRIADO_201;
    }

    public Codigo editarAluno(Aluno aluno) {
        Aluno alunoExistente = buscarAlunoPorEmail(aluno.getEmail());

        if (alunoExistente == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        alunoRepository.salvar(aluno);

        return Codigo.ATUALIZADO_202;
    }

    public Codigo excluirAlunoPorEmail(String email) {
        Aluno alunoExistente = buscarAlunoPorEmail(email);

        if (alunoExistente == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        alunoRepository.excluir(alunoExistente.getId());
        return Codigo.EXCLUIDO_204;
    }

    public Codigo excluirAlunoPorId(Long id) {
        Aluno alunoExistente = buscarAlunoPorId(id);

        if (alunoExistente == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        alunoRepository.excluir(alunoExistente.getId());
        return Codigo.EXCLUIDO_204;
    }

    public Aluno buscarAlunoPorId(Long id) {
        if (curso == null || curso.getAlunos() == null) {
            return null;
        }

        return alunoRepository.buscarPorId(id);
    }

    public List<Disciplina> listarDisciplinas() {
        if (curso == null) {
            return null;
        }
        return disciplinaRepository.listarTodos();
    }

    public Disciplina buscarDisciplinaPorNome(String nome) {
        if (curso == null || curso.getDisciplinas() == null) {
            return null;
        }

       return disciplinaRepository.buscarPorNome(nome);
    }

    public List<Disciplina> buscarDisciplinasPorNomeCurso(String nome) {
        if (curso == null) {
            return null;
        }
        return disciplinaRepository.buscarPorNomeDoCurso(nome);
    }

    public List<Professor> listarProfessores() {
        return professorRepository.listarTodos();
    }

    public List<Professor> buscarProfessorPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }
        return professorRepository.buscarPorNome(nome);
    }

    public Professor buscarProfessorPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return professorRepository.buscarPorEmail(email);
    }

    public List<Aluno> listarAlunos() {
        return alunoRepository.listarTodos();
    }

    public List<Aluno> buscarAlunoPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }
        return alunoRepository.buscarPorNome(nome);
    }

    public Aluno buscarAlunoPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return alunoRepository.buscarPorEmail(email);
    }

    // === MÉTODOS ESPECÍFICOS DE MATRÍCULA ===

    public Codigo matricularAluno(Long alunoId, Long disciplinaId) {
        Aluno aluno = buscarAlunoPorId(alunoId);
        Disciplina disciplina = buscarDisciplinaPorId(disciplinaId);

        if (aluno == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        if (disciplina == null) {
            return Codigo.DISCIPLINA_NAO_ENCONTRADA_404;
        }

        if (verificarLimiteCreditos(alunoId, disciplinaId) == Codigo.LIMITE_CREDITOS_EXCEDIDO_422) {
            return Codigo.LIMITE_CREDITOS_EXCEDIDO_422;
        }

        disciplina.adicionarAluno(aluno);
        return Codigo.MATRICULA_REALIZADA_201;
    }

    public Codigo desmatricularAluno(Long alunoId, Long disciplinaId) {
        Aluno aluno = buscarAlunoPorId(alunoId);
        Disciplina disciplina = buscarDisciplinaPorId(disciplinaId);

        if (aluno == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        if (disciplina == null) {
            return Codigo.DISCIPLINA_NAO_ENCONTRADA_404;
        }

        disciplina.removerAluno(aluno);
        return Codigo.MATRICULA_CANCELADA_200;
    }

    public Codigo verificarLimiteCreditos(Long alunoId, Long disciplinaId) {
        Aluno aluno = buscarAlunoPorId(alunoId);
        Disciplina disciplina = buscarDisciplinaPorId(disciplinaId);

        if (aluno == null) {
            return Codigo.ALUNO_NAO_ENCONTRADO_404;
        }

        if (disciplina == null) {
            return Codigo.DISCIPLINA_NAO_ENCONTRADA_404;
        }

        int creditosAtuais = aluno.calcularCreditosAtuais();
        int creditosNovos = disciplina.getQuantidadeCreditos();
        int limiteCreditos = Aluno.LIMITE_CREDITOS_POR_SEMESTRE;

        if (creditosAtuais + creditosNovos > limiteCreditos) {
            return Codigo.LIMITE_CREDITOS_EXCEDIDO_422;
        }

        return Codigo.OK_200; 
    }


   
    public List<Aluno> listarAlunosCurso(Curso curso) {
        if (curso == null) {
            return null;
        }
        return alunoRepository.listarAlunosPorCurso(curso);
    }

    public Curso buscarCursoPorId(Long id) {
        if (id == null) {
            return null;
        }
        return cursoRepository.buscarPorId(id);
    }
}
