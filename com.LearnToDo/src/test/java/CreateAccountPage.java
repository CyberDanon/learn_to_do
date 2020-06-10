import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Random;

public class CreateAccountPage {

    WebDriver driver;
    private static final String NAME = "Viktor";
    private static final String AP_CUSTOMER_NAME = "ap_customer_name";
    private static final String AP_EMAIL = "ap_email";
    private static final String AP_PASSWORD = "ap_password";
    private static final String AP_PASSWORD_CHECK = "ap_password_check";
    private static final String NumberOfPassword = "123456789";


    public CreateAccountPage(WebDriver driver, WebElement stringOfUsername){
        this.driver = driver;
        this.stringOfUsername = stringOfUsername;
    }

    @FindBy(id = AP_CUSTOMER_NAME)
    private WebElement stringOfUsername;

    @FindBy(id = AP_EMAIL)
    private  WebElement stringOfEmail;

    @FindBy(id = AP_PASSWORD)
    private WebElement stringOfPassword;

    @FindBy (id = AP_PASSWORD_CHECK )
    private WebElement stringOFCheckPassword;

    @FindBy(id = "continue")
    private  WebElement continueButton;

    public void inputUserName(){
        stringOfUsername.click();
        stringOfUsername.sendKeys(NAME);

    }
    public void inputEmail(){
        Random random = new Random();
        int n = random.nextInt(100)+1;
        String email = "viktorrr" + n + "@gmail.com";

        stringOfEmail.click();
        stringOfEmail.sendKeys(email);
    }
    public void inputPassword(){
        stringOfPassword.click();
        stringOfPassword.sendKeys(NumberOfPassword);

    }
    public void inputCheckPassword(){
        stringOFCheckPassword.click();
        stringOFCheckPassword.sendKeys(NumberOfPassword);

    }
    public void clickToContinueButton(){
        continueButton.click();
        continueButton.sendKeys(NumberOfPassword);

    }
}
