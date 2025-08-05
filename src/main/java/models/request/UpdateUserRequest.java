package models.request;

import models.response.UserRole;

import java.util.Objects;

public class UpdateUserRequest {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Integer age;
    private Boolean isActive;
    private UserRole role;

    public UpdateUserRequest() {}

    public UpdateUserRequest(Long id) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

}
