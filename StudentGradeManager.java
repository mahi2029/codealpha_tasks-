import java.util.ArrayList;
import java.util.Scanner;

// Class to represent a Student
class Student {
    private String name;
    private ArrayList<Double> grades;

    public Student(String name) {
        this.name = name;
        grades = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addGrade(double grade) {
        grades.add(grade);
    }

    public ArrayList<Double> getGrades() {
        return grades;
    }

    public double getAverage() {
        if (grades.isEmpty()) return 0;
        double sum = 0;
        for (double grade : grades) {
            sum += grade;
        }
        return sum / grades.size();
    }

    public double getHighest() {
        if (grades.isEmpty()) return 0;
        double max = grades.get(0);
        for (double grade : grades) {
            if (grade > max) {
                max = grade;
            }
        }
        return max;
    }

    public double getLowest() {
        if (grades.isEmpty()) return 0;
        double min = grades.get(0);
        for (double grade : grades) {
            if (grade < min) {
                min = grade;
            }
        }
        return min;
    }
}

// Main Application Class
public class StudentGradeManager {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n=== Student Grade Manager ===");
            System.out.println("1. Add Student");
            System.out.println("2. Add Grade to Student");
            System.out.println("3. Display Summary Report");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = getIntInput();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addGradeToStudent();
                case 3 -> displaySummaryReport();
                case 4 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 4);
    }

    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        students.add(new Student(name));
        System.out.println("Student added successfully.");
    }

    private static void addGradeToStudent() {
        if (students.isEmpty()) {
            System.out.println("No students found. Add a student first.");
            return;
        }

        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        Student student = findStudentByName(name);

        if (student != null) {
            System.out.print("Enter grade (0 - 100): ");
            double grade = getDoubleInput();
            if (grade >= 0 && grade <= 100) {
                student.addGrade(grade);
                System.out.println("Grade added successfully.");
            } else {
                System.out.println("Invalid grade. Must be between 0 and 100.");
            }
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void displaySummaryReport() {
        if (students.isEmpty()) {
            System.out.println("No students to display.");
            return;
        }

        System.out.println("\n--- Summary Report ---");
        for (Student student : students) {
            System.out.println("Name: " + student.getName());
            System.out.println("Grades: " + student.getGrades());
            if (!student.getGrades().isEmpty()) {
                System.out.printf("Average: %.2f\n", student.getAverage());
                System.out.printf("Highest: %.2f\n", student.getHighest());
                System.out.printf("Lowest: %.2f\n", student.getLowest());
            } else {
                System.out.println("No grades available.");
            }
            System.out.println();
        }
    }

    private static Student findStudentByName(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name.trim())) {
                return student;
            }
        }
        return null;
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next(); // discard invalid input
        }
        int num = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return num;
    }

    private static double getDoubleInput() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Please enter a valid decimal number: ");
            scanner.next(); // discard invalid input
        }
        double num = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        return num;
    }
}


