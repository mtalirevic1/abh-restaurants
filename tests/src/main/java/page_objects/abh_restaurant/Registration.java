package page_objects.abh_restaurant;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import page_objects.PageBase;

public class Registration extends PageBase {
    final static private String PAGE_URL_REGEX = "\\/register\\d*";
    final static private String FIRST_NAME_INPUT_FIELD_CSS = "form[class = 'login-register-form'] div[class = 'row'] div input[placeholder= 'First Name']";
    final static private String LAST_NAME_INPUT_FIELD_CSS = "form[class = 'login-register-form'] div[class = 'row'] div input[placeholder= 'Last Name']";
    final static private String EMAIL_INPUT_FIELD_CSS = "form[class = 'login-register-form'] div[class = 'row'] div input[placeholder= 'Email']";
    final static private String PHONE_NUMBER_CSS = "form[class = 'login-register-form'] div[class = 'row'] div input[placeholder= 'Phone Number']";
    final static private String ADDRESS_FIELD_CSS = "form[class = 'login-register-form'] div[class = 'row'] div input[placeholder= 'Address']";
    final static private String PASSWORD_FIELD_CSS = "form[class = 'login-register-form'] div[class = 'row'] div input[placeholder= 'Password']";
    final static private String CONFIRM_PASSWORD_CSS = "form[class = 'login-register-form'] div[class = 'row'] div input[placeholder= 'Confirm Password']";
    final static private String CONFIRM_REGISTRATION_BUTTON_CSS = "div[class='row'] div button[type='submit']";

    public Registration(WebDriver driver) {
        super(driver, PAGE_URL_REGEX);
        initElements();
    }

    @FindBy(css = FIRST_NAME_INPUT_FIELD_CSS)
    private WebElement firstNameInputField;

    @FindBy(css = LAST_NAME_INPUT_FIELD_CSS)
    private WebElement lastNameInputField;

    @FindBy(css = EMAIL_INPUT_FIELD_CSS)
    private WebElement emailInputField;

    @FindBy(css = PHONE_NUMBER_CSS)
    private WebElement phoneNumberField;

    @FindBy(css = ADDRESS_FIELD_CSS)
    private WebElement addressInputField;

    @FindBy(css = PASSWORD_FIELD_CSS)
    private WebElement passwordField;

    @FindBy(css = CONFIRM_PASSWORD_CSS)
    private WebElement confirmPassword;

    @FindBy(css = CONFIRM_REGISTRATION_BUTTON_CSS)
    private WebElement confirmRegistrationButton;

    public WebElement getFirstNameInputField() {
        return firstNameInputField;
    }

    public WebElement getLastNameInputField() {
        return lastNameInputField;
    }

    public WebElement getEmailInputField() {
        return emailInputField;
    }

    public WebElement getPhoneNumberField() {
        return phoneNumberField;
    }

    public WebElement getAddressInputField() {
        return addressInputField;

    }

    public WebElement getPasswordField() {
        return passwordField;
    }

    public WebElement getConfirmPassword() {
        return confirmPassword;
    }

    public WebElement getConfirmRegistrationButton() {
        return confirmRegistrationButton;
    }

    public HomePage makeRegistration(String firstName, String lastName, String email, String phoneNumber, String address, String password, String confirmPassword) {
        getFirstNameInputField().sendKeys(firstName);
        getLastNameInputField().sendKeys(lastName);
        getEmailInputField().sendKeys(email);
        getPhoneNumberField().sendKeys(phoneNumber);
        getAddressInputField().sendKeys(address);
        getPasswordField().sendKeys(password);
        getConfirmPassword().sendKeys(confirmPassword);
        getConfirmRegistrationButton().click();
        return new HomePage(getDriver());
    }
}
