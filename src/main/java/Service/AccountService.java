package Service;

import java.util.*;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;


    //constructor for creating a new AccountService with a new accountDAO
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /*constructor for AccountService when accountDAO is provided
    * @param accountDAO
    */
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }


    /*
     * @param account: an account object of the account to be created
     * returns an object of the newly created account if the account doesnt already exist
     */
    public Account createAccount(Account account) {
        if (accountDAO.getAccountByUsername(account.getUsername()) == null) {
            return accountDAO.createAccount(account);
        } else {
            return null;
        }
    }


    /*
     * @param username: the username of the account being checked for
     * returns true if an account with the username exists, false otherwise
     */
    public boolean checkIfAccountExists(String username) {
        if(accountDAO.getAccountByUsername(username) == null) {
            return false;
        } else {
            return true;
        }
    }


    /*
     * @param account_id: the account id of the account that is being checked to see if it exists
     * returns true if an account with the account_id exists, false otherwise
     */
    public boolean checkByAccountId(int account_id) {
        if(accountDAO.getAccountById(account_id) == null) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * @param account: a account object containing creditials of an account wanting to login
     * returns the account object for the account if the account exists, null otherwise
     */
    public Account login(Account account) {
        if (accountDAO.login(account) == null) {
            return null;
        } else {
            return accountDAO.login(account);
        }
    }


    /*
     * @param account_id: the account_id for the account that is returned
     * returns an account object for the provided account_id
     */
    public Account getAccountById(int account_id) {
        return getAccountById(account_id);
    }
}
