package com.example.inventorytask.repository;

import com.example.inventorytask.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByItemStatusAndItemEnteredByUser(String status, String enteredBy);

}
