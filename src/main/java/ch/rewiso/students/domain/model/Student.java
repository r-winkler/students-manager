package ch.rewiso.students.domain.model;

public class Student {

    private final Long id;
    private String name;
    private Integer age;
    private String studentClass;

    public Student(Long id, String name, Integer age, String studentClass) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.studentClass = studentClass;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }
}
