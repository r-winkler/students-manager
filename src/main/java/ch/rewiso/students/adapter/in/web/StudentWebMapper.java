package ch.rewiso.students.adapter.in.web;

import ch.rewiso.students.domain.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentWebMapper {

    public Student toDomain(StudentRequest request) {
        return new Student(null, request.name(), request.age(), request.studentClass());
    }

    public StudentResponse toResponse(Student student) {
        return new StudentResponse(student.getId(), student.getName(), student.getAge(), student.getStudentClass());
    }
}
