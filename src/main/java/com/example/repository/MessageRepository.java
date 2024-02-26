package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.*;
import java.util.*;

public interface MessageRepository extends JpaRepository<Message, Long>{    
}
