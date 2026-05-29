package com.innovaccer.applm.pom;

import com.innovaccer.utils.v2.BasePage;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DemPage extends BasePage {

    String clientId;

    public DemPage() {
        super();
    }

    public DemPage _navigateToDemScreen() {
        String url = getScenarioContext().getRunTimeProperty("Environment") + "/dem/";
        navigateToURL(url);
        return this;
    }

    public DemPage _navigateToDemSavedClientPage() {
        String url = getScenarioContext().getRunTimeProperty("Environment") +
                "/dem/client/c1463937-c32b-450e-ab70-68577329cb3a";
        navigateToURL(url);
        return this;
    }

    public DemPage _clickOnCreateClientButton() {
        clickOnButton("Create Client");
        return this;
    }

    public DemPage _fillClientNameInTextBox() {
        fillData("ClientName");
        return this;
    }

    public DemPage _selectDatabaseFromDropdown() {
        selectDropDown("Database Dropdown");
        clickOnButton("Database Dropdown");
        return this;
    }

    public DemPage _clickOnCreateClientButtonOnOverlay() {
        clickOnButton("Create Client Button");
        return this;
    }

    public DemPage _validateThatClientIsCreatedSuccessfully() {
        clientId = getBrowserUtils().getCurrentUrl().substring(
                getBrowserUtils().getCurrentUrl().lastIndexOf('/') + 1);
        logComment("Client Id in URL: " + clientId);
        return this;
    }

    public void _validateClientsHeadingLabel() {
        boolean status = getActions().isElementDisplayed("Clients Label");
        assertTrue("Clients Label", status, true);
    }

    public void _clickOnDemInstallerOption() {
        getActions().clickOnButton("DEM Installer");
    }

    public void _validateUrlForDemInstallerPage() {
        String url = getBrowserUtils().getCurrentUrl();
        assertTrue("DEM Installer URL",
                url.endsWith("/dem/installer"), true);
    }

    public void _validateDownloadButtonIsPresent() {
        boolean status = isElementDisplayed("Download Button");
        assertTrue("Download Button", status, true);
    }

    public void _validateClientNavBarOptions() {
        List<WebElement> webElements = getActions().getWebElements("Client Details Nav Bar");
        assertEquals("Client Details Nav Bar Count", String.valueOf(5),
                String.valueOf(webElements.size()), false);
    }

    public void _clickOnEditIcon() {
        clickOnButton("Edit Icon");
    }

    public void _enterDirectoryPath() {
        fillData("Directory Path Input");
    }

    public void _clickOnSaveButton() {
        clickOnButton("Save Button");
    }

    public void _validateUpdatedDirectoryPath() {
        boolean status = isElementDisplayed("Updated Directory Path");
        assertTrue("Updated Directory Path", status, true);
    }

    public void _clickOnNotificationTab() {
        clickOnButton("Notification Tab");
    }

    public void _clickOnAddMoreButton() {
        clickOnButton("Add More");
    }

    public void _enterEmailInInput() {
        fillData("Notification Email");
    }

//    public void _selectInfoColumn() {
//    }

    public void _validateEmailAndColumn() {
        boolean status = getActions().isElementDisplayed("Updated Notification Email");
        assertTrue("Updated Notification Email", status, true);
    }

    public void _enterTextInSearchFilter() {
        fillData("Client Search Filter");
    }

    public void _validateClientUsingSearchFilter() {
        List<WebElement> webElements = getActions().getWebElements("Client Row");
        assertEquals("Clients with Filter", String.valueOf(1),
                String.valueOf(webElements.size()), false);

    }
}