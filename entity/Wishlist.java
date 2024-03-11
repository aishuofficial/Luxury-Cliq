package com.LUXURYCLIQ.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@ToString
public class Wishlist
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;


    private boolean deletedById=true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userEntity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productInfo;

}


