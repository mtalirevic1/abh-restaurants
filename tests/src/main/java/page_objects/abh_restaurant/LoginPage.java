package page_objects.abh_restaurant;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import page_objects.PageBase;

public class LoginPage extends PageBase {
    final static private String PAGE_URL_REGEX = "\\/login\\d*";
    final static private String CREATE_ACCOUNT_BUTTON_CSS = "form[class = 'login-register-form'] div h5 a[href= '/register']";
    final static private String EMAIL_INPUT_FIELD_CSS = "div[class='row'] input[id='email']";
    final static private String PASSWORD_INPUT_FIELD_CSS = "div[class='row'] input[id='password']";
    final static private String LOGIN_BUTTON_CSS = "div[class='row'] div button[type='submit']";

    public LoginPage(WebDriver driver) {
        super(driver, PAGE_URL_REGEX);
        initElements();
    }

    @FindBy(css = CREATE_ACCOUNT_BUTTON_CSS)
    private WebElement createAccountButton;

    @FindBy(css = EMAIL_INPUT_FIELD_CSS)
    private WebElement emailInputField;

    @FindBy(css = PASSWORD_INPUT_FIELD_CSS)
    private WebElement passwordInputField;

    @FindBy(css = LOGIN_BUTTON_CSS)
    private WebElement loginButton;

    public WebElement getCreateAccountButton() {
        return createAccountButton;
    }

    public WebElement getEmailInputField() {
        return emailInputField;
    }

    public WebElement getPasswordInputField() {
        return passwordInputField;
    }

    public WebElement getLoginButton() {
        return loginButton;
    }

    public Registration clickRegisterButton() {
        getCreateAccountButton().click();
        return new Registration(getDriver());
    }

    public HomePage loginToRestaurants(String email, String password) {
        getEmailInputField().sendKeys(email);
        getPasswordInputField().sendKeys(password);
        getLoginButton().click();
        return new HomePage(getDriver());
    }


}
