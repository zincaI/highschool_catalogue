package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.Data;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class AppUser {
    private static final String JSON_FILE_PATH = "appusers.json"; // Calea către fișierul JSON

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;

    // Constructor implicit necesar pentru serializare/de-serializare JSON
    public AppUser() {}

    public AppUser(int id, String firstName, String lastName, String email, String password, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }


    // Metoda pentru a salva un utilizator în fișierul JSON
    public void save() throws IOException {
        List<AppUser> users = loadUsers();
        users.add(this); // Adăugăm utilizatorul curent în lista existentă

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(JSON_FILE_PATH), users); // Salvăm lista actualizată în fișierul JSON
    }

    // Metoda pentru a încărca toți utilizatorii din fișierul JSON
    public static List<AppUser> loadUsers() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, AppUser.class);
        File file = new File(JSON_FILE_PATH);
        if (!file.exists()) {
            file.createNewFile(); // Creăm fișierul dacă nu există
            return new ArrayList<>(); // Returnăm o listă goală
        }
        return mapper.readValue(file, listType); // Încărcăm utilizatorii din fișierul JSON
    }
}
