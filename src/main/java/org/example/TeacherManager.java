package org.example;

import javafx.scene.control.TextArea;

import java.util.List;

public class TeacherManager {
    public static TextArea createStudentTextArea(List<AppUser> users) {
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

    public static TextArea createStudentWithGradesTextArea(List<AppUser> users, List<Grade> grades, List<Subject> subjects){
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
    static Subject findSubjectByIdSubject(List<Subject> subjects, int subjectId) {
        for (Subject subject : subjects) {
            if (subject.getIdSubject() == subjectId) {
                return subject;
            }
        }
        return null; // Returnăm null dacă nu găsim o materie cu ID-ul dat
    }
}
