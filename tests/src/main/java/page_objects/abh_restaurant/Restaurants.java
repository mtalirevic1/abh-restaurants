package page_objects.abh_restaurant;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import page_objects.PageBase;

public class Restaurants extends PageBase {
    final static private String PAGE_URL_REGEX = "\\/restaurants\\d*";
    final static private String RESERVE_BUTTON_XPATH = "/html/body/div[2]/div[1]/div[2]/div[1]/div[2]/div[1]/div/div/a/button";

    public Restaurants(WebDriver driver) {
        super(driver, PAGE_URL_REGEX);
        initElements();
    }

    @FindBy(xpath = RESERVE_BUTTON_XPATH)
    private WebElement reserveButton;

    public WebElement getReserveButton() {
        return reserveButton;
    }

    public void clickReserveButton() {
        getReserveButton().click();
    }
}
