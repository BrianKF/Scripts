package myPackage;
 
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

 
public class myClass {
 
    public static void main(String[] args) {
        // declaration and instantiation of objects/variables
        WebDriver driver = new FirefoxDriver();
        String baseUrl = "http://uatplatform.adtheorent.com/";
        String expectedTitle = "AdTheorentMedia - Mobile AD DSP";
        String actualTitle = "";
 
        // launch Firefox and direct it to the Base URL
        driver.get(baseUrl);
 
        // get the actual value of the title
        actualTitle = driver.getTitle();
        System.out.println(actualTitle);
        /*
         * compare the actual title of the page witht the expected one and print
         * the result as "Passed" or "Failed"
         */
        if (actualTitle.contentEquals(expectedTitle)){
            System.out.println("Test Passed!");
        } else {
            System.out.println("Test Failed.  Titles do not match");
        }
        
        //close Firefox
        driver.close();
        
        // exit the program explicitly
        System.exit(0);
    }
 
}