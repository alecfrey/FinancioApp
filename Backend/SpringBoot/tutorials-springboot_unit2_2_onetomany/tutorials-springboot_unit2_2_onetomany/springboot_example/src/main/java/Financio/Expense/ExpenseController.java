package Financio.Expense;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

import Financio.ReportedExpense.ReportedExpense;
import Financio.ReportedExpense.ReportedExpenseRepository;
import Financio.Users.User;
import Financio.Users.UserRepository;

@RestController
public class ExpenseController { // ToDo : Remake

    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReportedExpenseRepository reportedExpenseRepository;
    
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/expense")
    List<Expense> getAllexpense(){
        return expenseRepository.findAll();
    }

    @GetMapping(path = "/expense/{id}")
    Expense getExpensetById( @PathVariable int id){
    	return expenseRepository.findById(id);
    }
    
    @PostMapping(path = "/expense")
    String createExpense(@RequestBody ObjectNode json) {
    	String toReturn = new String();
    	try {
    		Expense newExpense = new Expense(json.get("resturant").asDouble(),json.get("subscriptions").asDouble(),json.get("essentials").asDouble(),json.get("grocery").asDouble(),json.get("gas").asDouble(),json.get("alcohol").asDouble(),json.get("other").asDouble(),userRepository.findById(json.get("userId").asInt()));
    		Expense expenseDb = expenseRepository.save(newExpense);
        	toReturn =  "{\"expenseId\":\"" + expenseDb.getId() + "\"}";
    	} catch(Exception e) {
    		System.out.println(e);
    		toReturn = failure;
    	}	finally {
    		
    	}
    	return toReturn;
    }
    
    @GetMapping(path = "/expense/{id}/reported")
    String getReportedTotlas(@PathVariable int id){
    	List<ReportedExpense> reports = reportedExpenseRepository.findByExpense(expenseRepository.findById(id));
    	double totalResturant = 0;
    	double totalSubscriptions = 0;
    	double totalEssentials = 0;
    	double totalGrocery = 0;
    	double totalGas = 0;
    	double totalAlcohol = 0;
    	double totalOther = 0;
    	for(int i = 0; i < reports.size(); i ++) {
    		totalResturant += reports.get(i).getResturant();
    		totalSubscriptions += reports.get(i).getSubscriptions();
    		totalEssentials += reports.get(i).getEssentials();
    		totalGrocery += reports.get(i).getGrocery();
    		totalGas += reports.get(i).getGas();
    		totalAlcohol += reports.get(i).getAlcohol();
    		totalOther += reports.get(i).getOther();
    	}
    	return "{\"resturant\":"+totalResturant+",\"subscriptions\":"+totalSubscriptions+",\"essentials\":"+totalEssentials+",\"grocery\":"+totalGrocery+",\"gas\":"+totalGas+",\"alcohol\":"+totalAlcohol+",\"other\":"+totalOther+"}";
    }
}
