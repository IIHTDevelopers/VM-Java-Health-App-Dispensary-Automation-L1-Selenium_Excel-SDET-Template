package testcases;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import coreUtilities.testutils.ApiHelper;
import coreUtilities.utils.FileOperations;
import pages.StartupPage;
import pages.dispensary_Pages;
//import pages.socialService_Pages;
import testBase.AppTestBase;
import testdata.LocatorsFactory;

public class dispensary_testcase extends AppTestBase
{
	Map<String, String> configData;
	Map<String, String> loginCredentials;
	String expectedDataFilePath = testDataFilePath + "expected_data.xlsx";
	String loginFilePath = loginDataFilePath + "Login.xlsx";
	StartupPage startupPage;
	dispensary_Pages dispensary_PagesInstance;
	LocatorsFactory locatorsFactoryInstance;


	@Parameters({"browser", "environment"})
	@BeforeClass(alwaysRun = true)
	public void initBrowser(String browser, String environment) throws Exception {
		configData = new FileOperations().readExcelPOI(config_filePath, environment);
		configData.put("url", configData.get("url").replaceAll("[\\\\]", ""));
		configData.put("browser", browser);

		boolean isValidUrl = new ApiHelper().isValidUrl(configData.get("url"));
		Assert.assertTrue(isValidUrl, configData.get("url")+" might be Server down at this moment. Please try after sometime.");
		initialize(configData);
		startupPage = new StartupPage(driver);
	}

	@Test(priority = 1, groups = {"sanity"}, description="Verify the title and url of  the current page")
	public void verifyTitleOfTheHomePage() throws Exception {

		dispensary_PagesInstance = new dispensary_Pages(driver);
		locatorsFactoryInstance = new LocatorsFactory(driver);

		Map<String, String> loginData = new FileOperations().readExcelPOI(loginFilePath, "credentials");
		Assert.assertTrue(dispensary_PagesInstance.loginToHealthAppByGivenValidCredetial(loginData),"Login failed, Invalid credentials ! Please check manually");

		Map<String, String> expectedData = new FileOperations().readExcelPOI(expectedDataFilePath, "healthApp");
		Assert.assertEquals(dispensary_PagesInstance.verifyTitleOfThePage(),expectedData.get("dasboardTitle")) ;
		Assert.assertEquals(dispensary_PagesInstance.verifyURLOfThePage(),expectedData.get("pageUrl")) ;
		Assert.assertTrue(locatorsFactoryInstance.totalDoctorTextIsPresent(driver).isDisplayed(), "total doctors text is not present in the current page, Please check manually");
	}
	
	@Test(priority = 2, groups = {"sanity"}, description="verify the Dispensary module is present or not?")
	public void verifyDispensaryModuleIsPresentOrNot() throws Exception {
		dispensary_PagesInstance = new dispensary_Pages(driver);
		locatorsFactoryInstance = new LocatorsFactory(driver);
		Assert.assertTrue(dispensary_PagesInstance.verifyDispensaryModuleIsPresentOrNot(), "Dispensary Module is not present, please check manually");
		Assert.assertTrue(locatorsFactoryInstance.registeredPatientTextElementIsPresent(driver).isDisplayed(), "Registered Patient Text Element is not present in the current page, Please check manually");
	}
	
	@Test(priority = 3, groups = {"sanity"}, description="verify all sub-modules are displayed correctly after clicking on the expand icon of Dispensary Module")
	public void verifyAllSubModulesArePresent() throws Exception {
		dispensary_PagesInstance = new dispensary_Pages(driver);
		locatorsFactoryInstance = new LocatorsFactory(driver);
		Assert.assertTrue(dispensary_PagesInstance.verifyAllSubModulesArePresentAndClickOnDispensary(), "Any of the elememt is not present, please check manually");
		Assert.assertTrue(locatorsFactoryInstance.morningCounterIsPresent(driver).isDisplayed(), "Morning Counter is not present in the current page, Please check manually");
	}
	
	
	@Test(priority = 4, groups = {"sanity"}, description="scroll to the bottom of the Sale page and verify that Print Invoice button,Discard button,Invoice History and Credit Limitis and Balance text are peresent or not?")
	public void scrollToButtomAndVerifyTheFields() throws Exception {
		dispensary_PagesInstance = new dispensary_Pages(driver);
		locatorsFactoryInstance = new LocatorsFactory(driver);
		Assert.assertTrue(dispensary_PagesInstance.scrollToButtomAndVerifytheFields(), "Any of the elememt is not present, please check manually");
		Assert.assertTrue(locatorsFactoryInstance.printInvoiceButtonElementIsPresent(driver).isDisplayed(), "Print Invoice Button Element is not present in the current page, Please check manually");
	}
	
	
	@Test(priority = 5, groups = {"sanity"}, description="Perform the keyboard operation to open the Add New Patient popup Page  and verify that the popup is displayed or not")
	public void performTheKeyboardOperationToOpenThePopup() throws Exception {
		dispensary_PagesInstance = new dispensary_Pages(driver);
		locatorsFactoryInstance = new LocatorsFactory(driver);
		Map<String, String> expectedData = new FileOperations().readExcelPOI(expectedDataFilePath, "PageTitle");
		Assert.assertEquals(dispensary_PagesInstance.performTheKeyboardOperationToOpenthePopup(),expectedData.get("AddNewPatientPageTitle"),"Not able to do the keyboard operation, please check manually");
		Assert.assertTrue(locatorsFactoryInstance.firstNameFieldIsPresent(driver).isDisplayed(), "firstName field is not present in the current page, Please check manually");
	}
	
