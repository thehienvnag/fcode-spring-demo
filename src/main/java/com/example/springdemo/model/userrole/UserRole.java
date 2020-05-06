package com.example.springdemo.model.userrole;

import com.example.springdemo.model.role.Role;
import com.example.springdemo.model.user.User;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "user_role")
@Entity
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", insertable = false, nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userid", referencedColumnName = "Id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roleid", referencedColumnName = "Id")
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String toString() {
      return "UserRole{id=" + id +
        "}";
    }
}