package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String courseId;
    private String courseName;
    private int capacity; // 0 means unlimited
    private List<String> enrolledStudentIds = new ArrayList<>();

    public Course(String courseId, String courseName, int capacity) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.capacity = Math.max(0, capacity);
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCapacity() { return capacity; }
    public List<String> getEnrolledStudentIds() { return enrolledStudentIds; }

    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setCapacity(int capacity) { this.capacity = Math.max(0, capacity); }

    public boolean hasSpace() {
        return capacity == 0 || enrolledStudentIds.size() < capacity;
    }

    public void addStudent(String studentId) {
        if (!enrolledStudentIds.contains(studentId)) {
            enrolledStudentIds.add(studentId);
        }
    }

    public void removeStudent(String studentId) {
        enrolledStudentIds.remove(studentId);
    }

    @Override
    public String toString() {
        String cap = capacity == 0 ? "unlimited" : String.valueOf(capacity);
        return String.format("%s | %s | capacity: %s | enrolled: %d",
                courseId, courseName, cap, enrolledStudentIds.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course c = (Course) o;
        return Objects.equals(courseId, c.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}

