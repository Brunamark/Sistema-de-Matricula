package enums;

public enum Cursos {
    ENGENHARIA_DE_SOFTWARE("Engenharia de Software"),
    CIENCIA_DA_COMPUTACAO("Ciência da Computação"),
    ENGENHARIA_DA_COMPUTACAO("Engenharia da Computação");

    private final String nomeCurso;

    Cursos(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }
}

