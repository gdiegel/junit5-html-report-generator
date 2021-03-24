package com.example.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ContainerDTest {

    static class ContainerDATest {
        @Test
        @DisplayName("Nice name D-A-A")
        void nodeDAA() {
            System.out.println("Passing node D-A-A");
        }
    }

    static class ContainerDBTest {
        @Test
        @DisplayName("Nice name D-A-B")
        void nodeDAB() {
            System.out.println("Passing node D-A-B");
        }
    }

    @Test
    @DisplayName("Nice name D-C")
    void nodeDC() {
        System.out.println("Passing node D-C");
    }
}
