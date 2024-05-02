package org.example;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.List;

import static org.example.Main.findSubjectByIdSubject;

public class StudentManager {

    public static TextArea createStudentTextArea(int studentId,List<Grade> grades,List<Subject> subjects) {
        TextArea studentTextArea = new TextArea();
        studentTextArea.setEditable(false);
        studentTextArea.setPrefSize(300, 200);

        // Populate student information in the TextArea
        StringBuilder studentInfo = new StringBuilder();
                for (Grade grade : grades) {
            if (grade.getAppId() == studentId) {
                Subject subject = findSubjectByIdSubject(subjects, grade.getSubjectId());
                studentInfo.append("Date: ").append(grade.getDate()).append(", Subject: ").append(subject != null ? subject.getName() : "N/A").append(", Grade: ").append(grade.getValue()).append("\n");
            }
        }
        studentTextArea.setText(studentInfo.toString());

        return studentTextArea;
    }

}
