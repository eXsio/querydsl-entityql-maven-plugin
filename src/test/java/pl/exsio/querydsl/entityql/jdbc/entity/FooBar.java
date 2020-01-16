package pl.exsio.querydsl.entityql.jdbc.entity;

import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;

@Value
public class FooBar {

    @Id
    @With
    private final Long id;
}
