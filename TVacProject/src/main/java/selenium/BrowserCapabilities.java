package selenium;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by kdodonov on 22.09.2017.
 */
public class BrowserCapabilities {
    public static DesiredCapabilities getCapabilities(Browser browser) {
        switch (browser.getBrowserName()) {
            case "chrome": {
                return getChromeCapabilities();
            }
            case "firefox": {
                return getFirefoxCapabilities();
            }
            case "ie":
            default: {
                return getIECapabilities();
            }
        }
    }

    private static DesiredCapabilities getIECapabilities() {
        return null;
    }

    private static DesiredCapabilities getFirefoxCapabilities() {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        return capabilities;
    }

    private static DesiredCapabilities getChromeCapabilities() {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");

        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        return capabilities;

    }
}
