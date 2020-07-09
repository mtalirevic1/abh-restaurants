package page_objects.abh_restaurant;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import page_objects.PageBase;

import java.util.List;

public class UserDetails extends PageBase {
    final static private String PAGE_URL_REGEX = "\\/user\\d*";
    final static private String RESERVATION_LIST_CSS = "div[class = 'reservation-list'] div span";
    final static private String LOG_OUT_BUTTON_CSS = "div[class = 'user-page-bar'] ul li";

    public UserDetails(WebDriver driver) {
        super(driver, PAGE_URL_REGEX);
        initElements();
    }

    @FindBy(css = RESERVATION_LIST_CSS)
    private List<WebElement> reservationList;

    @FindBy(css = LOG_OUT_BUTTON_CSS)
    private List<WebElement> logOutButton;

    public List<WebElement> getReservationList() {
        return reservationList;
    }

    public List<WebElement> getLogOutButton() {
        return logOutButton;
    }

    public Boolean checkReservationDetails(String restaurantReservation, int index) {
        return restaurantReservation.equals(getReservationList().get(index).getText());
    }

    public HomePage clickLogoutButton() {
        getLogOutButton().get(1).click();
        return new HomePage(getDriver());
    }
}
