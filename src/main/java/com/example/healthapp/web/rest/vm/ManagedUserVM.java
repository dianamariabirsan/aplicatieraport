package com.example.healthapp.web.rest.vm;

import com.example.healthapp.service.dto.AdminUserDTO;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * View Model extending the AdminUserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends AdminUserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    /**
     * Public self-registration is allowed only for PACIENT.
     * Null is accepted for backward compatibility; backend will force PACIENT.
     */
    @Pattern(regexp = "^PACIENT$")
    private String tipCont;

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipCont() {
        return tipCont;
    }

    public void setTipCont(String tipCont) {
        this.tipCont = tipCont;
    }

    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + ", tipCont='" + tipCont + "'} ";
    }
}
