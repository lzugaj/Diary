package com.luv2code.diary.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "NOTE")
public class Note extends BaseEntity implements Comparable<Note> {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "event_date")
    private Date eventDate;

    @Column(name = "creation_date")
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Override
    public int compareTo(Note other) {
        return getEventDate().compareTo(other.getEventDate());
    }
}
