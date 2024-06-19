package org.example.runner;

import org.example.annotation.AfterSuite;
import org.example.annotation.BeforeSuite;
import org.example.annotation.CsvSource;
import org.example.annotation.Test;
import org.example.example.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;

public class TestRunner {

    public static void runTests(Class<?> c) throws InvocationTargetException, IllegalAccessException {
        Service service = new Service();
        Method[] declareMethods = c.getDeclaredMethods();
        ArrayList<Method> beforeMethods = new ArrayList<>();
        ArrayList<Method> afterMethods = new ArrayList<>();
        ArrayList<Method> testMethods = new ArrayList<>();
        for (Method declareMethod : declareMethods) {
            if (declareMethod.isAnnotationPresent(BeforeSuite.class)) {
                if (!beforeMethods.isEmpty()) {
                    throw new IllegalArgumentException("В классе больше одного метода с аннотацией BeforeSuite");
                }
                beforeMethods.add(declareMethod);
            }
            if (declareMethod.isAnnotationPresent(AfterSuite.class)) {
                if (!afterMethods.isEmpty()) {
                    throw new IllegalArgumentException("В классе больше одного метода с аннотацией AfterSuite");
                }
                afterMethods.add(declareMethod);
            }
            if (declareMethod.isAnnotationPresent(Test.class)) {
                testMethods.add(declareMethod);
            }
        }

        if (!beforeMethods.isEmpty()) {
            beforeMethods.stream().findFirst().get().invoke(service);
        }

        testMethods.sort(Comparator.comparingInt(method -> method.getAnnotation(Test.class).priority()));

        for (Method method : testMethods) {
            if (method.isAnnotationPresent(CsvSource.class)) {
                String[] parseString = method.getAnnotation(CsvSource.class).value().split(",");
                Object[] parseParams = parseCsvParams(parseString, method.getParameterTypes());
                method.invoke(service, parseParams);
            } else {
                method.invoke(service);
            }
        }

        if (!afterMethods.isEmpty()) {
            afterMethods.stream().findFirst().get().invoke(service);
        }

    }

    private static Object[] parseCsvParams(String[] csvParams, Class<?>[] parameterTypes) {
        Object[] parsedParams = new Object[csvParams.length];
        for (int i = 0; i < csvParams.length; i++) {
            String param = csvParams[i].trim();
            if (parameterTypes[i] == int.class) {
                parsedParams[i] = Integer.parseInt(param);
            } else if (parameterTypes[i] == boolean.class) {
                parsedParams[i] = Boolean.parseBoolean(param);
            } else if (parameterTypes[i] == String.class) {
                parsedParams[i] = param;
            }
        }
        return parsedParams;
    }
}
