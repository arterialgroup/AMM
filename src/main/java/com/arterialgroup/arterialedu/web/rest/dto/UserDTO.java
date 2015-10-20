package com.arterialgroup.arterialedu.web.rest.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

public class UserDTO {

	private Long id;
	
    //@Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @NotNull
    @Size(min = 5, max = 100)
    private String password;

    @Size(max = 50)
    private String accreditation;

    @Size(max = 50)
    private String accreditationType;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @Size(max = 50)
    private String state;

    @Size(min = 2, max = 5)
    private String langKey;

    private List<String> roles;

    public UserDTO() {
    }

    public UserDTO(Long id, String login, String password, String accreditation, String accreditationType, String firstName, String lastName, String email, String state, String langKey,
                   List<String> roles) {
        this.id = id;
    	this.login = login;
        this.password = password;
        this.accreditation = accreditation;
        this.accreditationType = accreditationType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.langKey = langKey;
        this.roles = roles;
    }

    public Long getId(){
    	return this.id;
    }
    
    public String getPassword() {
        return password;
    }

    public String getAccreditation() {
        return accreditation;
    }

    public String getAccreditationType() {
        return accreditationType;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getState() {
        return state;
    }

    public String getLangKey() {
        return langKey;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserDTO u = (UserDTO) o;

        if ( ! Objects.equals(login, u.getLogin())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(login);
    }
    
    @Override
    public String toString() {
        return "UserDTO{" +
        "login='" + login + '\'' +
        ", password='" + password + '\'' +
        ", accreditation='" + accreditation + '\'' +
        ", accreditationType='" + accreditationType + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", state='" + state + '\'' +
        ", langKey='" + langKey + '\'' +
        ", roles=" + roles +
        '}';
    }
}
