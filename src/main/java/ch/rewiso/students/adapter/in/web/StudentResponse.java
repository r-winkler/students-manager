package ch.rewiso.students.adapter.in.web;

public record StudentResponse(
        Long id,
        String name,
        Integer age,
        String studentClass
) {
}
