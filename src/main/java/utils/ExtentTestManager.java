package utils;

import java.util.HashMap;
import java.util.Map;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class ExtentTestManager {  
	
	private ExtentTestManager() {
		throw new IllegalStateException("Utility class");
	}
	
	@SuppressWarnings("rawtypes")
	static Map extentTestMap = new HashMap();
	static ExtentReports extent = ExtentManager.getReporter(StaticValues.testname);

	public static synchronized ExtentTest getTest() {
		return (ExtentTest) extentTestMap.get((int) (Thread.currentThread().getId()));
	}

	public static synchronized void endTest() {
		extent.endTest((ExtentTest) extentTestMap.get((int) (Thread.currentThread().getId())));
	}

	public static synchronized ExtentTest startTest(String testName) {
		return startTest(testName, "");
	}

	@SuppressWarnings("unchecked")
	public static synchronized ExtentTest startTest(String testName, String desc) {
		ExtentTest test = extent.startTest(testName, desc);
		extentTestMap.put((int) (Thread.currentThread().getId()), test);

		return test;
	}
}
