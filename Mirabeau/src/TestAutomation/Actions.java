package TestAutomation;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.sikuli.script.Finder;
import org.sikuli.script.Pattern;
import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Actions {
	
	FirefoxDriver Mozila = new FirefoxDriver();
	String NrOfResults;
	
	@Test	
	public void OpenBrowser() {
		Mozila.manage().window().maximize();;
		Mozila.navigate().to("http://automationpractice.com/");
		Mozila.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (Mozila.findElement(By.xpath("/html/body/div/div[1]/header/div[3]/div/div/div[1]/a/img")).isDisplayed()) {
			Reporter.log("Website opened successfully");
		}
		else {
			Reporter.log("Website didn't opened");
		}			
	}
	
	@Test (dependsOnMethods = {"OpenBrowser"} , priority = 1)
	public void VisibleSearchBox() throws InterruptedException {		
		String ImageLocation = System.getProperty("user.home") + "\\Documents\\Search.PNG";
		Pattern SearchPicture = new Pattern(ImageLocation);
		
		Finder FindSearchBox = new Finder(SearchPicture.getImage());
		FindSearchBox.find(SearchPicture);
		
		if (FindSearchBox.hasNext() && Mozila.findElement(By.xpath("//*[@id=\"search_query_top\"]")).isDisplayed()) {
			Reporter.log("Search box is visible and has the world \"Search\" in it.");
		}
		else {
			Reporter.log("Search box is not visible");
		}
	}

	@Test (dependsOnMethods = {"OpenBrowser"} , priority = 2)
	@Parameters ({"SearchInformation"})
	public void Search(String SearchInfo) throws InterruptedException {
		Mozila.findElement(By.xpath("//*[@id=\"search_query_top\"]")).clear();
		Thread.sleep(500);
		Mozila.findElement(By.xpath("//*[@id=\"search_query_top\"]")).sendKeys(SearchInfo);
		Mozila.findElement(By.xpath("/html/body/div/div[1]/header/div[3]/div/div/div[2]/form/button")).click();
		Thread.sleep(2000);
		
		try {
			Mozila.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div[2]/p"));
			NrOfResults = Mozila.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div[2]/ul")).getAttribute("childElementCount");
			Reporter.log("The entered search information has returned "+ NrOfResults +" results");
		}
		catch (Exception e) {
			Reporter.log("No results were found for your search "+ SearchInfo);
		}
	}
}
