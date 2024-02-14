package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;
import java.io.IOException;
//import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Grade {
    private static final String JSON_FILE_PATH = "grade.json";

    private int gradeId;
    private int value;
    private int appId;
    private String date;
    private int subjectId;



    private static int lastGradeId = 0;

    public Grade() {}

    public Grade(int value, int appId, String date, int subjectId) {
        this.gradeId = ++lastGradeId;
        this.value = value;
        this.appId = appId;
        this.date = date;
        this.subjectId = subjectId;
    }

//    public void save() throws IOException {
//        List<Grade> grades = loadGrades();
//        grades.add(this);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValue(new File(JSON_FILE_PATH), grades);
//    }




    public static List<Grade> loadGrades() throws IOException {
        File file = new File(JSON_FILE_PATH);
        if (!file.exists()) {
            file.createNewFile();
            return new ArrayList<>();
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, new TypeReference<List<Grade>>() {});
    }

    public void save() throws IOException {
        List<Grade> grades = loadGrades();
        this.gradeId = ++lastGradeId; // Actualizăm ID-ul notei

        grades.add(this);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(JSON_FILE_PATH), grades);
    }

//public void nextIdStatus(){
//        idStatus++;
//}

}
