package Financio.Expense;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Financio.Users.User;

public interface ExpenseRepository extends JpaRepository<Expense, Long> { 
    Expense findById(int expenseId);
    List<Expense> findByUser(User user);
}
