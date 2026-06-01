import java.util.List;

public class Admin extends User{

    public Admin(int user_ID, String name, String email, String password,
                 int age, double income, double netWorth, String securityQ, String securityA, String role) {
        super(user_ID, name, email, password, age, income, netWorth, securityQ, securityA, role = "Admin");
    }

    public Admin(String name, String email, String password, String securityQ, String securityA){
        super(name, email, password, securityQ, securityA);
    }

    public List<Portfolio> getAllPortfolios(){
        return DatabaseManager.getAllPortfolios();
    }

    public List<User> getallUsers(){
        return DatabaseManager.getAllUsers();
    }
}