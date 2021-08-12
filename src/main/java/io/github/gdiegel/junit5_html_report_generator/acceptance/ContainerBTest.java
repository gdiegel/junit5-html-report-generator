package io.github.gdiegel.junit5_html_report_generator.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ContainerBTest {

    @Test
    @DisplayName("Node B-A")
    void nodeBA() {
        System.out.println("Failing node B-A");
        throw new RuntimeException("Boom!");
    }

}
