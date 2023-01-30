package com.ltp.gradesubmission.service;

import java.util.List;

import com.ltp.gradesubmission.entity.Course;
import com.ltp.gradesubmission.entity.Grade;
import com.ltp.gradesubmission.entity.Student;
import com.ltp.gradesubmission.exception.GradeNotFoundException;
import com.ltp.gradesubmission.exception.StudentNotEnrolledException;
import com.ltp.gradesubmission.repository.CourseRepository;
import com.ltp.gradesubmission.repository.GradeRepository;
import com.ltp.gradesubmission.repository.StudentRepository;
import java.util.Optional;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@AllArgsConstructor // this automatically performs dependency injection of the beans we need to use, without having to use @autowired every time
@Service
public class GradeServiceImpl implements GradeService {
    
    GradeRepository gradeRepository; // this is autowired and can perform dependency injection thanks to the @AllArgsConstructor annotation
    StudentRepository studentRepository; // this is autowired and can perform dependency injection thanks to the @AllArgsConstructor annotation
    CourseRepository courseRepository; // this is autowired and can perform dependency injection thanks to the @AllArgsConstructor annotation

    
    @Override
    public Grade getGrade(Long studentId, Long courseId) {
        Optional<Grade> grade = gradeRepository.findByStudentIdAndCourseId(studentId, courseId); // it finds the grade by student id and course id
        return unwrapGrade(grade, studentId, courseId); // it recalls the unwrapGrade function that handles it in order to prevent a 500 code if there isn't a corresponding entity
    }

    @Override
    public Grade saveGrade(Grade grade, Long studentId, Long courseId) {
        Student student = StudentServiceImpl.unwrapStudent(studentRepository.findById(studentId), studentId); // it recalls the unwrapStudent function that handles it in order to prevent a 500 code if there isn't a corresponding entity
        Course course = CourseServiceImpl.unwrapCourse(courseRepository.findById(courseId), courseId); // it recalls the unwrapCourse function that handles it in order to prevent a 500 code if there isn't a corresponding entity
        if (!student.getCourses().contains(course)) throw new StudentNotEnrolledException(studentId, courseId); // it checks if the student is not enrolled in this course: if it's not, it throws a custom 404
        grade.setStudent(student); // it sets the student associated to the grade
        grade.setCourse(course); // it sets the course associated to the grade
        return gradeRepository.save(grade); // it saves the grade
    }

    @Override
    public Grade updateGrade(String score, Long studentId, Long courseId) {
        Optional<Grade> grade = gradeRepository.findByStudentIdAndCourseId(studentId, courseId); // it finds the grade by student id and course id
        Grade unwrappedGrade = unwrapGrade(grade, studentId, courseId); // it recalls the unwrapGrade function that handles it in order to prevent a 500 code if there isn't a corresponding entity
        unwrappedGrade.setScore(score); // it sets the new score on the found grade
        return gradeRepository.save(unwrappedGrade); // it saves the grade with the new score associated to
    }

    @Override
    public void deleteGrade(Long studentId, Long courseId) {
        gradeRepository.deleteByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public List<Grade> getStudentGrades(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    @Override
    public List<Grade> getCourseGrades(Long courseId) {
        return gradeRepository.findByCourseId(courseId);
    }

    @Override
    public List<Grade> getAllGrades() {
        return (List<Grade>) gradeRepository.findAll();
    }
    
    static Grade unwrapGrade(Optional<Grade> entity, Long studentId, Long courseId) {
        if (entity.isPresent()) return entity.get(); // it checks if there's a corresponding entity; if there is, it gets it
        else throw new GradeNotFoundException(studentId, courseId); // if there isn't, it throws a custom 404
    }


}
