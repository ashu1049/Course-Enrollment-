package service;

import domain.Course;
import domain.Enrollment;
import domain.Student;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RegistrationManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Student> students = new LinkedHashMap<>();
    private final Map<String, Course> courses = new LinkedHashMap<>();
    private final List<Enrollment> enrollments = new ArrayList<>();

    // Simple id generators
    private transient AtomicInteger studentSeq = new AtomicInteger(1000);
    private transient AtomicInteger courseSeq = new AtomicInteger(2000);
    private transient AtomicInteger enrollSeq = new AtomicInteger(3000);

    public RegistrationManager() {
        ensureTransientInits();
    }

    // Called after deserialization (if needed)
    private void ensureTransientInits() {
        if (studentSeq == null) studentSeq = new AtomicInteger(1000 + students.size());
        if (courseSeq == null) courseSeq = new AtomicInteger(2000 + courses.size());
        if (enrollSeq == null) enrollSeq = new AtomicInteger(3000 + enrollments.size());
    }

    public String generateStudentId() { ensureTransientInits(); return "S" + studentSeq.getAndIncrement(); }
    public String generateCourseId() { ensureTransientInits(); return "C" + courseSeq.getAndIncrement(); }
    public String generateEnrollmentId() { ensureTransientInits(); return "E" + enrollSeq.getAndIncrement(); }

    // Student operations
    public Student addStudent(String name, String email) {
        String id = generateStudentId();
        Student s = new Student(id, name, email);
        students.put(id, s);
        return s;
    }

    public List<Student> listStudents() { return new ArrayList<>(students.values()); }

    public Student findStudentById(String id) { return students.get(id); }

    public List<Student> searchStudentsByName(String q) {
        String lower = q.toLowerCase();
        return students.values().stream()
                .filter(s -> s.getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public boolean deleteStudent(String studentId) {
        Student removed = students.remove(studentId);
        if (removed == null) return false;
        // remove enrollments and update courses
        List<Enrollment> toRemove = enrollments.stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .collect(Collectors.toList());
        for (Enrollment e : toRemove) {
            unenroll(e.getStudentId(), e.getCourseId());
        }
        return true;
    }

    // Course operations
    public Course addCourse(String name, int capacity) {
        String id = generateCourseId();
        Course c = new Course(id, name, capacity);
        courses.put(id, c);
        return c;
    }

    public List<Course> listCourses() { return new ArrayList<>(courses.values()); }

    public Course findCourseById(String id) { return courses.get(id); }

    public boolean deleteCourse(String courseId) {
        Course removed = courses.remove(courseId);
        if (removed == null) return false;
        // remove enrollments and update students
        List<Enrollment> toRemove = enrollments.stream()
                .filter(e -> e.getCourseId().equals(courseId))
                .collect(Collectors.toList());
        for (Enrollment e : toRemove) {
            unenroll(e.getStudentId(), e.getCourseId());
        }
        return true;
    }

    // Enrollment operations
    public String enroll(String studentId, String courseId) {
        Student s = findStudentById(studentId);
        Course c = findCourseById(courseId);
        if (s == null) return "Student not found.";
        if (c == null) return "Course not found.";
        if (!c.hasSpace()) return "Course is full.";
        // prevent duplicate
        boolean already = enrollments.stream()
                .anyMatch(en -> en.getStudentId().equals(studentId) && en.getCourseId().equals(courseId));
        if (already) return "Student already enrolled in this course.";
        // create enrollment
        String eId = generateEnrollmentId();
        Enrollment en = new Enrollment(eId, studentId, courseId);
        enrollments.add(en);
        s.enrollCourse(courseId);
        c.addStudent(studentId);
        return "OK:" + eId;
    }

    public boolean unenroll(String studentId, String courseId) {
        Optional<Enrollment> found = enrollments.stream()
                .filter(e -> e.getStudentId().equals(studentId) && e.getCourseId().equals(courseId))
                .findFirst();
        if (!found.isPresent()) return false;
        Enrollment e = found.get();
        enrollments.remove(e);
        Student s = findStudentById(studentId);
        Course c = findCourseById(courseId);
        if (s != null) s.unenrollCourse(courseId);
        if (c != null) c.removeStudent(studentId);
        return true;
    }

    public List<Enrollment> listEnrollments() { return new ArrayList<>(enrollments); }

    public List<Course> getCoursesForStudent(String studentId) {
        return enrollments.stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .map(e -> findCourseById(e.getCourseId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsForCourse(String courseId) {
        return enrollments.stream()
                .filter(e -> e.getCourseId().equals(courseId))
                .map(e -> findStudentById(e.getStudentId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // For saving/loading with serialization we ensure transient fields are restored
    private Object readResolve() {
        ensureTransientInits();
        return this;
    }
}
