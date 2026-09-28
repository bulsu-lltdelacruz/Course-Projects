package tourist;

import java.time.LocalDate;

public class Tourist {
    private String touristID;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthday;
    private String gender;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;

        public Tourist(String touristID, String firstName, String middleName, String lastName,
                       LocalDate birthday, String gender, String phoneNumber,
                       String email, String username, String password) {
            this.touristID = touristID;
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
            this.birthday = birthday;
            this.gender = gender;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.username = username;
            this.password = password;
        }

        public String getTouristID() {
            return touristID;
        }

        public void setTouristID(String touristID) {
            this.touristID = touristID;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public LocalDate getBirthday() {
            return birthday;
        }

        public void setBirthday(LocalDate birthday) {
            this.birthday = birthday;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

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

        public String getFullName() {
            return firstName + " " + middleName + " " + lastName;
        }

}
