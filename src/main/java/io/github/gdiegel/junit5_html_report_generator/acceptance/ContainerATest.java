package io.github.gdiegel.junit5_html_report_generator.acceptance;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ContainerATest {

    @Test
    @DisplayName("Node A-A")
    void nodeAA() {
        System.out.println("Passing node A-A");
    }

    @Test
    @DisplayName("Node A-B")
    @Disabled
    void nodeAB() {
        System.out.println("Skipping node A-B");
    }

}
