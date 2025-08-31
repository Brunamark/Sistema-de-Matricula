package exceptions;

import enums.Codigo;


public class ResultadoOperacao {
    private final boolean sucesso;
    private final Codigo codigo;
    private final String mensagem;
    private final String logDetalhado;
    private final Object dados;
    
    public ResultadoOperacao(boolean sucesso, Codigo codigo, String mensagem, String logDetalhado) {
        this(sucesso, codigo, mensagem, logDetalhado, null);
    }
    
    public ResultadoOperacao(boolean sucesso, Codigo codigo, String mensagem, String logDetalhado, Object dados) {
        this.sucesso = sucesso;
        this.codigo = codigo;
        this.mensagem = mensagem;
        this.logDetalhado = logDetalhado;
        this.dados = dados;
    }
    
    public static ResultadoOperacao sucesso(String mensagem) {
        return new ResultadoOperacao(true, Codigo.OK_200, mensagem, null);
    }
    
    public static ResultadoOperacao sucesso(String mensagem, Object dados) {
        return new ResultadoOperacao(true, Codigo.OK_200, mensagem, null, dados);
    }
    
    public static ResultadoOperacao erro(Codigo codigo, String mensagem) {
        return new ResultadoOperacao(false, codigo, mensagem, null);
    }
    
    public static ResultadoOperacao erro(Codigo codigo, String mensagem, String logDetalhado) {
        return new ResultadoOperacao(false, codigo, mensagem, logDetalhado);
    }
    
    public boolean isSucesso() {
        return sucesso;
    }
    
    public Codigo getCodigo() {
        return codigo;
    }
    
    public String getMensagem() {
        return mensagem;
    }
    
    public String getLogDetalhado() {
        return logDetalhado;
    }
    
    public Object getDados() {
        return dados;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getDados(Class<T> tipo) {
        return (T) dados;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResultadoOperacao{");
        sb.append("sucesso=").append(sucesso);
        sb.append(", codigo=").append(codigo);
        sb.append(", mensagem='").append(mensagem).append('\'');
        if (dados != null) {
            sb.append(", dados=").append(dados);
        }
        sb.append('}');
        return sb.toString();
    }
}
