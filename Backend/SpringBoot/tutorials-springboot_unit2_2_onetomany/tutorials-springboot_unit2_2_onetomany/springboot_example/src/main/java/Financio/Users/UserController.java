package Financio.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

import Financio.Expense.Expense;
import Financio.Expense.ExpenseRepository;




/**
 * 
 * @author Jeffery Kasper
 * 
 */ 

@RestController
public class UserController { 

    @Autowired
    UserRepository userRepository;
    @Autowired
    ExpenseRepository expenseRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/user")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/user/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @PostMapping(path = "/user")
    String createUser(@RequestBody ObjectNode json){
    	String toReturn = new String();
    	try {
    		User newUser = new User(json.get("username").asText(),json.get("password").asText(),json.get("address").asText(),json.get("city").asText(),json.get("state").asText(),json.get("zip").asInt(),json.get("firstName").asText(),json.get("middleInitial").asText(),json.get("lastName").asText(),json.get("gender").asText(),json.get("picturePath").asText(),json.get("email").asText());
    		User userDb = userRepository.save(newUser);
        	toReturn =  "{\"userId\":\"" + userDb.getUserId() + "\"}";
    	} catch(Exception e) {
    		System.out.println(e);
    		toReturn = failure;
    	}	finally {
    		
    	}
    	return toReturn;
    }

    @PutMapping("/user/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }   
    
    @GetMapping(path = "/user/{id}/expense")
    Expense getUserExpense(@PathVariable int id) {
    	return expenseRepository.findByUser(userRepository.findById(id)).get(0);
    }
}
