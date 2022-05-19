package Financio.Users;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author Jeffery Kasper
 * 
 */ 

public interface UserRepository extends JpaRepository<User, Long> { // ToDo : Remake
    User findById(int id);
    void deleteById(int id);
}
