import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class PopulateDatabase {
    public static void main(String[] args) {
        ArrayList<String> departments = getFileData("src/department.txt");
        ArrayList<String> teachers = getFileData("src/teacher.txt");
        ArrayList<String> assignmentTypes = getFileData("src/assignment_types.txt");

        // Populate Departments
        for (int i = 0; i < departments.size(); i++) {
            System.out.println("INSERT INTO Departments (department_id, department_name) VALUES (" + (i + 1) + ", '" + departments.get(i) + "');");
        }

        // Populate Teachers
        for (int i = 0; i < teachers.size(); i++) {
            String[] teacherDetails = teachers.get(i).split(" ");
            System.out.println("INSERT INTO Teachers (teacher_id, first_name, last_name, department_id) VALUES (" + (i + 1) + ", '" + teacherDetails[1] + "', '" + teacherDetails[2] + "', " + teacherDetails[0] + ");");
        }

        // Populate Students
        for (int i = 1; i <= 5000; i++) {
            System.out.println("INSERT INTO Students (student_id, first_name, last_name) VALUES (" + i + ", 'Student', '" + i + "');");
        }

        // Populate Assignment Types
        for (int i = 0; i < assignmentTypes.size(); i++) {
            System.out.println("INSERT INTO Assignment_Types (assignment_type_id, assignment_type) VALUES (" + (i + 1) + ", '" + assignmentTypes.get(i) + "');");
        }

        // Populate Rooms
        String[] floors = {"B", "1", "2", "3", "4", "5", "6", "7", "8"};
        String[] wings = {"N", "S", "E", "W"};
        for (String floor : floors) {
            for (String wing : wings) {
                for (int room = 1; room <= 20; room++) {
                    System.out.println("INSERT INTO Rooms (floor, wing, room_number) VALUES ('" + floor + "', '" + wing + "', " + room + ");");
                }
            }
        }

        // Populate Course Offerings
        Random random = new Random();
        for (int courseId = 1; courseId <= departments.size(); courseId++) {
            int offerings = random.nextInt(5) + 1; // Randomly generate 1-5 offerings
            for (int offering = 1; offering <= offerings; offering++) {
                int teacherId = random.nextInt(teachers.size()) + 1;
                int roomNumber = random.nextInt(20) + 1;
                int periodNumber = random.nextInt(10) + 1;
                System.out.println("INSERT INTO Course_Offering (offering_id, course_id, room_number, period_number, teacher_id) VALUES (" + offering + ", " + courseId + ", " + roomNumber + ", " + periodNumber + ", " + teacherId + ");");
            }
        }

        // Populate Rosters
        for (int studentId = 1; studentId <= 5000; studentId++) {
            for (int period = 1; period <= 10; period++) {
                int courseId = random.nextInt(departments.size()) + 1;
                System.out.println("INSERT INTO Rosters (roster_id, student_id, course_id, period_number) VALUES (" + studentId + period + ", " + studentId + ", " + courseId + ", " + period + ");");
            }
        }

        // Populate Assignments
        for (int courseId = 1; courseId <= departments.size(); courseId++) {
            for (int assignmentId = 1; assignmentId <= 15; assignmentId++) {
                String assignmentName = "Assignment" + assignmentId;
                int assignmentTypeId = random.nextInt(assignmentTypes.size()) + 1;
                int teacherId = random.nextInt(teachers.size()) + 1;
                int studentId = random.nextInt(5000) + 1;
                System.out.println("INSERT INTO Assignments (assignment_id, assignment_name, assignment_type_id, teacher_id, student_id, course_id) VALUES (" + assignmentId + ", '" + assignmentName + "', " + assignmentTypeId + ", " + teacherId + ", " + studentId + ", " + courseId + ");");
            }
        }

        // Populate Grades
        for (int studentId = 1; studentId <= 5000; studentId++) {
            for (int assignmentId = 1; assignmentId <= 15; assignmentId++) {
                int grade = random.nextInt(26) + 75; // Random grade between 75-100
                System.out.println("INSERT INTO Grades (student_id, assignment_id, grade) VALUES (" + studentId + ", " + assignmentId + ", " + grade + ");");
            }
        }
    }

    public static ArrayList<String> getFileData(String fileName) {
        ArrayList<String> fileData = new ArrayList<>();
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    fileData.add(line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }
        return fileData;
    }
}

