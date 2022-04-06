package com.example.taskmanager.Entity;
import javax.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@RequiredArgsConstructor
@EqualsAndHashCode
public class Tokens {
    @Id
    private String jwtToken;

    @ManyToOne
    private User user;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Tokens{" +"username"+user.getUsername()+'\''+
                "jwtToken='" + jwtToken + '\'' +
                '}';
    }
}
