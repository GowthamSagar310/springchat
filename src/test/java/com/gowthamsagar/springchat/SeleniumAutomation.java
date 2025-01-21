package com.gowthamsagar.springchat;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class SeleniumAutomation {

    public static void main(String[] args) {

        List<List<String>> users = Arrays.asList (
                Arrays.asList("admin", "dummy"),
                Arrays.asList("test1", "dummy"),
                Arrays.asList("test2", "dummy")
        );

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();

        List<Point> positions = Arrays.asList(
                new Point(0, 0), // Top-left
                new Point(screenWidth/2, 0),     // Top-right
                new Point(screenWidth/2, screenHeight/2)  // Bottom-right
        );
        int width = screenWidth/2;
        int height = screenHeight/2;

        for (int i = 0; i < users.size(); i++) {

            List<String> user = users.get(i);
            ChromeOptions options = new ChromeOptions();
            WebDriver driver = new ChromeDriver(options);

            driver.manage().window().setPosition(positions.get(i));
            driver.manage().window().setSize(new Dimension(width,height));

            driver.get("http://localhost:9090/");

            driver.findElement(By.id("username")).sendKeys(user.get(0));
            driver.findElement(By.id("password")).sendKeys(user.get(1));
            driver.findElement(By.id("signin")).click();

            try {
                Thread.sleep(1000);
            }
            catch(Exception e) {
                // do nothing
            }
        }

    }


}
