import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.opencsv.CSVWriter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class PriceTracker {

	public static void main(String[] args) throws IOException {

//		String SearchKeyword = "earphone";
//		List<String> UrlList = getItemUrlWithSearch(SearchKeyword);
//		getProductDataFromUrl(UrlList);

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
			if(item_link_Url.get(i).trim().length()==0 && item_link_Url.get(i).trim()=="" ) {
				item_link_Url.remove(i);
			}
			System.out.println("item List value:- "+item_link_Url.get(i));
		}
		
		return item_link_Url;
	}

	public static void getProductDataFromUrl(List<String> UrlList) throws IOException {
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
		String Filepath = new File("Result.csv").getCanonicalPath();
		writeDataLineByLine(Filepath, UrlList, item_Name, item_Price);

		driver.quit();
	}
	
	public static void writeDataLineByLine(String filePath, List<String> Url , List<String> Name, List<String> Price)
	{
		//File path as specified
	    File file = new File(filePath);
	    
	    //creating required file 
	    try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    try {
	    	//File Writer interface 
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);
	  
	        // adding Columns to csv file
	        String[] header = { "Url", "Item_Name", "Item_Price", "Date_Checked" };
	        writer.writeNext(header);
	  
	        //adding data to csv the first Time
	        for(int i=0;i<Url.size();i++) {
	        	// displaying current date and time
	            Calendar cal = Calendar.getInstance();
	            SimpleDateFormat simpleformat = new SimpleDateFormat("dd/MM/yyyy hh:mm:s");
	            System.out.println("Today's date and time = "+simpleformat.format(cal.getTime()));
	            // displaying date
	            Format f = new SimpleDateFormat("MM/dd/yy");
	            String strDate = f.format(new Date());
	            System.out.println("Current Date = "+strDate);
	        	String[] data1 = { Url.get(i), Name.get(i), Price.get(i) , strDate};
		        writer.writeNext(data1);
	        }
	  
	        // closing Interface
	        writer.close();
	    }
	    catch (IOException e) {
	        //checking for any IO Exception
	        e.printStackTrace();
	    }
	}

}
