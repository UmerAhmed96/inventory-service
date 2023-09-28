package com.example.inventorytask.service;


import com.example.inventorytask.dto.ItemDTO;
import com.example.inventorytask.dto.LoginRequest;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ItemService {
    List<ItemDTO> getAllItems();
    ItemDTO getItemById(int itemId);
    ItemDTO createItem(ItemDTO itemDTO);
    ItemDTO updateItem(int itemId, ItemDTO updatedItemDTO);
    void deleteItem(int itemId);
    AccessTokenResponse signIn();
    void deleteAll();
    public Page<ItemDTO> getAllItemsPage(int pageSize, int page, String sortBy);


    }
