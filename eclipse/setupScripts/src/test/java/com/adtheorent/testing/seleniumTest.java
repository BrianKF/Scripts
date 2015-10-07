package com.adtheorent.testing;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;

public class seleniumTest extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*googlechrome", "http://uatplatform.adtheorent.com/");
		selenium.start();
	}

	@Test
	public void testseleniumTes() throws Exception {
		selenium.open("/login.aspx");
		selenium.click("id=name");
		selenium.type("id=name", "Brian.Frazier@adtheorent.com");
		selenium.type("id=password", "AdTheorent155");
		selenium.click("id=lkbLogin");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Campaign");
		selenium.waitForPageToLoad("30000");
		selenium.click("id=ContentPlaceHolder1_gvGrid_lblName_0");
		selenium.waitForPageToLoad("30000");
		selenium.click("id=ContentPlaceHolder1_lnkbtnDemographicTarget");
		selenium.waitForPageToLoad("30000");
		selenium.click("id=rdoGender_3");
		selenium.click("id=ddlMinAge");
		selenium.select("id=ddlMinAge", "label=10");
		selenium.select("id=ddlMaxAge", "label=79");
		selenium.click("id=ContentPlaceHolder1_lnkbtnSave");
		selenium.waitForPageToLoad("30000");
		selenium.click("id=ContentPlaceHolder1_lnkbtnLaunch");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Creative Assignment");
		selenium.waitForPageToLoad("30000");
		selenium.select("id=ddlCampaign", "label=QA_Application_Health_Check");
		selenium.click("//option[@value='9094']");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Campaign");
		selenium.waitForPageToLoad("30000");
		selenium.click("id=ContentPlaceHolder1_gvGrid_lblName_0");
		selenium.waitForPageToLoad("30000");
		selenium.click("id=ContentPlaceHolder1_lnkbtnDemographicTarget");
		selenium.waitForPageToLoad("30000");
		selenium.click("id=rdoGender_4");
		selenium.select("id=ddlMinAge", "label=5");
		selenium.select("id=ddlMaxAge", "label=90");
		selenium.click("id=ContentPlaceHolder1_lnkbtnSave");
		selenium.waitForPageToLoad("30000");
		selenium.click("id=ContentPlaceHolder1_lnkbtnLaunch");
		selenium.waitForPageToLoad("30000");
		selenium.click("id=ucHeader_lnkbtnLogout");
		selenium.waitForPageToLoad("30000");

	}

	@After
	public void tearDown() throws Exception {
	selenium.stop();
	}
}