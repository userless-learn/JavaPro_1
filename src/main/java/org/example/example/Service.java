package org.example.example;

import org.example.annotation.AfterSuite;
import org.example.annotation.BeforeSuite;
import org.example.annotation.CsvSource;
import org.example.annotation.Test;

public class Service {

    @BeforeSuite
    public static void add() {
        System.out.println("Add service");
    }

    @AfterSuite
    public static void delete() {
        System.out.println("Delete service");
    }

    @Test(priority = 1)
    public void test1() {
        System.out.println("Service test with priority 1");
    }

    @Test(priority = 9)
    public void test2() {
        System.out.println("Service test with priority 9");
    }

    @Test
    @CsvSource("10, Java, 20, true")
    public void testCsv(int a, String b, int c, boolean d) {
        System.out.println("Service test with csv and priority 5, a = " + a + " , b = " + b + " , c = " + c + " , d = " + d);
    }

}
