package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.*;
import java.util.*;


public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUsername(String username);
}
