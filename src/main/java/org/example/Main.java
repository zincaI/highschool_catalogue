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
import java.util.Objects;
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
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws IOException {

        loginStage = new Stage();

        // Setăm culoarea textului la roșu pentru a atrage atenția

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.BLACK);

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
                    showTeacherWindow(appConnectedUser);
                } else {
                    loginStage.close();
                    showStudentWindow(appConnectedUser.getId());
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
    private boolean textAreas = false; // Flag to track whether the TextArea has been added

    private void showTeacherWindow(AppUser appConnectedUser) {
        Stage teacherStage = new Stage();

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.BLACK);

        final TextArea[] studentWithGradesTextArea = {TeacherManager.createStudentWithGradesTextArea(users, grades, subjects)};

        teacherStage.setTitle("Teacher Interface");

        // Creează un ScrollPane care va conține conținutul ferestrei
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(Color.rgb(216, 228, 188), CornerRadii.EMPTY, Insets.EMPTY)));


        // Butonul pentru "Show students"
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-color: green; -fx-border-width: 2px;"); // Setăm culoarea de fundal, culoarea textului, fontul textului, marginile și culoarea marginii butonului
        exitButton.setOnAction(event -> teacherStage.close());

        Button showStudentsButton = new Button("Show students");
        showStudentsButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-color: green; -fx-border-width: 2px;");
        showStudentsButton.setOnAction(event -> {
            //TextArea studentTextArea = createStudentTextArea(users);
            TextArea studentTextArea = TeacherManager.createStudentTextArea(users);

            VBox.setMargin(studentTextArea, new Insets(10, 0, 0, 0));
            if(!studentsTextAreaAdded) {// Set margin to separate TextArea from the button
                studentsTextAreaAdded = true;// Set margin to separate TextArea from the button
                root.getChildren().add(root.getChildren().indexOf(showStudentsButton) + 1, studentTextArea); // Add the TextArea after the button
            }
        });

        // Butonul pentru "Show students with grades"
        Button showStudentsWithGradesButton = new Button("Show students with grades");
        showStudentsWithGradesButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-color: green; -fx-border-width: 2px;");
        showStudentsWithGradesButton.setOnAction(event -> {
//            TextArea studentWithGradesTextArea = TeacherManager.createStudentWithGradesTextArea(users,grades,subjects);
            VBox.setMargin(studentWithGradesTextArea[0], new Insets(10, 0, 0, 0));
            if(!studentsWithGradesTextAreaAdded) {// Set margin to separate TextArea from the button
                studentsWithGradesTextAreaAdded=true;
                root.getChildren().add(root.getChildren().indexOf(showStudentsWithGradesButton) + 1, studentWithGradesTextArea[0]); // Add the TextArea after the button
            }
        });
        // TextFields pentru introducerea datelor


        // Button for adding a grade
        Button addButton = new Button("Add Grade");
        addButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-color: green; -fx-border-width: 2px;");
        addButton.setOnAction(event -> {
            if (!textAreas) {
                TextField studentIdField = new TextField();
                studentIdField.setPromptText("Student ID");

                TextField gradeField = new TextField();
                gradeField.setPromptText("Grade");

                TextField subjectIdField = new TextField();
                subjectIdField.setPromptText("Subject id");

                Button doneButton = new Button("Done");
                doneButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-color: green; -fx-border-width: 2px;");
                doneButton.setOnAction(e -> {
                    // Retrieve the student ID and grade from text fields
                    String studentIdText = studentIdField.getText();
                    String gradeText = gradeField.getText();
                    String subjectIdText = subjectIdField.getText();

                    // Perform validation and add the grade to the list
                    if (!studentIdText.isEmpty() && !gradeText.isEmpty()) {
                        int studentId = Integer.parseInt(studentIdText);
                        int grade = Integer.parseInt(gradeText);
                        int subjectId=Integer.parseInt(subjectIdText);
                        // Call a method to add the grade to the list

//                        appConnectedUser.getId() == subject.getAppId()
                        Subject subj = null;
                        for(Subject subject:subjects){
                            if(subject.getIdSubject()==subjectId)
                                subj=subject;
                        }
                        if(grade<=10&&grade>=0&&
                                Objects.requireNonNull(subj).getAppId()==appConnectedUser.getId()&&
                                Objects.equals(appConnectedUser.getRole(), "student")){
                        try {
                            addGrade(users,
                                    subjects,
                                    grades,
                                    studentId,
                                    subjectId,
                                    grade,
                                    appConnectedUser);

                            root.getChildren().remove(studentWithGradesTextArea[0]); // Remove the existing text area
                            studentWithGradesTextArea[0] = TeacherManager.createStudentWithGradesTextArea(users, grades, subjects); // Recreate the text area with updated data
                            root.getChildren().add(root.getChildren().indexOf(showStudentsWithGradesButton) + 1, studentWithGradesTextArea[0]); // Add the updated text area after the button
                        } catch (IOException ex) {
                            errorLabel.setText("Please fill all text box and make sure the ids are valid.");
                            throw new RuntimeException(ex);
                        }

                        }
                        else{
                            if(! Objects.equals(appConnectedUser.getRole(), "student")){
                                errorLabel.setText("The student id is not valid.");
                            }
                           else if(grade>10||grade<0){
                                errorLabel.setText("Invalid grade. Please add a grade between 0 and 10.");
                            }
                            else if(Objects.requireNonNull(subj).getAppId()!=appConnectedUser.getId()){
                                errorLabel.setText("You can't modify this subject's grade as you are not it's teacher.");


                            }

                        }
                    } else {
                        // Display an error message if any field is empty
                        //("Please fill all text box and make sure the ids are valid.");
                        errorLabel.setText("Please fill all text box and make sure the ids are valid.");

                    }
                });



                // Add the text fields and "Done" button to the layout
                root.getChildren().addAll(studentIdField, gradeField,subjectIdField, doneButton,errorLabel);
                textAreas = true;
            }
        });


        // Adaugă butoanele în VBox-ul principal
        root.getChildren().addAll(exitButton,showStudentsButton, showStudentsWithGradesButton,addButton);

        // Setează VBox-ul ca și conținut pentru ScrollPane
        scrollPane.setContent(root);

        Scene teacherScene = new Scene(scrollPane, 300, 200); // Setează ScrollPane ca și rădăcină a scenei
        // teacherScene.setFill(Color.rgb(216, 228, 188));
        teacherStage.setScene(teacherScene);
        teacherStage.setTitle("Show students");
        teacherStage.show();
    }



    // Funcția pentru afișarea ferestrei pentru elev
    private void showStudentWindow(int studentId) {
        Stage studentStage = new Stage();
        studentStage.setTitle("Student Interface");

        // Create a VBox to contain the window content
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.rgb(216, 228, 188), CornerRadii.EMPTY, Insets.EMPTY)));
        root.setSpacing(10);
        // Create a Button to exit the window
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-color: green; -fx-border-width: 2px;"); // Setăm culoarea de fundal, culoarea textului, fontul textului, marginile și culoarea marginii butonului
        exitButton.setOnAction(event -> studentStage.close()); // Set action to close the window

        Button showGradesButton = getButton(studentId, root, exitButton);

        // Add components to the VBox layout
        root.getChildren().addAll(showGradesButton);

        Scene studentScene = new Scene(root, 300, 200);
        studentStage.setScene(studentScene);
        studentStage.show();
    }

    private Button getButton(int studentId, VBox root, Button exitButton) {
        Button showGradesButton = new Button("Show Grades");
        showGradesButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-color: green; -fx-border-width: 2px;"); // Setăm culoarea de fundal, culoarea textului, fontul textului, marginile și culoarea marginii butonului
        showGradesButton.setOnAction(event -> {
            TextArea gradesTextArea = StudentManager.createStudentTextArea(studentId,grades,subjects);
            root.getChildren().remove(showGradesButton); // Remove the button after it's clicked
            root.getChildren().addAll(gradesTextArea, exitButton); // Add the text area and exit button
        });
        return showGradesButton;
    }

    public void addGrade(List<AppUser> users,
                         List<Subject> subjects,
                         List<Grade> grades,
                         int studentId,
                         int subjectId,
                         int newGrade,
                         AppUser appConnectedUser)
            throws IOException {
        Subject subject = findSubjectByIdSubject(subjects, subjectId);
        AppUser student = findUserById(users, studentId);

        if (student != null && subject != null && student.getRole().equals("student") && appConnectedUser.getId() == subject.getAppId()) {
            Grade grade = new Grade();

            // Validate grade
            if (newGrade < 0 || newGrade > 10) {
                System.out.println("Invalid input. Please enter a valid grade (0-10).");
                return;
            }

            grade.setValue(newGrade);
            grade.setAppId(student.getId());
            LocalDate currentDate = LocalDate.now();
            String date = currentDate.toString();
            grade.setDate(date);
            grade.setSubjectId(subject.getIdSubject());

            grade.save();
            grades.add(grade);

            System.out.println("The grade was added successfully to " + student.getFirstName() + " " + student.getLastName() + " for the subject " + subject.getName());
        } else {
            System.out.println("The student or the subject were not found or incorrectly introduced.");
        }
    }

    private static AppUser findUserById(List<AppUser> users, int userId) {
        for (AppUser user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null; // Returnăm null dacă nu găsim un utilizator cu ID-ul dat
    }

    // Metodă pentru a găsi o materie după ID
    static Subject findSubjectByIdSubject(List<Subject> subjects, int subjectId) {
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
    //
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

}

