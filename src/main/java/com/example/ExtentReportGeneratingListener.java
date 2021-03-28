package com.example;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.junit.platform.commons.PreconditionViolationException;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.util.stream.Collectors;

class ExtentReportGeneratingListener implements TestExecutionListener {

    private final ExtentSparkReporter reporter = new ExtentSparkReporter("report.html");
    private final ExtentReports extentReport = new ExtentReports();

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        this.extentReport.attachReporter(reporter);
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        extentReport.flush();
    }

    @Override
    public void dynamicTestRegistered(TestIdentifier testIdentifier) {
        System.out.printf("Registered test [%s]%n", testIdentifier.getDisplayName());
    }

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        extentReport.createTest(testIdentifier.getDisplayName()).skip(reason);
    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testIdentifier.isTest()) {
            {
                final String parentClass = determineParentClass(testIdentifier);
                final String childName = testIdentifier.getDisplayName();
                final ExtentTest test = extentReport.createTest(parentClass);
                switch (testExecutionResult.getStatus()) {
                    case SUCCESSFUL: {
                        test.createNode(childName).pass(testExecutionResult.toString());
                        break;
                    }
                    case ABORTED: {
                        test.createNode(childName).log(Status.WARNING, testExecutionResult.toString());
                        break;
                    }
                    case FAILED: {
                        test.createNode(childName).fail(testExecutionResult.toString());
                        break;
                    }
                    default:
                        throw new PreconditionViolationException("Unsupported execution status:" + testExecutionResult.getStatus());
                }
            }
        }
    }

    private String determineParentClass(TestIdentifier testIdentifier) {
        final var parent = testIdentifier.getParentId();
        String parentClass = "";
        if (parent.isPresent()) {
            parentClass = UniqueId.parse(parent.get())
                    .getSegments().stream()
                    .filter(segment -> segment.getType().equals("class"))
                    .map(UniqueId.Segment::getValue)
                    .collect(Collectors.joining());
        }
        return parentClass;
    }
}
