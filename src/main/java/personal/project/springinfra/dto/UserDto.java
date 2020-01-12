package personal.project.springinfra.dto;

import personal.project.springinfra.assets.Regex;
import personal.project.springinfra.assets.ValidationGroups.UpdateValidationGroup;
import personal.project.springinfra.assets.status.UserStatus;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserDto extends BaseDto {
    @NotNull(message = "id is required", groups = UpdateValidationGroup.class)
    @Min(value = 1, message = "Minimum acceptable value for id is 1", groups = UpdateValidationGroup.class)
    private Long id;
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
    @NotNull(message = "status is required")
    private UserStatus status;
    @NotNull(message = "version is required", groups = UpdateValidationGroup.class)
    private Long version;

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

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
    //endregion
}
