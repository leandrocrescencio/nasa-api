package api.nasa.gov;

import static io.restassured.RestAssured.given;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.ApiBase;
import base.Common;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.ExtentTestManager;
import utils.PropertiesUtils;

public class CuriosityTestsNasaApi extends ApiBase {

	
	@Test(priority = 1, description="Check availability")
	public void test01() {
		 given()
	        .spec (rspec)
	        .basePath(PropertiesUtils.getValue("pathCu"))
	        .when ()
	        .port(443)
	        .queryParam("api_key",PropertiesUtils.getValue("apiKey"))
	        .get ("")
	        .then ()
	        .contentType (ContentType.JSON)
	        .statusCode (200);
		 
		 ExtentTestManager.getTest().log(LogStatus.INFO, "Api: Curiosity available.");
		 
		 given()
	        .spec (rspec)
	        .basePath(PropertiesUtils.getValue("pathMan"))
	        .when ()
	        .port(443)
	        .queryParam("api_key",PropertiesUtils.getValue("apiKey"))
	        .get ("")
	        .then ()
	        .contentType (ContentType.JSON)
	        .statusCode (200);
		 
		 ExtentTestManager.getTest().log(LogStatus.INFO, "Api: Manifest available.");
	}
	
	
	@Test(priority = 2, description="Check first 10 photos from curiosity equals")
	public void test02() {
		 Response response =given()
	        .spec (rspec)
	        .basePath(PropertiesUtils.getValue("pathCu"))
	        .when ()
	        .port(443)
	        .queryParam("api_key",PropertiesUtils.getValue("apiKey"))
	        .queryParam("sol", PropertiesUtils.getValue("sol"))
	        .queryParam("page", PropertiesUtils.getValue("page"))
	        .get ("")
	        .then ()
	        .contentType (ContentType.JSON)
	        .statusCode (200).extract() .response();
		 	 
		 List<Map<String, String>> photos = response.jsonPath().getList("photos");
		 			 
		 Response response2 =given()
			        .spec (rspec)
			        .basePath(PropertiesUtils.getValue("pathCu"))
			        .when ()
			        .port(443)
			        .queryParam("api_key",PropertiesUtils.getValue("apiKey"))
			        .queryParam("sol", PropertiesUtils.getValue("sol"))
			        .queryParam("page", PropertiesUtils.getValue("page"))
			        .queryParam("earth_date", PropertiesUtils.getValue("earth_date"))
			        .get ("")
			        .then ()
			        .contentType (ContentType.JSON)
			        .statusCode (200).extract() .response();
		 
		 
		 List<Map<String, String>> photos2 = response2.jsonPath().getList("photos");
		 
		 Common.checkfirstphotos(photos,photos2, 10);
		 
		 ExtentTestManager.getTest().log(LogStatus.INFO, "First 10 photos checked.");
		 

	}
	
	@DataProvider(name = "Cameras")
	public String[] cams() {
		  return new String[]	{ "FHAZ","RHAZ","MAST","CHEMCAM","MAHLI","MARDI","NAVCAM","PANCAM","MINITES"};
	}
	
	
	@Test(priority = 3, description="Check the number of pics taken from a Cam", dataProvider="Cameras")
	public void test03(String rovercam) {
				
		 Response manifest = given()
			        .spec (rspec)
			        .basePath(PropertiesUtils.getValue("pathMan"))
			        .when ()
			        .port(443)
			        .queryParam("api_key",PropertiesUtils.getValue("apiKey"))
			        .queryParam("sol", PropertiesUtils.getValue("sol"))
			        .get ("")
			        .then ()			       
			        .contentType (ContentType.JSON)
			        .statusCode(200).extract().response();
		 
		List<Map<String, String>> cameras = manifest.jsonPath().getList("photo_manifest.photos");
		
		final int index = Common.getSolIndex(cameras, PropertiesUtils.getValue("sol"));
				
		ExtentTestManager.getTest().log(LogStatus.INFO, "Data extracted from Manifest: " + cameras.get(index).toString() );
		 
		Response response = given()
			        .spec (rspec)
			        .basePath(PropertiesUtils.getValue("pathCu"))
			        .when ()
			        .port(443)
			        .queryParam("api_key",PropertiesUtils.getValue("apiKey"))
			        .queryParam("sol", PropertiesUtils.getValue("sol"))
			        .get ("")
			        .then ()
			        .contentType (ContentType.JSON)
			        .statusCode(200).extract().response();
	 
		int total_photos = response.jsonPath().getList("photos").size();
		
		Assert.assertTrue(cameras.get(index).toString().contains("total_photos="+total_photos));
		
		ExtentTestManager.getTest().log(LogStatus.INFO, "Total Photos validated: " + total_photos );
	 
		
		if (cameras.get(index).toString().contains(rovercam)) {
			
			Response response2 = given()
			        .spec (rspec)
			        .basePath(PropertiesUtils.getValue("pathCu"))
			        .when ()
			        .port(443)
			        .queryParam("api_key",PropertiesUtils.getValue("apiKey"))
			        .queryParam("sol", PropertiesUtils.getValue("sol"))
			        .queryParam("camera", rovercam)
			        .get ("")
			        .then ()
			        .contentType (ContentType.JSON)
			        .statusCode(200).extract().response();
				 
		 
		 List<Map<String, String>> camerasize = response2.jsonPath().getList("photos");
		
		 ExtentTestManager.getTest().log(LogStatus.INFO, rovercam +": "+ camerasize.size() +" of " + total_photos );
		 
		 boolean moreThan10 = true;

         if (camerasize.size() >= 10) {
                 moreThan10 = false;
          }
        

         Assert.assertEquals(moreThan10,true);
			
		} else {
			
			ExtentTestManager.getTest().log(LogStatus.INFO, rovercam +" has 0 photos taken on this sol="+ PropertiesUtils.getValue("sol"));
		}
		
		 
		 
		 
	}

	

}