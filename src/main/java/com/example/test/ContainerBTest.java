package com.example.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ContainerBTest {

    @Test
    void nodeBA() {
        System.out.println("Passing node B-A");
    }

    @Test
    @DisplayName("Nice name B-B")
    void nodeBB() {
        System.out.println("Failing node B-A");
        throw new RuntimeException("Boom!");
    }

}
