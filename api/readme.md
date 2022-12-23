# Spring Boot 3 <img align="center" alt="kuber" width="45px" src="https://img.icons8.com/color/512/spring-logo.png" />

### Spring e Spring Boot

**Spring** e **Spring Boot** não são a mesma coisa com nomes distintos.

**Spring** é um **framework** para desenvolvimento de aplicações em Java, criado em meados de 2002 por Rod Johnson, que se tornou bastante popular e adotado ao redor do mundo devido a sua simplicidade e facilidade de integração com outras tecnologias.

O framework foi desenvolvido de maneira **modular**, na qual cada recurso que ele disponibiliza é representado por um módulo, que pode ser adicionado em uma aplicação conforme as necessidades. Com isso, em cada aplicação podemos adicionar apenas os módulos que fizerem sentido, fazendo assim com que ela seja mais leve. Existem diversos módulos no Spring, cada um com uma finalidade distinta, como por exemplo: o módulo **MVC**, para desenvolvimento de aplicações Web e API's Rest; o módulo **Security**, para lidar com controle de autenticação e autorização da aplicação; e o módulo **Transactions**, para gerenciar o controle transacional.

Entretanto, um dos grandes problemas existentes em aplicações que utilizavam o Spring era a parte de configurações de seus módulos, que era feita toda com arquivos XML, sendo que depois de alguns anos o framework também passou a dar suporte a configurações via classes Java, utilizando, principalmente, anotações. Em ambos os casos, dependendo do tamanho e complexidade da aplicação, e também da quantidade de módulos do Spring utilizados nela, tais configurações eram bastante extensas e de difícil manutenção.

Além disso, iniciar um novo projeto com o Spring era uma tarefa um tanto quanto complicada, devido a necessidade de realizar tais configurações no projeto.

Justamente para resolver tais dificuldades é que foi criado um novo módulo do Spring, chamado de **Boot**, em meados de 2014, com o propósito de agilizar a criação de um projeto que utilize o Spring como framework, bem como simplificar as configurações de seus módulos.

O lançamento do Spring Boot foi um marco para o desenvolvimento de aplicações Java, pois tornou tal tarefa mais simples e ágil de se realizar, facilitando bastante a vida das pessoas que utilizam a linguagem Java para desenvolver suas aplicações.



### Hello World no Spring Boot

```java
package med.voll.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String olaMUndo() {
        return "Hello World Spring!";
    }
}

```

```@RequestBody``` - A anotação que indica um parâmetro de método deve ser vinculada ao corpo da solicitação da web.
Receber os dados do corpo da requisição em um parâmetro no Controller

### Enviando dados para a API

```java
    @PostMapping
    public void cadastrar(@RequestBody String json) {
        System.out.println(json);}
```
### JSON

JSON (JavaScript Object Notation) é um formato utilizado para representação de informações, assim como XML e CSV.

Uma API precisa receber e devolver informações em algum formato, que representa os recursos gerenciados por ela. O JSON é um desses formatos possíveis, tendo se popularizado devido a sua leveza, simplicidade, facilidade de leitura por pessoas e máquinas, bem como seu suporte pelas diversas linguagens de programação.

Um exemplo de representação de uma informação no formato XML seria:

```xml
<produto>
    <nome>Mochila</nome>
    <preco>89.90</preco>
    <descricao>Mochila para notebooks de até 17 polegadas</descricao>
</produto>
```

Já a mesma informação poderia ser represetada no formato JSON da seguinte maneira:

```json
{
    “nome” : “Mochila”,
    “preco” : 89.90,
    “descricao” : “Mochila para notebooks de até 17 polegadas”
}
```

Perceba como o formato JSON é muito mais compacto e legível. Justamente por isso se tornou o formato universal utilizado em comunicação de aplicações, principalmente no caso de APIs REST.

### lidando com CORS

Quando desenvolvemos APIs e queremos que todos os seus recursos fiquem disponíveis a qualquer cliente HTTP, uma das coisas que vem à nossa cabeça é o CORS (Cross-Origin Resource Sharing), em português, “compartilhamento de recursos com origens diferentes”. Se ainda não aconteceu com você, fique tranquilo, é normal termos erros de CORS na hora de consumir e disponibilizar APIs.

