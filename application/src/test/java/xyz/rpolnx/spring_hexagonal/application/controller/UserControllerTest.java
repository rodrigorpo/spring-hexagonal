package xyz.rpolnx.spring_hexagonal.application.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.SneakyThrows;
import xyz.rpolnx.spring_hexagonal.application.config.GlobalExceptionHandler;
import xyz.rpolnx.spring_hexagonal.application.config.SerializationConfig;
import xyz.rpolnx.spring_hexagonal.application.exceptions.UnexpectedErrorException;
import xyz.rpolnx.spring_hexagonal.application.model.UserDTO;
import xyz.rpolnx.spring_hexagonal.domain.exception.NotFoundException;
import xyz.rpolnx.spring_hexagonal.domain.model.User;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.CreateUserUseCase;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.DeleteUserUseCase;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.GetAllUsersUseCase;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.GetSingleUserUseCase;
import xyz.rpolnx.spring_hexagonal.domain.usecase.user.UpdateUserUseCase;

//@SpringBootTest
//@AutoConfigureMockMvc

@AutoConfigureMockMvc
@ContextConfiguration(classes = {UserController.class, GlobalExceptionHandler.class, SerializationConfig.class})
@WebMvcTest
public class UserControllerTest {

    @MockBean
    private CreateUserUseCase createUser;
    @MockBean
    private DeleteUserUseCase deleteUser;
    @MockBean
    private GetAllUsersUseCase getAllUsers;
    @MockBean
    private GetSingleUserUseCase getSingleUser;
    @MockBean
    private UpdateUserUseCase updateUser;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;


    @SneakyThrows
    @Test
    @DisplayName("Should return 404 when path not found")
    public void ShouldReturnNotFound() {
        this.mockMvc.perform(get("/not-found"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("")));
    }

    @SneakyThrows
    @Test
    @DisplayName("When getting users, should return 200 and users list")
    public void getAllUsers() {
        List<UUID> ids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ids.add(UUID.randomUUID());
        }
        List<User> users = generateUserEntityById(ids, User.class, null);

        when(getAllUsers.getAll()).thenReturn(users);

        List<UserDTO> usersDto = generateUserEntityById(ids, UserDTO.class, generateUserDto());

        this.mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(usersDto)));
    }

    @SneakyThrows
    @Test
    @DisplayName("When get single user, should return 200 and user")
    public void getUser() {
        UUID id = UUID.randomUUID();

        User user = new User(id, "USER");
        when(getSingleUser.get(id)).thenReturn(user);

        this.mockMvc.perform(get("/users/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(user)));
    }

    @SneakyThrows
    @Test
    @DisplayName("When get single user and not found, should return 404")
    public void notFoundUser() {
        UUID id = UUID.randomUUID();
        when(getSingleUser.get(id)).thenThrow(new NotFoundException("User not found"));

        boolean containsMessage = this.mockMvc.perform(get("/users/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("User not found");

        assertTrue(containsMessage);
    }

    @SneakyThrows
    @Test
    @DisplayName("When get single user and get unexpected error, should return 500")
    public void unexpectedError() {
        UUID id = UUID.randomUUID();
        when(getSingleUser.get(id)).thenThrow(new UnexpectedErrorException("Generic error"));

        String contentAsString = this.mockMvc.perform(get("/users/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectNode exceptionWrapper = mapper.readValue(contentAsString, ObjectNode.class);

        Assertions.assertEquals("Generic error", exceptionWrapper.get("cause").textValue());
    }

    @SneakyThrows
    @Test
    @DisplayName("When creating user, should return 200 and created user id")
    public void createUser() {

        User request = new User(null, "USER");

        UUID id = UUID.randomUUID();
        User expected = new User(id, "USER");
        when(createUser.create(request)).thenReturn(expected);

        this.mockMvc.perform(post("/users")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(UserDTO.fromUser(expected).onlyId())));
    }

    @SneakyThrows
    @Test
    @DisplayName("When creating user with null name, should return 400 with bad request message")
    public void createUserWithNullName() {

        User request = new User(null, null);

        boolean containsMessage = this.mockMvc.perform(post("/users")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Name cannot be null");

        assertTrue(containsMessage);
    }

    @SneakyThrows
    @Test
    @DisplayName("When updating user, should return 204")
    public void updateUser() {

        UUID id = UUID.randomUUID();
        User request = new User(null, "USER");

        this.mockMvc.perform(put("/users/{id}", id)
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    @DisplayName("When deleting user, should return 204")
    public void deletingUser() {

        UUID id = UUID.randomUUID();
        User request = new User(null, "USER");

        this.mockMvc.perform(delete("/users/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private Generate<UserDTO> generateUserDto() {
        return (UUID id, String name) -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(id);
            userDTO.setName(name);
            return userDTO;
        };
    }

    @SneakyThrows
    private static <T> List<T> generateUserEntityById(List<UUID> items, Class<T> clazz, Generate<T> gen) {
        List<T> users = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            T instance;

            if (Objects.nonNull(gen)) {
                instance = gen.generate(items.get(i), "User: #" + i);
            } else {
                instance = clazz
                        .getConstructor(UUID.class, String.class)
                        .newInstance(items.get(i), "User: #" + i);
            }
            users.add(instance);
        }

        return users;
    }

    interface Generate<T> {
        T generate(UUID id, String name);
    }
}