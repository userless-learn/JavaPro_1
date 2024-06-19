package org.example;

import org.example.example.Service;
import org.example.runner.TestRunner;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) {
        try {
            TestRunner.runTests(Service.class);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}