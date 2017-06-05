
package ir.university.toosi.tms.readerwrapper;

import java.io.Serializable;

public class Person implements Serializable{
    int userId;
    String userName;
    String EmplymentCode;
    String[] cards;
    byte[] fingers;

    public Person() {
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmplymentCode() {
        return EmplymentCode;
    }

    public void setEmplymentCode(String emplymentCode) {
        EmplymentCode = emplymentCode;
    }

    public String[] getCards() {
        return cards;
    }

    public void setCards(String[] cards) {
        this.cards = cards;
    }

    public byte[] getFingers() {
        return fingers;
    }

    public void setFingers(byte[] fingers) {
        this.fingers = fingers;
    }
}
