package entity;

import enums.Codigo;
import exceptions.ExceptionHandler;

import java.util.List;
import java.util.ArrayList;

public class Aluno extends Usuario {
    private Curso curso;
    private List<Disciplina> disciplinas;
    private int creditosAtuais;
    private int creditosObrigatoriosCompletos;
    private int creditosOptativosCompletos;

    public static final int QUANTIDADE_MAXIMA_DISCIPLINA_OBRIGATORIA = 2;
    public static final int QUANTIDADE_MAXIMA_DISCIPLINA_OPTATIVA = 2;
    public static final int LIMITE_CREDITOS_POR_SEMESTRE = 24;
    public static final int CREDITOS_MINIMOS_PARA_GRADUACAO = 240;

    private Aluno() {
        super();
        this.disciplinas = new ArrayList<>();
        this.creditosAtuais = 0;
        this.creditosObrigatoriosCompletos = 0;
        this.creditosOptativosCompletos = 0;
    }

    private Aluno(Builder builder) {
        super(builder.id, builder.email, builder.senha, builder.nome);
        this.curso = builder.curso;
        this.disciplinas = builder.disciplinas != null ? builder.disciplinas : new ArrayList<>();
        this.creditosAtuais = builder.creditosAtuais;
        this.creditosObrigatoriosCompletos = builder.creditosObrigatoriosCompletos;
        this.creditosOptativosCompletos = builder.creditosOptativosCompletos;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
        recalcularCreditos();
    }

    public int getCreditosAtuais() {
        return creditosAtuais;
    }

    public void setCreditosAtuais(int creditosAtuais) {
        this.creditosAtuais = creditosAtuais;
    }

    public int getCreditosObrigatoriosCompletos() {
        return creditosObrigatoriosCompletos;
    }

    public void setCreditosObrigatoriosCompletos(int creditosObrigatoriosCompletos) {
        this.creditosObrigatoriosCompletos = creditosObrigatoriosCompletos;
    }

    public int getCreditosOptativosCompletos() {
        return creditosOptativosCompletos;
    }

    public void setCreditosOptativosCompletos(int creditosOptativosCompletos) {
        this.creditosOptativosCompletos = creditosOptativosCompletos;
    }

    // === MÉTODOS DE CRÉDITOS ===
    public int calcularCreditosAtuais() {
        if (disciplinas == null || disciplinas.isEmpty()) {
            return 0;
        }
        
        return disciplinas.stream()
                .mapToInt(Disciplina::getQuantidadeCreditos)
                .sum();
    }

    public int calcularCreditosObrigatoriosAtuais() {
        if (disciplinas == null || disciplinas.isEmpty()) {
            return 0;
        }
        
        return disciplinas.stream()
                .filter(d -> !d.isOptativa())
                .mapToInt(Disciplina::getQuantidadeCreditos)
                .sum();
    }

    public int calcularCreditosOptativosAtuais() {
        if (disciplinas == null || disciplinas.isEmpty()) {
            return 0;
        }
        
        return disciplinas.stream()
                .filter(Disciplina::isOptativa)
                .mapToInt(Disciplina::getQuantidadeCreditos)
                .sum();
    }

    public int calcularTotalCreditosCompletos() {
        return creditosObrigatoriosCompletos + creditosOptativosCompletos;
    }

    public int calcularCreditosRestantes() {
        int totalCompletos = calcularTotalCreditosCompletos();
        return Math.max(0, CREDITOS_MINIMOS_PARA_GRADUACAO - totalCompletos);
    }

    public boolean podeMatricular(Disciplina disciplina) {
        if (disciplina == null) return false;
        
        int creditosComNova = calcularCreditosAtuais() + disciplina.getQuantidadeCreditos();
        return creditosComNova <= LIMITE_CREDITOS_POR_SEMESTRE;
    }

    public boolean podeGraduar() {
        return calcularTotalCreditosCompletos() >= CREDITOS_MINIMOS_PARA_GRADUACAO;
    }

    public void recalcularCreditos() {
        this.creditosAtuais = calcularCreditosAtuais();
    }

    public void completarDisciplina(Disciplina disciplina) {
        if (disciplina == null) return;
        
        if (disciplina.isOptativa()) {
            creditosOptativosCompletos += disciplina.getQuantidadeCreditos();
        } else {
            creditosObrigatoriosCompletos += disciplina.getQuantidadeCreditos();
        }
        
        if (disciplinas != null) {
            disciplinas.removeIf(d -> d.getId().equals(disciplina.getId()));
            recalcularCreditos();
        }
    }

    public int getCreditosDisponiveis() {
        return LIMITE_CREDITOS_POR_SEMESTRE - calcularCreditosAtuais();
    }

    public double getProgressoGraduacao() {
        int totalCompletos = calcularTotalCreditosCompletos();
        return (double) totalCompletos / CREDITOS_MINIMOS_PARA_GRADUACAO * 100;
    }

    // === BUILDER ATUALIZADO ===
    public static class Builder {
        private Long id;
        private String email;
        private String senha;
        private String nome;
        private Curso curso;
        private List<Disciplina> disciplinas;
        private int creditosAtuais = 0;
        private int creditosObrigatoriosCompletos = 0;
        private int creditosOptativosCompletos = 0;

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

        public Builder disciplinas(List<Disciplina> disciplinas) {
            this.disciplinas = disciplinas;
            return this;
        }

