package Financio.ReportedExpense;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Financio.Expense.Expense;
import Financio.Users.User;

public interface ReportedExpenseRepository extends JpaRepository<ReportedExpense, Long> { 
    ReportedExpense findById(int expenseId);
    List<ReportedExpense> findByExpense(Expense expense);
}
