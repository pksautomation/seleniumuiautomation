package com.innovaccer.applm.pom;

import com.innovaccer.utils.v2.BasePage;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePage extends BasePage {

    List<WebElement> appsOnHomePage = new ArrayList<>();
    List<String> expectedAppNamesOnHomePage = new ArrayList<>();
    List<String> actualAppNamesOnHomePage = new ArrayList<>();

    String[] expectedAppNamesArrayOnHomePage = {"DAP", "Patients", "DEM Installer", "Analytics", "Care Management",
            "Outreach", "Faas", "Studio", "AIMS", "Innote config tool", "Dcaas", "Referrals", "Appstore", "FHIR Apps",
            "User Admin", "Workflow Builder", "Settings", "Audit", "InReport"};

    public HomePage() {
        super();
    }

    public void _compareCountOfOptions() {
        appsOnHomePage = getActions().getWebElements("App List On Home Page");
        getAssertionUtils().assertEquals("App List On Home Page",
                String.valueOf(19), String.valueOf(appsOnHomePage.size()), false);
    }

    public void _compareValuesOfOptions() {
        appsOnHomePage.forEach(appsOnHomePage -> actualAppNamesOnHomePage.add(appsOnHomePage.getText()));
        expectedAppNamesOnHomePage = Arrays.asList(expectedAppNamesArrayOnHomePage);
        getAssertionUtils().assertTrue("App Lists Match",
                actualAppNamesOnHomePage.equals(expectedAppNamesOnHomePage), true);
    }


}
