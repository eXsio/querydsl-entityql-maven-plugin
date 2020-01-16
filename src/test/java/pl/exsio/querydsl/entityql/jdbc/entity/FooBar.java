package pl.exsio.querydsl.entityql.jdbc.entity;

import org.springframework.data.annotation.Id;

import java.util.Optional;

public class FooBar {

    @Id
    private final Long id;

    public FooBar(Long id) {
        this.id = id;
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }
}
