// Updated ModelStudentAccount.java
package database;

import android.graphics.Bitmap;

public class ModelStudentAccount extends ModelAccount{
    public String getGradeLevel() {
        return gradeLevel;
    }
    public String getStudentCode() {
        return studentCode;
    }

    public String getRelationToStudent() {
        return relationToStudent;
    }

    public String getGuardianFirstName() {
        return guardianFirstName;
    }

    public String getGuardianMiddleName() {
        return guardianMiddleName;
    }

    public String getGuardianLastName() {
        return guardianLastName;
    }
    public String getSection(){return section;}

    String studentCode;
    String gradeLevel;
    String section;
    String relationToStudent;
    String guardianFirstName;
    String guardianMiddleName;
    String guardianLastName;

    public ModelStudentAccount(AccountType accountType, String firstName, String middleName,
                               String lastName, String emailAddress, String phoneNumber, String username,
                               String password,
                               String studentCode, String gradeLevel, String section,String relationToStudent,
                               String guardianFirstName, String guardianMiddleName, String guardianLastName, Bitmap imgBit)
    {
        super(accountType, firstName, middleName,
                lastName, emailAddress,
                phoneNumber, username,
                password, imgBit);
        this.studentCode = studentCode;
        this.gradeLevel = gradeLevel;
        this.section = section;
        this.relationToStudent = relationToStudent;
        this.guardianFirstName = guardianFirstName;
        this.guardianMiddleName = guardianMiddleName;
        this.guardianLastName = guardianLastName;
        this.imgBit = imgBit;
    }
}