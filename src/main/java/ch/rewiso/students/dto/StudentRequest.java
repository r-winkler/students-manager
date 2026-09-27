package ch.rewiso.students.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentRequest(
        @NotBlank(message = "Name must not be blank") String name,
        @NotNull(message = "Age must not be null") @Min(value = 0, message = "Age must not be negative") Integer age,
        @NotBlank(message = "Class must not be blank") String studentClass
) {
}
