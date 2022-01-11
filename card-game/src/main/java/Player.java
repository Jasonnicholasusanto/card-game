

public class Player {
    private String name;
    private int money;
    private String username;
    private String password;

    public Player(String name, String username, String password, int money){
        this.name = name;
        this.money = money;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setUsername(String username) {this.username = username;}

    public void setPassword(String password) {this.password = password;}

}
