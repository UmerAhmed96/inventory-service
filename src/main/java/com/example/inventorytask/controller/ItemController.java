package com.example.inventorytask.controller;

import com.example.inventorytask.dto.ItemDTO;
import com.example.inventorytask.model.Item;
import com.example.inventorytask.service.ItemService;
import com.example.inventorytask.util.CommonUtil;
import io.swagger.annotations.*;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/app/item")
@Api(tags = "Item Controller", description = "Operations related to items")
public class ItemController {

    private static Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    private CommonUtil commonUtil;

    @PostMapping
    @ApiOperation(value = "Create a new item", response = ItemDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Item created successfully"),
            @ApiResponse(code = 400, message = "Invalid request body")
    })
    public ResponseEntity<ItemDTO> createItem(
            @ApiParam(value = "Item information for creation", required = true) @Valid @RequestBody ItemDTO itemDTO, BindingResult result) {
        logger.info("ItemController::createItem  called  ");

        //Last modified by user and date added here
        String username = commonUtil.getKeycloakSecurityContext().getToken().getPreferredUsername();
        itemDTO.setItemEnteredByUser(username);
        itemDTO.setItemLastModifiedByUser(username);

        return ResponseEntity.status(201).body(itemService.createItem(itemDTO));
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

        logger.info("ItemController::updateItem called  ");

        //Last modified by user and date added here
        String username = commonUtil.getKeycloakSecurityContext().getToken().getPreferredUsername();
        updatedItemDTO.setItemLastModifiedByUser(username);

        return ResponseEntity.ok(itemService.updateItem(itemId, updatedItemDTO));
    }

    @GetMapping
    @ApiOperation(value = "Get all items", response = ItemDTO.class, responseContainer = "List")
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        logger.info("ItemController::getAllItems  called  ");

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
        logger.info("ItemController::getItemById  called  ");

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

        logger.info("ItemController::deleteItem  called  ");

        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/token")
    public ResponseEntity<AccessTokenResponse> loginUser() {
        logger.info("ItemController::loginUser  called  ");

        return new ResponseEntity<>(itemService.signIn(), HttpStatus.OK);
    }

    @DeleteMapping
    @ApiOperation(value = "Delete all items", response = ItemDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item found with the given ID"),
            @ApiResponse(code = 404, message = "Item not found with the given ID")
    })
    public ResponseEntity<String> deleteAllItems() {
        logger.info("ItemController::deleteAllItems  called  ");

        itemService.deleteAll();
        return ResponseEntity.ok("All Items deleted");
    }

    @GetMapping(params = {"pageSize", "page", "sortBy"})
    public Page<ItemDTO> getItemsPage(
            @RequestParam(defaultValue = "2") int pageSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "itemName") String sortBy
    ) {
        logger.info("ItemController::getItemsPage called  ");
        return itemService.getAllItemsPage(pageSize, page, sortBy);
    }

    @GetMapping(params = {"itemStatus", "itemEnteredByUser"})
    public List<Item> getItemsByStatusAndEnteredBy(
            @RequestParam("itemStatus") String status,
            @RequestParam("itemEnteredByUser") String enteredBy) {

        logger.info("ItemController::getItemsByStatusAndEnteredBy called  ");


        return itemService.getItemsByStatusAndEnteredBy(status, enteredBy);
    }
}
