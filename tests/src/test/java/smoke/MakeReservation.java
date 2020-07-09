package smoke;

import org.testng.Assert;
import org.testng.annotations.Test;
import page_objects.abh_restaurant.*;
import testUtils.TestBase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MakeReservation extends TestBase {
    private static final String FIRST_NAME = "Alice";
    private static final String LAST_NAME = "Lopez";
    private static final String EMAIL = "alic5@live.com";
    private static final String PHONE_NUMBER = "12345";
    private static final String ADDRESS = "Pijacna 118";
    private static final String PASSWORD = "1234";
    private static final String HEADER_TEXT = "Make a free reservation";


    @Test(priority = 0)
    public void openRestaurantsHomePage() {
        new HomePage(driver)
                .clickLoginButton(2);
    }

    @Test(priority = 1)
    public void openRegistrationPage() {
        new LoginPage(driver)
                .clickRegisterButton();
    }

    @Test(priority = 2)
    public void populateRegistrationForm() {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMHHmm");
        Date date = new Date();
        new Registration(driver)
                .makeRegistration(FIRST_NAME, LAST_NAME, formatter.format(date) + EMAIL, PHONE_NUMBER, ADDRESS, PASSWORD, PASSWORD);
    }

    @Test(priority = 3)
    public void checkUserIsRegistered() {
        Assert.assertTrue(new HomePage(driver)
                .checkMainText(HEADER_TEXT));
    }

    @Test(priority = 4)
    public void openUserDetails() {
        new HomePage(driver)
                .clickUserDetails(2);
    }

    @Test(priority = 5)
    public void logOut() {
        new UserDetails(driver)
                .clickLogoutButton();
    }

    @Test(priority = 6)
    public void openHomePage() {
        new HomePage(driver)
                .clickLoginButton(2);
    }

    @Test(priority = 7)
    public void loginToRestaurantsPage() {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMHHmm");
        Date date = new Date();
        new LoginPage(driver)
                .loginToRestaurants(formatter.format(date) + EMAIL, PASSWORD);
    }

    @Test(priority = 8)
    public void checkUserLoginIsSuccessful() {
        Assert.assertTrue(new HomePage(driver)
                .checkMainText(HEADER_TEXT));
    }

    @Test(priority = 9)
    public void openRestaurantsPage() {
        new HomePage(driver)
                .openRestaurantsPage(1);
    }

    @Test(priority = 10)
    public void openFirstRestaurant() {
        new Restaurants(driver)
                .clickReserveButton();
    }

    @Test(priority = 11)
    public void fillReservationForm() {
        new Restaurant(driver)
                .findTables("1", "2019-10-24", "14:30");
    }

    @Test(priority = 12)
    public void isAvailabilityShown() {
        Assert.assertTrue(new Restaurant(driver)
                .checkAvailabilityText("Availability on 2019-10-24 around 14:30 for 1 People:"));
    }

    @Test(priority = 13)
    public void chooseBestTime() {
        new Restaurant(driver).clickTheBestTime();
    }

    @Test(priority = 14)
    public void checkReservationConfirmation() {
        Assert.assertTrue(new ReservationDetails(driver)
                .checkReservationCompletionText("Complete your reservation"));
    }
}
