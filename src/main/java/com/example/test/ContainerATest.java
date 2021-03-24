package com.example.test;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ContainerATest {

    @Test
    @DisplayName("Nice name A-A")
    void nodeAA() {
        System.out.println("Passing node A-A");
    }

    @Test
    @DisplayName("Nice name A-B")
    @Disabled
    void nodeAB() {
        System.out.println("Skipping node A-B");
    }

}
