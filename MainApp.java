package app;

import domain.Course;
import domain.Enrollment;
import domain.Student;
import persistence.DataStore;
import service.RegistrationManager;

import java.util.List;
import java.util.Scanner;

public class MainApp {

    private static final String DATA_FILE = "data/registration_data.ser";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DataStore ds = new DataStore(DATA_FILE);

        RegistrationManager manager = ds.load();
        if (manager == null) {
            manager = new RegistrationManager();
            System.out.println("Starting with empty data.");
        } else {
            System.out.println("Loaded saved data.");
        }

        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": createStudent(sc, manager); break;
                case "2": createCourse(sc, manager); break;
                case "3": enrollStudent(sc, manager); break;
                case "4": listStudents(manager); break;
                case "5": listCourses(manager); break;
                case "6": listEnrollments(manager); break;
                case "7": searchStudent(sc, manager); break;
                case "8": unenrollStudent(sc, manager); break;
                case "9": deleteStudent(sc, manager); break;
                case "10": deleteCourse(sc, manager); break;
                case "s": saveData(manager, ds); break;
                case "l": manager = loadData(ds, manager); break;
                case "q": running = false; break;
                default: System.out.println("Unknown option."); break;
            }
        }

        // auto-save on exit
        saveData(manager, ds);
        System.out.println("Goodbye!");
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n--- Student Registration ---");
        System.out.println("1) Add student");
        System.out.println("2) Add course");
        System.out.println("3) Enroll student in course");
        System.out.println("4) List students");
        System.out.println("5) List courses");
        System.out.println("6) List enrollments");
        System.out.println("7) Search student by name");
        System.out.println("8) Unenroll student from course");
        System.out.println("9) Delete student");
        System.out.println("10) Delete course");
        System.out.println("s) Save now");
        System.out.println("l) Load data from disk (restart state)");
        System.out.println("q) Quit");
        System.out.print("Choose: ");
    }

    private static void createStudent(Scanner sc, RegistrationManager manager) {
        System.out.print("Student name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("Name cannot be empty."); return; }
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        if (!isValidEmail(email)) { System.out.println("Invalid email format."); return; }
        Student s = manager.addStudent(name, email);
        System.out.println("Added: " + s);
    }

    private static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.indexOf("@") > 0 && email.indexOf("@") < email.length() - 1;
    }

    private static void createCourse(Scanner sc, RegistrationManager manager) {
        System.out.print("Course name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) { System.out.println("Name cannot be empty."); return; }
        System.out.print("Capacity (0 = unlimited): ");
        String capStr = sc.nextLine().trim();
        int cap = 0;
        try { cap = Integer.parseInt(capStr); }
        catch (NumberFormatException ex) { System.out.println("Invalid number; using unlimited."); cap = 0; }
        Course c = manager.addCourse(name, Math.max(0, cap));
        System.out.println("Added: " + c);
    }

    private static void enrollStudent(Scanner sc, RegistrationManager manager) {
        System.out.print("Student ID: ");
        String sid = sc.nextLine().trim();
        System.out.print("Course ID: ");
        String cid = sc.nextLine().trim();
        String result = manager.enroll(sid, cid);
        if (result.startsWith("OK:")) {
            System.out.println("Enrollment successful. id=" + result.substring(3));
        } else {
            System.out.println("Enrollment failed: " + result);
        }
    }

    private static void listStudents(RegistrationManager manager) {
        List<Student> all = manager.listStudents();
        System.out.println("\nStudents:");
        if (all.isEmpty()) System.out.println(" (none)");
        for (Student s : all) System.out.println(" " + s);
    }

    private static void listCourses(RegistrationManager manager) {
        List<Course> all = manager.listCourses();
        System.out.println("\nCourses:");
        if (all.isEmpty()) System.out.println(" (none)");
        for (Course c : all) System.out.println(" " + c);
    }

    private static void listEnrollments(RegistrationManager manager) {
        List<Enrollment> all = manager.listEnrollments();
        System.out.println("\nEnrollments:");
        if (all.isEmpty()) System.out.println(" (none)");
        for (Enrollment e : all) System.out.println(" " + e);
    }

    private static void searchStudent(Scanner sc, RegistrationManager manager) {
        System.out.print("Search name query: ");
        String q = sc.nextLine().trim();
        List<Student> found = manager.searchStudentsByName(q);
        if (found.isEmpty()) System.out.println("No students match.");
        else {
            System.out.println("Matches:");
            found.forEach(s -> System.out.println(" " + s));
        }
    }

    private static void unenrollStudent(Scanner sc, RegistrationManager manager) {
        System.out.print("Student ID: ");
        String sid = sc.nextLine().trim();
        System.out.print("Course ID: ");
        String cid = sc.nextLine().trim();
        boolean ok = manager.unenroll(sid, cid);
        System.out.println(ok ? "Unenrolled." : "No such enrollment.");
    }

    private static void deleteStudent(Scanner sc, RegistrationManager manager) {
        System.out.print("Student ID to delete: ");
        String sid = sc.nextLine().trim();
        boolean ok = manager.deleteStudent(sid);
        System.out.println(ok ? "Deleted student and related enrollments." : "Student not found.");
    }

    private static void deleteCourse(Scanner sc, RegistrationManager manager) {
        System.out.print("Course ID to delete: ");
        String cid = sc.nextLine().trim();
        boolean ok = manager.deleteCourse(cid);
        System.out.println(ok ? "Deleted course and related enrollments." : "Course not found.");
    }

    private static void saveData(RegistrationManager manager, DataStore ds) {
        boolean ok = ds.save(manager);
        System.out.println(ok ? "Saved to disk." : "Save failed.");
    }

    private static RegistrationManager loadData(DataStore ds, RegistrationManager current) {
        RegistrationManager loaded = ds.load();
        if (loaded == null) {
            System.out.println("Load failed or no saved file. Keeping current in-memory data.");
            return current;
        } else {
            System.out.println("Loaded data from disk.");
            return loaded;
        }
    }
}


// Compile:
// javac -d out src/domain/*.java src/service/*.java src/persistence/*.java src/app/*.java
//
// Run:
// java -cp out app.MainApp