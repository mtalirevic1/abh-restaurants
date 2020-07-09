package page_objects.abh_restaurant;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import page_objects.PageBase;

import java.util.List;

public class Restaurant extends PageBase {
    final static private String PAGE_URL_REGEX = "\\/restaurant\\d*";
    final static private String NUMBER_OF_PEOPLE_INPUT_FIELD_CSS = "form[class = 'reservation-form'] select";
    final static private String OPTION_1_CSS = "option[value='1']";
    final static private String DATE_INPUT_FIELD_CSS = "form[class = 'reservation-form'] input[type='date']";
    final static private String TIME_INPUT_FIELD_CSS = "form[class = 'reservation-form'] input[type='time']";
    final static private String FIND_TABLE_BUTTON_CSS = "form[class = 'reservation-form'] button[type='submit']";
    final static private String RESERVATION_AVAILABILITY_XPATH = "/html/body/div[2]/div[1]/div[2]/div/div[2]/div[2]/div[1]/h3";
    final static private String RESERVATION_TIME_XPATH = "/html/body/div[2]/div[1]/div[2]/div/div[2]/div[2]/div[1]/p[2]/span";

    public Restaurant(WebDriver driver) {
        super(driver, PAGE_URL_REGEX);
        initElements();
    }

    @FindBy(css = NUMBER_OF_PEOPLE_INPUT_FIELD_CSS)
    private WebElement numberOfPeopleInputField;

    @FindBy(css = OPTION_1_CSS)
    private WebElement option1;

    @FindBy(css = DATE_INPUT_FIELD_CSS)
    private WebElement dateInputField;

    @FindBy(css = TIME_INPUT_FIELD_CSS)
    private WebElement timeInputField;

    @FindBy(css = FIND_TABLE_BUTTON_CSS)
    private WebElement findTableButton;

    @FindBy(xpath = RESERVATION_AVAILABILITY_XPATH)
    private WebElement reservationsAvailabilityText;

    @FindBy(xpath = RESERVATION_TIME_XPATH)
    private WebElement reservationTimeSpan;

    public WebElement getNumberOfPeopleInputField() {
        return numberOfPeopleInputField;
    }

    public WebElement getOption1() {
        return option1;
    }

    public WebElement getDateInputField() {
        return dateInputField;
    }

    public WebElement getTimeInputField() {
        return timeInputField;
    }

    public WebElement getFindTableButton() {
        return findTableButton;
    }

    public WebElement getReservationsAvailabilityText() {
        return reservationsAvailabilityText;
    }

    public WebElement getReservationTimeSpan() {
        return reservationTimeSpan;
    }

    public Restaurant findTables(String numberOfPeople, String date, String time) {
        getNumberOfPeopleInputField().click();
        getOption1().click();
        getDateInputField().click();
        getDateInputField().sendKeys(date);
        getTimeInputField().click();
        getTimeInputField().sendKeys(time);
        getFindTableButton().click();
        return new Restaurant(getDriver());
    }

    public boolean checkAvailabilityText(String text) {
        return reservationsAvailabilityText.getText().equals(text);
    }

    public void clickTheBestTime() {
        getReservationTimeSpan().click();
    }
}
