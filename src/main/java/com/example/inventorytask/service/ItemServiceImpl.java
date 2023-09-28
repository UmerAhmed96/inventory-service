package com.example.inventorytask.service;

import com.example.inventorytask.dto.ItemDTO;
import com.example.inventorytask.dto.LoginRequest;
import com.example.inventorytask.exception.ItemAlreadyExistsException;
import com.example.inventorytask.exception.ItemNotFoundException;
import com.example.inventorytask.exception.KeycloakException;
import com.example.inventorytask.mapper.ItemMapper;
import com.example.inventorytask.model.Item;
import com.example.inventorytask.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    public final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthUrl;

    @Value("${keycloak.resource}")
    private String client;

    @Value("${keycloak.realm}")
    private String realm;


    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public List<ItemDTO> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(itemMapper::toItemDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDTO getItemById(int itemId) {
        return itemRepository.findById(itemId)
                .map(itemMapper::toItemDTO)
                .orElseThrow(() -> new ItemNotFoundException("Item with ID " + itemId + " not found"));
    }

    @Override
    public ItemDTO createItem(ItemDTO itemDTO) {
        int itemId = itemDTO.getItemId();
        if (itemRepository.existsById(itemId)) {
            throw new ItemAlreadyExistsException("Item with ID " + itemId + " already exists");
        }

        Item newItem = itemMapper.toItem(itemDTO);
        return itemMapper.toItemDTO(itemRepository.save(newItem));
    }

    @Override
    public ItemDTO updateItem(int itemId, ItemDTO updatedItemDTO) {
        // Check if the item with the given ID exists
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with ID " + itemId + " not found"));

        // Map fields from updatedItemDTO to existingItem using the ItemMapper
        Item updatedItem = itemMapper.toItem(updatedItemDTO);

        // Set the itemId of the existing item (if necessary)
        updatedItem.setItemId(itemId);

        // Update the fields in existingItem
        existingItem.setItemName(updatedItem.getItemName());
        existingItem.setItemEnteredByUser(updatedItem.getItemEnteredByUser());
        existingItem.setItemEnteredDate(updatedItem.getItemEnteredDate());
        existingItem.setItemBuyingPrice(updatedItem.getItemBuyingPrice());
        existingItem.setItemSellingPrice(updatedItem.getItemSellingPrice());
        existingItem.setItemLastModifiedDate(updatedItem.getItemLastModifiedDate());
        existingItem.setItemLastModifiedByUser(updatedItem.getItemLastModifiedByUser());
        existingItem.setItemStatus(updatedItem.getItemStatus());

        // Save the updated item
        return itemMapper.toItemDTO(itemRepository.save(existingItem));
    }

    @Override
    public void deleteItem(int itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ItemNotFoundException("Item with ID " + itemId + " not found");
        }
        itemRepository.deleteById(itemId);
    }


    public AccessTokenResponse signIn() {
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", "JxbxYKlE9s7eiWVMgnIWaYXwCl6G6xi2");
        clientCredentials.put("grant_type", "password");
        Configuration configuration =
                new Configuration(keycloakAuthUrl, realm, client, clientCredentials, null);
        AuthzClient authzClient = AuthzClient.create(configuration);
        AccessTokenResponse accessTokenResponse;
        try {
            accessTokenResponse = authzClient.obtainAccessToken("umer", "123");
        } catch (HttpResponseException exception) {
            logger.info("Invalid credentials. Exception: {}", exception.toString());
            throw new KeycloakException(501,"Login Failed");
        } catch (Exception ignored) {
            logger.info("Exception: {}", ignored.toString());
            throw new KeycloakException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ignored.getMessage());
        }
        return accessTokenResponse;
    }

    @Override
    public void deleteAll() {
        itemRepository.deleteAll();
    }
    public Page<ItemDTO> getAllItemsPage(int pageSize, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortBy));
        return itemRepository.findAll(pageable).map(itemMapper::toItemDTO);
    }


}
