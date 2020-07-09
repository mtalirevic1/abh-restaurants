package page_objects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class PageBase {
    private WebDriver driver;

    public PageBase() {
    }

    public PageBase(WebDriver driver, String pageUrlRegex) {
        this.driver = driver;
        driver.manage().window().maximize();
        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
        webDriverWait.until(ExpectedConditions.urlMatches(pageUrlRegex));
    }

    public WebDriver getDriver() {
        return this.driver;
    }

    public void initElements() {
        PageFactory.initElements(driver, this);
    }

    public JavascriptExecutor getJavaScriptExecutor() {
        return (JavascriptExecutor) driver;
    }

    public void get(String url) {
        this.driver.get(url);
    }
}
