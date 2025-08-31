package enums;

public enum Codigo {
    OK_200("Operação realizada com sucesso"),
    CRIADO_201("Recurso criado com sucesso"),
    ATUALIZADO_202("Recurso atualizado com sucesso"),
    EXCLUIDO_204("Recurso excluído com sucesso"),
    
    DADOS_INVALIDOS_400("Dados inválidos fornecidos"),
    NAO_AUTORIZADO_401("Usuário não autorizado"),
    ACESSO_NEGADO_403("Acesso negado para esta operação"),
    NAO_ENCONTRADO_404("Recurso não encontrado"),
    METODO_NAO_PERMITIDO_405("Método não permitido"),
    JA_EXISTE_406("Recurso já existe no sistema"),
    CONFLITO_409("Conflito de dados detectado"),
    CAMPO_OBRIGATORIO_422("Campo obrigatório não informado"),
    
    ERRO_INTERNO_500("Erro interno do servidor"),
    NAO_IMPLEMENTADO_501("Funcionalidade não implementada"),
    ERRO_BANCO_DADOS_502("Erro ao acessar banco de dados"),
    SERVICO_INDISPONIVEL_503("Serviço temporariamente indisponível"),
    
    
    DISCIPLINA_CRIADA_201("Disciplina criada com sucesso"),
    DISCIPLINA_JA_EXISTE_406("Disciplina já existe"),
    DISCIPLINA_NAO_ENCONTRADA_404("Disciplina não encontrada"),
    DISCIPLINA_INATIVA_409("Disciplina está inativa"),
    LIMITE_CREDITOS_EXCEDIDO_422("Limite de créditos da disciplina excedido"),
    
    PROFESSOR_CRIADO_201("Professor criado com sucesso"),
    PROFESSOR_JA_EXISTE_406("Professor já existe"),
    PROFESSOR_NAO_ENCONTRADO_404("Professor não encontrado"),
    PROFESSOR_SEM_PERMISSAO_403("Professor sem permissão para esta disciplina"),
    
    ALUNO_CRIADO_201("Aluno criado com sucesso"),
    ALUNO_JA_EXISTE_406("Aluno já existe"),
    ALUNO_NAO_ENCONTRADO_404("Aluno não encontrado"),
    ALUNO_JA_MATRICULADO_409("Aluno já matriculado nesta disciplina"),
    ALUNO_NAO_MATRICULADO_404("Aluno não está matriculado nesta disciplina"),
    
    CURSO_CRIADO_201("Curso criado com sucesso"),
    CURSO_JA_EXISTE_406("Curso já existe"),
    CURSO_NAO_ENCONTRADO_404("Curso não encontrado"),
    CURSO_COM_ALUNOS_409("Não é possível excluir curso com alunos matriculados"),
    
    SECRETARIO_CRIADO_201("Secretário criado com sucesso"),
    SECRETARIO_JA_EXISTE_406("Secretário já existe"),
    SECRETARIO_NAO_ENCONTRADO_404("Secretário não encontrado"),
    
    MATRICULA_REALIZADA_201("Matrícula realizada com sucesso"),
    MATRICULA_CANCELADA_200("Matrícula cancelada com sucesso"),
    LIMITE_MATRICULAS_EXCEDIDO_422("Limite de matrículas por semestre excedido"),
    PRE_REQUISITO_NAO_ATENDIDO_422("Pré-requisito da disciplina não atendido"),
    HORARIO_CONFLITO_409("Conflito de horário detectado"),
    
    LOGIN_SUCESSO_200("Login realizado com sucesso"),
    CREDENCIAIS_INVALIDAS_401("E-mail ou senha inválidos"),
    SENHA_ALTERADA_200("Senha alterada com sucesso"),
    SENHA_ATUAL_INCORRETA_401("Senha atual incorreta"),
    
    EMAIL_INVALIDO_422("Formato de e-mail inválido"),
    SENHA_FRACA_422("Senha deve ter pelo menos 6 caracteres"),
    NOME_MUITO_CURTO_422("Nome deve ter pelo menos 2 caracteres"),
    CREDITOS_INVALIDOS_422("Quantidade de créditos deve ser maior que zero"),
    
    DADOS_NULOS_400("Dados obrigatórios não podem ser nulos"),
    OPERACAO_NAO_PERMITIDA_405("Operação não permitida no estado atual"),
    LIMITE_TENTATIVAS_EXCEDIDO_429("Limite de tentativas excedido"),
    BACKUP_REALIZADO_200("Backup dos dados realizado com sucesso"),
    SISTEMA_MANUTENCAO_503("Sistema em manutenção");

    private final String mensagem;

    Codigo(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
    
    public boolean isSucesso() {
        return name().startsWith("OK_") || 
               name().contains("_200") || 
               name().contains("_201") || 
               name().contains("_202") || 
               name().contains("_204");
    }
    
    public boolean isErroCliente() {
        return name().contains("_4");
    }
    
    public boolean isErroServidor() {
        return name().contains("_5");
    }
    
    public int getCodigoNumerico() {
        String codigo = name().replaceAll(".*_(\\d+).*", "$1");
        try {
            return Integer.parseInt(codigo);
        } catch (NumberFormatException e) {
            return 200;
        }
    }
}

