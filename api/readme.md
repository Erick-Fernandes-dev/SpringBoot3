# Boas práticas e segurança da API com Spring Boot 3 <img align="center" alt="kuber" width="45px" src="https://img.icons8.com/color/512/spring-logo.png" />

## Padronizando retornos da API

**Código 204** - Requisição processada e sem conteúdo

**Código 201** - Significa que a requisição foi processada e o novo recurso foi criado (Created)

**Código 200** - Código do protocolo HTTP que significa "OK"


**ResponseEntity** - Classe responsável por controlar a resposta devolvida pelo 
farmework.

```java
@GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        //converter de Medico para DadosListagemMedico
//        return repository.findAll().stream().map(DadosListagemMedico::new).toList();

        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);


    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {

        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));


    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
//        repository.deleteById(id);
        //exclusao logica
        var medico = repository.getReferenceById(id);
        medico.excluir();

        return ResponseEntity.notFound().build();//retornar 204

```

### Devolvendo o código HTTP 201

```java
@PostMapping
    @Transactional//detecta a transação do banco de dados
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
//        System.out.println(dados);

        var medico = new Medico(dados);
        repository.save(medico);
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));


    }
```
### Códigos do protocolo HTTP

O **protocolo HTTP** (Hypertext Transfer Protocol, RFC 2616) é o protocolo responsável por fazer a comunicação entre o cliente, que normalmente é um browser, e o servidor. Dessa forma, a cada “requisição” feita pelo cliente, o servidor responde se ele obteve sucesso ou não. Se não obtiver sucesso, na maioria das vezes, a resposta do servidor será uma sequência numérica acompanhada por uma mensagem. Se não soubermos o que significa o código de resposta, dificilmente saberemos qual o problema que está acontecendo, por esse motivo é muito importante saber quais são os códigos HTTP e o que significam.

#### Categoria de códigos
Os códigos HTTP (ou HTTPS) possuem três dígitos, sendo que o primeiro dígito significa a classificação dentro das possíveis cinco categorias.

**1XX:** Informativo – a solicitação foi aceita ou o processo continua em andamento;

**2XX:** Confirmação – a ação foi concluída ou entendida;

**3XX:** Redirecionamento – indica que algo mais precisa ser feito ou precisou ser feito para completar a solicitação;

**4XX:** Erro do cliente – indica que a solicitação não pode ser concluída ou contém a sintaxe incorreta;

**5XX:** Erro no servidor – o servidor falhou ao concluir a solicitação.

#### Principais códigos de erro

Como dito anteriormente, conhecer os principais códigos de erro HTTP vai te ajudar a identificar problemas em suas aplicações, além de permitir que você entenda melhor a comunicação do seu navegador com o servidor da aplicação que está tentando acessar.

**Error 403**
O código 403 é o erro “Proibido”. Significa que o servidor entendeu a requisição do cliente, mas se recusa a processá-la, pois o cliente não possui autorização para isso.

**Error 404**
Quando você digita uma URL e recebe a mensagem Error 404, significa que essa URL não te levou a lugar nenhum. Pode ser que a aplicação não exista mais, a URL mudou ou você digitou a URL errada.

**Error 500**
É um erro menos comum, mas de vez em quando ele aparece. Esse erro significa que há um problema com alguma das bases que faz uma aplicação rodar. Esse erro pode ser, basicamente, no servidor que mantém a aplicação no ar ou na comunicação com o sistema de arquivos, que fornece a infraestrutura para a aplicação.

**Error 503**
O erro 503 significa que o serviço acessado está temporariamente indisponível. Causas comuns são um servidor em manutenção ou sobrecarregado. Ataques maliciosos, como o DDoS, causam bastante esse problema.

