package org.example;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.List;

import static org.example.Main.findSubjectByIdSubject;

public class StudentManager {

//    List<AppUser> users = AppUser.loadUsers();
//    List<Subject> subjects = Subject.loadUsers();
//    List<Grade> grades = Grade.loadGrades();


    public static TextArea createStudentTextArea(int studentId,List<Grade> grades,List<Subject> subjects) {
        TextArea studentTextArea = new TextArea();
        studentTextArea.setEditable(false);
        studentTextArea.setPrefSize(300, 200);

        // Populate student information in the TextArea
        StringBuilder studentInfo = new StringBuilder();
                for (Grade grade : grades) {
            if (grade.getAppId() == studentId) {
                Subject subject = findSubjectByIdSubject(subjects, grade.getSubjectId());
                studentInfo.append("Date: " + grade.getDate() + ", Subject: " + (subject != null ? subject.getName() : "N/A") +
                        ", Grade: " + grade.getValue() + "\n");
            }
        }
        studentTextArea.setText(studentInfo.toString());

        return studentTextArea;
    }

}
