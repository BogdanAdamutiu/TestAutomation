package TestAutomation;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.sikuli.script.Finder;
import org.sikuli.script.Pattern;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Actions {
	
	FirefoxDriver Mozila = new FirefoxDriver();
	String NrOfResults;
	String Suggestion;
	String Items;
	int Results;
	int BadResult;
	
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
		Thread.sleep(500);
		
		try {
			Suggestion = Mozila.findElement(By.xpath("/html/body/div[2]/ul/li[1]")).getAttribute("innerText");
			Assert.assertTrue(Suggestion.toLowerCase().contains(SearchInfo.toLowerCase()), "Search suggestion appeared and doesn't contain information about your search!");
			Reporter.log("Search suggestion appeared and contain information about your search.");		
		}
		catch (Exception e) {
			Reporter.log("There were no search suggestion for your search criteria "+ SearchInfo);
		}
		
		Mozila.findElement(By.xpath("/html/body/div/div[1]/header/div[3]/div/div/div[2]/form/button")).click();
		Thread.sleep(1000);
	}
	
	@Test(dependsOnMethods = "Search")
	@Parameters ({"SearchInformation"})
	public void CheckResults(String CheckSearch) {
		try {
			if (Mozila.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div[2]/p")).isDisplayed()) {
				Reporter.log("No results were found when searching for "+ CheckSearch);
				return;
			}			
		}
		catch (Exception e) {
			NrOfResults = Mozila.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div[2]/ul")).getAttribute("childElementCount");
			Results = Integer.parseInt(NrOfResults);
			
			for (int i = 1; i <= Results; i++) {
				Items  = Mozila.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div[2]/ul/li["+ i +"]/div/div[2]/h5/a")).getText();
				if (!Items.toLowerCase().contains(CheckSearch.toLowerCase())) {
					BadResult ++;
				}
			}
			Assert.assertTrue(BadResult == 0, "There are "+ BadResult +" results of the search for "+ CheckSearch +" that have no connection with this search!");
			
			Reporter.log("The search has worked correctly");
		}	
	}
	
	@Test (priority = 4)
	public void CloseBrowser() {
		try {
			Mozila.close();
			Reporter.log("Browser has been closed successfully");
		}
		catch (Exception e) {
			Reporter.log("Browser hasn't been closed successfully");
		}
		
	}
}
