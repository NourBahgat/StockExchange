package com.example.demo2;

public class TransactionRequest {
    private User user;
    private RequestType type;
    private Stock stock;
    private Double amount;

    public TransactionRequest(User username, RequestType operation, Stock stockLabel, Double amount) {
        this.user = username;
        this.type = operation;
        this.stock = stockLabel;
        this.amount = amount;
    }

    public String getUsername() { return user.getUsername(); }
    public String getLabel() {
        if (stock == null) return "";
        return stock.getActualLabel();
    }
    public Double getAmount() { return amount; }
    public User getUser() { return user; }
    public Stock getStock() { return stock; }
    public String getActionStr() {
        switch (type) {
            case DEPOSIT:
                return "DEPOSIT";
            case WITHDRAWAL:
                return "WITHDRAW";
            case BUY_STOCK:
                return "BUY";
            case SELL_STOCK:
                return "SELL";
            default:
                break;
        }
        return null;
    }

    public RequestType getRequestType() { return type; }
}