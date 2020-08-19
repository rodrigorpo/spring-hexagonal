package xyz.rpolnx.spring_hexagonal.application.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static lombok.AccessLevel.PRIVATE;

import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.rpolnx.spring_hexagonal.domain.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(NON_NULL)
public class UserDTO {
    public UUID id;
    @NotNull(message = "Name cannot be null")
    private String name;

    public User toUser() {
        return new User(id, name);
    }

    public static UserDTO fromUser(@NonNull User user) {
        return new UserDTO(user.getId(), user.getName());
    }

    public UserDTO onlyId() {
        this.name = null;
        return this;
    }
}
