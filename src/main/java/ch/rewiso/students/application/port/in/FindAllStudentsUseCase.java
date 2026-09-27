package ch.rewiso.students.application.port.in;

import ch.rewiso.students.domain.model.Student;

import java.util.List;

public interface FindAllStudentsUseCase {

    List<Student> findAll();
}
