package exceptions;

import enums.Codigo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExceptionHandler {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private boolean logDetalhado = true;
    
    // === DISCIPLINA ===
    public ResultadoOperacao disciplinaJaExiste(String nomeDisciplina) {
        return criarResultadoErro(
            Codigo.DISCIPLINA_JA_EXISTE_406,
            "Disciplina '" + nomeDisciplina + "' já existe no sistema",
            "DISCIPLINA_DUPLICADA"
        );
    }
    
    public ResultadoOperacao disciplinaNaoEncontrada(String nomeDisciplina) {
        return criarResultadoErro(
            Codigo.DISCIPLINA_NAO_ENCONTRADA_404,
            "Disciplina '" + nomeDisciplina + "' não foi encontrada",
            "DISCIPLINA_NAO_ENCONTRADA"
        );
    }
    
    public ResultadoOperacao disciplinaCriada(Object disciplina) {
        return criarResultadoSucesso(
            Codigo.DISCIPLINA_CRIADA_201,
            "Disciplina criada com sucesso", 
            disciplina
        );
    }
    
    public ResultadoOperacao disciplinaInativa(String nomeDisciplina) {
        return criarResultadoErro(
            Codigo.DISCIPLINA_INATIVA_409,
            "Disciplina '" + nomeDisciplina + "' está inativa",
            "DISCIPLINA_INATIVA"
        );
    }
    
    // === PROFESSOR ===
    public ResultadoOperacao professorJaExiste(String nomeProfessor) {
        return criarResultadoErro(
            Codigo.PROFESSOR_JA_EXISTE_406,
            "Professor '" + nomeProfessor + "' já existe no sistema",
            "PROFESSOR_DUPLICADO"
        );
    }
    
    public ResultadoOperacao professorNaoEncontrado(String nomeProfessor) {
        return criarResultadoErro(
            Codigo.PROFESSOR_NAO_ENCONTRADO_404,
            "Professor '" + nomeProfessor + "' não foi encontrado",
            "PROFESSOR_NAO_ENCONTRADO"
        );
    }
    
    public ResultadoOperacao professorCriado(Object professor) {
        return criarResultadoSucesso(
            Codigo.PROFESSOR_CRIADO_201,
            "Professor criado com sucesso", 
            professor
        );
    }
    
    public ResultadoOperacao professorSemPermissao(String operacao) {
        return criarResultadoErro(
            Codigo.PROFESSOR_SEM_PERMISSAO_403,
            "Professor sem permissão para: " + operacao,
            "PROFESSOR_SEM_PERMISSAO"
        );
    }
    
    // === ALUNO ===
    public ResultadoOperacao alunoJaExiste(String nomeAluno) {
        return criarResultadoErro(
            Codigo.ALUNO_JA_EXISTE_406,
            "Aluno '" + nomeAluno + "' já existe no sistema",
            "ALUNO_DUPLICADO"
        );
    }
    
    public ResultadoOperacao alunoNaoEncontrado(String nomeAluno) {
        return criarResultadoErro(
            Codigo.ALUNO_NAO_ENCONTRADO_404,
            "Aluno '" + nomeAluno + "' não foi encontrado",
            "ALUNO_NAO_ENCONTRADO"
        );
    }
    
    public ResultadoOperacao alunoCriado(Object aluno) {
        return criarResultadoSucesso(
            Codigo.ALUNO_CRIADO_201,
            "Aluno criado com sucesso", 
            aluno
        );
    }
    
    public ResultadoOperacao alunoJaMatriculado(String nomeAluno, String nomeDisciplina) {
        return criarResultadoErro(
            Codigo.ALUNO_JA_MATRICULADO_409,
            String.format("Aluno '%s' já está matriculado na disciplina '%s'", nomeAluno, nomeDisciplina),
            "ALUNO_JA_MATRICULADO"
        );
    }
    
    public ResultadoOperacao alunoNaoMatriculado(String nomeAluno, String nomeDisciplina) {
        return criarResultadoErro(
            Codigo.ALUNO_NAO_MATRICULADO_404,
            String.format("Aluno '%s' não está matriculado na disciplina '%s'", nomeAluno, nomeDisciplina),
            "ALUNO_NAO_MATRICULADO"
        );
    }
    
    // === CURSO ===
    public ResultadoOperacao cursoJaExiste(String nomeCurso) {
        return criarResultadoErro(
            Codigo.CURSO_JA_EXISTE_406,
            "Curso '" + nomeCurso + "' já existe no sistema",
            "CURSO_DUPLICADO"
        );
    }
    
    public ResultadoOperacao cursoNaoEncontrado(String nomeCurso) {
        return criarResultadoErro(
            Codigo.CURSO_NAO_ENCONTRADO_404,
            "Curso '" + nomeCurso + "' não foi encontrado",
            "CURSO_NAO_ENCONTRADO"
        );
    }
    
    public ResultadoOperacao cursoCriado(Object curso) {
        return criarResultadoSucesso(
            Codigo.CURSO_CRIADO_201,
            "Curso criado com sucesso", 
            curso
        );
    }
    
    public ResultadoOperacao cursoComAlunos(String nomeCurso) {
        return criarResultadoErro(
            Codigo.CURSO_COM_ALUNOS_409,
            "Não é possível excluir o curso '" + nomeCurso + "' pois possui alunos matriculados",
            "CURSO_COM_ALUNOS"
        );
    }
    
    // === SECRETÁRIO ===
    public ResultadoOperacao secretarioJaExiste(String nomeSecretario) {
        return criarResultadoErro(
            Codigo.SECRETARIO_JA_EXISTE_406,
            "Secretário '" + nomeSecretario + "' já existe no sistema",
            "SECRETARIO_DUPLICADO"
        );
    }
    
    public ResultadoOperacao secretarioNaoEncontrado(String nomeSecretario) {
        return criarResultadoErro(
            Codigo.SECRETARIO_NAO_ENCONTRADO_404,
            "Secretário '" + nomeSecretario + "' não foi encontrado",
            "SECRETARIO_NAO_ENCONTRADO"
        );
    }
    
    public ResultadoOperacao secretarioCriado(Object secretario) {
        return criarResultadoSucesso(
            Codigo.SECRETARIO_CRIADO_201,
            "Secretário criado com sucesso", 
            secretario
        );
    }
    
    // === MATRÍCULA ===
    public ResultadoOperacao matriculaRealizada(String nomeAluno, String nomeDisciplina) {
        return criarResultadoSucesso(
            Codigo.MATRICULA_REALIZADA_201,
            String.format("Aluno '%s' matriculado na disciplina '%s' com sucesso", nomeAluno, nomeDisciplina),
            null
        );
    }
    
    public ResultadoOperacao matriculaCancelada(String nomeAluno, String nomeDisciplina) {
        return criarResultadoSucesso(
            Codigo.MATRICULA_CANCELADA_200,
            String.format("Matrícula do aluno '%s' na disciplina '%s' foi cancelada", nomeAluno, nomeDisciplina),
            null
        );
    }
    
    public ResultadoOperacao limiteMatriculasExcedido(int limite) {
        return criarResultadoErro(
            Codigo.LIMITE_MATRICULAS_EXCEDIDO_422,
            String.format("Limite de matrículas por semestre excedido. Máximo permitido: %d", limite),
            "LIMITE_MATRICULAS_EXCEDIDO"
        );
    }
    
    public ResultadoOperacao preRequisitoNaoAtendido(String disciplina, String preRequisito) {
        return criarResultadoErro(
            Codigo.PRE_REQUISITO_NAO_ATENDIDO_422,
            String.format("Pré-requisito '%s' não atendido para a disciplina '%s'", preRequisito, disciplina),
            "PRE_REQUISITO_NAO_ATENDIDO"
        );
    }
    
    public ResultadoOperacao horarioConflito(String disciplina1, String disciplina2) {
        return criarResultadoErro(
            Codigo.HORARIO_CONFLITO_409,
            String.format("Conflito de horário entre as disciplinas '%s' e '%s'", disciplina1, disciplina2),
            "HORARIO_CONFLITO"
        );
    }
    
    public ResultadoOperacao limiteCreditosExcedido(String disciplina) {
        return criarResultadoErro(
            Codigo.LIMITE_CREDITOS_EXCEDIDO_422,
            "Limite de créditos excedido para a disciplina: " + disciplina,
            "LIMITE_CREDITOS_EXCEDIDO"
        );
    }
    
    // === AUTENTICAÇÃO ===
    public ResultadoOperacao loginSucesso(Object usuario) {
        return criarResultadoSucesso(
            Codigo.LOGIN_SUCESSO_200,
            "Login realizado com sucesso",
            usuario
        );
    }
    
    public ResultadoOperacao credenciaisInvalidas() {
        return criarResultadoErro(
            Codigo.CREDENCIAIS_INVALIDAS_401,
            "E-mail ou senha inválidos",
            "CREDENCIAIS_INVALIDAS"
        );
    }
    
    public ResultadoOperacao senhaAlterada() {
        return criarResultadoSucesso(
            Codigo.SENHA_ALTERADA_200,
            "Senha alterada com sucesso",
            null
        );
    }
    
    public ResultadoOperacao senhaAtualIncorreta() {
        return criarResultadoErro(
            Codigo.SENHA_ATUAL_INCORRETA_401,
            "Senha atual incorreta",
            "SENHA_ATUAL_INCORRETA"
        );
    }
    
    // === VALIDAÇÕES ===
    public ResultadoOperacao emailInvalido(String email) {
        return criarResultadoErro(
            Codigo.EMAIL_INVALIDO_422,
            "Formato de e-mail inválido: " + email,
            "EMAIL_INVALIDO"
        );
    }
    
    public ResultadoOperacao senhaFraca() {
        return criarResultadoErro(
            Codigo.SENHA_FRACA_422,
            "Senha deve ter pelo menos 6 caracteres",
            "SENHA_FRACA"
        );
    }
    
    public ResultadoOperacao nomeMuitoCurto(String nome) {
        return criarResultadoErro(
            Codigo.NOME_MUITO_CURTO_422,
            "Nome '" + nome + "' deve ter pelo menos 2 caracteres",
            "NOME_MUITO_CURTO"
        );
    }
    
    public ResultadoOperacao creditosInvalidos(int creditos) {
        return criarResultadoErro(
            Codigo.CREDITOS_INVALIDOS_422,
            "Quantidade de créditos inválida: " + creditos + ". Deve ser maior que zero",
            "CREDITOS_INVALIDOS"
        );
    }
    
    // === GENÉRICOS ===
    public ResultadoOperacao dadosNulos(String entidade) {
        return criarResultadoErro(
            Codigo.DADOS_NULOS_400,
            entidade + " não pode ser nulo",
            "DADOS_NULOS"
        );
    }
    
    public ResultadoOperacao campoObrigatorio(String campo) {
        return criarResultadoErro(
            Codigo.CAMPO_OBRIGATORIO_422,
            "Campo '" + campo + "' é obrigatório",
            "CAMPO_OBRIGATORIO"
        );
    }
    
    public ResultadoOperacao dadosInvalidos(String mensagem) {
        return criarResultadoErro(
            Codigo.DADOS_INVALIDOS_400,
            "Dados inválidos: " + mensagem,
            "DADOS_INVALIDOS"
        );
    }
    
    public ResultadoOperacao acessoNegado(String operacao, String mensagem) {
        return criarResultadoErro(
            Codigo.ACESSO_NEGADO_403,
            "Acesso negado para operação: " + operacao + ". " + mensagem,
            "ACESSO_NEGADO"
        );
    }
    
    public ResultadoOperacao naoAutorizado(String mensagem) {
        return criarResultadoErro(
            Codigo.NAO_AUTORIZADO_401,
            "Usuário não autorizado: " + mensagem,
            "NAO_AUTORIZADO"
        );
    }
    
    public ResultadoOperacao metodoNaoPermitido(String metodo) {
        return criarResultadoErro(
            Codigo.METODO_NAO_PERMITIDO_405,
            "Método não permitido: " + metodo,
            "METODO_NAO_PERMITIDO"
        );
    }
    
    public ResultadoOperacao operacaoNaoPermitida(String operacao) {
        return criarResultadoErro(
            Codigo.OPERACAO_NAO_PERMITIDA_405,
            "Operação não permitida: " + operacao,
            "OPERACAO_NAO_PERMITIDA"
        );
    }
    
    public ResultadoOperacao erroInterno(String detalhes) {
        return criarResultadoErro(
            Codigo.ERRO_INTERNO_500,
            "Erro interno do sistema",
            "ERRO_INTERNO",
            detalhes
        );
    }

    public ResultadoOperacao servicoIndisponivel(String servico) {
        return criarResultadoErro(
            Codigo.SERVICO_INDISPONIVEL_503,
            "Serviço temporariamente indisponível: " + servico,
            "SERVICO_INDISPONIVEL"
        );
    }
    
    public ResultadoOperacao sistemaManutencao() {
        return criarResultadoErro(
            Codigo.SISTEMA_MANUTENCAO_503,
            "Sistema em manutenção. Tente novamente mais tarde",
            "SISTEMA_MANUTENCAO"
        );
    }
    
    // === SUCESSO GENÉRICO ===
    public ResultadoOperacao sucesso(String mensagem) {
        return criarResultadoSucesso(Codigo.OK_200, mensagem, null);
    }
    
    public ResultadoOperacao sucesso(Object dados) {
        return criarResultadoSucesso(Codigo.OK_200, "Operação realizada com sucesso", dados);
    }
    
    public ResultadoOperacao sucesso(String mensagem, Object dados) {
        return criarResultadoSucesso(Codigo.OK_200, mensagem, dados);
    }
    
    public ResultadoOperacao atualizado(String entidade) {
        return criarResultadoSucesso(
            Codigo.ATUALIZADO_202,
            entidade + " atualizado com sucesso",
            null
        );
    }
    
    public ResultadoOperacao excluido(String entidade) {
        return criarResultadoSucesso(
            Codigo.EXCLUIDO_204,
            entidade + " excluído com sucesso",
            null
        );
    }
    
    // === MÉTODOS AUXILIARES ===
    private ResultadoOperacao criarResultadoErro(Codigo codigo, String mensagem, String tipoErro) {
        return criarResultadoErro(codigo, mensagem, tipoErro, null);
    }
    
    private ResultadoOperacao criarResultadoErro(Codigo codigo, String mensagem, String tipoErro, String detalhes) {
        String log = criarLogErro(tipoErro, mensagem, detalhes);
        
        if (logDetalhado) {
            System.err.println(log);
        }
        
        return new ResultadoOperacao(false, codigo, mensagem, log);
    }
    
    private ResultadoOperacao criarResultadoSucesso(Codigo codigo, String mensagem, Object dados) {
        String log = criarLogSucesso(mensagem);
        
        if (logDetalhado) {
            System.out.println(log);
        }
        
        return new ResultadoOperacao(true, codigo, mensagem, log, dados);
    }
    
    private String criarLogErro(String tipo, String mensagem, String detalhes) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(LocalDateTime.now().format(FORMATTER)).append("] ");
        sb.append("[ERRO] [").append(tipo).append("] ");
        sb.append(mensagem);
        
        if (detalhes != null && !detalhes.trim().isEmpty()) {
            sb.append(" | Detalhes: ").append(detalhes);
        }
        
        return sb.toString();
    }
    
    private String criarLogSucesso(String mensagem) {
        return String.format("[%s] [SUCESSO] %s",
            LocalDateTime.now().format(FORMATTER),
            mensagem
        );
    }
    
    public boolean isLogDetalhado() {
        return logDetalhado;
    }
    
    public void setLogDetalhado(boolean logDetalhado) {
        this.logDetalhado = logDetalhado;
    }
}
