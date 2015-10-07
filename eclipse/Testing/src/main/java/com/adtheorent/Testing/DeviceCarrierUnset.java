package com.adtheorent.Testing;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DeviceCarrierUnset {

	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new FirefoxDriver();
    	System.out.println("Launching TabletTargeting Script");
    	System.out.println("Launching Firefox Web Browser");
    	System.out.println("");
        driver.get("http://uatplatformv2.adtheorent.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.findElement(By.xpath(".//*[@id='txtLoginID']")).sendKeys("Brian.Frazier@AdTheorent.com");
        driver.findElement(By.xpath(".//*[@id='txtPassword']")).sendKeys("AdTheorent155");
        driver.findElement(By.xpath(".//*[@id='frmLogin']/p/button")).click();
        driver.findElement(By.xpath("html/body/section/div[1]/div[2]/ul/li[2]/a/span")).click();
        driver.findElement(By.xpath(".//*[@id='tblCampaign']/tbody/tr/td[2]/a")).click();
        driver.findElement(By.xpath(".//*[@id='tblLineItem']/tbody/tr/td[2]/a")).click();
        driver.findElement(By.xpath(".//*[@id='tblStrategy']/tbody/tr[1]/td[2]/a[1]")).click();
        driver.findElement(By.xpath(".//*[@id='spandevice']")).click();
        driver.findElement(By.xpath(".//*[@id='licarrier']/a")).click();
        driver.findElement(By.xpath(".//*[@id='carrier']/div[2]/div[2]/p[3]/a/i")).click();
        driver.findElement(By.xpath(".//*[@id='divModalDevice']/div/div/div[2]/div[2]/button[1]")).click();
        
        Thread.sleep(2000);
        WebElement element = driver.findElement(By.id("spanCarriers"));
        String strng = element.getText();
        System.out.println(strng);
        Assert.assertEquals("None", strng);
        
        Thread.sleep(2000);
        driver.quit();
        
	}
}