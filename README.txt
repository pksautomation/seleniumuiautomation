================================================================================
  DEMO UI AUTOMATION SUIT
  JSON-Driven UI Test Suite for PKS Automation Lab
================================================================================

Project Name    : demo_ui_automation_suit
GroupId         : demo_ui_automation_suit
ArtifactId      : demo_ui_automation_suit
Version         : 0.0.1-SNAPSHOT
Java Version    : 1.8
Build Tool      : Maven 3.x
Test Framework  : TestNG 6.14.3
Core Dependency : AutomationUtils (com.pksautomation.automationutils)

Sibling Project : ../automationutils  (must be built before running this suite)


================================================================================
1. PROJECT OVERVIEW
================================================================================

This project is a data-driven UI automation test suite for the PKS Automation
Lab web platform. Tests are defined declaratively in JSON files and executed
at runtime using Java reflection via the shared AutomationUtils framework.

The suite validates:
  - Login / Logout flows
  - User Admin (create, search, deactivate, delete users)
  - Home page app launcher (app count and names)
  - DEM (Data Exchange Manager) client management
  - FaaS metadata deployment

Design philosophy:
  - Separate WHAT to test (JSON scenarios) from HOW to interact (Page Objects)
  - Non-technical stakeholders can review scenario JSON without reading Java
  - Page Object methods are reusable building blocks across multiple scenarios
  - Centralized locators in JSON files (no hard-coded XPaths in Java)


================================================================================
2. ARCHITECTURE DESIGN
================================================================================

  Flow diagrams (end-to-end, reflection, login, TestNG lifecycle):
    See Section 2.6

