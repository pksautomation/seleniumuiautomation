package com.innovaccer.applm.enums;

public enum APICategory {

	LoginAPI(1),CreateUserAPI(2),SetACLForUSerAPI(3),SetPermissionForUSerAPI(4),GetUserTypeAPI(5),GetOrgTypeAPI(6),GetUserInfoAPI(7),GetRolesAPI(8),
	GetPermissionsAPIForGivenRoleAPI(9),GetACLInfoForPatientAPI(10),GetPermissionsForGivenUser(11),GetUserInfoForGivenUser(12),
	DeleteUserAPI(13),CreateCustomeAPI(14),CreateModuleAPI(15),CreateSubModuleAPI(16),CreateTenantAPI(17),CreatePermissionsAPI(18),CreateRoutesAPI(19),
	PatchCustomerPermissionsAPI(20),RegisterTenantAPI(21), CreateAdminUserAPI(22), AssignRoleToAdminAPI(23),CreateCustomeReq(24), CreateModuleReq(25),
	CreateSubModuleReq(26), CreateTenantReq(27), CreatePermissionsReq(28), CreateRoutesReq(29),RegisterTenantReq(30), CreateAdminUserReq(31), PatchTenantPermissionsAPI(32),
	UpdateLicenseAPI(33),CreateApplication(34),ApplicationStatus(35),editApplication(36),GetApplicationCredential(37),AclStrategies(38), AccessToken(39),AppInfo(40),CreateApplicationDraft(41);
	
	private int value;
	APICategory(int val){
		this.value = val;
	}
	public int getValue(){
		return value;
	}
	
}


