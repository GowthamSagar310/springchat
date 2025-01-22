package com.gowthamsagar.springchat;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class SeleniumLoginTest {

    @Test
    public void automateLogin() {

        System.setProperty("java.awt.headless", "false");

        List<List<String>> users = Arrays.asList (
                Arrays.asList("admin", "dummy"),
                Arrays.asList("test1", "dummy"),
                Arrays.asList("test2", "dummy")
        );

        for (List<String> user : users) {

            ChromeOptions options = new ChromeOptions();
            WebDriver driver = new ChromeDriver(options);

            driver.get("http://localhost:9090/");

            driver.findElement(By.id("username")).sendKeys(user.get(0));
            driver.findElement(By.id("password")).sendKeys(user.get(1));
            driver.findElement(By.id("signin")).click();

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                // do nothing
            }
        }
    }
}
