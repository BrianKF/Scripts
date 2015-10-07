package com.adtheorent.Testing;
// Brian Frazier - AdTheorent 09/30/2015
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ResetDeviceTargeting {

	public static void main(String[] args) throws InterruptedException {

		WebDriver driver = new FirefoxDriver();
        	System.out.println("Launching ResetDeviceTargeting Script");
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
          
            Thread.sleep(5000);
            
            // Check to see if any Devices or OS versions are set
            System.out.println("Executing Device Targeting testing.");
            System.out.println("Now Setting all Device items to OFF");
            System.out.println("");
            driver.findElement(By.xpath(".//*[@id='show_device_detailed']")).click();
            
            WebElement SelectAll1;
            WebElement SelectAll2;
            WebElement TargetOS;

            SelectAll1 = driver.findElement(By.xpath(".//*[@id='chkallDevice']"));
            SelectAll2 = driver.findElement(By.xpath(".//*[@id='chkallOs']"));
            TargetOS = driver.findElement(By.xpath(".//*[@id='chkTargetOs']"));
            
            if(!SelectAll1.isSelected()){
            	SelectAll1.click();
            }
            if(!SelectAll2.isSelected()){
            	SelectAll2.click();
            }
            if(!TargetOS.isSelected()){
                TargetOS.click();
            }
            
            driver.findElement(By.xpath(".//*[@id='chkallDevice']")).click();
            driver.findElement(By.xpath(".//*[@id='chkallOs']")).click();
            driver.findElement(By.xpath(".//*[@id='chkTargetOs']")).click();
            System.out.println("Done!");
            System.out.println("");
            driver.findElement(By.xpath(".//*[@id='divModalDevice']/div/div/div[2]/div[2]/button[1]")).click();
          
            WebElement element = driver.findElement(By.id("spandevice"));
            String strng = element.getText();
            System.out.println(strng);
            Assert.assertEquals("None", strng);
     
            
            Thread.sleep(2000);
            driver.quit();
           }
            
	
	}

