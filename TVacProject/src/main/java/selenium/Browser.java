package selenium;

/**
 * Created by kdodonov on 22.09.2017.
 */
public class Browser {
        private String browserName;
        private String version;
        private String platform;

        public String getBrowserName() {
            return browserName;
        }

        public void setBrowserName(String browserName) {
            this.browserName = browserName;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }
}
