package com.ltp.gradesubmission.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity // this marks the class as an entity
@Table(name = "student") // this instructs JPA to create a table with the name "student"
public class Student {

    @Id // this indicates to JPA that this field is the id field
    @GeneratedValue(strategy = GenerationType.IDENTITY) // this will generate a random id value
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "name", nullable = false) // a student can't be stored without filling the name field
    private String name;

    @NonNull
    @Column(name = "birth_date", nullable = false) // a student can't be stored without filling the birth_date field
    private LocalDate birthDate;

    @JsonIgnore // when a student entity is being serialized into JSON we don’t want this field to be part of the JSON, otherwise the serialization process will go into a recursive loop and then the request will get aborted
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL) // any operation that we perform on a particular student will get cascaded to all of its associated child entities
    private List<Grade> grades;

    @JsonIgnore // this prevents a recursive loop in deserialization that would lead to a 500 status code
    @ManyToMany
    @JoinTable( // this creates a join table which works as a bridge between course and student
            name = "course_student",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )
    private Set<Course> courses; // I'm using Set instead of List because Set prevents duplicate entries
    
}
