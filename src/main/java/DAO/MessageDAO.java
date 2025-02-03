package DAO;

import java.sql.*;
import java.util.*;


import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    /*Inserts a new message into the database
    * returns an object of the newly inserted message or null if an error occurs
    */
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int generated_message_id = (int) rs.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch()); 
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }

    /*
     * returns a list of all messages currently in the database or null if there are no messages
     * currently in the database
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> allMessages = new ArrayList<>();

        try {
            String sql = "SELECT * from message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), 
                rs.getInt("posted_by"), rs.getString("message_text"), 
                rs.getLong("time_posted_epoch"));
                allMessages.add(message);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return allMessages;
    }

    /*
     * returns an object of a specific message if it exists in the db
     */
    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"), rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * returns an object containing the deleted message if the message existed
     */
    public Message deleteMessageById(int message_id) {
        Message message = null;
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                message = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"), rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
            }
            if(message != null) {
                String deleteSql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setInt(1, message_id);
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }


    /*
     * returns an object containing the newly updated message if the message exists
     */
    public Message updateMessage(int message_id, String message_text) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            preparedStatement.setString(2, message_text);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"), rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * returns a list of all messages created by a specific user or an empty list
     * if the user has no messages created
     */
    public List<Message> getAllMessageByUser(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"), rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
