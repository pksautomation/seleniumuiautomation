package testrunner;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.pksautomation.utils.v2.reflections.TestScenarioExecuter;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.pksautomation.utils.v2.Helper;
import com.pksautomation.utils.v2.AssertionUtils;
import com.pksautomation.utils.v2.Config;
import com.pksautomation.utils.v2.LoggerUtils;
import com.pksautomation.utils.v2.customexception.CustomRuntimeException;
import com.pksautomation.utils.v2.testNG.TestBase;

public class ScenariosRunner extends TestBase {
	
	
	@Test(dataProvider = "ScenariosRunner")
	public void scenarioRunner(String[] testData) {
		LoggerUtils logger = new LoggerUtils();
		Config config = Config.getConfig();
		TestScenarioExecuter testScenarioExecuter = new TestScenarioExecuter();
		config.setTestScenarioExecutor(testScenarioExecuter);
		config.getUtilityObjectManager().getBrowserUtils().openBrowser();
		logger.logComment(" Start Execution of test Scenarios : " + testData[0]);
		logger.logComment(" Test Description : " + testData[1]);
		String jsonFilePath = System.getProperty("user.dir") + "/src/test/resources/TestData/ScenariosFiles/" + testData[0] + ".json";
		try {
			config.getTestScenarioExecutor().executeScenarioFromJsonFile(jsonFilePath);
		} catch (Exception e) {
			logger.logException(e.getMessage(), e, false);
		}
	}
}
