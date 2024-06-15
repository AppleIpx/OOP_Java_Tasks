package electronic_voting_system;

public class User {
    private String login;
    private String password;
    private String role;
    private String fullName;
    private String birthDate;

    public User(String login, String password, String role, String fullName, String birthDate) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.birthDate = birthDate;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }


}
