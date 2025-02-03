package DAO;

import java.sql.*;
import java.util.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    /*
     * returns an object of the newly created account if the account was successfully created
     */
    public Account createAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account(username, password) VALUES(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()) {
                int generated_account_id = (int) rs.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch(SQLException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

    /*
     * returns an object containing account information for the account with the username provided
     * or null if no account exists with that username
     */
    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Account account = new Account(rs.getInt("account_id"),
                    rs.getString("username"), rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

    /*
     * returns an object containing account information for the account with the id provided
     * or null if no account exists with that id
     */
    public Account getAccountById(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Account account = new Account(rs.getInt("account_id"),
                    rs.getString("username"), rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }


    /*
     * "login the account" by verifying that an account with a specific username and password
     * combination exists and returns an account object if it does exist, returns null otherwise
     */
    public Account login(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Account a = new Account(rs.getInt("account_id"),
                    rs.getString("username"),rs.getString("password"));
                return a;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


}
