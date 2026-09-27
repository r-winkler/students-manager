package ch.rewiso.students.application.port.in;

import ch.rewiso.students.domain.model.Student;

public interface CreateStudentUseCase {

    Student create(Student student);
}
