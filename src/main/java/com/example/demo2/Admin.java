package com.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.*;

public class Admin {
    private String adminName;
    private String adminPassword;
    @FXML
    private ListView<String> usernameListView;

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public Admin() {
        this.adminName = "soso";
        this.adminPassword = "1234";
    }

    public static String[] searchUserInCSV(String username) {
        String csvFile = "users.csv";
        String line;
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                if (data.length >= 3 && data[0].equals(username)) {
                    return new String[]{data[1], data[2]};
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeUser(String username) {
        String csvFile = "users.csv";
        String tempFile = "temp.csv";

        File inputFile = new File(csvFile);
        File tempOutputFile = new File(tempFile);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempOutputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[0].equals(username)) {
                    continue;
                }
                writer.write(line + System.lineSeparator());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Copy content back from temp file to original file
        try (BufferedReader reader = new BufferedReader(new FileReader(tempOutputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + System.lineSeparator());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Delete the temporary file
        if (!tempOutputFile.delete()) {
            System.err.println("Could not delete temp file");
        }
    }

    public void addUser(String username, String password, double credit, boolean type) {
        try {
            FileWriter writer = new FileWriter("users.csv", true);
            writer.append(username).append(",").append(password).append(",")
                    .append(String.valueOf(credit)).append(",")
                    .append(",0").append(String.valueOf(type)).append("\n");
            writer.flush(); // Explicitly flush the writer
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeStock(Stock stock) {
        String csvFile = "stocks.csv";
        String tempFile = "temp2.csv";

        File inputFile = new File(csvFile);
        File tempOutputFile = new File(tempFile);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempOutputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[0].equals(stock.getLabel())) {
                    continue;
                }
                writer.write(line + System.getProperty("line.separator"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(tempOutputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                writer.write(line + System.getProperty("line.separator"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}