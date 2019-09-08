package utils;

public class StaticValues {
	
	private StaticValues() {
		throw new IllegalStateException("Utility class");
	}

	public static final String PATH_PROJECT = System.getProperty("user.dir");

	public static final String OS = System.getProperty("os.name");

	public static final int TIMEOUT = 30;
	
	public static final long TIMESTAMP = 1;
	
	protected static String testname;

	public static String getTestName() {
		return testname;
	}

	public static void setTestName(String className) {
		testname = className;
	}

}
