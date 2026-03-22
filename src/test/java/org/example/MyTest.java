package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class MyTest {

    private WebDriver chromeDriver;
    private static final String baseUrl = "https://telemart.ua/ua/";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().clearDriverCache().browserVersion("146").setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
        this.chromeDriver = new ChromeDriver(chromeOptions);
    }

    @BeforeMethod
    public void preconditions() {
        chromeDriver.get(baseUrl);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        chromeDriver.quit();
    }

    @Test
    public void testClickOnGamersMenu() {
        WebElement gamersLink = chromeDriver.findElement(
                By.xpath("//a[contains(@href,'main-for-gamers')]")
        );
        Assert.assertNotNull(gamersLink);
        gamersLink.click();

        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        Assert.assertTrue(
                chromeDriver.getCurrentUrl().contains("gamers"),
                "URL повинен містити 'gamers'"
        );
    }

    @Test
    public void testSearchFieldInput() {
        WebElement searchField = chromeDriver.findElement(
                By.xpath("//input[contains(@class,'search__input') and @name='search_que']")
        );
        Assert.assertNotNull(searchField);

        String searchText = "ноутбук";
        searchField.sendKeys(searchText);

        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        String actualValue = searchField.getAttribute("value");
        Assert.assertEquals(actualValue, searchText,
                "Поле пошуку повинно містити текст 'ноутбук'");
    }

    @Test
    public void testSearchResults() {
        WebElement searchField = chromeDriver.findElement(
                By.xpath("//input[contains(@class,'search__input')]")
        );
        searchField.sendKeys("iPhone");
        searchField.sendKeys(Keys.ENTER);

        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        List<WebElement> results = chromeDriver.findElements(
                By.xpath("//div[contains(@class,'product-item')]")
        );

        Assert.assertTrue(results.size() > 0,
                "Повинні бути результати пошуку");

        System.out.println("Знайдено товарів: " + results.size());
    }
}