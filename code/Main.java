import entity.*;
import service.SisMatImp;
import exceptions.ResultadoOperacao;

import java.util.Scanner;

public class Main {
    private static SisMatImp sistema = new SisMatImp();
    private static Scanner scanner = new Scanner(System.in);
    private static Usuario usuarioLogado = null;

    public static void main(String[] args) {
        
        System.out.println("=== SISTEMA DE MATRÍCULA UNIVERSITÁRIA === 🎓");
        System.out.println("Bem-vindo ao SisMat!");
        
        while (true) {
            if (usuarioLogado == null) {
                exibirMenuLogin();
            } else {
                exibirMenuPrincipal();
            }
        }
    }
    
    private static void exibirMenuLogin() {
        while (usuarioLogado == null) {
            System.out.println("\n=== TELA DE LOGIN ===");
            System.out.println("1. Fazer Login");
            System.out.println("2. Sair do Sistema");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInt();
            
            switch (opcao) {
                case 1:
                    fazerLogin();
                    break;
                case 2:
                    System.out.println("Obrigado por usar o SisMat!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void fazerLogin() {
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        ResultadoOperacao resultado = sistema.efetuarLogin(email, senha);
        
        if (resultado.isSucesso()) {
            usuarioLogado = (Usuario) resultado.getDados();
            System.out.println(resultado.getMensagem());
            System.out.println("Bem-vindo, " + usuarioLogado.getNome() + 
                             " (" + usuarioLogado.getClass().getSimpleName() + ")!");
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void exibirMenuPrincipal() {
        while (usuarioLogado != null) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("Usuário: " + usuarioLogado.getNome() + 
                             " (" + usuarioLogado.getClass().getSimpleName() + ")");
            
            if (usuarioLogado instanceof Secretario) {
                exibirMenuSecretario();
            } else if (usuarioLogado instanceof Professor) {
                exibirMenuProfessor();
            } else if (usuarioLogado instanceof Aluno) {
                exibirMenuAluno();
            }
            
            System.out.println("0. Logout");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInt();
            
            if (opcao == 0) {
                logout();
            } else {
                processarOpcao(opcao);
            }
        }
    }

    private static void exibirMenuSecretario() {
        System.out.println("\n === GERENCIAMENTO DE DISCIPLINAS ===");
        System.out.println("1. Criar Disciplina");
        System.out.println("2. Editar Disciplina");
        System.out.println("3. Excluir Disciplina");
        System.out.println("4. Buscar Disciplina por ID");
        System.out.println("5. Buscar Disciplina por Nome");
        System.out.println("6. Listar Todas as Disciplinas");
        
        System.out.println("\n === GERENCIAMENTO DE PROFESSORES ===");
        System.out.println("7. Criar Professor");
        System.out.println("8. Editar Professor");
        System.out.println("9. Excluir Professor");
        System.out.println("10. Buscar Professor por ID");
        System.out.println("11. Buscar Professor por Nome");
        
        System.out.println("\n === GERENCIAMENTO DE ALUNOS ===");
        System.out.println("12. Criar Aluno");
        System.out.println("13. Editar Aluno");
        System.out.println("14. Excluir Aluno");
        System.out.println("15. Buscar Aluno por ID");
        System.out.println("16. Buscar Aluno por Nome");
        
        System.out.println("\n=== RELATÓRIOS ===");
        System.out.println("17. Gerar Currículo Semestral");
        System.out.println("18. Listar Disciplinas de um Curso");
    }

    private static void exibirMenuProfessor() {
        System.out.println("\n=== ÁREA DO PROFESSOR ===");
        System.out.println("1. Ver Minhas Disciplinas");
        System.out.println("2. Buscar Disciplina por ID");
        System.out.println("3. Buscar Aluno por ID");
        System.out.println("4. Ver Informações de Aluno");
    }

    private static void exibirMenuAluno() {
        System.out.println("\n=== ÁREA DO ALUNO ===");
        System.out.println("1. Ver Minhas Disciplinas");
        System.out.println("2. Buscar Disciplina por ID");
        System.out.println("3. Ver Meu Curso");
        System.out.println("4. Buscar Professor por ID");
    }

    private static void processarOpcao(int opcao) {
        if (usuarioLogado instanceof Secretario) {
            processarOpcaoSecretario(opcao);
        } else if (usuarioLogado instanceof Professor) {
            processarOpcaoProfessor(opcao);
        } else if (usuarioLogado instanceof Aluno) {
            processarOpcaoAluno(opcao);
        }
    }

    private static void processarOpcaoSecretario(int opcao) {
        switch (opcao) {
            case 1: criarDisciplina(); break;
            case 2: editarDisciplina(); break;
            case 3: excluirDisciplina(); break;
            case 4: buscarDisciplinaPorId(); break;
            case 5: buscarDisciplinaPorNome(); break;
            case 6: listarDisciplinas(); break;
            case 7: criarProfessor(); break;
            case 8: editarProfessor(); break;
            case 9: excluirProfessor(); break;
            case 10: buscarProfessorPorId(); break;
            case 11: buscarProfessorPorNome(); break;
            case 12: criarAluno(); break;
            case 13: editarAluno(); break;
            case 14: excluirAluno(); break;
            case 15: buscarAlunoPorId(); break;
            case 16: buscarAlunoPorNome(); break;
            case 17: gerarCurriculoSemestral(); break;
            default: System.out.println("Opção inválida!");
        }
    }

    private static void processarOpcaoProfessor(int opcao) {
        switch (opcao) {
            case 1: listarDisciplinas(); break;
            case 2: buscarDisciplinaPorId(); break;
            case 3: buscarAlunoPorId(); break;
            case 4: buscarAlunoPorNome(); break;
            default: System.out.println("Opção inválida!");
        }
    }

    private static void processarOpcaoAluno(int opcao) {
        switch (opcao) {
            case 1: listarDisciplinas(); break;
            case 2: buscarDisciplinaPorId(); break;
            case 3: verMeuCurso(); break;
            case 4: buscarProfessorPorId(); break;
            default: System.out.println(" Opção inválida!");
        }
    }

    private static void criarDisciplina() {
        System.out.println("\n=== CRIAR DISCIPLINA ===");
        
        System.out.print("Nome da disciplina: ");
        String nome = scanner.nextLine();

        System.out.print("Quantidade de créditos (1-12): ");
        int creditos = lerInt();
        
        System.out.print("É optativa? (true/false): ");
        boolean optativa = lerBoolean();
        
        System.out.print("Está ativa? (true/false): ");
        boolean ativa = lerBoolean();

        ResultadoOperacao resultado = sistema.criarDisciplina(nome, creditos, optativa, ativa, usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            System.out.println(resultado.getMensagem());
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void editarDisciplina() {
        System.out.println("\n=== EDITAR DISCIPLINA ===");
        
        System.out.print("ID da disciplina: ");
        Long id = lerLong();
        
        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Nova quantidade de créditos: ");
        int creditos = lerInt();
        
        System.out.print("É optativa? (true/false): ");
        boolean optativa = lerBoolean();
        
        System.out.print("Está ativa? (true/false): ");
        boolean ativa = lerBoolean();

        ResultadoOperacao resultado = sistema.editarDisciplina(id, nome, creditos, optativa, ativa, usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            System.out.println(resultado.getMensagem());
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void excluirDisciplina() {
        System.out.println("\n === EXCLUIR DISCIPLINA ===");
        
        System.out.print("Nome da disciplina: ");
        String nome = scanner.nextLine();

        ResultadoOperacao resultado = sistema.excluirDisciplina(nome, usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            System.out.println(resultado.getMensagem());
        } else {
            System.out.println( resultado.getMensagem());
        }
    }

    private static void buscarDisciplinaPorId() {
        System.out.println("\n === BUSCAR DISCIPLINA POR ID ===");
        
        System.out.print("ID da disciplina: ");
        Long id = lerLong();

        ResultadoOperacao resultado = sistema.buscarDisciplinaPorId(id, usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            Disciplina disciplina = (Disciplina) resultado.getDados();
            exibirDisciplina(disciplina);
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void buscarDisciplinaPorNome() {
        System.out.println("\n=== BUSCAR DISCIPLINA POR NOME ===");
        
        System.out.print("Nome da disciplina: ");
        String nome = scanner.nextLine();

        ResultadoOperacao resultado = sistema.buscarDisciplinaPorNome(nome, usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            Disciplina disciplina = (Disciplina) resultado.getDados();
            exibirDisciplina(disciplina);
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void listarDisciplinas() {
        System.out.println("\n === LISTA DE DISCIPLINAS ===");

        ResultadoOperacao resultado = sistema.listarDisciplinas(usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            System.out.println(resultado.getMensagem());
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void criarProfessor() {
        System.out.println("\n === CRIAR PROFESSOR ===");
        
        System.out.print("Nome do professor: ");
        String nome = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        ResultadoOperacao resultado = sistema.criarProfessor(nome, email, senha, usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            System.out.println(resultado.getMensagem());
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void editarProfessor() {
        System.out.println("\n=== EDITAR PROFESSOR ===");
        
        System.out.print("ID do professor: ");
        Long id = lerLong();
        
        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Novo email: ");
        String email = scanner.nextLine();
        
        System.out.print("Nova senha: ");
        String senha = scanner.nextLine();

        ResultadoOperacao resultado = sistema.editarProfessor(id, nome, email, senha, usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            System.out.println(resultado.getMensagem());
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void excluirProfessor() {
        System.out.println("\n=== EXCLUIR PROFESSOR ===");
        
        System.out.print("ID do professor: ");
        Long id = lerLong();

        ResultadoOperacao resultado = sistema.excluirProfessor(id, usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            System.out.println(resultado.getMensagem());
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void buscarProfessorPorId() {
        System.out.println("\n=== BUSCAR PROFESSOR POR ID ===");
        
        System.out.print("ID do professor: ");
        Long id = lerLong();

        ResultadoOperacao resultado = sistema.buscarProfessorPorId(id, usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            Professor professor = (Professor) resultado.getDados();
            exibirProfessor(professor);
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void buscarProfessorPorNome() {
        System.out.println("\n=== BUSCAR PROFESSOR POR NOME ===");
        
        System.out.print("Nome do professor: ");
        String nome = scanner.nextLine();

        ResultadoOperacao resultado = sistema.buscarProfessorPorNome(nome, usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            Professor professor = (Professor) resultado.getDados();
            exibirProfessor(professor);
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void criarAluno() {
        System.out.println("\n === CRIAR ALUNO ===");
        
        System.out.print("Nome do aluno: ");
        String nome = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        System.out.print("Curso: ");
        String curso = scanner.nextLine();

        Curso cursoBuscado = (Curso) sistema.buscarCursoPorNome(curso).getDados();
        ResultadoOperacao resultado = sistema.criarAluno(nome, email, senha, cursoBuscado, usuarioLogado.getId());

        if (resultado.isSucesso()) {
            System.out.println(resultado.getMensagem());
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void editarAluno() {
        System.out.println("\n === EDITAR ALUNO ===");
        
        System.out.print("ID do aluno: ");
        Long id = lerLong();
        
        System.out.print(" Novo nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Novo email: ");
        String email = scanner.nextLine();
        
        System.out.print("Nova senha: ");
        String senha = scanner.nextLine();

        System.out.print("Novo curso: ");
        String curso = scanner.nextLine();

        Curso cursoBuscado = (Curso) sistema.buscarCursoPorNome(curso).getDados();

        ResultadoOperacao resultado = sistema.editarAluno(id, nome, email, senha, cursoBuscado, usuarioLogado.getId());

        if (resultado.isSucesso()) {
            System.out.println(resultado.getMensagem());
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void excluirAluno() {
        System.out.println("\n=== EXCLUIR ALUNO ===");
        
        System.out.print("ID do aluno: ");
        Long id = lerLong();

        ResultadoOperacao resultado = sistema.excluirAluno(id, usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            System.out.println(resultado.getMensagem());
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void buscarAlunoPorId() {
        System.out.println("\n === BUSCAR ALUNO POR ID ===");
        
        System.out.print("ID do aluno: ");
        Long id = lerLong();

        ResultadoOperacao resultado = sistema.buscarAlunoPorId(id, usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            Aluno aluno = (Aluno) resultado.getDados();
            exibirAluno(aluno);
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void buscarAlunoPorNome() {
        System.out.println("\n === BUSCAR ALUNO POR NOME ===");
        
        System.out.print("Nome do aluno: ");
        String nome = scanner.nextLine();

        ResultadoOperacao resultado = sistema.buscarAlunoPorNome(nome, usuarioLogado.getId());
        
        if (resultado.isSucesso()) {
            Aluno aluno = (Aluno) resultado.getDados();
            exibirAluno(aluno);
        } else {
            System.out.println(resultado.getMensagem());
        }
    }

    private static void gerarCurriculoSemestral() {
        System.out.println("\n === GERAR CURRÍCULO SEMESTRAL ===");
        ResultadoOperacao resultado = sistema.gerarCurriculoSemestral(usuarioLogado.getId());

        if (resultado.isSucesso()) {
            System.out.println(resultado.getDados());
        } else {
            System.out.println( resultado.getMensagem());
        }
    }

    private static void verMeuCurso() {
        System.out.println("\n === MEU CURSO ===");
        if (usuarioLogado instanceof Aluno) {
            Aluno aluno = (Aluno) usuarioLogado;
            if (aluno.getCurso() != null) {
                exibirCurso(aluno.getCurso());
            } else {
                System.out.println(" Você não está matriculado em nenhum curso.");
            }
        }
    }

    private static void exibirDisciplina(Disciplina disciplina) {
        System.out.println("=== INFORMAÇÕES DA DISCIPLINA ===");
        System.out.println("ID: " + disciplina.getId());
        System.out.println("Nome: " + disciplina.getNome());
        System.out.println("Créditos: " + disciplina.getQuantidadeCreditos());
        System.out.println("Optativa: " + (disciplina.isOptativa() ? "Sim" : "Não"));
        System.out.println("Ativa: " + (disciplina.isAtiva() ? "Sim" : "Não"));
        System.out.println("Professor: " + 
            (disciplina.getProfessor() != null ? disciplina.getProfessor().getNome() : "Não definido"));
        System.out.println("👥 Alunos matriculados: " + disciplina.getQuantidadeAlunosMatriculados());
    }

    private static void exibirProfessor(Professor professor) {
        System.out.println("=== INFORMAÇÕES DO PROFESSOR ===");
        System.out.println("ID: " + professor.getId());
        System.out.println("Nome: " + professor.getNome());
        System.out.println("Email: " + professor.getEmail());
    }

    private static void exibirAluno(Aluno aluno) {
        System.out.println("=== INFORMAÇÕES DO ALUNO ===");
        System.out.println("ID: " + aluno.getId());
        System.out.println("Nome: " + aluno.getNome());
        System.out.println("Email: " + aluno.getEmail());
        System.out.println("Curso: " + 
            (aluno.getCurso() != null ? aluno.getCurso().getNome().getNomeCurso() : "Não definido"));
        System.out.println("Disciplinas: " + 
            (aluno.getDisciplinas() != null ? aluno.getDisciplinas().size() : 0));
    }

    private static void exibirCurso(Curso curso) {
        System.out.println("=== INFORMAÇÕES DO CURSO ===");
        System.out.println("ID: " + curso.getId());
        System.out.println("Nome: " + curso.getNome().getNomeCurso());
        System.out.println("Créditos: " + curso.getCredito());
        System.out.println("Disciplinas: " + 
            (curso.getDisciplinas() != null ? curso.getDisciplinas().size() : 0));
        System.out.println("Alunos: " + 
            (curso.getAlunos() != null ? curso.getAlunos().size() : 0));
    }

    private static void logout() {
        usuarioLogado = null;
        System.out.println("Logout realizado com sucesso!");
    }

    private static int lerInt() {
        try {
            int valor = Integer.parseInt(scanner.nextLine());
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("Por favor, digite um número válido.");
            return lerInt();
        }
    }

    private static Long lerLong() {
        try {
            Long valor = Long.parseLong(scanner.nextLine());
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("Por favor, digite um ID válido.");
            return lerLong();
        }
    }

    private static boolean lerBoolean() {
        String valor = scanner.nextLine().toLowerCase();
        if ("true".equals(valor) || "sim".equals(valor) || "s".equals(valor)) {
            return true;
        } else if ("false".equals(valor) || "não".equals(valor) || "nao".equals(valor) || "n".equals(valor)) {
            return false;
        } else {
            System.out.println("Por favor, digite 'true' ou 'false':");
            return lerBoolean();
        }
    }
}