package ch.rewiso.students.application.service;

import ch.rewiso.students.application.exception.StudentNotFoundException;
import ch.rewiso.students.application.port.in.CreateStudentUseCase;
import ch.rewiso.students.application.port.in.DeleteStudentUseCase;
import ch.rewiso.students.application.port.in.FindAllStudentsUseCase;
import ch.rewiso.students.application.port.in.FindStudentByIdUseCase;
import ch.rewiso.students.application.port.in.UpdateStudentUseCase;
import ch.rewiso.students.application.port.out.StudentRepositoryPort;
import ch.rewiso.students.domain.model.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService implements CreateStudentUseCase, FindAllStudentsUseCase, FindStudentByIdUseCase,
        UpdateStudentUseCase, DeleteStudentUseCase {

    private final StudentRepositoryPort studentRepositoryPort;

    public StudentService(StudentRepositoryPort studentRepositoryPort) {
        this.studentRepositoryPort = studentRepositoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return getStudentOrThrow(id);
    }

    @Override
    public Student create(Student student) {
        return studentRepositoryPort.save(student);
    }

    @Override
    public Student update(Long id, Student student) {
        Student existing = getStudentOrThrow(id);
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        existing.setStudentClass(student.getStudentClass());
        return studentRepositoryPort.save(existing);
    }

    @Override
    public void delete(Long id) {
        Student existing = getStudentOrThrow(id);
        studentRepositoryPort.delete(existing);
    }

    private Student getStudentOrThrow(Long id) {
        return studentRepositoryPort.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }
}
