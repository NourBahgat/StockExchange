package com.example.demo2;
import java.util.*;

public class User {
    private String username;
    private String password;
    private double accountBalance;
//    private boolean depositPending; // Flag for pending deposit approval
//    private boolean withdrawPending; // Flag for pending withdraw approval

    public User(String username, String password, double accountBalance) {
        this.username = username;
        this.password = password;
        this.accountBalance = accountBalance;
//        this.depositPending = false;
//        this.withdrawPending = false;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
//
//    public double getAccountBalance() {
//        return accountBalance;
//    }
//
//    public void setAccountBalance(double accountBalance) {
//        this.accountBalance = accountBalance;
//    }
//    public void setDepositPending(boolean depositPending) {
//        this.depositPending = depositPending;
//    }
//
//    public void setWithdrawPending(boolean withdrawPending) {
//        this.withdrawPending = withdrawPending;
//    }
//
//    // Method to deposit funds with admin approval
//    public void depositWithAdminApproval(double amount) {
//        if (depositPending) {
//            // Process deposit with admin approval
//            accountBalance += amount;
//            depositPending = false; // Reset pending flag
//        } else {
//            System.out.println("Deposit pending admin approval.");
//        }
//    }
//
//    // Method to withdraw funds with admin approval
//    public void withdrawWithAdminApproval(double amount) {
//        if (withdrawPending) {
//            // Process withdraw with admin approval
//            if (accountBalance >= amount) {
//                accountBalance -= amount;
//                withdrawPending = false; // Reset pending flag
//            } else {
//                System.out.println("Insufficient funds.");
//            }
//        } else {
//            System.out.println("Withdrawal pending admin approval.");
//        }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accountBalance=" + accountBalance +
                '}';
    }
}
