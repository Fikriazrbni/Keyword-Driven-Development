package com.framework;

import com.framework.factory.DynamicXMLFactory;

import java.sql.SQLException;

public class MainEngine {
    public static void main(String[] args) throws SQLException {
        String strCurrentPath = System.getProperty("user.dir");
        System.out.println("Current path: " + strCurrentPath);
        DynamicXMLFactory.init().createTest("Test Dynamic XML");
    }
}
