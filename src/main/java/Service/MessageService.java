package Service;

import java.util.*;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    public MessageDAO messageDAO;


    //constructor for creating a new MessageService with a new messageDAO
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    /*constructor for MessageService when messageDAO is povided
    * @param messageDAO
    */
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    //returns all messages
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    //@param: A message object
    //returns the message if the message does not already exist
    public Message addMessage(Message message) {
        if (messageDAO.getMessageById(message.getMessage_id()) == null) {
            return messageDAO.createMessage(message);
        } else {
            return null;
        }
    }

    /*
     * @param: the message_id for the message that is to be returned
     * returns a message object for the message_id if the message exists
     */
    public Message getMessageById(int message_id) {
        if (messageDAO.getMessageById(message_id) != null) {
            return messageDAO.getMessageById(message_id);
        } else {
            return null;
        }
    }

    /*
     * @param: message id for the message to be deleted
     * returns an object of the deleted message 
     */
    public Message deleteMessageById(int message_id) {
        return messageDAO.deleteMessageById(message_id);
    }

    /*
     * @param: message_id: the id of the message to be updated
     * @param: message_text: the new text for the message
     * returns an object containing the updated message
     */
    public Message updateMessage(int message_id, String message_text) {
        if (messageDAO.getMessageById(message_id) != null) {
            return messageDAO.updateMessage(message_id, message_text);
        } else {
            return null;
        }
    }

    /*
     * @param account_id: the account_id of the account to get all messages from
     * returns a list of message objects that are all messages from the account provided in input
     */
    public List<Message> getAllMessagesByUser(int account_id) {
        return messageDAO.getAllMessageByUser(account_id);
    }


    
}
