package com.adtheorent.testing;

import com.thoughtworks.selenium.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

public class pokingAroundTest extends SeleneseTestCase {
		@Before
		public void setUp() throws Exception {
			selenium = new DefaultSelenium("localhost", 4444, "*googlechrome", "http://uatplatform.adtheorent.com/");
			selenium.start();
}

		@Test
		public void testpokingAroundTest() throws Exception {
			selenium.open("/login.aspx");
			selenium.type("id=name", "Brian.Frazier@adtheorent.com");
			selenium.type("id=password", "AdTheorent155");
			selenium.click("id=lkbLogin");
			selenium.waitForPageToLoad("30000");
			selenium.click("link=Media Provider");
			selenium.waitForPageToLoad("30000");
			selenium.click("link=Campaign");
			selenium.waitForPageToLoad("30000");
			selenium.click("link=Creative Library");
			selenium.waitForPageToLoad("30000");
			selenium.click("link=Campaign");
			selenium.waitForPageToLoad("30000");
			selenium.click("id=ContentPlaceHolder1_gvGrid_lblName_0");
			selenium.waitForPageToLoad("30000");
			selenium.click("id=ContentPlaceHolder1_lnkbtnTemperatureTarget");
			selenium.waitForPageToLoad("30000");
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