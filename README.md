# Static Code Generation for QueryDSL EntityQL 

[![Build Status](https://www.travis-ci.com/eXsio/querydsl-entityql-maven-plugin.svg?branch=master)](https://www.travis-ci.com/eXsio/querydsl-entityql-maven-plugin.svg?branch=master)
[![](https://jitpack.io/v/eXsio/querydsl-entityql-maven-plugin.svg)](https://jitpack.io/#eXsio/querydsl-entityql-maven-plugin)

This repository is a part of [EntityQL](https://github.com/eXsio/querydsl-entityql) project.
It is a Maven Plugin that generates QueryDSL-SQL compatible Static Models from JPA Entities. 

## Generating JPA Models:

- add the following Maven Repositories to your ```pom.xml```:

```xml

    <!-- JitPack repository for EntityQL Maven plugin -->
    <pluginRepositories>
        <pluginRepository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </pluginRepository>
    </pluginRepositories>

```
 
- add the following Maven Plugin to your ```pom.xml```:

```xml

    <plugin>
        <groupId>com.github.eXsio</groupId>
        <artifactId>querydsl-entityql-maven-plugin</artifactId>
        <version>1.2.8</version>
        <configuration>
            <generators>
                <!-- use as many generators as you need -->
                <generator>
                    <sourcePackage>pl.exsio.querydsl.entityql.examples.entity</sourcePackage>
                    <destinationPackage>pl.exsio.querydsl.entityql.examples.entity.generated</destinationPackage>
                    <!-- below settings are not required. If not provided, will have the below, default values -->
                    <!-- <destinationPath>${project.basedir}\src\main\java</destinationPath>-->
                    <!-- <filenamePattern>Q%s.java</filenamePattern>-->
                </generator>
            </generators>
        </configuration>
        <dependencies>
            <!-- use EntityQL in the same version as in project -->
            <dependency>
                <groupId>com.github.eXsio</groupId>
                <artifactId>querydsl-entityql</artifactId>
                <version>3.2.0</version>
            </dependency>
        </dependencies>
    </plugin>

```

- generate Static Models by running the ```querydsl-entityql:generate-models``` plugin from Command Line or your IDE

## Generating Spring Data JDBC Models:

- add the following Maven Repositories to your ```pom.xml```:

```xml

    <!-- JitPack repository for EntityQL Maven plugin -->
    <pluginRepositories>
        <pluginRepository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </pluginRepository>
    </pluginRepositories>

```

- add the following Maven Plugin to your ```pom.xml```:

```xml

    <plugin>
        <groupId>com.github.eXsio</groupId>
        <artifactId>querydsl-entityql-maven-plugin</artifactId>
        <version>1.2.8</version>
        <configuration>
            <generators>
                <!-- use as many generators as you need -->
                <generator>
                    <type>SPRING_DATA_JDBC</type>
                    <sourcePackage>pl.exsio.querydsl.entityql.examples.entity</sourcePackage>
                    <destinationPackage>pl.exsio.querydsl.entityql.examples.entity.generated</destinationPackage>
                    <!-- out of the box you can use one of the 2 provided naming strategies
                        - pl.exsio.querydsl.entityql.jdbc.UpperCaseWithUnderscoresNamingStrategy
                        - pl.exsio.querydsl.entityql.jdbc.DefaultNamingStrategy
                        If you want to use your own custom one, you will have to add a new <dependency/> that contains it
                    -->
                    <params>
                        <namingStrategy>pl.exsio.querydsl.entityql.jdbc.UpperCaseWithUnderscoresNamingStrategy</namingStrategy>
                    </params>
                    <!-- below settings are not required. If not provided, will have the below, default values -->
                    <!-- <destinationPath>${project.basedir}\src\main\java</destinationPath>-->
                    <!-- <filenamePattern>Q%s.java</filenamePattern>-->
                </generator>
            </generators>
        </configuration>
        <dependencies>
            <!-- use EntityQL in the same version as in project -->
            <dependency>
                <groupId>com.github.eXsio</groupId>
                <artifactId>querydsl-entityql</artifactId>
                <version>3.2.0</version>
            </dependency>
        </dependencies>
    </plugin>

```

- generate Static Models by running the ```querydsl-entityql:generate-models``` plugin from Command Line or your IDE

## Using Models:

- add the following Maven Repositories to your ```pom.xml```:

```xml

<!-- JitPack repository for EntityQL library -->
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

```

 - add the following Maven Dependencies to your ```pom.xml```:
 
 ```xml
 
    <!-- EntityQL in the newest available version -->
    <dependency>
        <groupId>com.github.eXsio</groupId>
        <artifactId>querydsl-entityql</artifactId>
        <version>3.2.0</version>
    </dependency>

    <!-- basic dependencies required by EntityQL -->
    <dependency>
        <groupId>com.querydsl</groupId>
        <artifactId>querydsl-sql</artifactId>
        <version>5.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.hibernate.javax.persistence</groupId>
        <artifactId>hibernate-jpa-2.1-api</artifactId>
        <version>1.0.2.Final</version>
    </dependency>

    <!-- dependencies required by EntityQL when used with Spring -->
    <dependency>
        <groupId>com.querydsl</groupId>
        <artifactId>querydsl-sql-spring</artifactId>
        <version>5.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.reflections</groupId>
        <artifactId>reflections</artifactId>
        <version>0.9.12</version>
    </dependency>

```

- configure QueryDSL:

```java

@Bean
public SQLTemplates sqlTemplates() {
    //choose the implementation that matches your database engine
    return new H2Templates(); 
}

@Bean
public SQLQueryFactory queryFactory(DataSource dataSource, SQLTemplates sqlTemplates) {
    //last param is an optional varargs String with all the java.lang.Enum packages that you use in your Entities
    return new EntityQlQueryFactory(new Configuration(sqlTemplates), dataSource, "your.enums.package");
}

```

- Create SQL Queries with generated models:

```java

//obtain instances of generated models
 QBook book = QBook.INSTANCE; 
 QOrder order = QOrder.INSTANCE;
 QOrderItem orderItem = QOrderItem.INSTANCE;

//use them by creating and executing a Native Query using QueryDSL API
Long count = queryFactory.select(count())
                .from(
                        select(
                                 book.name, 
                                 order.id
                        )
                        .from(orderItem)
                        .innerJoin(orderItem.book, book)
                        .innerJoin(orderItem.order, order)
                        .where(book.price.gt(new BigDecimal("80")))
                        .groupBy(book.category) 
                ).fetchOne();
```

## Don't like calling Maven plugin manually?

Use this Maven Run Configuration to automate things:

```
mvn clean install querydsl-entityql:generate-models install --also-make -f pom.xml
```

**You can create and save this Run configuration in your IDE to make things easy**

Alternatively you can configure custom Maven profile that will pre-compile and generate the Query Models:

```xml
    <profile>
        <id>generate-query-models</id>
        <build>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.3</version>
                    <executions>
                        <execution>
                            <id>generate</id>
                            <phase>generate-sources</phase>
                            <goals>
                                <goal>compile</goal>
                            </goals>
                        </execution>
                    </executions>
                    <configuration>
                        ...
                    </configuration>
                </plugin>
                <plugin>
                    <groupId>com.github.eXsio</groupId>
                    <artifactId>querydsl-entityql-maven-plugin</artifactId>
                    <version>1.2.8</version>
                    <executions>
                        <execution>
                            <id>generate</id>
                            <phase>generate-sources</phase>
                            <goals>
                                <goal>generate-models</goal>
                            </goals>
                        </execution>
                    </executions>
                    <configuration>
                        ...
                    </configuration>
                    <dependencies>
                        ...
                    </dependencies>
                </plugin>

            </plugins>
        </build>
    </profile>
```

**This way Query Model generation will be a part of standard clean-install Maven build, as long as the Profile is enabled.**
**It will also ensure that Models are based on the current source code.**

## Examples

Feel free to browse the [Examples Project](https://github.com/eXsio/querydsl-entityql-examples) to find out how to use EntityQL in your code.
