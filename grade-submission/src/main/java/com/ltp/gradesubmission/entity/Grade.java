package com.ltp.gradesubmission.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "grade", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "course_id"}) // this is a multicolumn unique constraint: there can't be more than one grade with the same student id and course id
})

public class Grade {

    @Id // this marks the column as the id (primary key) column
    @GeneratedValue(strategy = GenerationType.IDENTITY) // this will generate a random id value
    @Column(name = "id")
    private Long id;
    
    @Column(name = "score", nullable = false) // a grade can't be stored without filling the score field
    private String score;    

    @ManyToOne(optional = false) // this marks a many-to-one relationship; a grade can't be stored without a relationship with a student
    @JoinColumn(name = "student_id", referencedColumnName = "id") // this indicates the foreign key and the primary key in the relationship
    private Student student;

    @ManyToOne(optional = false) // this marks a many-to-one relationship; a grade can't be stored without a relationship with a course
    @JoinColumn(name = "course_id", referencedColumnName = "id") // this indicates the foreign key and the primary key in the relationship
    private Course course;
    
}
