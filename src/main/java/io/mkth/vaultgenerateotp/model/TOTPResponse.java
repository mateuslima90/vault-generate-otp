package io.mkth.vaultgenerateotp.model;

public class TOTPResponse {

    private Boolean isValid;

    public TOTPResponse(Boolean isValid) {
        this.isValid = isValid;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
