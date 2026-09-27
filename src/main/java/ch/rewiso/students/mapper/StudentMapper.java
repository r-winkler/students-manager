package ch.rewiso.students.mapper;

import ch.rewiso.students.dto.StudentRequest;
import ch.rewiso.students.dto.StudentResponse;
import ch.rewiso.students.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student toEntity(StudentRequest request) {
        return new Student(request.name(), request.age(), request.studentClass());
    }

    public StudentResponse toResponse(Student student) {
        return new StudentResponse(student.getId(), student.getName(), student.getAge(), student.getStudentClass());
    }
}
