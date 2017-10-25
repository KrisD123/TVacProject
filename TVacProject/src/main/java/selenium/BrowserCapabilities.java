package selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;

/**
 * Created by kdodonov on 22.09.2017.
 */
public class BrowserCapabilities {
    private static final String DOWNLOAD_DIRECTORY = "C:\\Users\\kdodonov\\Desktop\\WorkDocs\\TA Selenium\\TempForDownload";

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
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
        capabilities.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
        capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        capabilities.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
        return capabilities;
    }

    private static DesiredCapabilities getFirefoxCapabilities() {

        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        capabilities.setJavascriptEnabled(true);

        FirefoxProfile profile = new FirefoxProfile();

        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.download.dir", DOWNLOAD_DIRECTORY);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/xml, text/html, text/plain, application/text, application/zip, application/json, application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        profile.setPreference("browser.download.manager.focusWhenStarting", false);
        profile.setPreference("browser.download.manager.useWindow", false);
        profile.setPreference("browser.download.manager.showAlertOnComplete", false);
        profile.setPreference("browser.download.manager.closeWhenDone", false);
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);
        return capabilities;
    }

    private static DesiredCapabilities getChromeCapabilities() {
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", DOWNLOAD_DIRECTORY);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);

        DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);

        options.addArguments("--ignore-certificate-errors");
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
        //capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "eager");
        return desiredCapabilities;


    }
}
