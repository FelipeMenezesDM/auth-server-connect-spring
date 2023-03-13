# Auth Server Connect
Biblioteca de integração com o Auth Server para geração de tokens de acesso ou validação de tokens de acesso em rotas autenticadas.

## Tópicos
- [Instalação com Maven](#instalação-com-maven)
- [Deploy manual](#deploy-manual)
- [Geração de tokens](#geração-de-tokens)
  - [Configuração básica](#configuração-básica)
  - [Provedores](#provedores) 
- [Validação de tokens](#validação-de-tokens)
- [Propriedades de configuração](#propriedades-de-configuração)

## Instalação com Maven
Crie o arquivo de configuração do maven ou inclua o repositório e o servidor no arquivo já existente:
```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" 
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>${server.github.username}</username>
      <password>${server.github.password}</password>
    </server>
  </servers>
  
  <activeProfiles>
    <activeProfile>general</activeProfile>
  </activeProfiles>

  <profiles>
    <profile>
      <id>general</id>
      <repositories>
        <repository>
          <id>central</id>
          <url>https://repo1.maven.org/maven2</url>
        </repository>
        <repository>
          <id>github</id>
          <url>https://maven.pkg.github.com/felipemenezesdm/auth-server-connect-spring</url>
        </repository>
      </repositories>
    </profile>
  </profiles>
</settings>
```

Inclua a dependência no arquivo pom:
```xml
<dependency>
  <groupId>br.com.felipemenezesdm</groupId>
  <artifactId>auth-server-connect-spring</artifactId>
  <version>1.0.0</version>
</dependency>
```

Execute com comando abaixo para download de dependências:
```
mvn install
```

## Deploy manual
O deploy da biblioteca é realizado automaticamente sempre que houver a criação de uma nova tag de versão. Entretanto, se for necessário realizar seu deploy manual, é preciso seguir os passos abaixo:

1. No _settings.xml_, confirmar que o servidor do GitHub está configurado:
    ```xml
      <servers>
        <server>
          <id>github</id>
          <username>${server.github.username}</username>
          <password>${server.github.password}</password>
        </server>
      </servers>
    ```
2. Executar o comando abaixo, substuindo os parâmetros por seus respectivos valores:
    ```
    mvn deploy -s settings.xml -Dserver.github.username=USERNAME -Dserver.github.password=PASSWORD
    ```

## Geração de tokens
Ao instalar a dependência, seguindo o exemplo do tópico anterior, a anotação _@AuthServerConnect_ estará disponível para uso nas classes de integração do seu projeto.

### Configuração básica
Abaixo, um exemplo de implamentação do gerador de tokens em um service:

```java
// Integration.java
@Service
@AuthServerConnection
public class Integration {
    AuthServerToken authServerToken;

    public String send() {
        return authServerToken.getAccessToken().getTokenValue();
    }
}
```
```yaml
# application.yml
auth-server:
  uri: http://localhost:1080/api/v1
  source:
    provider: application
    props:
      client-id: 9835e498-eb18-4d7a-8e8e-230b2190df1f
      client-secret: 51469bf9-081f-4023-a391-b94877bb5b1b
```

1. Informar a anotação _@AuthServerConnection_ para classe.
2. Criar uma propriedade do tipo _AuthServerToken_ que irá armazenar os tokens gerados.
3. Connfigurar as propriedades básicas da aplicação.

### Provedores
Atualmente, a biblioteca disponibiliza quatro formas para obter as credenciais para geração de tokens no Auth Server: **application**, **environment**, **aws** e **gcp**.

#### Application
As credenciais podem ser definidas diretamente no arquivo de propriedades da aplicação, no formado de yaml (.yml) ou resource bundle (.properties).

- yml
```yaml
auth-server:
  uri: http://localhost:1080/api/v1
  source:
    provider: application
    props:
      client-id: 9835e498-eb18-4d7a-8e8e-230b2190df1f
      client-secret: 51469bf9-081f-4023-a391-b94877bb5b1b
```

- resource bundle
```properties
auth-server.uri=http://localhost:1080/api/v1
auth-server.source.provider=application
auth-server.source.props.client-id=9835e498-eb18-4d7a-8e8e-230b2190df1f
auth-server.source.props.client-secret=51469bf9-081f-4023-a391-b94877bb5b1b
```

#### Environment
As credenciais podem ser definidas por meio de variáveis de ambiente no sistema.

```yaml
auth-server:
  uri: http://localhost:1080/api/v1
  source:
    provider: environment
```

```shell
export client_id=9835e498-eb18-4d7a-8e8e-230b2190df1f
export client_secret=51469bf9-081f-4023-a391-b94877bb5b1b
```

#### AWS
As credenciais podem ser obtidas a partir das Secrets da Amazon Web Services.

```yaml
auth-server:
  uri: http://localhost:1080/api/v1
  source:
    provider: aws
    props:
      region: us-east-1
      secret-name: my-secret-name
```

#### GCP
As credenciais podem ser obtidas a partir das Secrets do Google Cloud Platform.

```yaml
auth-server:
  uri: http://localhost:1080/api/v1
  source:
    provider: gcp
    props:
      project-id: gcp-project
      secret-name: my-secret-name
```

## Validação de tokens
Para validar todas as rotas de um controller, é necessário informar a anotação _@AuthServerValidation_ para a classe:

```java
@RestController
@AuthServerValidation(scopes = {"scope1"})
public class Controller {
    @Autowired
    Integration integration;

    @GetMapping("/test")
    public String test() {
        return integration.send();
    }
}
```

Para validar um rota específica, é necessário informar a anotação @AuthServerValidation para o méthodo onde a rota está mapeada:
```java
@RestController
public class Controller {
    @Autowired
    Integration integration;

    @GetMapping("/test")
    @AuthServerValidation(scopes = {"scope2", "scope3"})
    public String test() {
        return integration.send();
    }
}
```

É possível também definir os escopos de acesso para as rotas, tanto no controller quanto no método. Quando definido em ambos os targets, os escopos são mesclados:
```java
@RestController
@AuthServerValidation(scopes = {"scope1"})
public class Controller {
    @Autowired
    Integration integration;

    @GetMapping("/test")
    @AuthServerValidation(scopes = {"scope2", "scope3"})
    public String test() {
        return integration.send();
    }
}
```

## Propriedades de configuração
Descrição de todas as propriedades de configuração disponíveis para a biblioteca.

```yaml
# application-example.yml
auth-server:
  enabled: #
  name: #
  uri: #
  redirect-uri: #
  grant-type: #
  scopes: #
  source:
    provider: #
    props:
      project-id: #
      region: #
      client-id-key: #
      client-secret-key: #
      end-point: #
      secret-name: #
      client-id: #
      client-secret: #
```

- **enabled:**
    - **tipo:** _boolean_
    - **descrição:** definir se as validações estão habilitadas ou desabilitadas.
    - **obrigatório:** sim
    - **padrão:** true
- **name:**
    - **tipo:** _string_
    - **descrição:** nome de identificação do conector
    - **obrigatório:** não
    - **padrão:** _null_
- **uri:**
  - **tipo:** _string_
  - **descrição:** URL padrão para o servidor do Auth Server
  - **obrigatório:** sim
- **redirect-uri:**
  - **tipo:** _string_
  - **descrição:** URL de redirecionamento do cliente
  - **obrigatório:** não
  - **padrão:** _null_
- **grant-type:**
  - **tipo:** _string_
  - **descrição:** tipo de concessão do token
  - **obrigatório:** sim
  - **padrão:** client_credentials
- **scopes:**
    - **tipo:** _string_
    - **descrição:** escopos para geração do token, seperados por vírgula. Ex: scope1,scope2,scope2
    - **obrigatório:** não
    - **padrão:** null
- **source:**
  - **provider:**
      - **tipo:** _string_
      - **descrição:** tipo de provedor de credencials. Atualmente disponíveis: **application**, **environment**, **aws** e **gcp**
      - **obrigatório:** sim
      - **padrão:** environment
  - **props:**
    - **project-id**
      - **tipo:** _string_
      - **descrição:** exclusivo para o provedor **gcp**, para identificar o projeto do qual as credencials serão obtidas.
      - **obrigatório:** sim, quando o provedor for **gcp**
    - **region:**
      - **tipo:** _string_
      - **descrição:** exclusivo para o provedor **aws**, para identificar a região padrão do cliente.
      - **obrigatório:** sim, quando o provedor for **aws**
    - **client-id-key:**
      - **tipo:** _string_
      - **descrição:** definir a chave para o ID do cliente no payload de secrets ou nas variáveis de ambiente. Neste caso, esta configuração é válida para os provedores **aws**, **gcp** e **environment**. Por exemplo, se o client-id-key for definido como "my-client-id" e o provedor for "environment", será necessário criar uma variável de ambiente chamada "my-client-id" para armazenar o client-id que será usado para geração de tokens.
      - **obrigatório:** sim
      - **padrão:** client_id
    - **client-secret-key:**
      - **tipo:** _string_
      - **descrição:** definir a chave para a secret do cliente no payload de secrets ou nas variáveis de ambiente. Neste caso, esta configuração é válida para os provedores **aws**, **gcp** e **environment**. Por exemplo, se o client-secret-key for definido como "my-client-secret" e o provedor for "environment", será necessário criar uma variável de ambiente chamada "my-client-secret" para armazenar o client-secret que será usado para geração de tokens.
      - **obrigatório:** sim
      - **padrão:** client_secret
    - **end-point:**
      - **tipo:** _string_
      - **descrição:** quando o provedor for igual a **aws**, este parâmetro pode ser configurado para definir o endpoint de onde serão obtidas as credenciais. É bastante útil para quando se está utilizando o LocalStack, por exemplo.
      - **obrigatório:** não
    - **secret-name:**
      - **tipo:** _string_
      - **descrição:** definir o nome da secret onde serão obtidas as credenticiais. Válido para os provedores **aws** e **gcp**.
      - **obrigatório:** sim, quando o provedor for **aws** ou **gcp**
    - **client-id:**
      - **tipo:** _string_
      - **descrição:** valor do ID do cliente. A aplicação irá recuperar o valor desta propriedade quando o provedor for igual a **application**.
      - **obrigatório:** sim, quando o provedor **application**
    - **client-secret:**
      - **tipo:** _string_
      - **descrição:** valor da secret do cliente. A aplicação irá recuperar o valor desta propriedade quando o provedor for igual a **application**.
      - **obrigatório:** sim, quando o provedor for **application**
