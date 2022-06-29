package io.mkth.vaultgenerateotp.model;

public class TOTPValidation {

    private String code;

    public TOTPValidation() {
    }

    public TOTPValidation(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
