// Updated ModelAccount.java
package database;

import android.graphics.Bitmap;

public class ModelAccount {
    public String getId() {
        return id;
    }
    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        if(!middleName.isEmpty())
            return middleName;
        else
            return "";
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public Bitmap getImgBit() {
        return imgBit;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    String id;
    String firstName;
    String middleName;
    String lastName;
    String gradeLevel;
    String emailAddress;
    String phoneNumber;
    String username;
    String password;
    String photoUri;
    Bitmap imgBit = Bitmap.createBitmap(40,40,Bitmap.Config.ARGB_8888);
    AccountType accountType;

    public ModelAccount(AccountType accountType, String firstName, String middleName,
                        String lastName, String emailAddress,
                        String phoneNumber, String username,
                        String password, Bitmap imgBit)
    {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        this.imgBit = imgBit;
    }
    public ModelAccount(AccountType accountType, String firstName, String middleName,
                        String lastName, String gradeLevel, String emailAddress,
                        String phoneNumber, String username,
                        String password, Bitmap imgBit)
    {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gradeLevel = gradeLevel;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        this.imgBit = imgBit;
    }
}