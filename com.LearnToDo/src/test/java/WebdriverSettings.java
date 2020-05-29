import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class WebdriverSettings {
    public WebDriver driver;

    @BeforeTest
    public void setUp(){
        System.setProperty("webdriver.chrome.driver" , "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        SignInPage signInPage = PageFactory.initElements(driver , SignInPage.class);
        signInPage.open();
        driver.manage().window().maximize();
    }

    @AfterTest
    public void close(){
        driver.close();
    }
}
