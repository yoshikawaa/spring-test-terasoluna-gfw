# terasoluna-gfw-test

[![Build Status](https://travis-ci.org/yoshikawaa/terasoluna-gfw-test.svg?branch=master)](https://travis-ci.org/yoshikawaa/terasoluna-gfw-test)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/33b14fa152bb44d78b85e7952f6bc786)](https://www.codacy.com/app/yoshikawaa/terasoluna-gfw-test?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=yoshikawaa/terasoluna-gfw-test&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/33b14fa152bb44d78b85e7952f6bc786)](https://www.codacy.com/app/yoshikawaa/terasoluna-gfw-test?utm_source=github.com&utm_medium=referral&utm_content=yoshikawaa/terasoluna-gfw-test&utm_campaign=Badge_Coverage)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/https/oss.sonatype.org/io.github.yoshikawaa.gfw/terasoluna-gfw-test.svg)](https://oss.sonatype.org/content/repositories/snapshots/io/github/yoshikawaa/gfw/terasoluna-gfw-test/)
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg?style=flat)](https://github.com/yoshikawaa/terasoluna-gfw-test/blob/master/LICENSE.txt)

A personal (experimental,hobby) project to create Testing library for TERASOLUNA Framework 5.x with Spring TestContext Framework.

## Notes

* Supports upper Java 8.

----

## Getting Start

### Configure Maven.

(If you use SNAPSHOT version) Add SNAPSHOT repository. (settings.xml or pom.xml)

```xml
<repositories>
    <repository>
        <id>sonatype-snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <releases><enabled>false</enabled></releases>
        <snapshots><enabled>true</enabled></snapshots>
    </repository>
</repositories>
```

### Configure dependency.

Add dependency `terasoluna-gfw-test`.

```xml
<dependency>
    <groupId>io.github.yoshikawaa.gfw</groupId>
    <artifactId>terasoluna-gfw-test</artifactId>
    <version><!--$VERSION$--></version>
    <scope>test</scope>
</dependency>
```

Auto resolved follows dependency.

* `junit`
* `mockito-core`
* `spring-test`
* `spring-security-test`
* `terasoluna-gfw-web` (optional)

----

## Function References

### `TerasolunaGfwMockMvcSupport` for Controller Tests

```java
public class ControllerTest extends TerasolunaGfwMockMvcSupport {
    @Test
    public void test() throws Exception {
        mvc.perform(get("/"));
    }
}
```

Supports testing controller based on TERASOLUNA blank project with `MockMvc`.
Provides features as follows.

* using configured `MockMvc` as protected field `mvc`.
* logging `MvcResult`. (Logger name `org.springframework.test.web.servlet.result`)
* using `@Mock`, `@InjectMocks` as annotation driven.

> By default, `MockMvc` is configured with Bean definitions of TERASOLUNA blank project, Servlet Filters, with Spring Security. You can re-configure it as extending `MockMvcSupport`.

### `transaction` for `MockMvc#perform()`

```java
import static io.github.yoshikawaa.gfw.test.web.servlet.request.TerasolunaGfwMockMvcRequestPostProcessors.transaction;

public class ControllerTest extends TerasolunaGfwMockMvcSupport {
    @Test
    public void test() throws Exception {
        mvc.perform(post("/").with(transaction()));
    }
}
```

Supports validate `@TransactionTokenCheck` more easily.
Provides token namespace patterns as follows.

* `transaction()` : process global token.  (ex. `@TransactionTokenCheck(type = TransactionTokenType.IN)`)
* `transaction(String)` : process simple namespace token. (specified by class or method ex. `@TransactionTokenCheck(namespace = "sample", type = TransactionTokenType.IN)`)
* `transaction(String, String)` : process complex namespace token. (specified by class and method)

You can request invalid token as `transaction().useInvalidToken()`.

> If you request transaction token with request parameter, token will be validated by actual transaction token check mechanism.


### `resultMessages` for `ResultAction#andExpect()`

```java
import static io.github.yoshikawaa.gfw.test.web.servlet.result.TerasolunaGfwMockMvcResultMatchers.resultMessages;

public class ControllerTest extends TerasolunaGfwMockMvcSupport {
    @Test
    public void test() throws Exception {
        mvc.perform(post("/"))
            .andExpect(resultMessages().exists())
            .andExpect(resultMessages().type(StandardResultMessageType.ERROR))
            .andExpect(resultMessages().messageExists("message1", "message2", "message3"));
    }
}
```

Supports assert `ResultMessages` more easily.
Provides result matchers as follows.

* `exists()` : `ResultMessages` exists.
* `notExists()` : `ResultMessages` not exists.
* `type(StandardResultMessageType)` : `ResultMessages` has valid type. (ex. `ResultMessages.error()`)
* `codeExists(String...)` : `ResultMessages` contains valid codes. (ex. `ResultMessage.fromCode("e.xx.fw.8001")`)
* `textExists(String...)` : `ResultMessages` contains valid texts. (ex. `ResultMessage.fromText("message")`)
* `messageExists(String...)` : `ResultMessages` contains valid resolved messages.
* `messageExists(Locale, String...)` : `ResultMessages` contains valid resolved messages with specific locale.

You can obtain `ResultMessages` specific named as `resultMessages(String)`.
And can obtain from various source as follows.

* `fromModel()` : from model. (ex. `Model#addAttribute()`)
* `fromFlashMap()` : from flash map. (ex. `RedirectAttribute#addFlashAttribute()` with redirect) 
* `fromRequest()` : from request. (ex. `HttpServletRequest#setAttribute()`)
* `fromSession()` : from session. (ex. `HttpSession#setAttribute()`)

> By default, `resultMessages()` obtain `ResultMessages#DEFAULT_MESSAGES_ATTRIBUTE_NAME` attribute from request. This is because `SystemExceptionResolver` stores `ResultMessages` in request and flash map with the above name by default.
