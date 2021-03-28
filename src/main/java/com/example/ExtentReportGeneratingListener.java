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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class ExtentReportGeneratingListener implements TestExecutionListener {

    private final ExtentSparkReporter reporter = new ExtentSparkReporter("report.html");
    private final ExtentReports extentReport = new ExtentReports();
    private static final Map<TestIdentifier, TestExecutionResult> RESULTS = new HashMap<>();
    private static final Map<TestIdentifier, String> SKIPPED = new HashMap<>();

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        this.extentReport.attachReporter(reporter);
        testPlan.getChildren(getRoot(testPlan)).forEach(testIdentifier -> {
            System.out.printf("Adding parent [%s]%n", testIdentifier.getUniqueId());
            RESULTS.put(testIdentifier, null);
        });

    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        testPlan.getChildren(getRoot(testPlan)).forEach(klass -> {
            if (SKIPPED.containsKey(klass)) {
                extentReport.createTest(getKlassName(klass.getUniqueId())).skip(SKIPPED.get(klass));
                System.out.printf("Marking klass [%s] as skipped%n", klass.getDisplayName());
            } else if (RESULTS.containsKey(klass)) {
                final ExtentTest testKlass = extentReport.createTest(getKlassName(klass.getUniqueId()));
                testPlan.getDescendants(klass).forEach(test -> processTestNode(testKlass, klass, test));
            }
        });
        extentReport.flush();
    }

    private void processTestNode(ExtentTest testKlass, TestIdentifier klass, TestIdentifier test) {
        System.out.printf("Processing klass [%s], test [%s]%n", klass.getDisplayName(), test.getDisplayName());
        if (SKIPPED.containsKey(test)) {
            testKlass.createNode(test.getDisplayName()).skip(SKIPPED.get(test));
            System.out.printf("Marking test [%s] as skipped%n", test.getDisplayName());
            return;
        }
        final TestExecutionResult testResult = RESULTS.get(test);
        switch (testResult.getStatus()) {
            case SUCCESSFUL: {
                testKlass.createNode(test.getDisplayName()).pass(testResult.toString());
                break;
            }
            case ABORTED: {
                testKlass.createNode(test.getDisplayName()).log(Status.WARNING, testResult.toString());
                break;
            }
            case FAILED: {
                testKlass.createNode(test.getDisplayName()).fail(testResult.toString());
                break;
            }
            default:
                throw new PreconditionViolationException("Unsupported execution status:" + testResult.getStatus());
        }
    }

    @Override
    public void dynamicTestRegistered(TestIdentifier testIdentifier) {
    }

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        SKIPPED.put(testIdentifier, reason);
    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        RESULTS.put(testIdentifier, testExecutionResult);
    }

    private String getKlassName(String uniqueId) {
        return getKlassName(UniqueId.parse(uniqueId));
    }

    private String getKlassName(UniqueId uniqueId) {
        return uniqueId
                .getSegments().stream()
                .filter(segment -> segment.getType().equals("class"))
                .map(UniqueId.Segment::getValue)
                .collect(Collectors.joining());
    }

    private TestIdentifier getRoot(TestPlan testPlan) {
        return testPlan.getRoots().stream().findFirst().orElseThrow();
    }
}
