package ch.rewiso.students.application.port.in;

import ch.rewiso.students.domain.model.Student;

public interface FindStudentByIdUseCase {

    Student findById(Long id);
}
