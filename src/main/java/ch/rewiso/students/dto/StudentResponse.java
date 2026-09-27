package ch.rewiso.students.dto;

public record StudentResponse(
        Long id,
        String name,
        Integer age,
        String studentClass
) {
}
