import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;

public class MainPage extends WebdriverSettings {
public WebDriver driver;
    @Test
    public void signUp() throws InterruptedException {

        SignInPage signInPage = PageFactory.initElements(driver, SignInPage.class);
        signInPage.goToSignInPage();

        Random random = new Random();
        int n = random.nextInt(100) + 1;
        String email = "viktorrr" + n + "@gmail.com";

        CreateAccountPage createAccount = PageFactory.initElements(driver, CreateAccountPage.class);
        createAccount.inputUserName();
        createAccount.inputEmail();
        createAccount.inputPassword();
        createAccount.inputCheckPassword();
        sleep(1000);
        createAccount.clickToContinueButton();
        sleep(1000);
        CompleteRegistration completeRegistration = PageFactory.initElements(driver , CompleteRegistration.class);
        completeRegistration.conditionOfTheEndOfRegistration();
    }

}