![](https://caelum-online-public.s3.amazonaws.com/2700-spring-boot/02/Aula2-img1.png)

#### CORS

O CORS é um mecanismo utilizado para adicionar cabeçalhos HTTP que informam aos navegadores para permitir que uma aplicação Web seja executada em uma origem e acesse recursos de outra origem diferente. Esse tipo de ação é chamada de requisição cross-origin HTTP. Na prática, então, ele informa aos navegadores se um determinado recurso pode ou não ser acessado.

Mas por que os erros acontecem? Chegou a hora de entender!

#### Same-origin policy

Por padrão, uma aplicação Front-end, escrita em JavaScript, só consegue acessar recursos localizados na mesma origem da solicitação. Isso acontece por conta da política de mesma origem (same-origin policy), que é um mecanismo de segurança dos Browsers que restringe a maneira de um documento ou script de uma origem interagir com recursos de outra origem. Essa política possui o objetivo de frear ataques maliciosos.

Duas URLs compartilham a mesma origem se o protocolo, porta (caso especificado) e host são os mesmos. Vamos comparar possíveis variações considerando a URL ```https://cursos.alura.com.br/category/programacao```:

<table>
<tr>

  <th>URL</th>
  <th>Resultado</th>
  <th>Motivo</th>

  <tbody>
    <td>
    <a href="https://cursos.alura.com.br/category/front-end">https://cursos.alura.com.br/category/front-end</a></td>
    <td>Mesma origem</td>
    <td>Só o caminho difere</td>
    <tr><td><a href="http://cursos.alura.com.br/category/programacao" target="_blank" rel="noopener">http://cursos.alura.com.br/category/programacao</a></td><td>Erro de CORS</td><td>Protocolo diferente (http)</td></tr>
    <tr><td><a href="https://faculdade.alura.com.br:80/category/programacao" target="_blank" rel="noopener">https://faculdade.alura.com.br:80/category/programacao</a></td><td>Erro de CORS</td><td>Host diferente</td></tr>
  </tbody>
</tr>
</table>

Agora, fica a dúvida: o que fazer quando precisamos consumir uma API com URL diferente sem termos problemas com o CORS? Como, por exemplo, quando queremos consumir uma API que roda na porta 8000 a partir de uma aplicação React rodando na porta 3000. Veja só!

Ao enviar uma requisição para uma API de origem diferente, a API precisa retornar um header chamado ```Access-Control-Allow-Origin```. Dentro dele, é necessário informar as diferentes origens que serão permitidas para consumir a API, em nosso caso: ```Access-Control-Allow-Origin: http://localhost:3000.```

É possível permitir o acesso de qualquer origem utilizando o símbolo *(asterisco): ```Access-Control-Allow-Origin: *```. Mas isso não é uma medida recomendada, pois permite que origens desconhecidas acessem o servidor, a não ser que seja intencional, como no caso de uma API pública. Agora vamos ver como fazer isso no Spring Boot de maneira correta.

#### Habilitando diferentes origens no Spring Boot

```java
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
    }
}
```
http://localhost:3000 seria o endereço da aplicação Front-end e .allowedMethods os métodos que serão permitidos para serem executados. Com isso, você poderá consumir a sua API sem problemas a partir de uma aplicação Front-end.

### DTO com Java Record

**DTO**(Data Transfer Object)

``record``

**Nota:**

Uma classe de registro é um portador superficialmente imutável e transparente para um conjunto fixo de valores, chamados de componentes de registro. A linguagem Java fornece uma sintaxe concisa para declarar classes de registro, em que os componentes do registro são declarados no cabeçalho do registro. The list of record components declared in the record header form the record descriptor.

- Ele ajuda os desenvolvedores fornecendo uma sintaxe compacta para declaração de classes, evitando a conhecida verbosidade da linguagem Java.

- Em alguns casos, é útil preservar a imutabilidade dos dados criando os métodos equals, hashCode e toString.
-  primeiro método verifica a igualdade dos objetos quando todos os campos correspondem à mesma classe.(**equals**)
-  O segundo método retorna um valor inteiro exclusivo quando todos os campos correspondem.(**HashCode**)
-  O terceiro método fornece uma string derivada do nome da classe e dos nomes e valores de cada componente.(**toString**)

Lançado oficialmente no Java 16, mas disponível desde o Java 14 de maneira experimental, o Record é um recurso que permite representar uma classe imutável, contendo apenas atributos, construtor e métodos de leitura, de uma maneira muito simples e enxuta.

Esse tipo de classe se encaixa perfeitamente para representar classes DTO, já que seu objetivo é apenas representar dados que serão recebidos ou devolvidos pela API, sem nenhum tipo de comportamento.

Para se criar uma classe DTO imutável, sem a utilização do Record, era necessário escrever muito código. Vejamos um exemplo de uma classe DTO que representa um telefone:

```java

public final class Telefone {

    private final String ddd;
    private final String numero;

    public Telefone(String ddd, String numero) {
        this.ddd = ddd;
        this.numero = numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ddd, numero);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Telefone)) {
            return false;
        } else {
            Telefone other = (Telefone) obj;
            return Objects.equals(ddd, other.ddd)
              && Objects.equals(numero, other.numero);
        }
    }

    public String getDdd() {
        return this.ddd;
    }

    public String getNumero() {
        return this.numero;
    }
}

```
Agora com o Record, todo esse código pode ser resumido com uma única linha:

```java
public record Telefone(String ddd, String numero){}
```

Muito mais simples, não?!

Por baixo dos panos, o Java vai transformar esse Record em uma classe imutável, muito similar ao código exibido anteriormente.

#### Entidades JPA

As **entidades** JPA são POJOS (Plain Old Java Objects), que são objetos com design simplificado, ou seja, sem nada de especial. Essas entidades representam uma tabela do banco de dados, e cada instância desse objeto representa uma linha da tabela.

```java
package med.voll.api.medico;

import jakarta.persistence.*;
import lombok.*;
import med.voll.api.endereco.Endereco;

@Table(name = "medicos")
@Entity(name = "Medido")
@Getter
@NoArgsConstructor//gerar construtor padrão
@AllArgsConstructor//gerar construtor com todos os campos
@EqualsAndHashCode(of = "id")
public class Medico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String crm;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Embedded
    private Endereco endereco;

    public Medico(DadosCadastroMedico dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.crm = dados.crm();
        this.especialidade = dados.especialidade();
        this.endereco = new Endereco(dados.endereco());
    }
}
```
**Dependencia Spring data JPA**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

#### Interfaces Repository

```java
package med.voll.api.medico;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
}
```
O repositório seria uma classe para buscar informações no banco de dados ou no local onde as informações foram persistidas. Mas no caso do JpaRepository ele provê a ligação a determinada classe do Model com possibilidade de persistir no banco de dados.

#### Classes DAO

Em alguns projetos em Java, dependendo da tecnologia escolhida, é comum encontrarmos classes que seguem o padrão **DAO**, utilizado para isolar o acesso aos dados. Entretanto, neste curso utilizaremos um outro padrão, conhecido como **Repository**.

Mas aí podem surgir algumas dúvidas: qual a diferença entre as duas abordagens e o porquê dessa escolha?

#### **Padrão DAO**
O padrão de projeto DAO, conhecido também por **Data Access Object**, é utilizado para persistência de dados, onde seu principal objetivo é separar regras de negócio de regras de acesso a banco de dados. Nas classes que seguem esse padrão, isolamos todos os códigos que lidam com conexões, comandos SQLs e funções diretas ao banco de dados, para que assim tais códigos não se espalhem por outros pontos da aplicação, algo que dificultaria a manutenção do código e também a troca das tecnologias e do mecanismo de persistência.

#### **Implementação**
Vamos supor que temos uma tabela de produtos em nosso banco de dados. A implementação do padrão DAO seria o seguinte:

Primeiro, seria necessário criar uma classe básica de domínio Produto:

```java
public class Produto {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private String descricao;

    // construtores, getters e setters
}
```

Em seguida, precisaríamos criar a classe `ProdutoDao`, que fornece operações de persistência para a classe de domínio `Produto`:

```java
public class ProdutoDao {

    private final EntityManager entityManager;

    public ProdutoDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Produto produto) {
        entityManager.persist(produto);
    }

    public Produto read(Long id) {
        return entityManager.find(Produto.class, id);
    }

    public void update(Produto produto) {
        entityManger.merge(produto);
    }

    public void remove(Produto produto) {
        entityManger.remove(produto);
   }

}
```

No exemplo anterior foi utilizado a JPA como tecnologia de persistência dos dados da aplicação.

#### Padrão Repository

> **O repositório é um mecanismo para encapsular armazenamento, recuperação e comportamento de pesquisa, que emula uma coleção de objetos.**

Simplificando, um repositório também lida com dados e oculta consultas semelhantes ao DAO. No entanto, ele fica em um nível mais alto, mais próximo da lógica de negócios de uma aplicação. Um repositório está vinculado à regra de negócio da aplicação e está associado ao agregado dos seus objetos de negócio, retornando-os quando preciso.

Só que devemos ficar atentos, pois assim como no padrão DAO, regras de negócio que estão envolvidas com processamento de informações não devem estar presentes nos repositórios. Os repositórios não devem ter a responsabilidade de tomar decisões, aplicar algoritmos de transformação de dados ou prover serviços diretamente a outras camadas ou módulos da aplicação. Mapear entidades de domínio e prover as funcionalidades da aplicação são responsabilidades muito distintas.

Um repositório fica entre as regras de negócio e a camada de persistência:

Um repositório fica entre as regras de negócio e a camada de persistência:

1. Ele provê uma interface para as regras de negócio onde os objetos são acessados como em uma coleção;
2. Ele usa a camada de persistência para gravar e recuperar os dados necessários para persistir e recuperar os objetos de negócio.

Portanto, é possível até utilizar um ou mais DAOs em um repositório.

#### Por que o padrão repository ao invés do DAO utilizando Spring?

O padrão de repositório incentiva um design orientado a domínio, fornecendo uma compreensão mais fácil do domínio e da estrutura de dados. Além disso, utilizando o repository do Spring não temos que nos preocupar em utilizar diretamente a API da JPA, bastando apenas criar os métodos que o Spring cria a implementação em tempo de execução, deixando o código muito mais simples, menor e legível.

#### Flyway

O **Flyway** é um framework que permite o versionamento e gerenciamento do Banco de dados, com ele podemos controlar as versões do banco de dados.

**obs: As migrations deve funcionar de forma imutável**

#### Anotaçoẽs de Bean Validation

Como explicado no vídeo anterior, o Bean Validation é composto por diversas anotações que devem ser adicionadas nos atributos em que desejamos realizar as validações. Vimos algumas dessas anotações, como a `@NotBlank`, que indica que um atributo do tipo `String` não pode ser nulo e nem vazio.

EX:

```java

package med.voll.api.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.endereco.DadosEndereco;

public record DadosCadastroMedico(
        @NotBlank
        String nome,
        @NotBlank
        String email,
        @NotBlank
        @Pattern(regexp = "\\d{4,6}")
        String crm,
        @NotNull
        Especialidade especialidade,
        @NotNull @Valid
        DadosEndereco endereco) {
}
```

### Resumo do Módulo

- Adicionar novas dependências no projeto;
- Mapear uma entidade JPA e criar uma interface Repository para ela;
- Utilizar o Flyway como ferramenta de Migrations do projeto;
- Realizar validações com Bean Validation utilizando algumas de suas anotações, como a `@NotBlank`.

#### @GetMapping

- É uma anotação composta que funciona como um atalho para @RequestMapping(method = RequestMethod.GET).
- É a anotação mais recente

```java
@GetMapping
    public List<DadosListagemMedico> listar() {
        //converter de Medico para DadosListagemMedico
        return repository.findAll().stream().map(DadosListagemMedico::new).toList();
    }
```
### DTOs ou entidades?

Estamos utilizando DTOs para representar os dados que recebemos e devolvemos pela API, mas você provavelmente deve estar se perguntando “Por que ao invés de criar um DTO não devolvemos diretamente a entidade JPA no Controller?”. Para fazer isso, bastaria alterar o método `listar` no Controller para:

```java 
@GetMapping
public List<Medico> listar() {
    return repository.findAll();
}
```
Desse jeito o código ficaria mais enxuto e não precisaríamos criar o DTO no projeto. Mas, será que isso realmente é uma boa ideia?

#### Os problemas de receber/devolver entidades JPA

De fato é muito mais simples e cômodo não utilizar DTOs e sim lidar diretamente com as entidades JPA nos controllers. Porém, essa abordagem tem algumas desvantagens, inclusive causando vulnerabilidade na aplicação para ataques do tipo **Mass Assignment**.

Um dos problemas consiste no fato de que, ao retornar uma entidade JPA em um método de um Controller, o Spring vai gerar o JSON contendo **todos** os atributos dela, sendo que nem sempre esse é o comportamento que desejamos.

Eventualmente podemos ter atributos que não desejamos que sejam devolvidos no JSON, seja por motivos de segurança, no caso de dados sensíveis, ou mesmo por não serem utilizados pelos clientes da API.

#### Utilização da anotação @JsonIgnore

Nessa situação, poderíamos utilizar a anotação `@JsonIgnore`, que nos ajuda a ignorar certas propriedades de uma classe Java quando ela for serializada para um objeto JSON.

Sua utilização consiste em adicionar a anotação nos atributos que desejamos ignorar quando o JSON for gerado. Por exemplo, suponha que temos uma entidade JPA `Funcionario`, na qual desejamos ignorar o atributo `salario`:

```java
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "Funcionario")
@Table(name = "funcionarios")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;

    @JsonIgnore
    private BigDecimal salario;

    //restante do código omitido…
}
```

No exemplo anterior, o atributo `salario` da classe `Funcionario` não será exibido nas respostas JSON e o problema estaria solucionado.

Entretanto, pode acontecer de existir algum outro endpoint da API na qual precisamos enviar no JSON o salário dos funcionários, sendo que nesse caso teríamos problemas, pois com a anotação `@JsonIgnore` tal atributo **nunca** será enviado no JSON, e ao remover a anotação o atributo **sempre** será enviado. Perdemos, com isso, a flexibilidade de controlar quando determinados atributos devem ser enviados no JSON e quando não.

#### DTO

O padrão DTO (Data Transfer Object) é um padrão de arquitetura que era bastante utilizado antigamente em aplicações Java distribuídas (arquitetura cliente/servidor) para representar os dados que eram enviados e recebidos entre as aplicações cliente e servidor.

O padrão DTO pode (e deve) ser utilizado quando não queremos expor todos os atributos de alguma entidade do nosso projeto, situação igual a dos salários dos funcionários que abordamos anteriormente. Além disso, com a flexibilidade e a opção de filtrar quais dados serão transmitidos, podemos poupar tempo de processamento.


#### Loop infinito causando StackOverflowError

Outro problema muito recorrente ao se trabalhar diretamente com entidades JPA acontece quando uma entidade possui algum autorrelacionamento ou relacionamento bidirecional. Por exemplo, considere as seguintes entidades JPA:

```java 
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "Produto")
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = “id_categoria”)
    private Categoria categoria;

    //restante do código omitido…
}
```

```java

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "Produto")
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = “id_categoria”)
    private Categoria categoria;

    //restante do código omitido…
}

```

```java
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "Categoria")
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @OneToMany(mappedBy = “categoria”)
    private List<Produto> produtos = new ArrayList<>();

    //restante do código omitido…
}
```
Ao retornar um objeto do tipo `Produto` no Controller, o Spring teria problemas para gerar o JSON desse objeto, causando uma exception do tipo `StackOverflowError`. Esse problema ocorre porque o objeto produto tem um atributo do tipo `Categoria`, que por sua vez tem um atributo do tipo `List<Produto>`, causando assim um loop infinito no processo de serialização para JSON.

Tal problema pode ser resolvido com a utilização da anotação `@JsonIgnore` ou com a utilização das anotações `@JsonBackReference` e `@JsonManagedReference`, mas também poderia ser evitado com a utilização de um DTO que representa apenas os dados que devem ser devolvidos no JSON.

#### Paginação e ordenação

A paginação pode ser utilizida quando é feita uma requisição de consulta, serve para filtrar a quantidade de registros que você quer buscar. 

Já na Ordenação, você consegue dar mais inteligência a sua API, além de dar mais capacidade ao client para trabalhar com os dados sob demanda.

```java
@GetMapping
    public Page<DadosListagemMedico> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        //converter de Medico para DadosListagemMedico
//        return repository.findAll().stream().map(DadosListagemMedico::new).toList();

        return repository.findAll(paginacao).map(DadosListagemMedico::new);

    }
```
### Parâmetros de paginação

Conforme aprendemos nos vídeos anteriores, por padrão, os parâmetros utilizados para realizar a paginação e a ordenação devem se chamar **page**, **size** e **sort**. Entretanto, o Spring Boot permite que os nomes de tais parâmetros sejam modificados via configuração no arquivo **application.properties**.

Por exemplo, poderíamos traduzir para português os nomes desses parâmetros com as seguintes propriedades:

```properties
spring.data.web.pageable.page-parameter=pagina
spring.data.web.pageable.size-parameter=tamanho
spring.data.web.sort.sort-parameter=ordem
```
Com isso, nas requisições que utilizam paginação, devemos utilizar esses nomes que foram definidos. Por exemplo, para listar os médicos de nossa API trazendo apenas 5 registros da página 2, ordenados pelo e-mail e de maneira decrescente, a URL da requisição deve ser:

```html
http://localhost:8080/medicos?tamanho=5&pagina=1&ordem=email,desc
```

### Resumo do módulo

- Utilizar a anotação `@GetMapping` para mapear métodos em Controllers que produzem dados;
- Utilizar a interface `Pageable` do Spring para realizar consultas com paginação;
- Controlar a paginação e a ordenação dos dados devolvidos pela API com os parâmetros `page`, `size` e `sort`;
- Configurar o projeto para que os comandos SQL sejam exibidos no console.

### Requisições PUT

- Serve para atualizar o dados

**Criar o método com a annotation @PutMapping**
```java

@PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizarPaciente dados) {

        var paciente = repository.getReferenceById(dados.id());
        paciente.atualizarInformacoes(dados);

    }

```

**Cria um uma classe RECORD para atualizar os dados**

```java
package med.voll.api.paciente;

import med.voll.api.endereco.DadosEndereco;

public record DadosAtualizarPaciente(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        DadosEndereco endereco) {

}
```
**Criar método de atualização de informações na classe Paciente**

```java
public void atualizarInformacoes(DadosAtualizarPaciente dados) {

        if (dados.nome() != null) {
            this.nome = dados.nome();
        }
        if (dados.email() != null) {
            this.email = dados.email();
        }
        if (dados.telefone() != null) {
            this.telefone = dados.telefone();
        }
        if (dados.cpf() != null) {
            this.cpf = dados.cpf();
        }
        if (dados.endereco() != null) {
            endereco.atualizarInformacoes(dados.endereco());
        }
    }
```
**Criar método de atualização de informações na classe Endereco**

```java
public void atualizarInformacoes(DadosEndereco endereco) {

        if (endereco.logradouro() != null) {
            this.logradouro = endereco.logradouro();
        }

        if (endereco.bairro() != null) {
            this.bairro = endereco.bairro();
        }

        if (endereco.cep() != null) {
            this.cep = endereco.cep();
        }

        if (endereco.numero() != null) {
            this.numero = endereco.numero();
        }

        if (endereco.complemento() != null) {
            this.complemento = endereco.complemento();
        }

        if (endereco.cidade() != null) {
            this.cidade = endereco.cidade();
        }

        if (endereco.uf() != null) {
            this.uf = endereco.uf();
        }

    }
```

### Mass Assignment Attack

**Mass Assignment Attack** ou Ataque de Atribuição em Massa, em português, ocorre quando um usuário é capaz de inicializar ou substituir parâmetros que não deveriam ser modificados na aplicação. Ao incluir parâmetros adicionais em uma requisição, sendo tais parâmetros válidos, um usuário mal-intencionado pode gerar um efeito colateral indesejado na aplicação.

O conceito desse ataque refere-se a quando você injeta um conjunto de valores diretamente em um objeto, daí o nome atribuição em massa, que sem a devida validação pode causar sérios problemas.

Vamos a um exemplo prático. Suponha que você tem o seguinte método, em uma classe Controller, utilizado para cadastrar um usuário na aplicação:

```java
@PostMapping
@Transactional
public void cadastrar(@RequestBody @Valid Usuario usuario) {
    repository.save(usuario);
}
```

E a entidade JPA que representa o usuário:

```java
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "Usuario")
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private Boolean admin = false;

    //restante do código omitido…
}
```

Repare que o atributo `admin` da classe `Usuario` é inicializado como `false`, indicando que um usuário deve sempre ser cadastrado como não sendo um administrador. Porém, se na requisição for enviado o seguinte JSON:

```json
{
    “nome” : “Rodrigo”,
    “email” : “rodrigo@email.com”,
    “admin” : true
}
```
O usuário será cadastrado com o atributo `admin` preenchido como `true`. Isso acontece porque o atributo `admin` enviado no JSON existe na classe que está sendo recebida no Controller, sendo considerado então um atributo válido e que será preenchido no objeto `Usuario` que será instanciado pelo Spring.

Então, como fazemos para prevenir esse problema?

#### Prevenção

O uso do padrão DTO nos ajuda a evitar esse problema, pois ao criar um DTO definimos nele apenas os campos que podem ser recebidos na API, e no exemplo anterior o DTO não teria o atributo `admin`.

Novamente, vemos mais uma vantagem de se utilizar o padrão DTO para representar os dados que chegam e saem da API.

### PUT ou PATCH

Escolher entre o método HTTP PUT ou PATCH é uma dúvida comum que surge quando estamos desenvolvendo APIs e precisamos criar um endpoint para atualização de recursos. Vamos entender as diferenças entre as duas opções e quando utilizar cada uma.

#### PUT
O método PUT substitui todos os atuais dados de um recurso pelos dados passados na requisição, ou seja, estamos falando de uma atualização integral. Então, com ele, fazemos a atualização total de um recurso em apenas uma requisição.

#### PATCH
O método PATCH, por sua vez, aplica modificações parciais em um recurso. Logo, é possível modificar apenas uma parte de um recurso. Com o PATCH, então, realizamos atualizações parciais, o que torna as opções de atualização mais flexíveis.

#### Qual escolher?
Na prática, é difícil saber qual método utilizar, pois nem sempre saberemos se um recurso será atualizado parcialmente ou totalmente em uma requisição - a não ser que realizemos uma verificação quanto a isso, algo que não é recomendado.

O mais comum então nas aplicações é utilizar o método PUT para requisições de atualização de recursos em uma API

### Delete e exclusão Lógica

**@DeleteMapping**

```java

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id) {
//        repository.deleteById(id);
    }
```
**exclusão Lógica**

```java
    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id) {
//        repository.deleteById(id);
        //exclusao logica
        var medico = repository.getReferenceById(id);
        medico.excluir();

    }
```
**Cria um novo atributo na classe Medico**

```java
private Boolean ativo;

 public Medico(DadosCadastroMedico dados) {
        this.ativo = true;
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.crm = dados.crm();
        this.especialidade = dados.especialidade();
        this.endereco = new Endereco(dados.endereco());
    }
```
**Cria um método na classe Repository de Medico que retorne True**

```java
Page<Medico> findAllAtivoTrue(Pageable paginacao);
```

**Inclua esse Método no método listar**

```java
@GetMapping
    public Page<DadosListagemMedico> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        //converter de Medico para DadosListagemMedico
//        return repository.findAll().stream().map(DadosListagemMedico::new).toList();

        return repository.findAllAtivoTrue(paginacao).map(DadosListagemMedico::new);

    }
```

### Resumo do módulo

- Mapear requisições PUT com a anotação `@PutMapping`;
- Escrever um código para atualizar informações de um registro no banco de dados;
- Mapear requisições DELETE com a anotação `@DeleteMapping`;
- Mapear parâmetros dinâmicos em URL com a anotação `@PathVariable`;
- Implementar o conceito de exclusão lógica com o uso de um atributo booleano.
