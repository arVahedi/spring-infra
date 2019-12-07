package personal.project.springinfra.dto;

import personal.project.springinfra.assets.Regex;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class AddUserDto extends BaseDto {
    @NotBlank(message = "First-Name is required")
    private String firstName;
    @NotBlank(message = "Last-Name is required")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Pattern(regexp = Regex.EMAIL, message = "Email format is wrong")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = Regex.PHONE_NUMBER, message = "Phone number format is wrong")
    private String phoneNumber;

    //region Getter and Setter
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    //endregion
}