	@Test(priority = 6, groups = {"sanity"}, description="Validate the error message in Add New Patient form's firstname textfield after clicking on Ok Button without filling any information in the form")
	public void validateTheErrorMessageInFirstnameTextfield() throws Exception {
		dispensary_PagesInstance = new dispensary_Pages(driver);
		locatorsFactoryInstance = new LocatorsFactory(driver);
		Map<String, String> expectedData = new FileOperations().readExcelPOI(expectedDataFilePath, "healthAppErrorMessages");
		Assert.assertEquals(dispensary_PagesInstance.validateErrorMessageInFirstnameTextfield(),expectedData.get("FirstNameFieldErrorMessage"),"Error message is not present in the current page, Please check manually") ;
		Assert.assertTrue(locatorsFactoryInstance.errorMeesageInLastNameTextFieldErrorMessageIsPresent(driver).isDisplayed(), "Error message is not present in the current page, Please check manually");
	}
	
	@Test(priority = 7, groups = {"sanity"}, description="Fill all the text fields which are present inside the Add New Patient form and Validate entered values")
	public void fillAllTheTextfields() throws Exception {
		dispensary_PagesInstance = new dispensary_Pages(driver);
		locatorsFactoryInstance = new LocatorsFactory(driver);
		Map<String, String> expectedData = new FileOperations().readExcelPOI(expectedDataFilePath, "addNewPatientPopup");
		Assert.assertEquals(dispensary_PagesInstance.fillfirstNameTextFieldVerifyTheFirstName(expectedData), expectedData.get("firstName"),"firstName Text is not present in the current page, Please check manually");
		Assert.assertEquals(dispensary_PagesInstance.fillmiddelNameTextFieldVerifyThemiddleName(expectedData), expectedData.get("middleName"),"Middle name Text is not present in the current page, Please check manually");
		Assert.assertEquals(dispensary_PagesInstance.filllastNameTextfieldVerifylastName(expectedData), expectedData.get("lastName"),"lastName Text is not present in the current page, Please check manually");
		Assert.assertEquals(dispensary_PagesInstance.fillAgeTextFieldVerifyTheAge(expectedData), expectedData.get("age"),"Age Text is not present in the current page, Please check manually");
		Assert.assertEquals(dispensary_PagesInstance.fillContactNumberTextFieldVerifyContactNumber(expectedData), expectedData.get("contact"),"Contact Number Text is not present in the current page, Please check manually");
		Assert.assertEquals(locatorsFactoryInstance.ageTextFieldIsPresent(),expectedData.get("age"),"Age field Text is not present in the current page, Please check manually");
	}
	
	
	@Test(priority = 8, groups = {"sanity"}, description="On the New Consumption Entry's page, validate the confirm! Message that is Are you sure you want to Proceed ?")
	public void  validateTheConfirmMessage() throws Exception {
		dispensary_PagesInstance = new dispensary_Pages(driver);
		locatorsFactoryInstance = new LocatorsFactory(driver);
		Map<String, String> expectedData = new FileOperations().readExcelPOI(expectedDataFilePath, "healthAppErrorMessages");
		Assert.assertEquals(dispensary_PagesInstance.validateTheConfirmMessageOnTheNewConsumptionEntryPage(),expectedData.get("ConfirmationMessage"),"Confirmation message is not present in the current page, Please check manually") ;
		Assert.assertTrue(locatorsFactoryInstance.confirmMessageIsPresent(driver).isDisplayed(), "Confirmation message is not present in the current page, Please check manually");
	}
	
	
	@AfterClass(alwaysRun = true)
	public void tearDown() {
		System.out.println("before closing the browser");
		browserTearDown();
	}
	
	@AfterMethod
	public void retryIfTestFails() throws Exception {
		startupPage.navigateToUrl(configData.get("url"));
	}
}
