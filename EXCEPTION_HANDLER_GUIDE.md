# Sistema de Exception Handler Mapeado

## 🎯 **Nova Abordagem - Exceções Mapeadas**

Agora **todas as exceções estão mapeadas dentro do ExceptionHandler**! Você não precisa mais usar classes de exceção no seu código - apenas chama os métodos do handler.

## 📋 Estrutura Refatorada

### **ExceptionHandler - Centralizou TUDO**
```java
// Ao invés de: throw new DisciplinaJaExisteException("POO");
// Agora você faz: return handler.disciplinaJaExiste("POO");
```

### **Métodos Disponíveis no Handler:**

#### **🎓 Para Disciplinas:**
- `handler.disciplinaJaExiste(nome)`
- `handler.disciplinaNaoEncontrada(nome)`
- `handler.disciplinaDadosInvalidos(campo, valor)`
- `handler.disciplinaCriada(objeto)`
- `handler.disciplinaEditada(objeto)`
- `handler.disciplinaExcluida(nome)`

#### **👨‍🏫 Para Professores:**
- `handler.professorJaExiste(nome)`
- `handler.professorNaoEncontrado(nome)`
- `handler.professorDadosInvalidos(campo, valor)`
- `handler.professorCriado(objeto)`

#### **👨‍🎓 Para Alunos:**
- `handler.alunoJaExiste(nome)`
- `handler.alunoNaoEncontrado(nome)`
- `handler.alunoDadosInvalidos(campo, valor)`
- `handler.alunoCriado(objeto)`

#### **📚 Para Cursos:**
- `handler.cursoJaExiste(nome)`
- `handler.cursoNaoEncontrado(nome)`
- `handler.cursoDadosInvalidos(campo, valor)`
- `handler.cursoCriado(objeto)`

#### **📝 Para Matrículas:**
- `handler.matriculaLimiteExcedido(tipo, limite)`
- `handler.matriculaDisciplinaInativa(nome)`
- `handler.matriculaJaExistente(aluno, disciplina)`
- `handler.matriculaRealizada(aluno, disciplina)`

#### **🔧 Métodos Genéricos:**
- `handler.dadosNulos(entidade)`
- `handler.campoObrigatorio(campo)`
- `handler.acessoNegado(operacao)`
- `handler.erroInterno(detalhes)`
- `handler.sucesso(mensagem)`
- `handler.sucesso(mensagem, dados)`

## 🚀 Como Usar Agora

### **❌ ANTES (com exceções):**
```java
public ResultadoOperacao criarDisciplina(Disciplina disciplina) {
    return exceptionHandler.executarComTratamento(() -> {
        if (disciplina == null) {
            throw new DadosInvalidosException("Disciplina não pode ser nula");
        }
        
        if (disciplina.getNome().isEmpty()) {
            throw new DadosInvalidosException("nome", "vazio");
        }
        
        if (existeDisciplina(disciplina.getNome())) {
            throw new DisciplinaJaExisteException(disciplina.getNome());
        }
        
        salvarDisciplina(disciplina);
        return disciplina;
    });
}
```

### **✅ AGORA (com handler mapeado):**
```java
public ResultadoOperacao criarDisciplina(Disciplina disciplina) {
    // Validação de dados nulos
    if (disciplina == null) {
        return handler.dadosNulos("Disciplina");
    }
    
    // Validação de nome
    if (disciplina.getNome() == null || disciplina.getNome().trim().isEmpty()) {
        return handler.disciplinaDadosInvalidos("nome", "vazio ou nulo");
    }
    
    // Verifica se já existe
    if (existeDisciplina(disciplina.getNome())) {
        return handler.disciplinaJaExiste(disciplina.getNome());
    }
    
    // Salva e retorna sucesso
    salvarDisciplina(disciplina);
    return handler.disciplinaCriada(disciplina);
}
```

## � **Vantagens da Nova Abordagem**

### **1. 🧹 Código Mais Limpo**
- Sem `throw new Exception()`
- Sem blocos `try-catch`
- Validações diretas e claras

### **2. 🎯 Mais Intuitivo**
- Um método para cada tipo de erro
- Nomes descritivos e auto-explicativos
- Retorno direto do `ResultadoOperacao`

### **3. 🔧 Fácil Manutenção**
- Todas as mensagens centralizadas
- Logs padronizados automaticamente
- Códigos de erro consistentes

### **4. 📈 Melhor Performance**
- Sem overhead de exceções
- Validações mais rápidas
- Controle de fluxo simplificado

### **5. 🎨 Flexibilidade Total**
- Fácil personalizar mensagens
- Adicionar novos tipos de erro
- Configurar logs independentemente

## 📊 Exemplo Prático Completo

```java
public class MeuServico {
    private final ExceptionHandler handler = new ExceptionHandler();
    
    public ResultadoOperacao operacaoCompleta(String nome, int creditos) {
        // Validações simples e diretas
        if (nome == null || nome.trim().isEmpty()) {
            return handler.campoObrigatorio("nome");
        }
        
        if (creditos <= 0) {
            return handler.disciplinaDadosInvalidos("creditos", String.valueOf(creditos));
        }
        
        // Verificações de negócio
        if (existeDisciplina(nome)) {
            return handler.disciplinaJaExiste(nome);
        }
        
        // Operação bem-sucedida
        Disciplina disciplina = criarNovaDisciplina(nome, creditos);
        return handler.disciplinaCriada(disciplina);
    }
}
```

## 🧪 Teste o Sistema

Execute `ExemploExceptionHandler.java` para ver todos os cenários:

```bash
javac code/*.java code/entity/*.java code/enums/*.java code/exceptions/*.java
java ExemploExceptionHandler
```

## 📈 Comparação

| Aspecto | Antes | Agora |
|---------|-------|-------|
| **Código** | `throw new Exception()` | `return handler.metodo()` |
| **Legibilidade** | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| **Manutenção** | ⭐⭐ | ⭐⭐⭐⭐⭐ |
| **Performance** | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| **Flexibilidade** | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |

## � **Resultado Final**

Agora você tem um sistema onde:
- ✅ **Zero** classes de exceção no código de negócio
- ✅ **100%** das exceções mapeadas no handler
- ✅ **Código limpo** e fácil de entender
- ✅ **Mensagens consistentes** e padronizadas
- ✅ **Logs automáticos** organizados
- ✅ **Fácil extensão** para novos tipos de erro

**Sua única preocupação agora é a lógica de negócio - o handler cuida de todos os erros!** 🚀
