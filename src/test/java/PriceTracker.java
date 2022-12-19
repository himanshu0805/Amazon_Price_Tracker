import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class PriceTracker {

	public static void main(String[] args) {

		String SearchKeyword = "earphone";

		List<String> UrlList = getItemUrlWithSearch(SearchKeyword);
		getProductDataFromUrl(UrlList);

	}

	public static List<String> getItemUrlWithSearch(String searchKeyword){
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		driver.get("https://www.amazon.in/");
		
		WebElement search = driver.findElement(By.xpath("//input[contains(@id,'twotabsearchtextbox')]"));
		search.sendKeys(searchKeyword, Keys.ENTER);
		
		List<WebElement> item_link = driver.findElements(By.xpath(
				"//a[contains(@class,'a-link-normal ')]/ancestor::h2/a"));
		
		List<String> item_link_Url = new ArrayList<String>();
		
		for(int i=0 ; i < item_link.size();i++) {
			System.out.println("item List extract:- "+item_link.get(i).getAttribute("href"));
			item_link_Url.add(i, item_link.get(i).getAttribute("href"));
		}
		
		for(int i=0 ; i < item_link_Url.size();i++) {
			System.out.println("item List value:- "+item_link_Url.get(i));
		}
		
		return item_link_Url;
	}

	public static void getProductDataFromUrl(List<String> UrlList) {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		
		List<String> item_Name = new ArrayList<String>();
		List<String> item_Price = new ArrayList<String>();
		
		for (int i = 0; i < UrlList.size(); i++) {
			System.out.println("Url value:- " + UrlList.get(i));
			driver.get(UrlList.get(i).toString());
			item_Name.add(driver.findElement(By.xpath("//span[@id='productTitle']")).getText());
			item_Price.add(driver.findElement(By.xpath("//span[@class='a-price-whole']")).getText().replace("\"", ""));
			

		}

		driver.quit();
	}

}
