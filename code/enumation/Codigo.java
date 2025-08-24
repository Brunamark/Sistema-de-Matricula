package enumation;

public enum Codigo {
    OK_200("OK"),
    ERRO_404("NOTFOUND");

    private final String mensagem;

    Codigo(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}

