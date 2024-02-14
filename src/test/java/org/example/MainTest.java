package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;

public class MainTest extends TestCase {
    public void testCalculateAverageGradeForSubject() {
        // Mock data
        List<Grade> grades = new ArrayList<>();
        grades.add(new Grade( 10, 4, "2024-01-01", 1)); // nota 10
        grades.add(new Grade( 9, 4, "2024-01-01", 1));  // nota 9
        grades.add(new Grade( 8, 4, "2024-01-01", 2));  // nota 8

        Main main = new Main();
        double result = main.calculateAverageGradeForSubject(grades, 1,4); // calculam media pentru materia cu id-ul 1

        assertEquals(9.5, result); // media celor doua note cu id-ul 1 este 9.5
    }


}
