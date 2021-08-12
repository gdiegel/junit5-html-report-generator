# junit5-html-report-generator

JUnit 5 Listener that generates an HTML report using the [Extent reporting framework](https://www.extentreports.com/).

## Usage

```java
final LauncherDiscoveryRequestBuilder builder=LauncherDiscoveryRequestBuilder.request();
builder.selectors(selectPackage("com.example.test"));
final ExtentReportGeneratingListener extentReportGeneratingListener = new ExtentReportGeneratingListener();
LauncherFactory.create().execute(builder.build(),extentReportGeneratingListener);
```

The report will be generated as `report.html` in the project root.

