import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.xpath;

public class SignInPage{

    private static final String BUTTONOFCREATIONACCOUNTSUBMIT = "createAccountSubmit";
    private static final String SignInLocator = "//span[@class = 'nav-line-2 ']";
    private static final String URL = "http://localhost:63342/hello/%D0%A2%D0%B5%D1%81%D1%82%D1%8B%D0%9F%D1%80%D0%BE%D0%B5%D0%BA%D1%82%D0%B0/index.html?_ijt=j09f6kojmn5qi1t1026u2bfjul";

    WebDriver driver;
    public SignInPage(WebDriver driver){
        this.driver = driver;
    }
    @FindBy(id = BUTTONOFCREATIONACCOUNTSUBMIT)
    private WebElement buttonSubmitToSignInPage;

    @FindBy(xpath = SignInLocator)
    private WebElement signInLocator;

    public void open(){
        driver.get(URL);
    }
    public void goToSignInPage() throws InterruptedException {
        signInLocator.click();
        buttonSubmitToSignInPage.click();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        sleep(1000);
    }
}
