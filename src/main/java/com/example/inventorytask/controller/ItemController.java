package com.example.inventorytask.controller;

import com.example.inventorytask.dto.ItemDTO;
import com.example.inventorytask.dto.LoginRequest;
import com.example.inventorytask.model.Item;
import com.example.inventorytask.service.ItemService;
import io.swagger.annotations.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import java.security.Principal;
import java.util.List;

@CrossOrigin
@Validated
@RestController
@RequestMapping("/app/item")
@Api(tags = "Item Controller", description = "Operations related to items")
public class ItemController {

    private static Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;


    @PostMapping
    @ApiOperation(value = "Create a new item", response = ItemDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Item created successfully"),
            @ApiResponse(code = 400, message = "Invalid request body")
    })
    public ResponseEntity<ItemDTO> createItem(
            @ApiParam(value = "Item information for creation", required = true) @Valid @RequestBody ItemDTO itemDTO) {
        ItemDTO createdItem = itemService.createItem(itemDTO);
        return ResponseEntity.status(201).body(createdItem);
    }

    @PutMapping("/{itemId}")
    @ApiOperation(value = "Update an existing item", response = ItemDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item updated successfully"),
            @ApiResponse(code = 404, message = "Item not found with the given ID")
    })
    public ResponseEntity<ItemDTO> updateItem(
            @ApiParam(value = "ID of the item to update", required = true) @PathVariable int itemId,
            @ApiParam(value = "Updated item information", required = true) @RequestBody ItemDTO updatedItemDTO) {
        ItemDTO updatedItem = itemService.updateItem(itemId, updatedItemDTO);
        return ResponseEntity.ok(updatedItem);
    }

    @GetMapping
    @ApiOperation(value = "Get all items", response = ItemDTO.class, responseContainer = "List")
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        List<ItemDTO> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{itemId}")
    @ApiOperation(value = "Get item by ID", response = ItemDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item found with the given ID"),
            @ApiResponse(code = 404, message = "Item not found with the given ID")
    })
    public ResponseEntity<ItemDTO> getItemById(
            @ApiParam(value = "ID of the item to retrieve", required = true) @PathVariable int itemId) {
        ItemDTO item = itemService.getItemById(itemId);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{itemId}")
    @ApiOperation(value = "Delete an item by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Item deleted successfully"),
            @ApiResponse(code = 404, message = "Item not found with the given ID")
    })
    public ResponseEntity<Void> deleteItem(
            @ApiParam(value = "ID of the item to delete", required = true) @PathVariable int itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/token")
    public ResponseEntity<AccessTokenResponse> loginUser() {
        logger.info("UserController::loginUser => Login called  ");

        return new ResponseEntity<>(itemService.signIn(), HttpStatus.OK);
    }

    @DeleteMapping
    @ApiOperation(value = "Delete all items", response = ItemDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item found with the given ID"),
            @ApiResponse(code = 404, message = "Item not found with the given ID")
    })
    public ResponseEntity<String> deleteAllItems() {
            itemService.deleteAll();
        return ResponseEntity.ok("All Items deleted");
    }

    @GetMapping("/page")
    public Page<ItemDTO> getItemsPage(
            @RequestParam(defaultValue = "2") int pageSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "itemName") String sortBy
    ) {
        return itemService.getAllItemsPage(pageSize, page, sortBy);
    }
}
