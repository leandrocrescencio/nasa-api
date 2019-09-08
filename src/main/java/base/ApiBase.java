package base;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.relevantcodes.extentreports.LogStatus;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import utils.ExtentManager;
import utils.ExtentTestManager;
import utils.PropertiesUtils;
import utils.StaticValues;

public class ApiBase {

	protected RequestSpecification rspec;
	protected RequestSpecBuilder build;

	@BeforeClass
	public void setBaseUri() {
		build = new RequestSpecBuilder();
		build.setBaseUri(PropertiesUtils.getValue("baseuri"));
		rspec = build.build();

		StaticValues.setTestName("teste");
	}
	
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		if (StaticValues.getTestName().equalsIgnoreCase("teste")) {
			StaticValues.setTestName(this.getClass().getSimpleName());
		}
		ExtentTestManager.startTest(method.getName(), StaticValues.getTestName());
		if (!ExtentTestManager.getTest().getDescription().isEmpty()) {
			ExtentTestManager.getTest().log(LogStatus.INFO, ExtentTestManager.getTest().getDescription());
		}
	}

	@AfterMethod(alwaysRun = true)
	protected void afterMethod(ITestResult result) {
		for (String group : result.getMethod().getGroups()) {
			ExtentTestManager.getTest().assignCategory(group); // new
		}
		switch (result.getStatus()) {
		case ITestResult.FAILURE:
			ExtentTestManager.getTest().log(LogStatus.FAIL, result.getThrowable());
			break;
		case ITestResult.SKIP:
			ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped " + result.getThrowable());
			break;
		case ITestResult.SUCCESS:
			ExtentTestManager.getTest().log(LogStatus.PASS, "Teste Passed");
			break;
		default:
			if (ExtentTestManager.getTest().getRunStatus().equals(LogStatus.UNKNOWN)) {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "This test method is skipped");
			} else {
				ExtentTestManager.endTest();
			}
		}
		ExtentManager.getReporter(StaticValues.getTestName()).endTest(ExtentTestManager.getTest());
		ExtentManager.getReporter(StaticValues.getTestName()).flush();

	}

	protected String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}
	
}