package enums;

public enum Codigo {
    OK_200("OK"),
    ERRO_404("NOT_FOUND"),
    ERRO_405("METHOD_NOT_ALLOWED"),
    ERRO_500("INTERNAL_SERVER_ERROR"),
    ERRO_406("DISCIPLINA_JA_EXISTE"),
    ERRO_407("PROFESSOR_JA_EXISTE"),
    ERRO_408("ALUNO_JA_EXISTE"),
    ERRO_409("DISCIPLINA_NAO_ENCONTRADA"),
    ERRO_410("PROFESSOR_NAO_ENCONTRADO"),
    ERRO_411("ALUNO_NAO_ENCONTRADO");

    private final String mensagem;

    Codigo(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}

