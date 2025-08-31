
import entity.*;
import enums.Cursos;

public class Main {
    public static void main(String[] args) {
        
        // === EXEMPLO DE USO DOS BUILDERS ===
        
        // 1. Criando um Curso usando Builder
        Curso engenhariaSoftware = Curso.builder()
                .id(1L)
                .nome(Cursos.ENGENHARIA_DE_SOFTWARE)
                .credito(240)
                .build();

        // 2. Criando um Professor usando Builder
        Professor professor1 = Professor.builder()
                .id(10L)
                .nome("Dr. João Santos")
                .email("joao.santos@universidade.edu.br")
                .senha("prof123")
                .build();

        // 3. Criando Disciplinas usando Builder
        Disciplina programacao = Disciplina.builder()
                .id(1L)
                .nome("Programação Orientada a Objetos")
                .quantidadeCreditos(4)
                .professor(professor1)
                .optativa(false)
                .ativa(true)
                .build();

        Disciplina algoritmos = Disciplina.builder()
                .id(2L)
                .nome("Algoritmos e Estruturas de Dados")
                .quantidadeCreditos(6)
                .professor(professor1)
                .optativa(false)
                .ativa(true)
                .build();

        // 4. Criando Alunos usando Builder
        Aluno aluno1 = Aluno.builder()
                .id(100L)
                .nome("Maria Silva")
                .email("maria.silva@universidade.edu.br")
                .senha("aluna123")
                .curso(engenhariaSoftware)
                .adicionarDisciplina(programacao)
                .adicionarDisciplina(algoritmos)
                .build();

        Aluno aluno2 = Aluno.builder()
                .nome("Pedro Santos")
                .email("pedro.santos@universidade.edu.br")
                .senha("aluno456")
                .curso(engenhariaSoftware)
                .build();

        // 5. Criando um Secretário usando Builder
        Secretario secretario = Secretario.builder()
                .id(1L)
                .nome("Ana Administradora")
                .email("ana.admin@universidade.edu.br")
                .senha("admin789")
                .build();

        // 6. Atualizando o curso com disciplinas e alunos
        engenhariaSoftware.adicionarDisciplina(programacao);
        engenhariaSoftware.adicionarDisciplina(algoritmos);
        engenhariaSoftware.adicionarAluno(aluno1);
        engenhariaSoftware.adicionarAluno(aluno2);

        // === EXIBINDO INFORMAÇÕES ===
        System.out.println("=== CURSO ===");
        System.out.println("Nome: " + engenhariaSoftware.getNome().getNomeCurso());
        System.out.println("Créditos: " + engenhariaSoftware.getCredito());
        System.out.println("Disciplinas: " + engenhariaSoftware.getDisciplinas().size());
        System.out.println("Alunos: " + engenhariaSoftware.getAlunos().size());

        System.out.println("\n=== PROFESSOR ===");
        System.out.println("Nome: " + professor1.getNome());
        System.out.println("Email: " + professor1.getEmail());

        System.out.println("\n=== DISCIPLINAS ===");
        System.out.println("1. " + programacao.getNome() + " - " + programacao.getQuantidadeCreditos() + " créditos");
        System.out.println("2. " + algoritmos.getNome() + " - " + algoritmos.getQuantidadeCreditos() + " créditos");

        System.out.println("\n=== ALUNOS ===");
        System.out.println("1. " + aluno1.getNome() + " - Disciplinas: " + aluno1.getDisciplinas().size());
        System.out.println("2. " + aluno2.getNome() + " - Disciplinas: " + aluno2.getDisciplinas().size());

        System.out.println("\n=== SECRETÁRIO ===");
        System.out.println("Nome: " + secretario.getNome());
        System.out.println("Email: " + secretario.getEmail());
    }
}
