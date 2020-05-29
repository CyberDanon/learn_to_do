import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Random;

public class CreateAccountPage {

    WebDriver driver;
    private static final String NAME = "Viktor";
    private static final String NumberOfPassword = "123456789";

    public CreateAccountPage(WebDriver driver, WebElement stringOfUsername){
        this.driver = driver;
        this.stringOfUsername = stringOfUsername;
    }

    @FindBy(id = "ap_customer_name")
    private WebElement stringOfUsername;

    @FindBy(id = "ap_email")
    private  WebElement stringOfEmail;

    @FindBy(id = "ap_password")
    private WebElement stringOfPassword;

    @FindBy (id = "ap_password_check")
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