        public Builder creditosAtuais(int creditosAtuais) {
            this.creditosAtuais = creditosAtuais;
            return this;
        }

        public Builder creditosObrigatoriosCompletos(int creditosObrigatoriosCompletos) {
            this.creditosObrigatoriosCompletos = creditosObrigatoriosCompletos;
            return this;
        }

        public Builder creditosOptativosCompletos(int creditosOptativosCompletos) {
            this.creditosOptativosCompletos = creditosOptativosCompletos;
            return this;
        }

        public Builder adicionarDisciplina(Disciplina disciplina) {
            if (this.disciplinas == null) {
                this.disciplinas = new ArrayList<>();
            }
            this.disciplinas.add(disciplina);
            return this;
        }

        public Aluno build() {
            ExceptionHandler handler = new ExceptionHandler();
            if (nome == null || nome.trim().isEmpty()) {
                handler.campoObrigatorio("Nome do aluno");
            }
            if (email == null || email.trim().isEmpty()) {
                handler.campoObrigatorio("Email do aluno");
            }
            if (senha == null || senha.trim().isEmpty()) {
                handler.campoObrigatorio("Senha do aluno");
            }

            Aluno aluno = new Aluno(this);
            aluno.recalcularCreditos(); 
            return aluno;
        }
    }

    // === MÉTODOS EXISTENTES ATUALIZADOS ===
    public Codigo matricularDisciplinasObrigatorias(List<Disciplina> disciplinas) {
        if (disciplinas == null || disciplinas.isEmpty()) {
            return Codigo.DADOS_INVALIDOS_400;
        }

        long obrigatoriasAtuais = this.disciplinas.stream()
                .filter(d -> !d.isOptativa())
                .count();

        if (obrigatoriasAtuais + disciplinas.size() > QUANTIDADE_MAXIMA_DISCIPLINA_OBRIGATORIA) {
            return Codigo.DISCIPLINA_LOTADA_409;
        }

        int creditosNovos = disciplinas.stream()
                .mapToInt(Disciplina::getQuantidadeCreditos)
                .sum();

        if (calcularCreditosAtuais() + creditosNovos > LIMITE_CREDITOS_POR_SEMESTRE) {
            return Codigo.DISCIPLINA_LOTADA_409;
        }

        this.disciplinas.addAll(disciplinas);
        recalcularCreditos();

        return Codigo.DISCIPLINA_CRIADA_201;
    }

    public Codigo matricularDisciplinasOptativas(List<Disciplina> disciplinas) {
        if (disciplinas == null || disciplinas.isEmpty()) {
            return Codigo.DADOS_INVALIDOS_400;
        }

        long optativasAtuais = this.disciplinas.stream()
                .filter(Disciplina::isOptativa)
                .count();

        if (optativasAtuais + disciplinas.size() > QUANTIDADE_MAXIMA_DISCIPLINA_OPTATIVA) {
            return Codigo.DISCIPLINA_LOTADA_409;
        }

        int creditosNovos = disciplinas.stream()
                .mapToInt(Disciplina::getQuantidadeCreditos)
                .sum();

        if (calcularCreditosAtuais() + creditosNovos > LIMITE_CREDITOS_POR_SEMESTRE) {
            return Codigo.DISCIPLINA_LOTADA_409;
        }

        this.disciplinas.addAll(disciplinas);
        recalcularCreditos();

        return Codigo.DISCIPLINA_CRIADA_201;
    }

    public Curso verificarAtributosDoCurso() {
        return this.curso;
    }

    public Codigo cancelarMatricula() {
        if (disciplinas == null || disciplinas.isEmpty()) {
            return Codigo.DISCIPLINA_NAO_ENCONTRADA_404;
        }

        disciplinas.clear();
        recalcularCreditos();

        return Codigo.OK_200;
    }

    // === MÉTODOS DE RELATÓRIO ===
    public String gerarRelatorioCreditos() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DE CRÉDITOS ===\n");
        sb.append("Aluno: ").append(getNome()).append("\n");
        sb.append("Curso: ").append(curso != null ? curso.getNome().getNomeCurso() : "Não definido").append("\n\n");
        
        sb.append("CRÉDITOS ATUAIS:\n");
        sb.append("- Obrigatórios: ").append(calcularCreditosObrigatoriosAtuais()).append("\n");
        sb.append("- Optativos: ").append(calcularCreditosOptativosAtuais()).append("\n");
        sb.append("- Total atual: ").append(calcularCreditosAtuais()).append("/").append(LIMITE_CREDITOS_POR_SEMESTRE).append("\n\n");
        
        sb.append("CRÉDITOS COMPLETOS:\n");
        sb.append("- Obrigatórios: ").append(creditosObrigatoriosCompletos).append("\n");
        sb.append("- Optativos: ").append(creditosOptativosCompletos).append("\n");
        sb.append("- Total completo: ").append(calcularTotalCreditosCompletos()).append("/").append(CREDITOS_MINIMOS_PARA_GRADUACAO).append("\n\n");
        
        sb.append("PROGRESSO:\n");
        sb.append("- Créditos restantes: ").append(calcularCreditosRestantes()).append("\n");
        sb.append("- Progresso: ").append(String.format("%.1f", getProgressoGraduacao())).append("%\n");
        sb.append("- Pode graduar: ").append(podeGraduar() ? "SIM" : "NÃO").append("\n");
        
        return sb.toString();
    }
}
