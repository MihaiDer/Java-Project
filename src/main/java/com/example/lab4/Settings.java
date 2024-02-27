package com.example.lab4;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fileInputStream = new FileInputStream("C:\\Users\\user\\IdeaProjects\\Lab4\\src\\main\\java\\Settings.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRepositoryType() {
        return properties.getProperty("Repository");
    }
}