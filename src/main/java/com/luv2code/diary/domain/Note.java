package com.luv2code.diary.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "NOTE")
public class Note extends BaseEntity {

    private String title;

    private String description;

    private String location;

    private LocalDate eventDate;

    private LocalDate creationDate;

}
