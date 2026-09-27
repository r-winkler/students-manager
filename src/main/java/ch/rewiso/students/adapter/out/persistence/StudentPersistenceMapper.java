package ch.rewiso.students.adapter.out.persistence;

import ch.rewiso.students.domain.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentPersistenceMapper {

    public StudentJpaEntity toEntity(Student student) {
        return new StudentJpaEntity(student.getId(), student.getName(), student.getAge(), student.getStudentClass());
    }

    public Student toDomain(StudentJpaEntity entity) {
        return new Student(entity.getId(), entity.getName(), entity.getAge(), entity.getStudentClass());
    }
}
