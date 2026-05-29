package com.innovaccer.applm.pom;


import com.innovaccer.utils.v2.BasePage;

public class LoginPage extends BasePage{
		   
    public LoginPage() {
       super();
       //setPageObjectNameAtScenariosContext("LoginPage");
    }
    
    public LoginPage _clickOnInnovaccerLoginButton() { 
    	clickOnButton("InnovaccerLoginArrow");
	    return this;	    
    }
      
    public LoginPage _fillUserCredential() {
    	fillData("Password");
    	fillData("Email", false);
    	return this;
    }
    
  
    public LoginPage _clickOnSubmitButton() {
    	clickOnButton("SignIn");
    	return this;
    }
    //////////////// mention in base page
	public void _launch_browser() throws Throwable {		
		openBrowser();
	}

    public void _navigate_to_the_login_page() throws Throwable {
    	getBrowserUtils().navigateToLoginPage();
    	
	}
	
    public void login_into_application() throws Throwable  {
		_clickOnInnovaccerLoginButton()._fillUserCredential()._clickOnSubmitButton();
	}
    
    public void _verifyLoginSuccessFull() {
    	boolean status=isElementDisplayed("Email");
    	assertTrue("Email Display", !status, true);
    }
    
    public void _verifyLoginFail() {
    	boolean status=isElementDisplayed("Email");
    	assertTrue("Email Display", status, true);
    }
    
    public void _clickOnCareButton() {
    	clickOnButton("Care");
    	
    }
    
    public void _clickOnLogoutButton() {
    	clickOnButton("LogOut");
    }
    
    public void _verifyLogoutSuccessFull() {
    	String currUrl = getBrowserUtils().getCurrentUrl();
    	if(!currUrl.endsWith("/login"))
    		assertFail("Logout is not successfull",false, true);
    	else
    		assertTrue("Logout successful", true, false);
    }
    
}
