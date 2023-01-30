package com.ltp.gradesubmission.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course")
public class Course {

    @Id // this indicates to JPA that this field is the id field
    @GeneratedValue(strategy = GenerationType.IDENTITY) // this generates a random id value
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "subject", nullable = false) // a course can't be stored without filling the subject field
    private String subject;

    @NonNull
    @Column(name = "code", nullable = false, unique = true) // a course can't be stored without filling the code field; moreover, it must be a unique value (it can't be the same for another course)
    private String code;

    @NonNull
    @Column(name = "description", nullable = false) // a course can't be stored without filling the description field
    private String description;

    @JsonIgnore // when a course entity is being serialized into JSON we don’t want this field to be part of the JSON, otherwise the serialization process will go into a recursive loop and then the request will get aborted
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL) // any operation that we perform on a particular course will get cascaded to all of its associated child entities
    private List<Grade> grades;

    @JsonIgnore
    @ManyToMany // this establishes a many-to-many relationship
    @JoinTable( // this creates a join table which works as a bridge between course and student
            name = "course_student",
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"), // the join column will correspond to the entity that owns the association
            inverseJoinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id") // the inverse join column will correspond to the entity that doesn't own the association
    )
    private Set<Student> students; // I'm using Set instead of List because Set prevents duplicate entries

}
