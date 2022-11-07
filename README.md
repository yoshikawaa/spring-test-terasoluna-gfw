# spring-test-terasoluna-gfw

[![Build Status](https://github.com/yoshikawaa/spring-test-terasoluna-gfw/actions/workflows/maven.yml/badge.svg)](https://github.com/yoshikawaa/spring-test-terasoluna-gfw/actions)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/90eb08daa5ce4bb89f15a14691dcd98f)](https://www.codacy.com/gh/yoshikawaa/spring-test-terasoluna-gfw/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=yoshikawaa/spring-test-terasoluna-gfw&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/90eb08daa5ce4bb89f15a14691dcd98f)](https://www.codacy.com/gh/yoshikawaa/spring-test-terasoluna-gfw/dashboard?utm_source=github.com&utm_medium=referral&utm_content=yoshikawaa/spring-test-terasoluna-gfw&utm_campaign=Badge_Coverage)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.yoshikawaa.gfw/spring-test-terasoluna-gfw.svg)](https://repo.maven.apache.org/maven2/io/github/yoshikawaa/gfw/spring-test-terasoluna-gfw/)
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg?style=flat)](https://github.com/yoshikawaa/spring-test-terasoluna-gfw/blob/main/LICENSE.txt)

Library that supports testing of TERASOLUNA Server Framework 5.x Common Library with Spring TestContext Framework.

> This is a personal experimental project unrelated to TERASOLUNA. TERASOLUNA is a registered trademark of NTT DATA Corporation.

## Notes

* Supports upper Java 11
* Supports JUnit 4/5(Jupiter)
* Supports Terasoluna 5.7.1.SP1

----

## Artifacts

* [spring-test-terasoluna-gfw-context](#spring-test-terasoluna-gfw-context)
* [spring-test-terasoluna-gfw](#spring-test-terasoluna-gfw)

----

## spring-test-terasoluna-gfw-context

Library that easily set up tests that based on [TERASOLUNA blank project](https://github.com/terasolunaorg/terasoluna-gfw-web-multi-blank).

### Getting Start

```xml
<dependency>
    <groupId>io.github.yoshikawaa.gfw</groupId>
    <artifactId>spring-test-terasoluna-gfw-context</artifactId>
    <version>1.0.1</version>
    <scope>test</scope>
</dependency>
```

### Features

#### Test Context Annotations

* `@TerasolunaGfwDomainTest`
* `@TerasolunaGfwWebAppTest`

##### `@TerasolunaGfwDomainTest`

Supports configuring tests for domain layer (`@Service`, `@Repository`) that based on TERASOLUNA blank project.

```java
@RunWith(SpringRunner.class) // required only for JUnit4
@TerasolunaGfwDomainTest
public class ServiceTest {
}
```

Provides features as follows.

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath*:test-context.xml")
```

##### `@TerasolunaGfwWebAppTest`

Supports configuring tests for web application layer (`@Controller`) that based on TERASOLUNA blank project.

```java
@RunWith(SpringRunner.class) // required only for JUnit4
@TerasolunaGfwWebAppTest
public class ControllerTest {
}
```

Provides features as follows.

```java
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration({
            "classpath*:META-INF/spring/applicationContext.xml",
            "classpath*:META-INF/spring/spring-security.xml" }),
        @ContextConfiguration("classpath*:META-INF/spring/spring-mvc.xml") })
```

#### `MockMvc` Builders

* `TerasolunaGfwMockMvcBuilders`

##### `TerasolunaGfwMockMvcBuilders`

Supports configuring `MockMvc` that based on TERASOLUNA blank project.

```java
@RunWith(SpringRunner.class) // required only for JUnit4
@TerasolunaGfwWebAppTest
public class ControllerTest {
  @AutoWired
  WebApplicationContext context;
  MockMvc mvc;

  @Before
  public void setup() {
    mvc = TerasolunaGfwMockMvcBuilders.setup(context)
            .alwaysDo(log())
            .build();
  }

  @Test
  public void test() throws Exception {
    mvc.perform(get("/test")...
  }
}
```

Provides features as follows.

* Enable Web Application Context.
* Enable Servlet Filters.
  - `MDCClearFilter`
  - `exceptionLoggingFilter`
  - `XTrackMDCPutFilter`
  - `CharacterEncodingFilter`
* Enable Spring Security.

----

## spring-test-terasoluna-gfw

Library that easily test function of Terasoluna Framework 5.x Common Libraries.

### Getting Start

```xml
<dependency>
    <groupId>io.github.yoshikawaa.gfw</groupId>
    <artifactId>spring-test-terasoluna-gfw</artifactId>
    <version>1.0.1</version>
    <scope>test</scope>
</dependency>
```

### Features

#### Testing support for MockMvc

* `TerasolunaGfwMockMvcRequestPostProcessors`
  - `transaction()`
* `TerasolunaGfwMockMvcResultMatchers`
  - `transactionToken()`
  - `resultMessages()`

##### `transaction()`

Supports passing token for `@TransactionTokenCheck`.

```java
import static io.github.yoshikawaa.gfw.test.web.servlet.request.TerasolunaGfwMockMvcRequestPostProcessors.transaction;

    @Test
    public void test() throws Exception {
        mvc.perform(post("/").with(transaction()));
    }
```

Provides token namespace patterns as follows.

| pattern | description |
|---------|-------------|
| `transaction()`                   | process global token.<br> (`@TransactionTokenCheck`) |
| `transaction(String)`             | process single namespace token.<br>(`@TransactionTokenCheck(namespace = "sample")`) |
| `transaction(String, String)`     | process combined class and method namespace token. |

And you can process invalid token value as `transaction().useInvalidToken()`.

> If you send transaction token without `transaction()`, token will be validated by actual transaction token check mechanism.

##### `transactionToken()`

Supports asserting token generated by `@TransactionTokenCheck`.

```java
import static io.github.yoshikawaa.gfw.test.web.servlet.result.TerasolunaGfwMockMvcResultMatchers.transactionToken;

    @Test
    public void test() throws Exception {
        mvc.perform(post("/").with(transaction()))
            .andExpect(transactionToken().namespace("/"))
            .andExpect(transactionToken().updated());
    }
```

Provides result matchers as follows.

| matcher | description |
|---------|-------------|
| `global()`                   | assert global token.<br> (`@TransactionTokenCheck`) |
| `namespace(String)`          | assert single namespace token.<br>(`@TransactionTokenCheck(namespace = "sample")`) |
| `namespace(String, String)`  | assert combined class and method namespace token. |
| `updated()`                  | assert token is updated.<br>(`@TransactionTokenCheck(type = TransactionTokenType.IN)`) |
| `notUpdated()`               | assert token is not updated.<br>(`@TransactionTokenCheck(type = TransactionTokenType.CHECK)`) |
| `notExists()`                | assert token not exists. |

##### `resultMessages()`

Supports asserting `ResultMessages`.

```java
import static io.github.yoshikawaa.gfw.test.web.servlet.result.TerasolunaGfwMockMvcResultMatchers.resultMessages;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.ERROR;

    @Test
    public void test() throws Exception {
        mvc.perform(post("/"))
            .andExpect(resultMessages().type(ERROR))
            .andExpect(resultMessages().messageExists("message1", "message2", "message3"));
    }
```

Provides result matchers as follows.

| matcher | description |
|---------|-------------|
| `exists()`                         | assert `ResultMessages` exists. |
| `notExists()`                      | assert `ResultMessages` not exists. |
| `type(ResultMessageType)`          | assert `ResultMessages` has type.<br>(`ResultMessages.error()`) |
| `codeExists(String...)`            | assert `ResultMessages` has codes.<br>(`ResultMessage.fromCode("e.xx.fw.8001")`) |
| `textExists(String...)`            | assert `ResultMessages` has texts.<br>(`ResultMessage.fromText("message")`) |
| `messageExists(String...)`         | assert `ResultMessages` has resolved messages. (required `MessageSource`) |
| `messageExists(Locale, String...)` | assert `ResultMessages` has resolved messages. (required `MessageSource`) |

You can also obtain `ResultMessages` from various source and name.

```java
resultMessages("messages").fromModel()
```

| source | description |
|--------|-------------|
| `fromModel()`    | obtain from model.<br>(`Model#addAttribute()`) |
| `fromFlashMap()` | obtain from flash map.<br>(`RedirectAttribute#addFlashAttribute()`) |
| `fromRequest()`  | obtain from request.<br>(`HttpServletRequest#setAttribute()`) |
| `fromSession()`  | obtain from session.<br>(`HttpSession#setAttribute()`) |

> By default, `resultMessages()` obtain `ResultMessages#DEFAULT_MESSAGES_ATTRIBUTE_NAME` attribute from request. This is because `SystemExceptionResolver` stores `ResultMessages` in request and flash map with the above name.
