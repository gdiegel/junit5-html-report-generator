package com.example;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.util.List;

import static java.lang.System.exit;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class Runner {

    public static void main(String[] args) {
        final LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();
        builder.selectors(selectPackage("com.example.test"));
        final SummaryGeneratingListener summaryGeneratingListener = new SummaryGeneratingListener();
        ExtentSparkReporter spark = new ExtentSparkReporter("Spark.html");
        final ExtentReportGeneratingListener extentReportGeneratingListener = new ExtentReportGeneratingListener(spark);
        LauncherFactory.create().execute(builder.build(), summaryGeneratingListener, extentReportGeneratingListener);
        final List<TestExecutionSummary.Failure> failures = summaryGeneratingListener.getSummary().getFailures();
        System.out.printf("Finished test suite with [%s] failures%n", failures.size());
        failures.forEach(failure -> System.out.printf("%s: %s%n", failure.getTestIdentifier().getDisplayName(), failure.getException()));
        exit(failures.size() == 0 ? 0 : 1);
    }
}
