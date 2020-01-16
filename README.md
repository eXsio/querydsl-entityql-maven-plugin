# Static Code Generation for QueryDSL EntityQL

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
        <version>1.1.1</version>
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
                <version>2.2.0</version>
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
        <version>1.1.1</version>
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
                <version>2.2.0</version>
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
        <version>2.1.0</version>
    </dependency>

    <!-- basic dependencies required by EntityQL -->
    <dependency>
        <groupId>com.querydsl</groupId>
        <artifactId>querydsl-sql</artifactId>
        <version>4.2.2</version>
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
        <version>4.2.2</version>
    </dependency>
    <dependency>
        <groupId>org.reflections</groupId>
        <artifactId>reflections</artifactId>
        <version>0.9.11</version>
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

## Examples

Feel free to browse the [Examples Project](https://github.com/eXsio/querydsl-entityql-examples) to find out how to use EntityQL in your code.
