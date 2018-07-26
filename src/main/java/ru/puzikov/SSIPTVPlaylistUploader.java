package ru.puzikov;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;

class SSIPTVPlaylistUploader {
    private WebDriver driver;
    private String protocol = "http://";
    private String ssiptvPlaylistUrl = "ss-iptv.com/ru/users/playlist";

    SSIPTVPlaylistUploader() {
        driver = new SafariDriver();
    }

    boolean uploadFileWithKey(Path file, String key) {
        driver.get(protocol + ssiptvPlaylistUrl);
        final WebElement codeInput = driver.findElement(By.id("inptConnectionCodeInput"));
        WebElement addDeviceButton = driver.findElement(By.id("btnAddDevice"));
        codeInput.sendKeys(key);
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                String text = codeInput.getAttribute("value");
                try {
                    System.out.println("waiting for input value " + text);
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return text.equalsIgnoreCase(key);
            }
        });
        addDeviceButton.click();
        try {
            WebElement errorElement = driver.findElement(By.cssSelector(".alert-error"));
            System.out.println("Error occured " + errorElement.getText());
            driver.close();
            driver.quit();
            return false;
        } catch (NoSuchElementException el) {
            System.err.println("Proceeding " + el.getMessage());
        }
        try {
            WebElement fileInput = driver.findElement(By.name("uploadfile"));
            fileInput.sendKeys(file.toAbsolutePath().toString());
            fileInput.submit();
        } catch (NoSuchElementException el) {
            System.err.println("Can't find upload element " + el.getMessage());
        }
        driver.close();
        driver.quit();

        return true;
    }
}
