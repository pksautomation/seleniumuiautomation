package com.pksautomation.uidemo.pom;


import com.pksautomation.utils.v2.BasePage;
import com.pksautomation.utils.v2.cucumber.TestContext;

public class MyApplicationPage extends BasePage {


    public MyApplicationPage() {
    	super();
    }

    public void _goToAdminUserPage() {
    	String url=getScenarioContext().getRunTimeProperty("Environment")+"/admin/users";
    	navigateToURL(url);
    }

}

