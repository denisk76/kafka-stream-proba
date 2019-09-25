package ru.bmsgroup.kafkastreamproba.model;

public class Test {
    public static void main(String[] args) {
        new Test().new B();
    }

    class A {
        String str = "ab";

        A() {
            printLength();
        }

        void printLength() {
            System.out.println(str.length());
        }
    }

    class B extends A {
        String str = "abc";

        void printLength() {
            System.out.println(str.length());
        }
    }
}
