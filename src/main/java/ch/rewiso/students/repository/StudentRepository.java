package ch.rewiso.students.repository;

import ch.rewiso.students.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
