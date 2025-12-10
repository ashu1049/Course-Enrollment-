package domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Enrollment implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String enrollmentId;
    private final String studentId;
    private final String courseId;
    private final LocalDateTime timestamp;

    public Enrollment(String enrollmentId, String studentId, String courseId) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.timestamp = LocalDateTime.now();
    }

    public String getEnrollmentId() { return enrollmentId; }
    public String getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("%s | student:%s | course:%s | %s",
                enrollmentId, studentId, courseId, timestamp.toString());
    }
}

