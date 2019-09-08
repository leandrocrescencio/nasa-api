package base;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Map;

public class Common {

	
	private Common() {
		throw new IllegalStateException("Utility class");
	}
	
	public static int getSolIndex(List<Map<String, String>> cameras, String sol) {
		int count = 0;

		boolean tree = true;

		while (tree) {

			count++;
			if (cameras.get(count).toString().contains("sol=" + sol)) {
				tree = false;
			}
		}
		return count;

	}

	public static void checkfirstphotos(List<Map<String, String>> photos, List<Map<String, String>> photos2,
			int index) {

		int count = 0;
		while (count < index) {
			assertThat(photos.get(count), is(equalTo(photos2.get(count))));
			count++;
		}

	}
	
}