**Uma dica final:** dificilmente conseguimos guardar em nossa cabeça o que cada código significa, portanto, existem sites na internet que possuem todos os códigos e os significados para que possamos consultar quando necessário. Existem dois sites bem conhecidos e utilizados por pessoas desenvolvedoras, um para cada preferência: se você gosta de gatos, pode utilizar o [HTTP Cats](https://http.cat); já, se prefere cachorros, utilize o [HTTP Dogs](https://http.dog).


### Resumo do módulo

- Utilizar a classe ResponseEntity, do Spring, para personalizar os retornos dos métodos de uma classe Controller;
- Modificar o código HTTP devolvido nas respostas da API;
- Adicionar cabeçalhos nas respostas da API;
- Utilizar os códigos HTTP mais apropriados para cada operação realizada na API.

### Lidando com erros na API
```properties
server.error.include-stacktrace=never
```
### Propriedades do Spring Boot

O Spring Boot possui centenas de propriedades que podemos incluir nesse arquivo, sendo impossível memorizar todas elas. Sendo assim, é importante conhecer a documentação que lista todas essas propriedades, pois eventualmente precisaremos consultá-la.

Acesse o link da documentação: [Common-Application Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)

### Tratando os erros 404 e 400

```java
@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tartarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    private record DadosErroValidacao(String campo, String mensagem) {

        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }


    }

}
```
### Personalizando mensagens de erro

Você deve ter notado que o Bean Validation possui uma mensagem de erro para cada uma de suas anotações. Por exemplo, quando a validação falha em algum atributo anotado com `@NotBlank`, a mensagem de erro será: **must not be blank.**

Essas mensagens de erro não foram definidas na aplicação, pois são mensagens de erro **padrão** do próprio Bean Validation. Entretanto, caso você queira, pode personalizar tais mensagens.

Uma das maneiras de personalizar as mensagens de erro é adicionar o atributo **message** nas próprias anotações de validação:

```java
public record DadosCadastroMedico(
    @NotBlank(message = "Nome é obrigatório")
    String nome,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Formato do email é inválido")
    String email,

    @NotBlank(message = "Telefone é obrigatório")
    String telefone,

    @NotBlank(message = "CRM é obrigatório")
    @Pattern(regexp = "\\d{4,6}", message = "Formato do CRM é inválido")
    String crm,

    @NotNull(message = "Especialidade é obrigatória")
    Especialidade especialidade,

    @NotNull(message = "Dados do endereço são obrigatórios")
    @Valid DadosEndereco endereco) {}
```
Outra maneira é isolar as mensagens em um arquivo de propriedades, que deve possuir o nome **ValidationMessages.properties** e ser criado no diretório src/main/resources:

```properties
nome.obrigatorio=Nome é obrigatório
email.obrigatorio=Email é obrigatório
email.invalido=Formato do email é inválido
telefone.obrigatorio=Telefone é obrigatório
crm.obrigatorio=CRM é obrigatório
crm.invalido=Formato do CRM é inválido
especialidade.obrigatoria=Especialidade é obrigatória
endereco.obrigatorio=Dados do endereço são obrigatórios
```

E, nas anotações, indicar a chave das propriedades pelo próprio atributo message, delimitando com os caracteres `{` e `}`:

```java
public record DadosCadastroMedico(
    @NotBlank(message = "{nome.obrigatorio}")
    String nome,

    @NotBlank(message = "{email.obrigatorio}")
    @Email(message = "{email.invalido}")
    String email,

    @NotBlank(message = "{telefone.obrigatorio}")
    String telefone,

    @NotBlank(message = "{crm.obrigatorio}")
    @Pattern(regexp = "\\d{4,6}", message = "{crm.invalido}")
    String crm,

    @NotNull(message = "{especialidade.obrigatoria}")
    Especialidade especialidade,

    @NotNull(message = "{endereco.obrigatorio}")
    @Valid DadosEndereco endereco) {}

```
### O que foi aprendido: 

- Criar uma classe para isolar o tratamento de exceptions da API, com a utilização da anotação `@RestControllerAdvice`;
  
- Utilizar a anotação `@ExceptionHandler`, do Spring, para indicar qual exception um determinado método da classe de tratamento de erros deve capturar;

- Tratar erros do tipo **404 (Not Found)** na classe de tratamento de erros;

- Tratar erros do tipo **400 (Bad Request)**, para erros de validação do Bean Validation, na classe de tratamento de erros;

- Simplificar o JSON devolvido pela API em casos de erro de validação do Bean Validation.

## Autenticação e autorização

### Spring Security

O Spring contém um módulo específico para tratar de segurança, conhecido como **Spring Security**.

Para usarmos no Spring Boot, também vamos utilizar esse mesmo módulo, que já existia antes do Boot, o Spring Security, sendo um módulo dedicado para tratarmos das questões relacionadas com segurança em aplicações.

Essas aplicações podem ser tanto Web quanto uma API Rest, este último sendo o nosso caso. Portanto, esse módulo é completo e contém diversas facilidades e ferramentas para nos auxiliar nesse processo de implementar o mecanismo de autenticação e autorização da aplicação ou API.

Vamos entender o que é o **Spring Security**.

### Objetivos

- Autenticação
- Autorização (controle de acesso)
- Proteção contra-ataques (CSRF, clickjacking, etc.)

Em suma, o Spring Security possui três objetivos. Um deles é providenciar um serviço para customizarmos como será o controle de **autenticação** no projeto. Isto é, como os usuários efetuam login na aplicação.

Os usuários deverão preencher um formulário? É autenticação via token? Usa algum protocolo? Assim, ele possui uma maior flexibilidade para lidar com diversas possibilidades de aplicar um controle de autenticação.

O Spring Security possui, também, a **autorização**, sendo o controle de acesso para liberarmos a requisição na API ou para fazermos um controle de permissão.

Por exemplo, temos esses usuários e eles possuem a permissão "A", já estes usuários possuem a permissão "B". Os usuários com a permissão "A" podem acessar as URLs, os que tiverem a permissão "B", além dessas URLs, podem acessar outras URLs.

Com isso, conseguimos fazer um controle do acesso ao nosso sistema.

Há, também, um mecanismo de proteção contra os principais ataques que ocorre em uma aplicação, como o CSRF (Cross Site Request Forgery) e o clickjacking.

São esses os três principais objetivos do Spring Security, nos fornecer uma ferramenta para implementarmos autenticação e autorização no projeto e nos proteger dos principais ataques. Isso para não precisarmos implementar o código que protege a aplicação, sendo que já temos disponível.

No caso da nossa API, o que faremos é o controle de autenticação e autorização, o controle de acesso.

Até o momento, não nos preocupamos com essas questões de segurança. Logo, a nossa API está pública. Isso significa que qualquer pessoa que tiver acesso ao endereço da API, consegue disparar requisições, assim como fizemos usando o Insomnia.

Sabemos o endereço da API, no caso é `localhost:8080` - dado que está rodando local na máquina. Porém, após efetuarmos o deploy da aplicação em um servidor, seria o mesmo cenário: caso alguém descubra a endereço, conseguirá enviar requisição para cadastrar um médico ou paciente, etc.

O projeto não deveria ser público, somente os funcionários da clínica deveriam ter acesso. Claro que eles utilizarão o front-end ou aplicativo mobile, mas essas aplicações de clientes irão disparar requisições para a nossa API back-end, e esta deve estar protegida.

A API back-end não deve ser pública, ou seja, receber requisições sem um controle de acesso. A partir disso, entra o Spring Security para nos auxiliar na proteção dessa API no back-end.

> **ATENÇÃO!**

> **Autenticação em aplicação Web (Stateful) != Autenticação em API Rest (Stateless)**
>
> Um detalhe importante é que no caso dos cursos de Spring Boot, estamos desenvolvendo uma API Rest, não uma aplicação Web tradicional.

Essas aplicações desenvolvidas em Java usando o Spring, com o Spring MVC (Model-View-Controller) ou JSF (JavaServer Faces). A nossa ideia é desenvolvermos só o back-end, o front-end é separado e não é o foco deste curso.

O processo de autenticação em uma aplicação Web tradicional é **diferente** do processo de autenticação em uma API Rest. Em uma aplicação Web, temos um conceito chamado de stateful.

Toda vez que um usuário efetua o login em uma aplicação Web, o servidor armazena o estado. Isto é, cria as sessões e, com isso, consegue identificar cada usuário nas próximas requisições.

Por exemplo, esse usuário é dono de determinada sessão, e esses são os dados de memória deste usuário. Cada usuário possui um espaço na memória. Portanto, o servidor armazena essas sessões, espaços em memória e cada sessão contém os dados específicos de cada usuário.

Esse é o conceito de **Stateful**, é mantido pelo servidor.

Porém, em uma API Rest, não deveríamos fazer isso, porque um dos conceitos é que ela seja **stateless**, não armazena estado. Caso o cliente da API dispare uma requisição, o servidor processará essa requisição e devolverá a resposta.

Na próxima requisição, o servidor não sabe identificar quem é que está enviando, ele não armazena essa sessão. Assim, o processo de autenticação funciona um pouco diferente, caso esteja acostumado com a aplicação Web.

Como será o processo de autenticação em uma API? Temos diversas estratégias para lidarmos com a autenticação. Uma delas é usando **Tokens**, e usaremos o JWT - JSON Web Tokens como protocolo padrão para lidar com o gerenciamento desses tokens - geração e armazenamento de informações nos tokens.


![](https://cdn1.gnarususercontent.com.br/1/723333/8fbad164-5b03-4efc-914a-d6736307788d.png)

Esse diagrama contém um esquema do processo de autenticação na API. Lembrando que estamos focando no back-end, e não no front-end. Esta será outra aplicação, podendo ser Web ou Mobile.

No diagrama, o cliente da API seria um aplicativo mobile. Assim, quando o funcionário da clínica for abrir o aplicativo, será exibida uma tela de login tradicional, com os campos "Login" e "Senha" com um botão "Entrar", para enviar o processo de autenticação.

O usuário digita o login e senha, e clica no botão para enviar. Deste modo, a aplicação captura esses dados e dispara uma requisição para a API back-end - da mesma forma que enviamos pelo Insomnia.

Logo, o primeiro passo é a requisição ser disparada pelo aplicativo para a nossa API, e no corpo desta requisição é exibido o JSON com o login e senha digitados na tela de login.

O segundo passo é capturar esse login e senha e verificar se o usuário está cadastrado no sistema, isto é, teremos que consultar o banco de dados. Por isso, precisaremos ter uma tabela em que vamos armazenar os usuários e suas respectivas senhas, que podem acessar a API.

Da mesma maneira que temos uma tabela para armazenar os médicos e outra para os pacientes, teremos uma para guardar os usuários. Logo, o segundo passo do processo de autenticação é: a nossa API capturar esse login e senha, e ir ao banco de dados efetuar uma consulta para verificar a existência dos dados desse usuário.

Se for válido, a API gera um Token, que nada mais é que uma string. A geração desse Token segue o formato JWT, e esse token é devolvido na resposta para a aplicação de cliente, sendo quem disparou a requisição.

Esse é o processo de uma requisição para efetuar o login e autenticar em uma API Rest, usando tokens. Será esse processo que seguiremos neste curso.

Isto é, teremos um controller mapeando a URL de autenticação, receberemos um DTO com os dados do login e faremos uma consulta no banco de dados. Se tiver tudo certo, geramos um token e devolvemos para o front-end, para o cliente que disparou a requisição.

Esse token deve ser armazenado pelo aplicativo mobile/front-end. Há técnicas para guardar isso de forma segura, porque esse token que identifica se o usuário está logado.

Assim, nas requisições seguintes entra o processo de autorização, que consta no diagrama a seguir:

![](https://cdn1.gnarususercontent.com.br/1/723333/b60aa0dd-bdaa-4dfa-bb3d-469ff5220ec7.png)

Na requisição de cadastrar um médico, o aplicativo exibe o formulário de cadastro de médico - simplificamos no diagrama, mas considere que é um formulário completo - e após preenchermos os dados, clicamos no botão "Salvar".

Será disparada uma requisição para a nossa API - da mesma forma que fizemos no Insomnia. No entanto, além de enviar o JSON com os dados do médico no corpo da resposta, a requisição deve incluir um cabeçalho chamado authorization. Neste cabeçalho, levamos o token obtido no processo anterior, de login.

A diferença será essa: todas as URLs e requisições que desejarmos proteger, teremos que validar se na requisição está vindo o cabeçalho authorization com um token. E precisamos validar este token, gerado pela nossa API.

Portanto, o processo de autorização é: primeiro, chega uma requisição na API e ela lê o cabeçalho authorization, captura o token enviado e valida se foi gerado pela API. Teremos um código para verificar a validade do token.

Caso não seja válido, a requisição é interrompida ou bloqueada. Não chamamos o controller para salvar os dados do médico no banco de dados, devolvemos um erro **403** ou **401**. Há protocolos HTTP específicos para esse cenário de autenticação e autorização.

Pelo fato do token estar vindo, o usuário já está logado. Portanto, o usuário foi logado previamente e recebeu o token. Este token informa se o login foi efetuado ou não. Caso seja válido, seguimos com o fluxo da requisição.

O processo de autorização funciona assim justamente porque a nossa API deve ser Stateless. Ou seja, não armazena estado e não temos uma sessão informando se o usuário está logado ou não. É como se em cada requisição tivéssemos que logar o usuário.

Todavia, seria incomum enviar usuário e senha em todas as requisições. Para não precisarmos fazer isso, criamos uma URL para realizar a autenticação (onde é enviado o login e senha), e se estiver tudo certo a API gera um token e devolve para o front-end ou para o aplicativo mobile.

Assim, nas próximas requisições o aplicativo leva na requisição, além dos dados em si, o token. Logo, não é necessário mandar login e senha, somente o token. E nesta string, contém a informação de quem é esse usuário e, com isso, a API consegue recuperar esses dados.

Essa é uma das formas de fazer a autenticação em uma API Rest.

Caso já tenha aprendido a desenvolver uma aplicação Web tradicional, o processo de autenticação em uma API Rest é diferente. Não possui o conceito de sessão e cookies, é stateless - cada requisição é individual e não armazena o estado da requisição anterior.

Como o servidor sabe se estamos logados ou não? O cliente precisa enviar alguma coisa para não precisarmos enviar login e senha em toda requisição. Ele informa o login e a senha na requisição de logar, recebe um token e nas próximas ele direciona esse mesmo token.

### Adicionando o Spring Security

```xml
//código omitido

<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
</dependency>
//código omitido
```
### Hashing de senha

Ao implementar uma funcionalidade de autenticação em uma aplicação, independente da linguagem de programação utilizada, você terá que lidar com os dados de login e senha dos usuários, sendo que eles precisarão ser armazenados em algum local, como, por exemplo, um banco de dados.

**Senhas são informações sensíveis e não devem ser armazenadas em texto aberto**, pois se uma pessoa mal intencionada conseguir obter acesso ao banco de dados, ela conseguirá ter acesso às senhas de todos os usuários. Para evitar esse problema, você deve sempre utilizar algum algoritmo de hashing nas senhas antes de armazená-las no banco de dados.

Hashing nada mais é do que uma função matemática que converte um texto em outro texto totalmente diferente e de difícil dedução. Por exemplo, o texto Meu nome é Rodrigo pode ser convertido para o texto 8132f7cb860e9ce4c1d9062d2a5d1848, utilizando o algoritmo de hashing MD5.

Um detalhe importante é que os algoritmos de hashing devem ser de mão única, ou seja, não deve ser possível obter o texto original a partir de um hash. Dessa forma, para saber se um usuário digitou a senha correta ao tentar se autenticar em uma aplicação, devemos pegar a senha que foi digitada por ele e gerar o hash dela, para então realizar a comparação com o hash que está armazenado no banco de dados.

Existem diversos algoritmos de hashing que podem ser utilizados para fazer essa transformação nas senhas dos usuários, sendo que alguns são mais antigos e não mais considerados seguros hoje em dia, como o MD5 e o SHA1. Os principais algoritmos recomendados atualmente são:

- **Bcrypt**
- **Scrypt**
- **Argon2**
- **PBKDF2**

Ao longo do curso utilizaremos o algoritmo BCrypt, que é bastante popular atualmente. Essa opção também leva em consideração o fato de que o Spring Security já nos fornece uma classe que o implementa.

```@Bean``` - Serve para exportar uma classe para o Spring, fazendo com que ele consiga carregá-la e realize a sua injeção de dependência em outras


### Resumo do módulo

- Funciona o processo de autenticação e autorização em uma API Rest;
- Adicionar o Spring Security ao projeto;
- Funciona o comportamento padrão do Spring Security em uma aplicação;
- Implementar o processo de autenticação na API, de maneira Stateless, utilizando as classes e configurações do Spring Security.

### Json Web Token

**JSON Web Token**, ou JWT, é um padrão utilizado para a geração de tokens, que nada mais são do que Strings, representando, de maneira segura, informações que serão compartilhadas entre dois sistemas.

``@Component`` - É utilizado para que o Spring carregue uma classe/componente genérico
, ou seja, serve apenas para carregar uma classe automaticamente.

### Recuperando o token

``Bearer`` - Por padrão, o tipo de prefixo Bearer é utilizado para tokens JWT. 
