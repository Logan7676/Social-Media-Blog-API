package Controller;

import static org.mockito.ArgumentMatchers.contains;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.*;
import Model.*;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //define all required endpoints
        app.post("/register", this::registerUserHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.post("/login", this::loginHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);

        return app;
    }

    /* Handler to retrieve all messages.
     * @param context: the context object handles information HTTP requests and generates responses within Javalin. It will
     * be available to this method automatically thanks to the app.get method.
     */
    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    /* Handler to post a new message.
     * @param context: the context object handles information HTTP requests and generates responses within Javalin. It will
     *        be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        //check if the new message is too short, too long, or not posted by an already existing account
        if (message.getMessage_text() == "" || message.getMessage_text().length() > 255 || !accountService.checkByAccountId(message.getPosted_by())) {
            context.status(400);
            return;
        }
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage != null) {
            context.json(mapper.writeValueAsString(addedMessage));
        }
    }
    
    /*Handler to register a new user.
     * @param context: the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void registerUserHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        //checks if the password length and username length are long enough, as well as if an account with the username already exists
        if (account.getPassword().length() < 4 || account.getUsername().length() < 1|| accountService.checkIfAccountExists(account.getUsername())) {
            context.status(400);
            return;
        }
    
        Account newAccount = accountService.createAccount(account);
    
        if (newAccount != null) {
            context.json(mapper.writeValueAsString(newAccount));
       }
    }
    
    /*Handler to delete a message by message id
     * @param context: the context object handles information HTTP requests and generates responses within Javalin. It will
     *        be available to this method automatically thanks to the app.delete method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void deleteMessageByIdHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(id);

        if (deletedMessage == null) {
            context.status(200);
        } else {
            context.status(200).result(mapper.writeValueAsString(deletedMessage));
        }   
    }

    /*Handler to retrieve a message by its id
    * @param context: the context object handles information HTTP requests and generates responses within Javalin. It will
    *            be available to this method automatically thanks to the app.get method.
    */
    private void getMessageByIdHandler(Context context) {
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(id);
        if (message == null) {
            context.status(200);
        } else {
            context.json(message);
        }
    }

    /*Handler to login a user 
    * @param context: the context object handles information HTTP requests and generates responses within Javalin. It will
    *            be available to this method automatically thanks to the app.post method.
    * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
    */ 
    public void loginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loginAccount = accountService.login(account);

        if (loginAccount == null) {
            context.status(401);
        } else {
            context.status(200).json(mapper.writeValueAsString(loginAccount));
        }
    }

    /*Handler to retrieve all message from a specific user 
    * @param context: the context object handles information HTTP requests and generates responses within Javalin. It will
    *            be available to this method automatically thanks to the app.get method.
    */
    public void getMessagesByUserHandler(Context context) {
        int id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messageList = messageService.getAllMessagesByUser(id);
        
        context.status(200).json(messageList);
    }

    /*Handler to update message content to a new message 
    * @param context the context object handles information HTTP requests and generates responses within Javalin. It will
    *            be available to this method automatically thanks to the app.patch method.
    * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
    */
    public void updateMessageHandler(Context context) throws JsonProcessingException{
        int id = Integer.parseInt(context.pathParam("message_id"));
        String body = context.body();
        if (body == null) {
            context.status(400);
            return;
        }
        Message updatedMessage = context.bodyAsClass(Message.class);
        Message m = messageService.getMessageById(id);
        //checks if new message if within length boundaries, as well as if the message exists
        if (updatedMessage.getMessage_text().length() < 1 || updatedMessage.getMessage_text().length() > 255|| messageService.getMessageById(id) == null){
            context.status(400);
            return;
        } else {
            m.setMessage_text(updatedMessage.getMessage_text());
            messageService.updateMessage(id,m.getMessage_text());
            context.json(m);
        }
    }
}