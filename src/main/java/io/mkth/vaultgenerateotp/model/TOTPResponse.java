package io.mkth.vaultgenerateotp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TOTPResponse {

    private Boolean isValid;

    public TOTPResponse(Boolean isValid) {
        this.isValid = isValid;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean valid) {
        isValid = valid;
    }
}
