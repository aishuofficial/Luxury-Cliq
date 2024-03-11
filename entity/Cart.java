package com.LUXURYCLIQ.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@ToString
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;

    int  quantity=1;

    private boolean deletedById=true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userEntity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productInfo;


}


