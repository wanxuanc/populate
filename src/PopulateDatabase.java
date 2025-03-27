public class PopulateDatabase {
    public static void main(String[] args) {
        // INSERT INTO Teachers ( TeacherID, Name ) VALUES ( 200 , 'Teacher200 ');
        for (int i = 1; i <= 200; i++) {
            System.out.println("INSERT INTO Teachers ( TeacherID, Name ) VALUES ( " + i + ", 'Teacher" + i + "' );");
        }

        for (int i = 1; i <= 5000; i++) {
            System.out.println("INSERT INTO Students ( StudentID, Name ) VALUES ( " + i + ", 'Student" + i + "' );");
        }
    }
}
