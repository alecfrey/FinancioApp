package com.example.financio.ui.social;

/**
 * Public class User
 *
 * Creates a user object when adding a friend via userID in social fragment.
 */
public class User {

    private String fullName, userName, lastMessage, lastMessageTime, phoneNumber, state, city;
    private int userID;

    /**
     * Constructs a user from a given userID.
     * @param userID
     */
    public User(int userID) {
        this.userID = userID;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    /**
     * Sets user name.
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Sets user location based on city and state.
     * @param city
     * @param state
     */
    public void setUserLocation(String city, String state) {
        this.city = city;
        this.state = state;
    }

    /**
     * Sets user phone number.
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets last message sent by user.
     * @param lastMessage
     */
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    /**
     * Sets time of last message by user.
     * @param lastMessageTime
     */
    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    /**
     * Returns name of user.
     * @return
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns username.
     * @return
     */
    public String getUsername() { return userName;}

    /**
     * Returns string of last message by user.
     * @return
     */
    public String getLastMessage() {
        return lastMessage;
    }

    /**
     * Returns last message time by user.
     * @return
     */
    public String getLastMessageTime() {
        return lastMessageTime;
    }

    /**
     * Returns phone number of user.
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns user location based on city and state.
     * @return
     */
    public String getUserLocation() {
        if (!city.isEmpty() && !state.isEmpty()) {
            return city + ", " + state;

        } else if (!city.isEmpty()) {
            return city;

        } else if (!state.isEmpty()) {
            return state;

        } else {
            return "";
        }
    }

    /**
     * Returns userID of user.
     * @return
     */
    public int getUserID() {
        return userID;
    }
}
