import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class PriceTracker {

	public static void main(String[] args) throws IOException {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Selection:- ");
		System.out.println("1.) To Search for Keyword Warning it will overite the file");
		System.out.println("2.) Update daily price values");
		
		int number = sc.nextInt();

	    // switch statement to check size
	    switch (number) {

	      case 1:
	    	System.out.println("Enter keyword to search");
	    	String SearchKeyword = sc.nextLine();
	  		List<String> UrlList = getItemUrlWithSearch(SearchKeyword);
	  		getProductDataFromUrl(UrlList);
	        break;

	      case 2:
	    	ReadAndUpdateCsvFile("C:\\Users\\Himanshu\\Documents\\Project\\Amazon_Price_Tracker\\Result.csv");
	        break;

	      default:
	    	ReadAndUpdateCsvFile("C:\\Users\\Himanshu\\Documents\\Project\\Amazon_Price_Tracker\\Result.csv");
	        break;

	    }
	}

	public static List<String> getItemUrlWithSearch(String searchKeyword) {
		WebDriverManager.chromedriver().setup();
		ChromeOptions chromeOptions = new ChromeOptions();
	    chromeOptions.addArguments("--remote-allow-origins=*");
	    WebDriver driver = new ChromeDriver(chromeOptions);

		driver.get("https://www.amazon.in/");

		WebElement search = driver.findElement(By.xpath("//input[contains(@id,'twotabsearchtextbox')]"));
		search.sendKeys(searchKeyword, Keys.ENTER);

		List<WebElement> item_link = driver
				.findElements(By.xpath("//a[contains(@class,'a-link-normal ')]/ancestor::h2/a"));

		List<String> item_link_Url = new ArrayList<String>();

		for (int i = 0; i < item_link.size(); i++) {
			System.out.println("item List extract:- " + item_link.get(i).getAttribute("href"));
			item_link_Url.add(i, item_link.get(i).getAttribute("href"));
		}

		for (int i = 0; i < item_link_Url.size(); i++) {
			if (item_link_Url.get(i).trim().length() == 0 && item_link_Url.get(i).trim() == "") {
				item_link_Url.remove(i);
			}
			System.out.println("item List value:- " + item_link_Url.get(i));
		}

		return item_link_Url;
	}

	public static void getProductDataFromUrl(List<String> UrlList) throws IOException {
		WebDriverManager.chromedriver().setup();
		ChromeOptions chromeOptions = new ChromeOptions();
	    chromeOptions.addArguments("--remote-allow-origins=*");
	    WebDriver driver = new ChromeDriver(chromeOptions);

		List<String> item_Name = new ArrayList<String>();
		List<String> item_Price = new ArrayList<String>();
		List<String> item_Date = new ArrayList<String>();

		for (int i = 0; i < UrlList.size(); i++) {
			System.out.println("Url value:- " + UrlList.get(i));
			driver.get(UrlList.get(i).toString());
			item_Name.add(driver.findElement(By.xpath("//span[@id='productTitle']")).getText().replace(",", ""));
			item_Price.add(driver.findElement(By.xpath("//span[@class='a-price-whole']")).getText().replace("\"", ""));
		}
		String Filepath = new File("Result.csv").getCanonicalPath();
		System.out.println("FilePath:- " + Filepath);
		writeDataLineByLine(Filepath, UrlList, item_Name, item_Price, item_Date);

		driver.quit();
	}

	public static void writeDataLineByLine(String filePath, List<String> Url, List<String> Name, List<String> Price,List<String> item_Date) {
		// File path as specified
		File file = new File(filePath);

		// creating required file
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			// File Writer interface
			FileWriter outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile);

			// adding data to csv the first Time
			for (int i = 0; i < Url.size(); i++) {
				// displaying current date and time
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat simpleformat = new SimpleDateFormat("dd/MM/yyyy hh:mm:s");
				System.out.println("Today's date and time = " + simpleformat.format(cal.getTime()));
				// displaying date
				Format f = new SimpleDateFormat("MM/dd/yy");
				String strDate = f.format(new Date());
				System.out.println("Current Date = " + strDate);
				 
				if(Price.get(0).contains(";")) {
					String newDate = item_Date.get(i) +";"+ strDate;
					String[] data1 = { Url.get(i), Name.get(i).replace(",", ""), Price.get(i).replace(",", ""), newDate };
					writer.writeNext(data1);	
				}else {
					
					String[] data1 = { Url.get(i), Name.get(i).replace(",", ""), Price.get(i).replace(",", ""), strDate };
					writer.writeNext(data1);					
				}
			}

			// closing Interface
			writer.close();
		} catch (IOException e) {
			// checking for any IO Exception
			e.printStackTrace();
		}
	}

	public static void ReadAndUpdateCsvFile(String filePath) {
		try {
			FileReader fileReader = new FileReader(filePath);
			@SuppressWarnings("resource")
			CSVReader csvReader = new CSVReader(fileReader);
			
			List<String> item_URL = new ArrayList<String>();
			List<String> item_Name = new ArrayList<String>();
			List<String> item_Price = new ArrayList<String>();
			List<String> Price_Date = new ArrayList<String>();

			String[] line;
			while ((line = csvReader.readNext()) != null) {
				if (line != null) {
					
					String Strline = Arrays.toString(line);
					item_URL.add(Strline.split(",")[0].replace("[", "").replace("]", ""));
					item_Name.add(Strline.split(",")[1].replace("[", "").replace("]", ""));
					item_Price.add(Strline.split(",")[2].replace("[", "").replace("]", ""));
					Price_Date.add(Strline.split(",")[3].replace("[", "").replace("]", ""));
					
					
					
				}

			}
			getProductDataFromUrl(item_URL, item_Name,item_Price,Price_Date);
		
		} catch (Exception e) {

		}
	}
	
	public static void getProductDataFromUrl(List<String> item_URL,List<String> item_Name,List<String> item_Price,List<String> Price_Date) throws IOException {
		
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		for (int i = 0; i < item_URL.size(); i++) {
			String ProductLink = item_URL.get(i).toString().replace("[", "");
			System.out.println("Url value:- " + ProductLink.replace("[", ""));
			driver.get(ProductLink);
			
			String price = item_Price.get(i) +";"+ driver.findElement(By.xpath("//span[@class='a-price-whole']")).getText();		
			item_Price.set(i, price);
			
		}
		
		for(int j=0 ;j < item_URL.size(); j++) {
			System.out.println("Item price:- "+item_Price.get(j).toString());
		}
		
		
		String Filepath = new File("Result.csv").getCanonicalPath();
		System.out.println("FilePath:- " + Filepath);
		writeDataLineByLine(Filepath, item_URL, item_Name, item_Price,Price_Date);

		driver.quit();
	}

}
