import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PopulateDatabase {
    public static void main(String[] args) {
        ArrayList<String> departments = getFileData("src/department.txt");
        ArrayList<String> teachers = getFileData("src/teacher.txt");
        ArrayList<String> assignmentTypes = getFileData("src/assignment_types.txt");
        ArrayList<String> courses = getFileData("src/courses.txt");

        Random random = new Random();
        int numStudents = 5000;

        // Populate Departments
        for (int i = 0; i < departments.size(); i++) {
            System.out.println("INSERT INTO Departments (department_id, department_name) VALUES (" + (i + 1) + ", '" + departments.get(i) + "');");
        }

        // Populate Teachers
        for (int i = 0; i < teachers.size(); i++) {
            String[] teacherDetails = teachers.get(i).split(" ", 3);
            int departmentId = Integer.parseInt(teacherDetails[0]);
            String firstName = teacherDetails[1];
            String lastName = teacherDetails[2];
            System.out.println("INSERT INTO Teachers (teacher_id, first_name, last_name, department_id) VALUES (" + (i + 1) + ", '" + firstName + "', '" + lastName + "', " + departmentId + ");");
        }

        // Populate Students (Student, 1), (Student, 2)...
        for (int i = 1; i <= numStudents; i++) {
            System.out.println("INSERT INTO Students (student_id, first_name, last_name) VALUES (" + i + ", 'Student', '" + i + "');");
        }

        // Populate Assignment Types
        for (int i = 0; i < assignmentTypes.size(); i++) {
            System.out.println("INSERT INTO Assignment_Types (assignment_type_id, assignment_type) VALUES (" + (i + 1) + ", '" + assignmentTypes.get(i) + "');");
        }

        // Populate Courses
        for (int i = 0; i < courses.size(); i++) {
            String[] courseDetails = courses.get(i).split(" ", 2);
            int departmentId = Integer.parseInt(courseDetails[0]);
            String courseName = courseDetails[1];
            String courseType = "Regents";
            if (courseName.contains("AP")) {
                courseType = "AP";
            } else if (courseName.contains("Elective")) {
                courseType = "Elective";
            }
            System.out.println("INSERT INTO Courses (course_id, course_type, course_name, department_id) VALUES (" + (i + 1) + ", '" + courseType + "', '" + courseName + "', " + departmentId + ");");
        }

        // Generate Course Offerings
        ArrayList<String> courseOfferings = new ArrayList<>();
        int offeringId = 1;
        String[] floors = {"B", "1", "2", "3", "4", "5", "6", "7", "8"};
        String[] wings = {"N", "S", "E", "W"};

        for (int courseId = 1; courseId <= courses.size(); courseId++) {
            int numOfferings = random.nextInt(5) + 1; // 1-5 offerings
            for (int i = 0; i < numOfferings; i++) {
                int teacherId = random.nextInt(teachers.size()) + 1;
                int periodNumber = random.nextInt(10) + 1;

                // Room number in 2W02 format
                String floor = floors[random.nextInt(floors.length)];
                String wing = wings[random.nextInt(wings.length)];
                int roomNum = random.nextInt(20) + 1;
                String roomNumberFormatted = floor + wing + String.format("%02d", roomNum);

                courseOfferings.add(offeringId + "," + courseId + "," + teacherId + "," + periodNumber + "," + roomNumberFormatted);
                System.out.println("INSERT INTO Course_Offering (offering_id, course_id, room_number, period_number, teacher_id) VALUES ("
                        + offeringId + ", " + courseId + ", '" + roomNumberFormatted + "', " + periodNumber + ", " + teacherId + ");");
                offeringId++;
            }
        }

        // Generate Rosters
        int rosterId = 1;
        Map<Integer, ArrayList<Integer>> offeringToStudents = new HashMap<>(); // offering_id -> student_ids
        for (int studentId = 1; studentId <= numStudents; studentId++) {
            ArrayList<Integer> assignedPeriods = new ArrayList<>();
            while (assignedPeriods.size() < 10) {
                int offeringIndex = random.nextInt(courseOfferings.size());
                String[] offering = courseOfferings.get(offeringIndex).split(",");
                int offeringIdParsed = Integer.parseInt(offering[0]);
                int courseId = Integer.parseInt(offering[1]);
                int teacherId = Integer.parseInt(offering[2]);
                int periodNumber = Integer.parseInt(offering[3]);
                String roomNumber = offering[4];

                if (!assignedPeriods.contains(periodNumber)) {
                    assignedPeriods.add(periodNumber);
                    System.out.println("INSERT INTO Rosters (roster_id, student_id, teacher_id, course_id, period_number, room_number) VALUES ("
                            + rosterId + ", " + studentId + ", " + teacherId + ", " + courseId + ", " + periodNumber + ", '" + roomNumber + "');");

                    offeringToStudents.putIfAbsent(offeringIdParsed, new ArrayList<>());
                    offeringToStudents.get(offeringIdParsed).add(studentId);
                    rosterId++;
                }
            }
        }

        // Generate Assignments and Grades
        int assignmentId = 1;
        for (String offeringStr : courseOfferings) {
            String[] offering = offeringStr.split(",");
            int offeringIdParsed = Integer.parseInt(offering[0]);
            int courseId = Integer.parseInt(offering[1]);
            int teacherId = Integer.parseInt(offering[2]);

            // Get course type (for assignment_type_id)
            String courseName = courses.get(courseId - 1).split(" ", 2)[1];
            String courseType = "Regents";
            if (courseName.contains("AP")) {
                courseType = "AP";
            } else if (courseName.contains("Elective")) {
                courseType = "Elective";
            }
            int assignmentTypeId = courseType.equals("AP") ? 1 : courseType.equals("Regents") ? 2 : 3;

            // 15 assignments per offering
            for (int a = 1; a <= 15; a++) {
                String assignmentName = (a <= 12 ? "Minor Assessment " : "Major Assessment ") + a;
                System.out.println("INSERT INTO Assignments (assignment_id, assignment_name, assignment_type_id, teacher_id, student_id, course_id) VALUES ("
                        + assignmentId + ", '" + assignmentName + "', " + assignmentTypeId + ", " + teacherId + ", NULL, " + courseId + ");");

                ArrayList<Integer> students = offeringToStudents.get(offeringIdParsed);
                if (students != null) {
                    for (int studentId : students) {
                        int grade = random.nextInt(26) + 75; // 75-100
                        System.out.println("INSERT INTO Grades (student_id, assignment_id, grade) VALUES (" + studentId + ", " + assignmentId + ", " + grade + ");");
                    }
                }
                assignmentId++;
            }
        }
    }

    public static ArrayList<String> getFileData(String fileName) {
        ArrayList<String> fileData = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    fileData.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }
        return fileData;
    }
}
