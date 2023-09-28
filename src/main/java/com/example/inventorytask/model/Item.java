package com.example.inventorytask.model;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Table(name = "items")
@Getter @Setter @NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int itemId;

    @Column(name = "item_name")
    private String itemName;

    @CreatedBy
    @Column(name = "item_entered_by_user")
    private String itemEnteredByUser;

    @CreatedDate
    @Column(name = "item_entered_date")
    private Date itemEnteredDate;

    @Column(name = "item_buying_price")
    private double itemBuyingPrice;

    @Column(name = "item_selling_price")
    private double itemSellingPrice;

    @LastModifiedBy
    @Column(name = "item_last_modified_by_user")
    private String itemLastModifiedByUser;

    @LastModifiedDate
    @Column(name = "item_last_modified_date")
    private Date itemLastModifiedDate;

    @Column(name = "item_status")
    private String itemStatus;
}
