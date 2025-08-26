package entity;

import enums.Codigo;

import java.util.List;

public class Secretario extends Usuario {
    public String gerarCurriculoSemestral() {
        //TODO
        return null;
    }

    public Codigo criarDisciplina(Disciplina disciplina) {
        Disciplina novaDisciplina = buscarDisciplinaPorNome(disciplina.getNome());

        if(novaDisciplina != null) {
            return Codigo.ERRO_406;
        }
            novaDisciplina = new Disciplina(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getQuantidadeCreditos(),
                disciplina.getCursos(),
                disciplina.isOptativa(),
                disciplina.isAtiva(),
                disciplina.getAlunos(),
                disciplina.getProfessor()
            );
        
        return Codigo.OK_200;
    }

    public Codigo editarDisciplina(Disciplina disciplina) {
        Disciplina disciplinaExistente = buscarDisciplinaPorNome(disciplina.getNome());
        if(disciplinaExistente == null) {
            return Codigo.ERRO_409;
        }
        disciplinaExistente.setNome(disciplina.getNome());
        disciplinaExistente.setQuantidadeCreditos(disciplina.getQuantidadeCreditos());
        disciplinaExistente.setCursos(disciplina.getCursos());
        disciplinaExistente.setOptativa(disciplina.isOptativa());
        disciplinaExistente.setAtiva(disciplina.isAtiva());
        disciplinaExistente.setAlunos(disciplina.getAlunos());
        disciplinaExistente.setProfessor(disciplina.getProfessor());
        return Codigo.OK_200;
    }

    public Codigo excluirDisciplinaPorNome(String nome) {
        Disciplina disciplinaExistente = buscarDisciplinaPorNome(nome);
        if(disciplinaExistente == null) {
            return Codigo.ERRO_409;
        }
        return Codigo.OK_200;
    }

    public Codigo criarProfessor(Professor professor) {
        Professor novoProfessor = buscarProfessorPorNome(getNome());
        if(novoProfessor == null){
            return Codigo.ERRO_407;
        }
        novoProfessor = new Professor(
            professor.getId(),
            professor.getEmail(),
            professor.getSenha(),
            professor.getNome(),
            professor.getDisciplina()
        );
        return Codigo.OK_200;
    }

        

    public String editarProfessor(Professor professor) {
        //TODO
        return null;
    }

    public Codigo excluirProfessorPorNome(String nome) {
        //TODO
        return null;
    }

    public List<Professor> listarProfessores() {
        //TODO
        return null;
    }

    public Professor buscarProfessorPorNome(String nome) {
        //TODO
        return null;
    }

    public String criarAluno(Aluno aluno) {
        //TODO
        return null;
    }

    public String editarAluno(Aluno aluno) {
        //TODO
        return null;
    }

    public Codigo excluirAlunoPorNome(String nome) {
        //TODO
        return null;
    }

    public List<Aluno> listarAlunos() {
        //TODO
        return null;
    }

    public Aluno buscarAlunoPorNome(String nome) {
        //TODO
        return null;
    }

}
