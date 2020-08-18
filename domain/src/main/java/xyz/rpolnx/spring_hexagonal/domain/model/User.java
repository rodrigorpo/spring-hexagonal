package xyz.rpolnx.spring_hexagonal.domain.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {
    private UUID id;
    private String name;

    public User withGenerateId() {
        this.id = UUID.randomUUID();
        return this;
    }
}
