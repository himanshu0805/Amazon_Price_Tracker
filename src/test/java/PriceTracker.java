import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class PriceTracker {

	public static void main(String[] args) {

		String SearchKeyword = "Laptop";

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		driver.get("https://www.amazon.in/");
		
		WebElement search = driver.findElement(By.xpath("//input[contains(@id,'twotabsearchtextbox')]"));
		search.sendKeys(SearchKeyword, Keys.ENTER);
		
		List<WebElement> item_link = driver.findElements(By.xpath(
				"//a[contains(@class,'a-link-normal ')]/ancestor::h2/a"));
		
		List<WebElement> item_link_Title = driver.findElements(By.xpath(
				"//a[contains(@class,'a-link-normal ')]/ancestor::h2/a/span"));
		
		List<WebElement> item_link_Price = driver.findElements(By.xpath(
				"//a[contains(@class,'a-link-normal ')]/ancestor::h2/a/following::span[@class='a-price-whole']"));

		for(int i=0 ; i < item_link.size();i++) {
			System.out.println("Item "+i);
			System.out.println("item Links:- "+item_link.get(i).getAttribute("href"));
			System.out.println("item Title:- "+item_link_Title.get(i).getText());
			System.out.println("item Price:- "+item_link_Price.get(i).getText());
			
			System.out.println("\n\n");
		}
		System.out.println("end loop");
		driver.quit();
		
	}

}
