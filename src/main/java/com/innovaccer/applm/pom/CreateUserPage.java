package com.innovaccer.applm.pom;

import com.innovaccer.utils.v2.ElementActionsUtils;

import com.innovaccer.utils.v2.BasePage;

public class CreateUserPage extends BasePage {
	
	public CreateUserPage() {
    }
	
	public CreateUserPage _clickOnNewUserButton() {
		clickOnButton("New user");
		return this;
	}
	
	public CreateUserPage _fillUserDetails() {
		try {
		fillData("First name");
		fillData("Middle name");
		fillData("Last name");
		fillData("Maiden name");
		fillData("Email");
		fillData("Date of birth");
		fillData("Job title");
		fillData("Department");
		fillData("Work phone");
		fillData("Secondary phone");
		selectDropDown("Gender");
		selectDropDown("Primary user type");
		selectDropDown("Secondary user type");
		selectDropDown("User location type");
		selectDropDown("Organization type");
		selectDropDown("Manager-level sponsor");
		selectDropDown("Chapter-level sponsor");
		selectDropDown("Role");
		selectDropDown("Groups");
		}catch(Exception e) {
			logException(e);
		}
		return this;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean _isCreateUserButtonEnable() {
		return isButtonClickable("Create User");
	}
	
    public void _verifyCreateButtonStatus() throws Throwable {
		boolean status = _isCreateUserButtonEnable();
		String buttonStatus = getTestData("CreateButtonStatus");
		if(buttonStatus.equalsIgnoreCase("Clickable"))
			assertTrue(" Clickable of Create User Button ", status, true,true);
		else {
			assertTrue(" Disable of Create User Button ", !status, true,true);
		}
    }
   
    public void _searchUserDetail() {
    	clickOnButton("Search");
    	String data = getTestData("Email");
    	fillData("Search",data);
    }
    
    public void _verifyUserExists() {
    	if(isElementDisplayed("Email")) {
    		logPass("User already exists", false);
    		assertTrue("User already exists", true,true);
    		
    	}
    	else
    		assertFail("No user found",false,true);
    }
    
    public void _clickOnCreateUserButton() {
    	clickOnButton("CreateUser");
    }
    
    public void _verifyUserCreation() {
    	if(isElementDisplayed("checkUserExists")) {
    		assertFail("Could not create a new user account, Email Already Exists",false,true);
    	}
    	else
    		assertTrue("New User is created",true,true);
	}
    
    public void _clickOnDeleteUser() {
    	clickOnButton("DeleteUser");
    }
    
    public void _clickOnDeactivateUser() {
    	clickOnButton("DeActivateUser");
    }
}
