package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main extends Application {

        private Stage loginStage;
        List<AppUser> users = AppUser.loadUsers();
        List<Subject> subjects = Subject.loadUsers();
        List<Grade> grades = Grade.loadGrades();

    public Main() throws IOException {
    }

    public static void main (String[]args){
        // Launch the JavaFX application
        launch(args);
    }

        @Override
        public void start (Stage primaryStage) throws IOException {

        loginStage = new Stage();

        // Setăm culoarea textului la roșu pentru a atrage atenția

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);

        // Creăm casetele de introducere pentru email și parolă
        TextField emailField = new TextField();
        emailField.setStyle("-fx-pref-width: 200px; -fx-background-color: rgb(143,188,143); -fx-text-fill: rgb(208,240,192);-fx-prompt-text-fill:rgb(208,240,192);"); // Setăm lățimea preferată și culoarea de fundal

        emailField.setPromptText("Introduce email");

        TextField passwordField = new TextField();
        passwordField.setStyle("-fx-pref-width: 200px; -fx-background-color: rgb(143,188,143); -fx-text-fill: rgb(208,240,192);-fx-prompt-text-fill:rgb(208,240,192);"); // Setăm lățimea preferată și culoarea de fundal

        passwordField.setPromptText("Introduce password");

        // Creăm un buton pentru a trimite datele introduse
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-color: green; -fx-border-width: 2px;"); // Setăm culoarea de fundal, culoarea textului, fontul textului, marginile și culoarea marginii butonului
        loginButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            // Aici poți adăuga codul pentru verificarea emailului și parolei
            // De exemplu, poți folosi aceste date pentru a verifica în lista de utilizatori din aplicația ta
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);
            boolean foundUser = false;
            AppUser appConnectedUser = null;
            for (AppUser user : users) {
                if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                    foundUser = true;
                    appConnectedUser = user;
                    break; // Ieșiți din buclă când găsiți o potrivire
                }
            }
            if (foundUser) {
                System.out.println("You are now connected in the catalogue");
                if (appConnectedUser.getRole().equals("teacher")) {
                    loginStage.close();
                    showTeacherWindow();
                } else {
                    loginStage.close();
                    showStudentWindow();
                }
            } else {
                errorLabel.setText("Email or password is incorrect");
            }

        });

        // Creăm un layout StackPane și adăugăm elementele în mijloc
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(Color.rgb(216, 228, 188), CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().addAll(
                new Label("Email:"),
                emailField,
                new Label("Password:"),
                passwordField,
                loginButton,
                errorLabel
        );

        // Creăm scena și o atașăm la fereastra principală
        Scene loginScene = new Scene(root, 300, 200);
        loginStage.setScene(loginScene);
        loginStage.setTitle("Login");
        loginStage.show();
    }

        // Funcția pentru afișarea ferestrei pentru profesor
        private boolean studentsTextAreaAdded = false; // Flag to track whether the TextArea has been added
    private boolean studentsWithGradesTextAreaAdded = false; // Flag to track whether the TextArea has been added
    private void showTeacherWindow() {
        Stage teacherStage = new Stage();
        teacherStage.setTitle("Teacher Interface");

        // Creează un ScrollPane care va conține conținutul ferestrei
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true); // Asigură că ScrollPane se potrivește lățimii ferestrei

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(Color.rgb(216, 228, 188), CornerRadii.EMPTY, Insets.EMPTY)));

        // Butonul pentru "Show students"
        Button showStudentsButton = new Button("Show students");
        showStudentsButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-color: green; -fx-border-width: 2px;");
        showStudentsButton.setOnAction(event -> {
            TextArea studentTextArea = createStudentTextArea();
            VBox.setMargin(studentTextArea, new Insets(10, 0, 0, 0)); // Set margin to separate TextArea from the button
            root.getChildren().add(root.getChildren().indexOf(showStudentsButton) + 1, studentTextArea); // Add the TextArea after the button
        });

        // Butonul pentru "Show students with grades"
        Button showStudentsWithGradesButton = new Button("Show students with grades");
        showStudentsWithGradesButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-color: green; -fx-border-width: 2px;");
        showStudentsWithGradesButton.setOnAction(event -> {
            TextArea studentWithGradesTextArea = createStudentWithGradesTextArea();
            VBox.setMargin(studentWithGradesTextArea, new Insets(10, 0, 0, 0)); // Set margin to separate TextArea from the button
            root.getChildren().add(root.getChildren().indexOf(showStudentsWithGradesButton) + 1, studentWithGradesTextArea); // Add the TextArea after the button
        });

        // Adaugă butoanele în VBox-ul principal
        root.getChildren().addAll(showStudentsButton, showStudentsWithGradesButton);

        // Setează VBox-ul ca și conținut pentru ScrollPane
        scrollPane.setContent(root);

        Scene teacherScene = new Scene(scrollPane, 300, 200); // Setează ScrollPane ca și rădăcină a scenei
        teacherStage.setScene(teacherScene);
        teacherStage.setTitle("Show students");
        teacherStage.show();
    }









    // Funcția pentru afișarea ferestrei pentru elev
        private void showStudentWindow () {
        Stage studentStage = new Stage();

        studentStage.setTitle("Student Interface");


        studentStage.show();
    }

    private TextArea createStudentTextArea() {
        TextArea studentTextArea = new TextArea();
        studentTextArea.setEditable(false);
        studentTextArea.setPrefSize(300, 200);

        // Populate student information in the TextArea
        StringBuilder studentInfo = new StringBuilder();
        for (AppUser user : users) {
            if (user.getRole().equals("student")) {
                studentInfo.append("ID: ").append(user.getId()).append(", Name: ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("\n");
            }
        }
        studentTextArea.setText(studentInfo.toString());

        return studentTextArea;
    }

    public TextArea createStudentWithGradesTextArea(){
        TextArea studentTextArea = new TextArea();
        studentTextArea.setEditable(false);
        studentTextArea.setPrefSize(300, 200);
        StringBuilder studentInfo = new StringBuilder();

        for(AppUser student:users){
            if(student.getRole().equals("student")){
            for (Grade grade : grades) {
            // Verificăm dacă nota este asociată elevului dorit
            if (grade.getAppId() == student.getId()) {
                // Găsim numele elevului
                //AppUser student = findUserById(users, student.getId());

                // Găsim numele materiei
                Subject subject = findSubjectByIdSubject(subjects, grade.getSubjectId());

               // assert student != null;
                assert subject != null;
                studentInfo.append("Student's id: ").append(student.getId()).append(", Name: ").append(student.getFirstName()).append(student.getLastName()).append("Subject: ").append(subject.getName()).append("Grade's id:").append(grade.getGradeId()).append("Grade's value: ").append(grade.getValue()).append("\n");
            }
            }
            }
        }

        studentTextArea.setText(studentInfo.toString());

        return studentTextArea;
    }


//    public static void main(String[] args) {
//        try {
//            List<AppUser> users = AppUser.loadUsers();
//            List<Subject> subjects = Subject.loadUsers();
//            List<Grade> grades = Grade.loadGrades();
//
//            Main mainInstance = new Main();
//
//            AppUser appConnectedUser=null;
//
//            Scanner scanner = new Scanner(System.in);
//
//            boolean foundUser = false;
//
//                while(foundUser==false) {
//                    System.out.print("Introduce email: ");
//                    String inputEmail = scanner.nextLine();
//                    System.out.print("Introduce password: ");
//                    String inputPassword = scanner.nextLine();
//
//                    for (AppUser user : users) {
//                        if (user.getEmail().equals(inputEmail) && user.getPassword().equals(inputPassword)) {
//                            foundUser = true;
//                            appConnectedUser = user;
//                            break; // Ieșiți din buclă când găsiți o potrivire
//                        }
//                    }
//                    if(foundUser){
//                        System.out.println("You are now connected in the catalogue");
//                        break;
//                    }
//                    }
//
//            if(foundUser) {
//                if(appConnectedUser.getRole().equals("teacher")) {
//                   int subjectId = 0;
//
//                    int value = -1;
//                    while(value!=0){
//                        System.out.println("The cases are:");
//                        System.out.println("0. Exit");
//                        System.out.println("1. View the students with their ids");
//                        System.out.println("2. View student names, ids, and grades");
//                        System.out.println("3. Add grade to student");
//                        System.out.println("4. Average grade per subject and per student");
//                        System.out.println("5. Delete grade");
//                        System.out.println("6. Sort students alphabetically ");
//                        System.out.println("7. Sort grades after date");
//
//                        System.out.print("Introduce value: ");
//                        do{
//                            if (scanner.hasNextInt()) {
//                                value = scanner.nextInt();
//                                scanner.nextLine();
//                            } else {
//                                System.out.println("Invalid input. Please enter a valid value.");
//                                scanner.nextLine(); // Consumăm newline-ul rămas în buffer
//                                value = -1; // Setăm name ca null pentru a forța continuarea buclei
//                            }
//                        } while (value==-1);
//
//                    switch(value){
//                        case 0:
//                            System.out.println("The exit was successful");
//                            break;
//                        case 1:
//                           mainInstance.showStudents(users);
//                            break;
//                        case 2:
//                            mainInstance.showStudentsWithGrades(users,subjects,grades);
//                            break;
//                        case 3:
//                            mainInstance.addGrade(users,subjects,grades,scanner,appConnectedUser);
//                            break;
//                        case 4:
//                            mainInstance.averageGrade(subjects,grades,scanner,appConnectedUser,mainInstance);
//                            break;
//                        case 5:
//                            mainInstance.deleteGrade(subjects,grades,scanner,appConnectedUser);
//                            break;
//                        case 6:
//                            mainInstance.sortStudentsBySurnames(users);
//                            break;
//                        case 7:
//                          mainInstance.sortGrades(users,subjects,grades);
//                            break;
//                        default:
//                            System.out.println("This is not a valid value");
//                            break;
//                    }}
//                }
//                else{
//                    int value=-1;
//                    int studentId = appConnectedUser.getId();
//
//                    while(value!=0){
//                        System.out.println("0. Exit 1. Show notes");
//                        System.out.println("Introduce value: ");
//
//                        do{
//                            if (scanner.hasNextInt()) {
//                                value = scanner.nextInt();
//                                scanner.nextLine();
//                            } else {
//                                System.out.println("Invalid input. Please enter a valid value.");
//                                scanner.nextLine(); // Consumăm newline-ul rămas în buffer
//                                value = 0; // Setăm name ca null pentru a forța continuarea buclei
//                            }
//                        } while (value == 0);
//                        switch (value){
//                        case 0:
//                            break;
//                            case 1:
//                   mainInstance.showStudentGrades(users,subjects,grades,studentId);
//                    break;
//
//                    }
//                }
//
//            }
//            scanner.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        //   }

    private static AppUser findUserById(List<AppUser> users, int userId) {
        for (AppUser user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null; // Returnăm null dacă nu găsim un utilizator cu ID-ul dat
    }

    // Metodă pentru a găsi o materie după ID
    private static Subject findSubjectByIdSubject(List<Subject> subjects, int subjectId) {
        for (Subject subject : subjects) {
            if (subject.getIdSubject() == subjectId) {
                return subject;
            }
        }
        return null; // Returnăm null dacă nu găsim o materie cu ID-ul dat
    }

    private static Subject findSubjectByUserId(List<Subject> subjects, int UserId) {
        for (Subject subject : subjects) {
            if (subject.getAppId() == UserId) {
                return subject;
            }
        }
        return null; // Returnăm null dacă nu găsim o materie cu ID-ul dat
    }

    public double calculateAverageGradeForSubject(List<Grade> grades, int subjectId,int student) {
        double totalGrade = 0;
        int count = 0;

        // Parcurgi toate notele pentru subiectul dat
        for (Grade grade : grades) {
            // Verifici dacă nota este asociată subiectului și profesorului dat
            if (grade.getSubjectId() == subjectId&&student==grade.getAppId()) {
                totalGrade += grade.getValue();
                count++;
            }
        }

        // Calculezi media
        if (count > 0) {
            return totalGrade / count;
        } else {
            return 0; // sau arunca o exceptie, in functie de logica aplicatiei
        }
    }

public void sortGrades(List<AppUser>users,List<Subject>subjects,List<Grade>grades){
    grades.sort(Comparator.comparing(Grade::getDate)
            .thenComparing(grade -> {
                AppUser st = findUserById(users, grade.getAppId());
                return st != null ? st.getLastName() : "";
            })
            .thenComparing(grade -> {
                Subject s = findSubjectByIdSubject(subjects, grade.getSubjectId());
                return s != null ? s.getName() : "";
            }));

    // Afișăm lista de note sortată după data calendaristică, numele studentului și numele materiei
    System.out.println("Grade sorted by date:");
    for (Grade grade : grades) {
        AppUser st = findUserById(users, grade.getAppId());
        Subject s = findSubjectByIdSubject(subjects, grade.getSubjectId());
        System.out.println("Date: " + grade.getDate() + ", Student name: " + (st != null ? st.getLastName() : "") + (st != null ? st.getFirstName() : "")
                + ", Subject: " + (s != null ? s.getName() : "") + ", Grade: " + grade.getValue());
    }
}

//public void showStudentsWithGrades(List<AppUser>users,List<Subject>subjects,List<Grade>grades){
//    for (Grade grade : grades) {
//        // Find the student associated with the grade
//        AppUser student = findUserById(users, grade.getAppId());
//        // Find the subject associated with the grade
//        Subject subject = findSubjectByIdSubject(subjects, grade.getSubjectId());
//
//        // Print student, grade, and subject information
//        if (student != null && subject != null) {
//            System.out.println("Student id: " + student.getId());
//            System.out.println("Student: " + student.getFirstName() + " " + student.getLastName());
//            System.out.println("Grade id: "+ grade.getGradeId());
//            System.out.println("Grade: " + grade.getValue());
//            System.out.println("Subject: " + subject.getName());
//            System.out.println();
//        }
//    }
//}

public void addGrade(List<AppUser>users,List<Subject>subjects,List<Grade>grades,Scanner scanner,AppUser appConnectedUser) throws IOException {
    System.out.println("Introduce student id: ");
    int studentId;// ID-ul studentului
    do{
        if (scanner.hasNextInt()) {
            studentId = scanner.nextInt();
            scanner.nextLine();
        } else {
            System.out.println("Invalid input. Please enter a valid student's id.");
            scanner.nextLine(); // Consumăm newline-ul rămas în buffer
            studentId = 0; // Setăm name ca null pentru a forța continuarea buclei
        }
    } while (studentId == 0);

    System.out.println("Introduce subject id: ");
    int subjectId;
    do{
        if (scanner.hasNextInt()) {
            System.out.println("Introduce subject's id:");
            subjectId = scanner.nextInt();
            scanner.nextLine();
        } else {
            System.out.println("Invalid input. Please enter a valid subject's id.");
            scanner.nextLine(); // Consumăm newline-ul rămas în buffer
            subjectId = 0; // Setăm name ca null pentru a forța continuarea buclei
        }
    } while (subjectId == 0);
    Subject subject = findSubjectByIdSubject(subjects, subjectId);


    AppUser student = findUserById(users, studentId);
    if (student != null && subject != null&&student.getRole().equals("student")&&appConnectedUser.getId() == subject.getAppId()) {
        Grade grade = new Grade();
        System.out.println("Introduce grade: ");
        int newGrade=-1;
        do{
        if (scanner.hasNextInt()) {
            newGrade = scanner.nextInt();
            scanner.nextLine();
            if(newGrade<0||newGrade>10){
                newGrade=-1;
                System.out.println("Invalid input. Please enter a valid grade.");
            }
        } else {
            System.out.println("Invalid input. Please enter a valid grade.");
            scanner.nextLine(); // Consumăm newline-ul rămas în buffer
            newGrade = -1; // Setăm name ca null pentru a forța continuarea buclei
        }
        }while(newGrade==-1);

        grade.setValue(newGrade);
        grade.setAppId(student.getId());
        LocalDate currentDate = LocalDate.now();
        String date=currentDate.toString();// Setăm ID-ul studentului ca AppId pentru notă
        grade.setDate(date); // Setăm data curentă
        grade.setSubjectId(subject.getIdSubject()); // Setăm ID-ul materiei pentru notă

        grade.save(); // Salvăm nota în fișierul JSON
        grades.add(grade);//Salvam data in lista

        System.out.println("The grade was added succesfully to " + student.getFirstName() + " " + student.getLastName() + " at the subject " + subject.getName());
    } else {
        System.out.println("The student or the subject were introduced incorrectly");
    }
}

public void deleteGrade(List<Subject>subjects,List<Grade>grades,Scanner scanner,AppUser appConnectedUser){
    // Introdu ID-ul notei pe care dorești să o ștergi
    System.out.println("Introduce the grade's id: ");
    int gradeToDelete;
    do{
        if (scanner.hasNextInt()) {
            gradeToDelete = scanner.nextInt();
            scanner.nextLine();
        } else {
            System.out.println("Invalid input. Please enter a valid grade's id.");
            scanner.nextLine(); // Consumăm newline-ul rămas în buffer
            gradeToDelete = 0; // Setăm name ca null pentru a forța continuarea buclei
        }
    } while (gradeToDelete == 0);

    boolean deleted = false; // Flag pentru a verifica daca nota a fost stearsa


    // Itereaza prin lista de note
    for (Grade grade : grades) {
        // Verifica daca ID-ul notei este egal cu ID-ul notei pe care dorim sa o stergem
        if (grade.getGradeId() == gradeToDelete) {
            // Sterge nota din lista
            if(grade.getSubjectId()==findSubjectByUserId(subjects,appConnectedUser.getId()).getIdSubject()) {
                grades.remove(grade);
                deleted = true;
            }
            else {
                System.out.println("The teacher can't alter this grade");
                break;
            }

            // Afiseaza un mesaj de confirmare
            System.out.println("The grade was deleted succesfully.");
            break; // Iesi din bucla dupa ce ai sters prima nota cu ID-ul dat
        }
    }

    // Verifica daca nota a fost stearsa
    if (!deleted) {
        System.out.println("There is no grade with the id " + gradeToDelete);
    }
}

public void averageGrade(List<Subject>subjects,List<Grade>grades,Scanner scanner,AppUser appConnectedUser,Main mainInstance){
    // Obține ID-ul subiectului asociat profesorului conectat
    int subjectId=findSubjectByUserId(subjects,appConnectedUser.getId()).getIdSubject();
    // Creează o instanță a clasei Main
    System.out.println("Introduce student id student: ");
    int idSt;
    do{
        if (scanner.hasNextInt()) {
            idSt = scanner.nextInt();
            scanner.nextLine();
        } else {
            System.out.println("Invalid input. Please enter a valid student's id.");
            scanner.nextLine(); // Consumăm newline-ul rămas în buffer
            idSt = 0; // Setăm name ca null pentru a forța continuarea buclei
        }
    } while (idSt == 0);

    // Apelarea metodei non-statice calculateAverageGradeForSubject folosind instanța Main
    double averageGrade = mainInstance.calculateAverageGradeForSubject(grades, subjectId,idSt);
    // Afisarea rezultatului
    System.out.println("The average grade for the subject with the id " + subjectId + " with the teacher's id  " + appConnectedUser.getId() + " is: " + averageGrade);
}

public void sortStudentsBySurnames(List<AppUser>users){
    List<AppUser> students = users.stream()
            .filter(user -> user.getRole().equals("student"))
            .collect(Collectors.toList());

    // Sortăm lista de studenți după surname
    students.sort((student1, student2) -> student1.getLastName().compareToIgnoreCase(student2.getLastName()));

    // Afișăm lista de studenți sortată
    System.out.println("Students sorted after students' surnames:");
    for (AppUser s : students) {
        System.out.println("ID: " + s.getId() + ", Surname: " + s.getLastName() + ", Firstname: " + s.getFirstName());
    }
}

//public void showStudentGrades(List<AppUser>users,List<Subject>subjects,List<Grade>grades,int studentId){
//    // Afisăm notele elevului pentru fiecare disciplină și data asociată
//    System.out.println("Grades: ");
//    for (Grade grade : grades) {
//        // Verificăm dacă nota este asociată elevului dorit
//        if (grade.getAppId() == studentId) {
//            // Găsim numele elevului
//            AppUser student = findUserById(users, studentId);
//
//            // Găsim numele materiei
//            Subject subject = findSubjectByIdSubject(subjects, grade.getSubjectId());
//
//            // Afișăm nota, data și disciplina asociate
//            System.out.println("Date: " + grade.getDate() + ", Subject: " + (subject != null ? subject.getName() : "N/A") +
//                    ", Grade: " + grade.getValue());
//        }
//    }
//}

}

