# junit5-html-report-generator

JUnit5 Listener that generates an HTML test report using
the [Extent reporting framework](https://www.extentreports.com/).

## Usage

### LauncherFactory

If you run your tests programmatically you may use the class directly as an argument when launching tests via
the `org.junit.platform.launcher.Launcher`

```java
final LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();
builder.selectors(selectPackage("com.example.test"));
final ExtentReportGeneratingListener extentReportGeneratingListener = new ExtentReportGeneratingListener();
LauncherFactory.create().execute(builder.build(), extentReportGeneratingListener);
```

### Service Provider Interface

Create a file `org.junit.platform.launcher.TestExecutionListener` in the path `src/main/resources/META-INF/services/`
and enter the fully qualified name of the class you want to load in that file:

```
io.github.gdiegel.junit5_html_report_generator.ExtentReportGeneratingListener
```

The Java `ServiceLoader` will automatically load the report generator. In any case the report will be generated
as `report.html` in the project root.

