package org.test.week06lab01.account.dto;

public class PrivateResponse {
    private String password;

    public PrivateResponse() {
    }

    public PrivateResponse(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
