import java.util.List;

public class Admin extends User{

    /**
     * Constructor
     * @param user_ID
     * @param name
     * @param email
     * @param password
     * @param age
     * @param income
     * @param netWorth
     * @param securityQ
     * @param securityA
     */
    public Admin(int user_ID, String name, String email, String password,
                 int age, double income, double netWorth, String securityQ, String securityA) {
        super(user_ID, name, email, password, age, income, netWorth, securityQ, securityA);
    }

    /**
     * Constructor
     * @param name
     * @param email
     * @param password
     * @param securityQ
     * @param securityA
     */
    public Admin(String name, String email, String password, String securityQ, String securityA){
        super(name, email, password, securityQ, securityA);
    }

    /**
     * empty constructor
     */
    public Admin(){

    }

    /**
     * 
     * @return List of all Portfolios object
     */
    public List<Portfolio> getAllPortfolios(){
        return DatabaseManager.getAllPortfolios();
    }

    /**
     * 
     * @return List of all Users object
     */
    public List<User> getallUsers(){
        return DatabaseManager.getAllUsers();
    }
} // end of admin