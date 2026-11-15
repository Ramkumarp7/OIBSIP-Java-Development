package com.oasis.exam;

import java.util.*;

public final class QuestionBank {
    private QuestionBank() {}
    public static List<Question> getQuestions() {
        List<Question> q = new ArrayList<>();
        q.add(new Question("Which keyword is used to inherit a class in Java?",
                new String[]{"implements", "extends", "inherits", "instanceof"}, 1,
                "The extends keyword creates a subclass from a superclass."));
        q.add(new Question("Which method is the entry point of a standard Java application?",
                new String[]{"start()", "run()", "main()", "init()"}, 2,
                "The JVM starts execution from public static void main(String[] args)."));
        q.add(new Question("Which collection does not allow duplicate elements?",
                new String[]{"ArrayList", "LinkedList", "HashSet", "Vector"}, 2,
                "A Set stores unique elements."));
        q.add(new Question("What is the size of the int primitive type in Java?",
                new String[]{"8 bits", "16 bits", "32 bits", "64 bits"}, 2,
                "Java int is a signed 32-bit primitive type."));
        q.add(new Question("Which block is always executed after try/catch, when control leaves normally?",
                new String[]{"final", "finally", "finalize", "throws"}, 1,
                "The finally block is used for cleanup and normally runs whether an exception occurs or not."));
        q.add(new Question("Which interface is commonly used to sort objects with a custom ordering?",
                new String[]{"Runnable", "Comparator", "Serializable", "Cloneable"}, 1,
                "Comparator defines an external/custom comparison strategy."));
        q.add(new Question("Which keyword prevents a class from being subclassed?",
                new String[]{"static", "private", "const", "final"}, 3,
                "A final class cannot be extended."));
        q.add(new Question("Which of these is not an object-oriented programming principle?",
                new String[]{"Encapsulation", "Polymorphism", "Compilation", "Inheritance"}, 2,
                "Compilation is a translation process, not an OOP principle."));
        q.add(new Question("Which package contains the Scanner class?",
                new String[]{"java.io", "java.util", "java.lang", "java.net"}, 1,
                "Scanner is part of java.util."));
        q.add(new Question("What does JVM stand for?",
                new String[]{"Java Variable Method", "Java Virtual Machine", "Joint Virtual Module", "Java Verified Mode"}, 1,
                "JVM means Java Virtual Machine."));
        return q;
    }
}
