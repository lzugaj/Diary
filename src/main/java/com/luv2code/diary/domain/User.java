package com.luv2code.diary.domain;

import com.luv2code.diary.domain.enums.UserStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "USER")
public class User extends BaseEntity implements Comparable<User> {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "number_of_notes")
    private Integer numberOfNotes;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "user")
    private List<Note> notes;

    @Override
    public int compareTo(User other) {
        if (!getLastName().equals(other.getLastName())) {
            return getLastName().compareTo(other.getLastName());
        } else if (!getFirstName().equals(other.getFirstName())) {
            return getFirstName().compareTo(other.getFirstName());
        } else {
            return getUsername().compareTo(other.getUsername());
        }
    }
}
