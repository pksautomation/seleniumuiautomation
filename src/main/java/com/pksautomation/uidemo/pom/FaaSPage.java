package com.pksautomation.uidemo.pom;

import com.pksautomation.utils.v2.BasePage;

public class FaaSPage extends BasePage {

    String clientId;

    public FaaSPage() {
        super();
    }

    public void _navigateToFaaSMetadataPage() {
    	String url=getScenarioContext().getRunTimeProperty("Environment")+"/faas/metadata";
    	navigateToURL(url);

    }

    public void _addMetadata() {
    	clickOnButton("Metadata");
    	String data = getTestData("Name");
    	fillData("MetadataName",data);
    	clickOnButton("SelectMetadata");
    	selectDropDown("Version");
    	clickOnButton("MetadataDeploy");
    }

    public void _isMetadataDeploymentAccepted() {
    	if(isElementDisplayed("MetadataAccepted")) {
    		assertTrue("Metadata deployment is accepted",true, false);
    	}
    	else
    		assertFail("Metadata deployment is not accepted",false, true);
    }


}
