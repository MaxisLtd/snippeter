package com.mbeliakov.snippeter.service.dto;

import com.mbeliakov.snippeter.domain.User;

/**
 * A DTO representing a user, with only the public attributes.
 */
public class UserModel {

    private Long id;

    private String login;

    public UserModel() {
        // Empty constructor needed for Jackson.
    }

    public UserModel(User user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.login = user.getLogin();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserModel{" +
            "id='" + id + '\'' +
            ", login='" + login + '\'' +
            "}";
    }
}
