public class User {
    private String name;
    private int user_id;
    private String password;
    private String emailId;

    public User(String name, int user_id, String password, String emailId) {
        this.name = name;
        this.emailId = emailId;
        this.user_id = user_id;
        this.password = password;
    }

    // Getters
    public int getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User_Id : " + user_id +
                "\nName : " + name +
                "\nEmailId : " + emailId;
    }
}
