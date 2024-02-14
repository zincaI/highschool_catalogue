package org.example;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            List<AppUser> users = AppUser.loadUsers();
            List<Subject> subjects = Subject.loadUsers();
            List<Grade> grades = Grade.loadGrades();

            Main mainInstance = new Main();

            AppUser appConnectedUser=null;

            Scanner scanner = new Scanner(System.in);

            boolean foundUser = false;

                while(foundUser==false) {
                    System.out.print("Introduce email: ");
                    String inputEmail = scanner.nextLine();
                    System.out.print("Introduce password: ");
                    String inputPassword = scanner.nextLine();

                    for (AppUser user : users) {
                        if (user.getEmail().equals(inputEmail) && user.getPassword().equals(inputPassword)) {
                            foundUser = true;
                            appConnectedUser = user;
                            break; // Ieșiți din buclă când găsiți o potrivire
                        }
                    }
                    if(foundUser){
                        System.out.println("You are now connected in the catalogue");
                        break;
                    }
                    }

            if(foundUser) {
                if(appConnectedUser.getRole().equals("teacher")) {
                   int subjectId = 0;

                    int value = -1;
                    while(value!=0){
                        System.out.println("The cases are:");
                        System.out.println("0. Exit");
                        System.out.println("1. View the students with their ids");
                        System.out.println("2. View student names, ids, and grades");
                        System.out.println("3. Add grade to student");
                        System.out.println("4. Average grade per subject and per student");
                        System.out.println("5. Delete grade");
                        System.out.println("6. Sort students alphabetically ");
                        System.out.println("7. Sort grades after date");

                        System.out.print("Introduce value: ");
                        do{
                            if (scanner.hasNextInt()) {
                                value = scanner.nextInt();
                                scanner.nextLine();
                            } else {
                                System.out.println("Invalid input. Please enter a valid value.");
                                scanner.nextLine(); // Consumăm newline-ul rămas în buffer
                                value = -1; // Setăm name ca null pentru a forța continuarea buclei
                            }
                        } while (value==-1);

                    switch(value){
                        case 0:
                            System.out.println("The exit was successful");
                            break;
                        case 1:
                           mainInstance.showStudents(users);
                            break;
                        case 2:
                            mainInstance.showStudentsWithGrades(users,subjects,grades);
                            break;
                        case 3:
                            mainInstance.addGrade(users,subjects,grades,scanner,appConnectedUser);
                            break;
                        case 4:
                            mainInstance.averageGrade(subjects,grades,scanner,appConnectedUser,mainInstance);
                            break;
                        case 5:
                            mainInstance.deleteGrade(subjects,grades,scanner,appConnectedUser);
                            break;
                        case 6:
                            mainInstance.sortStudentsBySurnames(users);
                            break;
                        case 7:
                          mainInstance.sortGrades(users,subjects,grades);
                            break;
                        default:
                            System.out.println("This is not a valid value");
                            break;
                    }}
                }
                else{
                    int value=-1;
                    int studentId = appConnectedUser.getId();

                    while(value!=0){
                        System.out.println("0. Exit 1. Show notes");
                        System.out.println("Introduce value: ");

                        do{
                            if (scanner.hasNextInt()) {
                                value = scanner.nextInt();
                                scanner.nextLine();
                            } else {
                                System.out.println("Invalid input. Please enter a valid value.");
                                scanner.nextLine(); // Consumăm newline-ul rămas în buffer
                                value = 0; // Setăm name ca null pentru a forța continuarea buclei
                            }
                        } while (value == 0);
                        switch (value){
                        case 0:
                            break;
                            case 1:
                   mainInstance.showStudentGrades(users,subjects,grades,studentId);
                    break;

                    }
                }

            }
            scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
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

public void showStudentsWithGrades(List<AppUser>users,List<Subject>subjects,List<Grade>grades){
    for (Grade grade : grades) {
        // Find the student associated with the grade
        AppUser student = findUserById(users, grade.getAppId());
        // Find the subject associated with the grade
        Subject subject = findSubjectByIdSubject(subjects, grade.getSubjectId());

        // Print student, grade, and subject information
        if (student != null && subject != null) {
            System.out.println("Student id: " + student.getId());
            System.out.println("Student: " + student.getFirstName() + " " + student.getLastName());
            System.out.println("Grade id: "+ grade.getGradeId());
            System.out.println("Grade: " + grade.getValue());
            System.out.println("Subject: " + subject.getName());
            System.out.println();
        }
    }
}

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
        System.out.println("Introduce grade: ");2344eeea
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

public void showStudents(List<AppUser>users){
    for (AppUser user : users) {
        if(user.getRole().equals("student")){
            System.out.println("Student ID: " + user.getId());
            System.out.println("First Name: " + user.getFirstName());
            System.out.println("Last Name: " + user.getLastName());
            System.out.println();
        }}
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

public void showStudentGrades(List<AppUser>users,List<Subject>subjects,List<Grade>grades,int studentId){
    // Afisăm notele elevului pentru fiecare disciplină și data asociată
    System.out.println("Grades: ");
    for (Grade grade : grades) {
        // Verificăm dacă nota este asociată elevului dorit
        if (grade.getAppId() == studentId) {
            // Găsim numele elevului
            AppUser student = findUserById(users, studentId);

            // Găsim numele materiei
            Subject subject = findSubjectByIdSubject(subjects, grade.getSubjectId());

            // Afișăm nota, data și disciplina asociate
            System.out.println("Date: " + grade.getDate() + ", Subject: " + (subject != null ? subject.getName() : "N/A") +
                    ", Grade: " + grade.getValue());
        }
    }
}

}