2.1 HIGH-LEVEL ARCHITECTURE
---------------------------

  +---------------------------+       +---------------------------+
  |   Test Orchestration      |       |   Test Definition Layer   |
  |                           |       |                           |
  |  testng.xml               |       |  ScenarioDetails.xlsx     |
  |  ScenariosRunner (TestNG) |<----->|  ScenariosFiles/*.json    |
  |  TestBase lifecycle hooks |       |  ScenarioTestData.json    |
  +-------------+-------------+       +-------------+-------------+
                |                                   |
                v                                   v
  +---------------------------+       +---------------------------+
  |   AutomationUtils (JAR)   |       |   Page Object Layer       |
  |                           |       |                           |
  |  TestScenarioExecuter     |-----> |  LoginPage, HomePage      |
  |  Config / ConfigSingleton |       |  CreateUserPage, DemPage  |
  |  BasePage, BrowserUtils   |       |  FaaSPage, MyApplication  |
  |  LoggerUtils, Assertions  |       |  PageLocatorsFile/*.json  |
  +-------------+-------------+       +---------------------------+
                |
                v
  +---------------------------+
  |   Browser Layer           |
  |  Selenium WebDriver       |
  |  Healenium (self-healing) |
  |  WebDriverManager         |
  +-------------+-------------+
                |
                v
  +---------------------------+
  |   Target Application      |
  |  PKS Automation Lab       |
  +---------------------------+


2.2 LAYERED DESIGN
------------------

  Layer 1 - Test Runner
    testng.xml defines the TestNG suite.
    ScenariosRunner extends TestBase and is the single entry point.
    Excel file (ScenarioDetails.xlsx) controls which scenarios run.

  Layer 2 - Scenario Engine (AutomationUtils)
    TestScenarioExecuter reads JSON, loads POM classes via reflection,
    and invokes public no-arg step methods in sequence.

  Layer 3 - Page Object Model (this project)
    com.pksautomation.uidemo.pom.* classes extend BasePage.
    Each page class encapsulates UI actions for one application screen.
    Locators are externalized to JSON files, not embedded in Java.

  Layer 4 - Test Data
    ScenarioTestData.json holds named datasets (Credential, CreateClient, etc.)
    Referenced by the "TestData" field in scenario step definitions.

  Layer 5 - Configuration
    config.properties drives browser, environment URL, timeouts, reporting.


2.3 EXECUTION FLOW
------------------

  1. Maven Surefire loads testng.xml
  2. TestBase.@BeforeTest  -> starts ExtentReports
  3. TestBase data provider "ScenariosRunner" reads ScenarioDetails.xlsx
     and returns enabled scenario rows (testScenarioID, description)
  4. For each scenario:
       a. TestBase.@BeforeMethod -> creates ThreadLocal Config + ExtentTest
       b. ScenariosRunner.scenarioRunner() opens browser
       c. Loads src/test/resources/TestData/ScenariosFiles/{testScenarioID}.json
       d. TestScenarioExecuter iterates Steps array:
            - Sets runtime property TestDataName (if TestData field is set)
            - Class.forName(Package + "." + ClassName)
            - Caches page object instance per class
            - Invokes MethodName() via reflection
       e. TestBase.@AfterMethod -> screenshot, logs, quit browser, flush report
  5. Reports written to ExtendReport/ and test-output/


2.4 DATA FLOW DIAGRAM
---------------------

  ScenarioDetails.xlsx
        |
        |  (testScenarioID = testScenarioID1)
        v
  testScenarioID1.json  ----->  Steps[].ClassName = LoginPage
        |                       Steps[].MethodName = _clickOnPksAutomationLabLoginButton
        |                       Steps[].TestData   = Credential
        v
  ScenarioTestData.json ----->  Credential.Email = "user@example.com"
        |                       Credential.Password = "****"
        v
  LoginPage.json        ----->  PksAutomationLabLoginArrow.Value = "//div[text()='PKS Automation Lab']"
        |                       Email.Value = "//input[@type='email']"
        v
  LoginPage._clickOnPksAutomationLabLoginButton()
        -> clickOnButton("PksAutomationLabLoginArrow")
        -> ElementActionsUtils clicks the PKS Automation Lab login tile
        v
  LoginPage._fillUserCredential()
        -> getTestData("Email")     reads from ScenarioTestData
        -> fillData("Email", value) resolves locator from LoginPage.json
        -> ElementActionsUtils fills the WebElement


2.5 KEY DESIGN PATTERNS
------------------------

  Page Object Model (POM)
    Each screen has a dedicated Java class. UI interaction logic lives in
    page methods; locators live in companion JSON files.

  Data-Driven Testing
    Scenario steps and input data are externalized to JSON/Excel.
    Same page method can be reused with different TestData datasets.

  Reflection-Based Step Execution
    TestScenarioExecuter decouples TestNG from individual test methods.
    Adding a new test = adding a JSON file + Excel row (no new Java test method).

  Fluent Interface (Method Chaining)
    Page methods return `this` for readable step sequences in Java,
    e.g. new LoginPage()._clickOnPksAutomationLabLoginButton()
                        ._fillUserCredential()._clickOnSubmitButton().

  ThreadLocal Context (AutomationUtils)
    Config is stored per-thread so parallel execution (when enabled) is safe.

  Singleton Cache (AutomationUtils)
    ConfigSingleton holds global locator cache and shared test data maps.
    TestScenarioExecuter caches one page object instance per class per scenario.


2.6 FLOW DIAGRAMS
-----------------

2.6.1 END-TO-END TEST EXECUTION FLOW
-------------------------------------

                        +------------------+
                        |   mvn clean test  |
                        +--------+---------+
                                 |
                                 v
                        +------------------+
                        |  Maven Surefire   |
                        |  loads testng.xml |
                        +--------+---------+
                                 |
                                 v
              +------------------------------------------+
              |  TestBase.@BeforeTest                   |
              |  Initialize ExtentReports               |
              +--------------------+---------------------+
                                   |
                                   v
              +------------------------------------------+
              |  DataProvider "ScenariosRunner"           |
              |  Read ScenarioDetails.xlsx                |
              |  Filter rows where testEnabled = Yes      |
              +--------------------+---------------------+
                                   |
                    +--------------+--------------+
                    |  For each enabled scenario   |
                    +--------------+--------------+
                                   |
         +-------------------------+-------------------------+
         |                                                   |
         v                                                   v
+---------------------------+              +---------------------------+
| TestBase.@BeforeMethod    |              |  testData[0] = testScenarioID1
| Create ThreadLocal Config |              |  testData[1] = Scenario Name
| Create ExtentTest node    |              +---------------------------+
+-------------+-------------+
              |
              v
+---------------------------+
| ScenariosRunner           |
| scenarioRunner(testData)  |
+-------------+-------------+
              |
              v
+---------------------------+
| BrowserUtils.openBrowser()|
| Launch Chrome (Healenium) |
+-------------+-------------+
              |
              v
+---------------------------+
| Load scenario JSON file   |
| ScenariosFiles/{id}.json  |
+-------------+-------------+
              |
              v
+---------------------------+
| TestScenarioExecuter      |
| executeScenarioFromJson   |
| (see diagram 2.6.2)       |
+-------------+-------------+
              |
              v
+---------------------------+
| TestBase.@AfterMethod     |
| Screenshot on failure     |
| Attach logs to report     |
| BrowserUtils.quitBrowser()|
| Flush ExtentReports       |
+-------------+-------------+
              |
              v
+---------------------------+
| Reports generated         |
| ExtendReport/testReport   |
| test-output/              |
+---------------------------+


2.6.2 REFLECTION-BASED STEP EXECUTION FLOW
-------------------------------------------

  testScenarioID1.json
         |
         |  Parse JSON: Package, FeatureName, Steps[]
         v
  +---------------------------+
  | TestScenarioExecuter      |
  | executeScenarioFromJson   |
  +-------------+-------------+
                |
                |  Set Extent category = FeatureName
                v
        +-------+-------+
        |  For each Step |
        +-------+-------+
                |
                v
     +----------------------+
     | TestData non-empty?  |
     +----------+-----------+
           Yes  |      No
                |       |
                v       |
     +----------------------+     |
     | putRunTimeProperty   |     |
     | ("TestDataName",     |     |
     |  testData value)     |     |
     +----------+-----------+     |
                |                 |
                +--------+--------+
                         |
                         v
              +----------------------+
              | Class.forName(       |
              |  Package.ClassName)  |
              +----------+-----------+
                         |
                         v
              +----------------------+
              | Instance in cache?   |
              +----+-------------+-----+
                No |             | Yes
                   v             |
         +----------------+      |
         | newInstance()  |      |
         | (calls super() |      |
         |  -> BasePage)  |      |
         +-------+--------+      |
                 |               |
                 +-------+-------+
                         |
                         v
              +----------------------+
              | getMethod(           |
              |  MethodName)         |
              | .invoke(instance)    |
              +----------+-----------+
                         |
              +----------+----------+
              | Success?            |
              +----+-----------+----+
                Yes|           |No
                   |           v
                   |  +------------------+
                   |  | LoggerUtils      |
                   |  | logException()   |
                   |  | BREAK step loop  |
                   |  +------------------+
                   |
                   v
         +---------+---------+
         | More steps?       |
         +----+---------+----+
           Yes|         |No
              |         |
              |         v
              |  +------------------+
              |  | Scenario complete|
              |  +------------------+
              |
              +----> (next Step)


2.6.3 LOGIN SCENARIO FLOW (testScenarioID1)
--------------------------------------------

  [ScenarioDetails.xlsx]
  testEnabled=Yes | testScenarioID1 | Successful Login
         |
         v
  [testScenarioID1.json]                    [config.properties]
         |                                  Environment URL
         v                                  Browser=chrome
  +------+------+------+------+------+
  | Step 1 | 2  | 3  | 4  | 5  |
  +--------+----+----+----+----+
       |    |    |    |    |
       |    |    |    |    +----------------------------+
       |    |    |    |                                 |
       |    |    |    +------------------+              |
       |    |    |                       |              |
       |    |    +-----------+           |              |
       |    |                |           |              |
       v    v                v           v              v

  Step 1: _navigate_to_the_login_page
  +------------------------------------------+
  | BrowserUtils.navigateToLoginPage()       |
  | -> {Environment}/login                 |
  +------------------------------------------+
                    |
                    v
  Step 2: _clickOnPksAutomationLabLoginButton
  +------------------------------------------+
  | clickOnButton("PksAutomationLabLoginArrow")|
  | Locator: //div[text()='PKS Automation Lab']|
  +------------------------------------------+
                    |
                    v
  Step 3: _fillUserCredential  (TestData=Credential set at Step 1)
  +------------------------------------------+
  | getTestData("Email")    <- ScenarioTestData.json |
  | getTestData("Password") <- ScenarioTestData.json |
  | fillData("Email")       <- LoginPage.json        |
  | fillData("Password")    <- LoginPage.json        |
  +------------------------------------------+
                    |
                    v
  Step 4: _clickOnSubmitButton
  +------------------------------------------+
  | clickOnButton("SignIn")                  |
  | Locator: //button[contains(text(),'Sign in')]|
  +------------------------------------------+
                    |
                    v
  Step 5: _verifyLoginSuccessFull
  +------------------------------------------+
  | isElementDisplayed("Email")              |
  | assertTrue: Email field NOT visible      |
  | (confirms redirect away from login page)  |
  +------------------------------------------+
                    |
                    v
            [PASS / FAIL]
            ExtentReport updated


2.6.4 TESTNG LIFECYCLE FLOW
----------------------------

  Suite Start
      |
      v
  @BeforeTest  (once)
  startReport() --> Init ExtentReports
      |
      v
  +---+-----------------------------+
  |   For each scenario (test method)|
  +---+-----------------------------+
      |
      v
  @BeforeMethod
  BeforeMethod()
    |-- Create Config (ThreadLocal)
    |-- Set ExtentTest for scenario
    |-- Set dynamic test name
      |
      v
  @Test
  scenarioRunner(testData)
    |-- openBrowser()
    |-- executeScenarioFromJsonFile()
      |
      v
  @AfterMethod
  getResult()
    |-- Attach screenshot (on failure)
    |-- Write test log to Extent
      |
      v
  @AfterMethod
  tearDown()
    |-- quitBrowser()
    |-- Flush ExtentReports
    |-- Clear ThreadLocal Config
      |
      v
  TestListener.afterInvocation
    |-- softAssert.assertAll()
      |
      v
  (next scenario or Suite End)


2.6.5 COMPONENT INTERACTION FLOW
---------------------------------

  ScenariosRunner
        |
        | extends
        v
     TestBase ---------------------------> TestListener (TestNG events)
        |
        | uses
        v
     Config (ThreadLocal)
        |
        +--------> UtilityObjectManager
        |               |
        |               +---> BrowserUtils -----> SelfHealingDriver -----> Chrome
        |               +---> WaitHelper
        |               +---> TestDataHelper -----> ScenarioTestData.json
        |               +---> AssertionUtils
        |               +---> ElementActionsUtils
        |
        +--------> TestScenarioExecuter
        |               |
        |               +---> LoginPage (extends BasePage)
        |               +---> HomePage  (extends BasePage)
        |               +---> DemPage   (extends BasePage)
        |                         |
        |                         +---> PageLocatorHelper -----> LoginPage.json
        |                         +---> LoggerUtils -----> ExtentReports
        |
        +--------> ConfigSingleton (global locator cache, test data maps)


================================================================================
3. DIRECTORY STRUCTURE
================================================================================

demo_ui_automation_suit/
|
|-- pom.xml                          Maven build; AutomationUtils dependency
|-- testng.xml                       Root TestNG suite (used by Surefire)
|-- README.txt                       This file
|
|-- src/
|   |-- main/java/com/pksautomation/uidemo/
|   |   |-- constants/
|   |   |   |-- AppConstant.java     Application-level static constants
|   |   |   +-- Constant.java        Shared runtime state (API URLs, tokens)
|   |   |-- enums/
|   |   |   |-- APICategory.java     API operation categories
|   |   |   +-- ApprovalQuestionType.java
|   |   +-- pom/                     Page Object Model classes
|   |       |-- LoginPage.java       Login / logout flows
|   |       |-- HomePage.java        Home page app validation
|   |       |-- MyApplicationPage.java  Admin user navigation
|   |       |-- CreateUserPage.java  User creation and management
|   |       |-- DemPage.java         DEM client operations
|   |       |-- FaaSPage.java        FaaS metadata deployment
|   |       +-- CommonUtils.java     Shared page utilities
|   |
|   +-- test/
|       |-- java/testrunner/
|       |   +-- ScenariosRunner.java TestNG entry point
|       +-- resources/
|           |-- testng.xml             Duplicate suite definition
|           |-- config/
|           |   +-- config.properties  Runtime configuration
|           |-- healenium.properties   Self-healing locator settings
|           +-- TestData/
|               |-- ScenariosFiles/    One JSON per scenario (testScenarioID*.json)
|               |-- ScneariosTestData/
|               |   +-- ScenarioTestData.json  Named input datasets
|               |-- PageLocatorsFile/  Element locators per page class
|               |-- RequestJson/       API request payloads (optional)
|               +-- ResponseJson/      API response fixtures (optional)
|
|-- ExtendReport/
|   +-- testReport.html              Extent HTML report output
|
+-- test-output/                     TestNG logs and execution reports


================================================================================
4. KEY CLASSES
================================================================================

4.1 ScenariosRunner (test entry point)
---------------------------------------
  Package : testrunner
  Extends : com.pksautomation.utils.v2.testNG.TestBase

  @Test(dataProvider = "ScenariosRunner")
  public void scenarioRunner(String[] testData)
    testData[0] = scenario ID  (e.g. testScenarioID1)
    testData[1] = scenario description

  Opens browser, loads JSON scenario file, delegates to TestScenarioExecuter.


4.2 Page Object Classes (com.pksautomation.uidemo.pom)
-------------------------------------------------------
  All extend com.pksautomation.utils.v2.BasePage.

  Class               Responsibility
  ----------------    --------------------------------------------------
  LoginPage           Navigate to login, click PKS Automation Lab tile,
                      fill credentials, sign in/out
  HomePage            Validate app tiles on home page
  MyApplicationPage   Navigate to /admin/users
  CreateUserPage      Create, search, deactivate, delete users
  DemPage             DEM client CRUD, installer, notifications
  FaaSPage            FaaS metadata deployment
  CommonUtils         Shared utility placeholder

  Page method naming convention: prefix with underscore (_)
  Example: _navigate_to_the_login_page(), _clickOnPksAutomationLabLoginButton(),
           _fillUserCredential()

  Methods must be public and no-arg to be invokable by reflection.


4.3 Constants and Enums
------------------------
  AppConstant         Static application metadata maps
  Constant            Runtime shared state for API/integration flows
  APICategory         Typed API operation categories
  ApprovalQuestionType Security and data-policy question types


================================================================================
5. TEST DATA STRUCTURE
================================================================================

5.1 Scenario Registry (Excel) - REQUIRED
-----------------------------------------
  File   : src/test/resources/TestData/ScenarioDetails.xlsx
  Sheet  : ScenarioData

  Columns:
    testEnabled      Yes / No  (only Yes rows are executed)
    testScenarioID   e.g. testScenarioID1
    testScenarioName e.g. Successful Login

  NOTE: This file must exist for tests to run. Create it if missing.


5.2 Scenario Definition (JSON)
-------------------------------
  Location: src/test/resources/TestData/ScenariosFiles/testScenarioID{n}.json

  {
    "TestCaseId":    "testScenarioID1",
    "Description":   "Human-readable description",
    "ZephyrScaleId": "JIRA-101",
    "FeatureName":   "Successful_Login",
    "Priority":      "High",
    "Package":       "com.pksautomation.uidemo.pom",
    "Steps": [
      {
        "ClassName":  "LoginPage",
        "MethodName": "_navigate_to_the_login_page",
        "TestData":   "Credential"
      },
      {
        "ClassName":  "LoginPage",
        "MethodName": "_clickOnPksAutomationLabLoginButton",
        "TestData":   ""
      },
      {
        "ClassName":  "LoginPage",
        "MethodName": "_fillUserCredential",
        "TestData":   ""
      }
    ]
  }

  Field reference:
    Package    Java package prefix for page object classes
    ClassName  Page object class name (without package)
    MethodName Public no-arg method to invoke on the page object
    TestData   Named dataset key from ScenarioTestData.json (empty = none)


5.3 Input Data (JSON)
----------------------
  File: src/test/resources/TestData/ScneariosTestData/ScenarioTestData.json

  Named datasets referenced by the TestData field in scenario steps:
    Credential, InvalidCredential
    UserCreationFNameMandatoryFieldValidation, UserCreationWithAllField
    CreateClient, ClientValidation, Metadata, SearchData

  Page methods call getTestData("fieldName") to read values from the
  active dataset (set via runtime property TestDataName).


5.4 Page Locators (JSON)
-------------------------
  Location: src/test/resources/TestData/PageLocatorsFile/{PageClassName}.json

  Each key maps to a UI element:
  {
    "PksAutomationLabLoginArrow": {
      "Strategy":    "xpath",
      "Value":       "//div[text()='PKS Automation Lab']",
      "Description": "PKS Automation Lab arrow icon on Login Home Page",
      "type":        "button"
    },
    "Email": {
      "Strategy":    "xpath",
      "Value":       "//input[@type='email']",
      "Description": "Email input field",
      "type":        "input"
    }
  }

  Loaded automatically by BasePage constructor via PageLocatorHelper.


================================================================================
6. CONFIGURATION
================================================================================

  File: src/test/resources/config/config.properties

  Key Property          Purpose
  ----------------      ------------------------------------------
  Browser               Browser type (default: chrome)
  Environment           Base application URL
  Email / Password      Default login credentials
  isHeadlessMode        Run browser headless (true/false)
  ObjectWaitTime        Element wait timeout in seconds (default: 20)
  TestDataJSONPath      Path to ScenarioTestData.json folder
  PageLocatorFilePath   Path to PageLocatorsFile folder
  ExtentReportEnable    Enable ExtentReports HTML output
  ResultsDir            Report output directory
  proxyhost/proxyport   Corporate proxy settings (if required)

  Override at runtime:
    mvn test -DBrowser=firefox -DisHeadlessMode=true


================================================================================
7. DEPENDENCY ON AUTOMATIONUTILS
================================================================================

  Maven coordinates:
    groupId    : com.pksautomation.automationutils
    artifactId : AutomationUtils
    version    : 0.0.1-SNAPSHOT

  Integration mechanism:
    During Maven validate phase, the built JAR and its POM are installed
    from the sibling project into the local Maven repository:

      ../automationutils/target/AutomationUtils-0.0.1-SNAPSHOT.jar
      ../automationutils/pom.xml

    This ensures all transitive dependencies (Selenium, RestAssured, etc.)
    are resolved automatically.

  Framework classes used from AutomationUtils:
    TestBase              TestNG lifecycle, data providers, reporting setup
    TestScenarioExecuter  JSON reflection scenario engine
    Config                ThreadLocal runtime context and properties
    BasePage              Parent of all page objects
    BrowserUtils          Browser open/close, navigation, screenshots
    ElementActionsUtils   Click, fill, dropdown actions
    AssertionUtils        Hard and soft assertions
    LoggerUtils           Step logging and Extent attachments
    PageLocatorHelper     JSON locator loading
    TestDataHelper        Named dataset resolution
    TestListener          TestNG failure/success handling

  See ../automationutils/README.txt for full AutomationUtils architecture.


================================================================================
8. BUILD AND RUN
================================================================================

  Prerequisites:
    1. JDK 8 installed
    2. Maven 3.x installed
    3. AutomationUtils built (see Section 9)
    4. ScenarioDetails.xlsx created with enabled scenarios
    5. config.properties updated (Environment URL, credentials)
    6. Chrome browser installed (or configured browser)

  Build AutomationUtils first:
    cd ../automationutils
    mvn clean install

  Compile this project:
    cd ../demo_ui_automation_suit
    mvn clean compile test-compile

  Run all enabled scenarios:
    mvn clean test

  Run with overrides:
    mvn clean test -DBrowser=chrome -DisHeadlessMode=false

  Compile only (skip tests):
    mvn clean compile -DskipTests

  Reports after execution:
    Extent HTML : ExtendReport/testReport.html
    TestNG      : test-output/
    Per-test log: test-output/Report/ReportsLogs/


================================================================================
9. SETUP CHECKLIST (NEW MACHINE)
================================================================================

  [ ] Clone demo_ui_automation_suit and sibling automationutils project
  [ ] Build AutomationUtils:  cd ../automationutils && mvn clean install
  [ ] Edit config.properties: set Environment, Email, Password, proxy if needed
  [ ] Create ScenarioDetails.xlsx with enabled scenario rows
  [ ] Verify scenario JSON Package field = com.pksautomation.uidemo.pom
  [ ] Run: mvn clean test
  [ ] Open ExtendReport/testReport.html to review results


================================================================================
10. SCENARIO COVERAGE
================================================================================

  Scenario ID       Feature Area              Page Objects
  ----------------  ------------------------  ---------------------------
  testScenarioID1   Successful Login          LoginPage
  testScenarioID2   Invalid Login             LoginPage
  testScenarioID3   Logout                    LoginPage
  testScenarioID4   User Creation Validation  LoginPage, CreateUserPage
  testScenarioID5   Full User Creation        LoginPage, MyApplicationPage,
                                              CreateUserPage
  testScenarioID6+  Home Page, DEM, FaaS      HomePage, DemPage, FaaSPage

  NOTE: Scenarios 6-21 may still reference the old package
        com.innovaccer.applm.pom in their JSON files.
        Update the "Package" field to com.pksautomation.uidemo.pom
        before running those scenarios.

  Login flow note:
    All login scenarios use _clickOnPksAutomationLabLoginButton to select
    the "PKS Automation Lab" tile on the login home page before entering
    credentials. The locator is defined as PksAutomationLabLoginArrow in
    PageLocatorsFile/LoginPage.json.


================================================================================
11. ADDING A NEW TEST SCENARIO
================================================================================

  Step 1: Add page methods (if needed)
    Add public no-arg methods to the appropriate POM class in
    src/main/java/com/pksautomation/uidemo/pom/

  Step 2: Add locators (if needed)
    Add element entries to the corresponding JSON file in
    src/test/resources/TestData/PageLocatorsFile/

  Step 3: Add test data (if needed)
    Add a named dataset to ScenarioTestData.json

  Step 4: Create scenario JSON
    Create src/test/resources/TestData/ScenariosFiles/testScenarioID{n}.json
    with Steps referencing your page methods

  Step 5: Register in Excel
    Add a row to ScenarioDetails.xlsx with testEnabled=Yes

  Step 6: Run
    mvn clean test


================================================================================
  END OF README
================================================================================
