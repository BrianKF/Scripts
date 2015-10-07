package com.adtheorent.Testing;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

@SuppressWarnings("deprecation")
public class DeviceTypeAndroid {
        private Selenium selenium;

        @Before
        public void setUp() throws Exception {
                selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://uatplatformv2.adtheorent.com/");
                selenium.start();
        }
        
        @Test
        public void testDeviceTypeAndroid() throws Exception {
                selenium.open("/");
                selenium.type("id=txtLoginID", "Brian.Frazier@adtheorent.com");
                selenium.type("id=txtPassword", "AdTheorent155");
                selenium.click("//button[@type='submit']");
                selenium.waitForPageToLoad("30000");
                selenium.open("http://uatplatformv2.adtheorent.com/Campaign");
                Thread.sleep(8000);
                selenium.click("link=QATesting");
                selenium.waitForPageToLoad("30000");
                Thread.sleep(2000);
                selenium.click("link=QATesting");
                selenium.waitForPageToLoad("30000");
                Thread.sleep(2000);
                selenium.click("link=QATestStrategy");
                selenium.waitForPageToLoad("30000");
                Thread.sleep(8000);
                selenium.click("id=spandevice");
                Thread.sleep(2000);
                selenium.click("id=Android_SmartPhone");
                Thread.sleep(2000);
                selenium.click("xpath=(//button[@type='button'])[39]");
                selenium.click("id=btnCloseModalReview");
                selenium.click("css=#dvModalRTDM > div.modal-dialog.modal-lg > div.modal-content > div.modal-header > button.close");
                selenium.click("css=#divModalExchange > div.modal-dialog > div.modal-content > div.modal-header > button.close.");
                selenium.click("css=#divModalContend > div.modal-dialog.modal-lg > div.modal-content > div.modal-header > button.close.");
                selenium.click("css=#divModalGeography > div.modal-dialog.modal-lg > div.modal-content > div.modal-header > button.close.");
                selenium.click("css=#divModalAudience > div.modal-dialog.modal-lg > div.modal-content > div.modal-header > button.close.");
                selenium.click("css=#divModalWeather > div.modal-dialog.modal-lg > div.modal-content > div.modal-header > button.close.");
                selenium.click("css=button.close.ModalClose");
                selenium.click("css=#divModalDayParting > div.modal-dialog.modal-lg > div.modal-content > div.modal-header > button.close.");
                selenium.click("css=#divModalCreative > div.modal-dialog > div.modal-content > div.modal-header > button.close.ModalClose");
                selenium.click("css=div.modal-content.smlPopup > div.modal-header > button.close");
                selenium.waitForPageToLoad("30000");
                //verifyEquals("All android smartphone", selenium.getText("id=spandevice"));
        }

        @After
        public void tearDown() throws Exception {
                selenium.stop();
        }
}