package ch.rewiso.students;

import ch.rewiso.students.dto.StudentRequest;
import ch.rewiso.students.dto.StudentResponse;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class StudentControllerIntegrationTest {

    @Autowired
    private Environment environment;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String baseUrl() {
        return "http://localhost:" + environment.getProperty("local.server.port") + "/api/students";
    }

    private HttpResponse<String> send(String method, String path, Object body) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(baseUrl() + path));
        HttpRequest.BodyPublisher publisher = body == null
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body));
        builder.method(method, publisher).header("Content-Type", "application/json");
        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private StudentResponse createStudent(String name, int age, String studentClass) throws Exception {
        HttpResponse<String> response = send("POST", "", new StudentRequest(name, age, studentClass));
        return objectMapper.readValue(response.body(), StudentResponse.class);
    }

    @Test
    @DisplayName("Should create a student")
    void shouldCreateStudent() throws Exception {
        HttpResponse<String> response = send("POST", "", new StudentRequest("Alice", 20, "10A"));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        StudentResponse body = objectMapper.readValue(response.body(), StudentResponse.class);
        assertThat(body.id()).isNotNull();
        assertThat(body.name()).isEqualTo("Alice");
        assertThat(body.age()).isEqualTo(20);
        assertThat(body.studentClass()).isEqualTo("10A");
    }

    @Test
    @DisplayName("Should return 400 ProblemDetail when creating an invalid student")
    void shouldReturn400WhenCreatingInvalidStudent() throws Exception {
        HttpResponse<String> response = send("POST", "", new StudentRequest("", null, ""));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Should return all students")
    void shouldReturnAllStudents() throws Exception {
        createStudent("Bob", 21, "11B");

        HttpResponse<String> response = send("GET", "", null);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        JsonNode students = objectMapper.readTree(response.body());
        assertThat(students.isArray()).isTrue();
        assertThat(students.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should return a student by id")
    void shouldReturnStudentById() throws Exception {
        StudentResponse created = createStudent("Carol", 19, "9C");

        HttpResponse<String> response = send("GET", "/" + created.id(), null);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        StudentResponse body = objectMapper.readValue(response.body(), StudentResponse.class);
        assertThat(body.name()).isEqualTo("Carol");
    }

    @Test
    @DisplayName("Should return 404 ProblemDetail when student ID does not exist")
    void shouldReturn404WhenNotFound() throws Exception {
        HttpResponse<String> response = send("GET", "/999999", null);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Should update an existing student")
    void shouldUpdateStudent() throws Exception {
        StudentResponse created = createStudent("Dave", 22, "12A");

        HttpResponse<String> response = send("PUT", "/" + created.id(), new StudentRequest("Dave Updated", 23, "12B"));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        StudentResponse body = objectMapper.readValue(response.body(), StudentResponse.class);
        assertThat(body.name()).isEqualTo("Dave Updated");
        assertThat(body.age()).isEqualTo(23);
        assertThat(body.studentClass()).isEqualTo("12B");
    }

    @Test
    @DisplayName("Should return 404 ProblemDetail when updating a non-existent student")
    void shouldReturn404WhenUpdatingNonExistentStudent() throws Exception {
        HttpResponse<String> response = send("PUT", "/999999", new StudentRequest("Ghost", 30, "0G"));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Should delete an existing student")
    void shouldDeleteStudent() throws Exception {
        StudentResponse created = createStudent("Eve", 24, "12C");

        HttpResponse<String> deleteResponse = send("DELETE", "/" + created.id(), null);
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        HttpResponse<String> getResponse = send("GET", "/" + created.id(), null);
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Should return 404 ProblemDetail when deleting a non-existent student")
    void shouldReturn404WhenDeletingNonExistentStudent() throws Exception {
        HttpResponse<String> response = send("DELETE", "/999999", null);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
