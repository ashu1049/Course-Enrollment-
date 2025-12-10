package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String studentId;
    private String name;
    private String email;
    private List<String> enrolledCourseIds = new ArrayList<>();

    public Student(String studentId, String name, String email) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getEnrolledCourseIds() { return enrolledCourseIds; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    public void enrollCourse(String courseId) {
        if (!enrolledCourseIds.contains(courseId)) {
            enrolledCourseIds.add(courseId);
        }
    }

    public void unenrollCourse(String courseId) {
        enrolledCourseIds.remove(courseId);
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s", studentId, name, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student s = (Student) o;
        return Objects.equals(studentId, s.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
