import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.openqa.selenium.By.xpath;

public class CompleteRegistration {
    WebDriver driver;

    public CompleteRegistration(WebDriver driver){
        this.driver = driver;
    }
    @FindBy(xpath = "//h4[contains(text(),'There was a problem')]")
    private WebElement callException;

    @FindBy(xpath = "//a[@class = 'a-link-emphasis']")
    private WebElement buttonToSignInPage;

    @FindBy(xpath = "//a[@href = '/gp/help/customer" +
            "/account-issues/ref=ap_cs_email_verify_warn_r" +
            "egister?ie=UTF8']")
    private WebElement PageOfCompleteVerification;

    public void conditionOfTheEndOfRegistration(){

        if( callException.isDisplayed()) {
           buttonToSignInPage.click();
        }
        else{
            PageOfCompleteVerification.click();
        }
    }

    }

