package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import javax.websocket.server.PathParam;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController 
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    //Register
    @PostMapping("/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account newAccount){
        if (newAccount == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        boolean usernameExists = newAccount.getUsername().length() > 0;
        boolean passwordLong = newAccount.getPassword().length() > 3;
        boolean notInDB = accountService.getAccountByUsername(newAccount.getUsername()) == null;
        if (usernameExists && passwordLong && notInDB){
            return ResponseEntity.status(HttpStatus.OK).body(accountService.addAccount(newAccount));
        } else if (!notInDB) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<Account> loginUser(@RequestBody Account account){
        
        Account loginAccount = accountService.login(account.getUsername(), account.getPassword());
        if (loginAccount != null){
            return ResponseEntity.ok().body(loginAccount);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        

    }

    //New Messages
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message newMessage){
        if (newMessage == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        boolean nonEmpty = newMessage.getMessage_text().length() > 0;
        boolean under255 = newMessage.getMessage_text().length() <= 255;
        boolean userinDB = accountService.getAccountByID(newMessage.getPosted_by()) != null;
        if (nonEmpty && under255 && userinDB){
            messageService.addMessage(newMessage);
            return ResponseEntity.status(200).body(newMessage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(newMessage);
        }
    }

    //Get All Messages
    @GetMapping("/messages")
    public List<Message> getAllMessages(){
        return messageService.getAllMessages();
    }

    //Retrieve Message By ID
    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessageByID(@PathVariable int id){
        return new ResponseEntity<>(messageService.getMessageByID(id), HttpStatus.OK);
    }

    //Delete Message By ID
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable int id){
        int messageDeleted = messageService.deleteMessageByID(id);
        if(messageDeleted > 0){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(1);
        } else {
            return ResponseEntity.
                    status(HttpStatus.OK)
                    .body(null);
        }
    }

    //Update Message by ID
    @PatchMapping("/messages/{id}")
    public ResponseEntity<Integer> updateMessageByID(@PathVariable int id, @RequestBody Message newMessage_wNewMessageText){
        String newMessageText = newMessage_wNewMessageText.getMessage_text();
        if (newMessageText.length() <= 0 || newMessageText.length() > 255){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
        Message message = messageService.patchMessageByID(id, newMessageText);
        if (message != null){
            return ResponseEntity.ok().body(1);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
    }

    //Retrieve all messages by User
    @GetMapping("/accounts/{account_ID}/messages")
    public ResponseEntity<List<Message>> getMessagesByUserID(@PathVariable int account_ID){
        if (accountService.getAccountByID(account_ID) == null){
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.ok().body(messageService.getMessagesByID(account_ID));
    }

}
