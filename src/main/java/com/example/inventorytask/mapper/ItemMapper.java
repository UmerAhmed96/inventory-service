package com.example.inventorytask.mapper;


import com.example.inventorytask.dto.ItemDTO;
import com.example.inventorytask.model.Item;
import org.springframework.stereotype.Component;


@Component
public class ItemMapper {

    public ItemDTO toItemDTO(Item item) {
        return ItemDTO.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .itemEnteredByUser(item.getItemEnteredByUser())
                .itemEnteredDate(String.valueOf(item.getItemEnteredDate()))
                .itemBuyingPrice(item.getItemBuyingPrice())
                .itemSellingPrice(item.getItemSellingPrice())
                .itemLastModifiedDate(String.valueOf(item.getItemLastModifiedDate()))
                .itemLastModifiedByUser(item.getItemLastModifiedByUser())
                .itemStatus(item.getItemStatus())
                .build();
    }

    public Item toItem(ItemDTO itemDTO) {
        Item item = new Item();
        item.setItemName(itemDTO.getItemName());
        item.setItemBuyingPrice(itemDTO.getItemBuyingPrice());
        item.setItemSellingPrice(itemDTO.getItemSellingPrice());
        item.setItemStatus(itemDTO.getItemStatus());

        return item;
    }
}
