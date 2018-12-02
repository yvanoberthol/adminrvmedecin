package com.yvanscoop.gestmedecins.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "entrez le nom de l'utilisateur.")
    @Size(min = 4,max = 20, message = "le nom doit etre de 4 à 20 caracteres.")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "entrez un mot de passe.")
    @Size(min = 8,max = 255, message = "le mot de passe doit être au moins de 8 caractères.")
    private String password;

    private boolean active;

    @ManyToMany
    @JoinTable(name = "users_roles")
    private Collection<Role> roles;

    public User() {
    }

    public User(String username, String password, boolean active) {
        this.username = username;
        this.password = password;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
