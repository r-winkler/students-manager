package ch.rewiso.students.adapter.out.persistence;

import ch.rewiso.students.application.port.out.StudentRepositoryPort;
import ch.rewiso.students.domain.model.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class StudentRepositoryAdapter implements StudentRepositoryPort {

    private final StudentJpaRepository studentJpaRepository;
    private final StudentPersistenceMapper studentPersistenceMapper;

    public StudentRepositoryAdapter(StudentJpaRepository studentJpaRepository, StudentPersistenceMapper studentPersistenceMapper) {
        this.studentJpaRepository = studentJpaRepository;
        this.studentPersistenceMapper = studentPersistenceMapper;
    }

    @Override
    public List<Student> findAll() {
        return studentJpaRepository.findAll().stream()
                .map(studentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentJpaRepository.findById(id).map(studentPersistenceMapper::toDomain);
    }

    @Override
    public Student save(Student student) {
        StudentJpaEntity saved = studentJpaRepository.save(studentPersistenceMapper.toEntity(student));
        return studentPersistenceMapper.toDomain(saved);
    }

    @Override
    public void delete(Student student) {
        studentJpaRepository.deleteById(student.getId());
    }
}
