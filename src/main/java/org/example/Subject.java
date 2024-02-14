package org.example;

import javax.persistence.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.Data;

//import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Data

public class Subject {
    private static final String JSON_FILE_PATH = "subjects.json";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int idSubject;
    private String name;
    private String description;
    private int appId;

    public Subject(){}

    public void save() throws IOException {
        List<Subject> subjects = loadUsers();
        subjects.add(this); // Adăugăm utilizatorul curent în lista existentă

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(JSON_FILE_PATH), subjects); // Salvăm lista actualizată în fișierul JSON
    }

    // Metoda pentru a încărca toți utilizatorii din fișierul JSON
    public static List<Subject> loadUsers() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Subject.class);
        File file = new File(JSON_FILE_PATH);
        if (!file.exists()) {
            file.createNewFile(); // Creăm fișierul dacă nu există
            return new ArrayList<>(); // Returnăm o listă goală
        }
        return mapper.readValue(file, listType); // Încărcăm utilizatorii din fișierul JSON
    }




}
