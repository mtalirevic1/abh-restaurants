package page_objects.abh_restaurant;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import page_objects.PageBase;

import java.util.List;

public class HomePage extends PageBase {
    final static private String PAGE_URL_REGEX = "\\d*";
    final static private String NAV_BAR_LIST_CSS = "#bs-example-navbar-collapse-1 > ul > li >a";
    final static private String MAIN_TEXT_CSS = "#main > div > div > h1";

    public HomePage(WebDriver driver) {
        super(driver, PAGE_URL_REGEX);
        initElements();
    }

    @FindBy(css = NAV_BAR_LIST_CSS)
    private List<WebElement> navBarList;

    @FindBy(css = MAIN_TEXT_CSS)
    private WebElement mainText;

    public List<WebElement> getNavBarList() {
        return navBarList;
    }

    public WebElement getMainText() {
        return mainText;
    }

    public LoginPage clickLoginButton(int index) {
        getNavBarList().get(index).click();
        return new LoginPage(getDriver());
    }

    public Restaurants openRestaurantsPage(int index) {
        getNavBarList().get(index).click();
        return new Restaurants(getDriver());
    }

    public UserDetails clickUserDetails(int index) {
        getNavBarList().get(index).click();
        return new UserDetails(getDriver());
    }

    public Boolean checkMainText(String headerText) {
        return headerText.equals(getMainText().getText());
    }
}
