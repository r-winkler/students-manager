package ch.rewiso.students.service;

import ch.rewiso.students.dto.StudentRequest;
import ch.rewiso.students.dto.StudentResponse;
import ch.rewiso.students.entity.Student;
import ch.rewiso.students.exceptions.StudentNotFoundException;
import ch.rewiso.students.mapper.StudentMapper;
import ch.rewiso.students.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findAll() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse findById(Long id) {
        return studentMapper.toResponse(getStudentOrThrow(id));
    }

    public StudentResponse create(StudentRequest request) {
        Student student = studentMapper.toEntity(request);
        return studentMapper.toResponse(studentRepository.save(student));
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = getStudentOrThrow(id);
        student.setName(request.name());
        student.setAge(request.age());
        student.setStudentClass(request.studentClass());
        return studentMapper.toResponse(studentRepository.save(student));
    }

    public void delete(Long id) {
        Student student = getStudentOrThrow(id);
        studentRepository.delete(student);
    }

    private Student getStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }
}
