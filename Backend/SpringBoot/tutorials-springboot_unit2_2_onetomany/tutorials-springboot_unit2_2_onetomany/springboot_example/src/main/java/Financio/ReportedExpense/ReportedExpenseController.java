package Financio.ReportedExpense;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

import Financio.Expense.ExpenseRepository;

@RestController
public class ReportedExpenseController { // ToDo : Remake

    @Autowired
    ReportedExpenseRepository reproted_expenseRepository;
    @Autowired
    ExpenseRepository expenseRepository;
    
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/reported_expense")
    List<ReportedExpense> getAllexpense(){
        return reproted_expenseRepository.findAll();
    }

    @GetMapping(path = "/reported_expense/{id}")
    ReportedExpense getExpensetById( @PathVariable int id){
    	return reproted_expenseRepository.findById(id);
    }
    
    @PostMapping(path = "/reported_expense")
    String createReportedExpense(@RequestBody ObjectNode json) {
    	String toReturn = new String();
    	try {
    		System.out.println(json.get("expenseId").asInt());
    		ReportedExpense newReportedExpense = new ReportedExpense(json.get("resturant").asDouble(),json.get("subscriptions").asDouble(),json.get("essentials").asDouble(),json.get("grocery").asDouble(),json.get("gas").asDouble(),json.get("alcohol").asDouble(),json.get("other").asDouble(),expenseRepository.findById(json.get("expenseId").asInt()));
    		ReportedExpense expenseDb = reproted_expenseRepository.save(newReportedExpense);
        	toReturn =  "{\"reportedExpenseId\":\"" + expenseDb.getId() + "\"}";
    	} catch(Exception e) {
    		System.out.println(e);
    		toReturn = failure;
    	}	finally {
    		
    	}
    	return toReturn;
    }
    
    
}
