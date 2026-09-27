package ch.rewiso.students.application.port.out;

import ch.rewiso.students.domain.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepositoryPort {

    List<Student> findAll();

    Optional<Student> findById(Long id);

    Student save(Student student);

    void delete(Student student);
}
