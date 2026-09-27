package ch.rewiso.students.adapter.in.web;

import ch.rewiso.students.application.port.in.CreateStudentUseCase;
import ch.rewiso.students.application.port.in.DeleteStudentUseCase;
import ch.rewiso.students.application.port.in.FindAllStudentsUseCase;
import ch.rewiso.students.application.port.in.FindStudentByIdUseCase;
import ch.rewiso.students.application.port.in.UpdateStudentUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final CreateStudentUseCase createStudentUseCase;
    private final FindAllStudentsUseCase findAllStudentsUseCase;
    private final FindStudentByIdUseCase findStudentByIdUseCase;
    private final UpdateStudentUseCase updateStudentUseCase;
    private final DeleteStudentUseCase deleteStudentUseCase;
    private final StudentWebMapper studentWebMapper;

    public StudentController(CreateStudentUseCase createStudentUseCase,
                              FindAllStudentsUseCase findAllStudentsUseCase,
                              FindStudentByIdUseCase findStudentByIdUseCase,
                              UpdateStudentUseCase updateStudentUseCase,
                              DeleteStudentUseCase deleteStudentUseCase,
                              StudentWebMapper studentWebMapper) {
        this.createStudentUseCase = createStudentUseCase;
        this.findAllStudentsUseCase = findAllStudentsUseCase;
        this.findStudentByIdUseCase = findStudentByIdUseCase;
        this.updateStudentUseCase = updateStudentUseCase;
        this.deleteStudentUseCase = deleteStudentUseCase;
        this.studentWebMapper = studentWebMapper;
    }

    @GetMapping
    public List<StudentResponse> getAll() {
        return findAllStudentsUseCase.findAll().stream()
                .map(studentWebMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable Long id) {
        return studentWebMapper.toResponse(findStudentByIdUseCase.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse create(@Valid @RequestBody StudentRequest request) {
        return studentWebMapper.toResponse(createStudentUseCase.create(studentWebMapper.toDomain(request)));
    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return studentWebMapper.toResponse(updateStudentUseCase.update(id, studentWebMapper.toDomain(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deleteStudentUseCase.delete(id);
    }
}
