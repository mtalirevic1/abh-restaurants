package page_objects.abh_restaurant;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import page_objects.PageBase;

public class ReservationDetails extends PageBase {
    final static private String PAGE_URL_REGEX = "\\/reservation-details\\d*";
    final static private String RESERVATION_CONFIRMATION_TEXT_XPATH = "/html/body/div[2]/div[1]/div[2]/div/div[1]/div/h1";

    public ReservationDetails(WebDriver driver) {
        super(driver, PAGE_URL_REGEX);
        initElements();
    }

    @FindBy(xpath = RESERVATION_CONFIRMATION_TEXT_XPATH)
    private WebElement reservationConfirmationText;

    public WebElement getReservationConfirmationText() {
        return reservationConfirmationText;
    }

    public boolean checkReservationCompletionText(String text) {
        return reservationConfirmationText.getText().equals(text);
    }
}
