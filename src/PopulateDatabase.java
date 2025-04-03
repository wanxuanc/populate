import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
public class PopulateDatabase {
    public static void main(String[] args) {
        ArrayList<String> fileData1 = getFileData("src/department.txt");
        ArrayList<String> fileData2 = getFileData("src/teacher.txt");
        ArrayList<String> fileData3 = getFileData("src/assignment_types.txt");
        for (int i = 0; i <fileData1.size(); i++){
            System.out.println("INSERT INTO departments ( department_id, department_name  ) VALUES ( " + (i+1) + ", '" + fileData1.get(i) + "' );");
        }

        // INSERT INTO Teachers ( TeacherID, Name ) VALUES ( 200 , 'Teacher200 ');
        for (int i = 1; i <= 200; i++) {
            System.out.println("INSERT INTO Teachers ( TeacherID, first_name, last_name ) VALUES ( " + i + ", 'Teacher', '" + i + "' );");
        }

        for (int i = 1; i <= 5000; i++) {
            System.out.println("INSERT INTO students ( student_id, first_name, last_name ) VALUES ( " + i + ", 'Student', '" + i + "' );");
        }

        for (int i = 0; i <fileData3.size(); i++){
            System.out.println("INSERT INTO assignment_types ( assignment_type_id, assignment_type  ) VALUES ( " + (i+1) + ", '" + fileData3.get(i) + "' );");
        }

        for (int i = 1; i <= 5000; i++) {
            System.out.println("INSERT INTO Grades ( );");
        }

    }

    public static ArrayList<String> getFileData(String fileName) {
        ArrayList<String> fileData = new ArrayList<String>();
        try {
            File f = new File(fileName);
            Scanner s = new Scanner(f);
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (!line.equals(""))
                    fileData.add(line);
            }
            return fileData;
        } catch (FileNotFoundException e) {
            return fileData;
        }

    }
}
