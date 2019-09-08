package utils;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
	private ExtentManager() {
		throw new IllegalStateException("Utility class");
	}
	private static ExtentReports extent;
  
    //Create an extent report instance
    public static ExtentReports createInstance(String testName) {
        String fileName = filePathMethod(testName);
        extent = new ExtentReports(fileName, false);
        extent.loadConfig(new File(StaticValues.PATH_PROJECT + "//src//test//resources//extent-config.xml"));
        extent.assignProject(testName);
        return extent;
    }
    
   public static synchronized ExtentReports getReporter(String testName) {
        if (extent == null) {
            extent = new ExtentReports(filePathMethod(testName), false);
            createInstance(testName);
        }
        
        return extent;
    }
    
    public static String filePathMethod(String testName) {
    	return StaticValues.PATH_PROJECT + "//extent-output//"+ testName +"_"+ dateTimeGenerator() +".html";
    }
	public static String dateTimeGenerator() {
		Format formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}
}
