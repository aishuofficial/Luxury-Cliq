package com.LUXURYCLIQ.entity;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
 public class Ordermanagement
 {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
     @GenericGenerator(name = "uuid2", strategy = "uuid2")
     @Type(type = "org.hibernate.type.UUIDCharType")
     private UUID uuid;

     private int quantity;

     private float orderPrice;

     private LocalDateTime orderDate;

     @ManyToOne
     @JoinColumn(name = "product_id")
     private Product product;

     @ManyToOne
     @JoinColumn(name="user_id")
     private User user;

     @OneToOne
     @JoinColumn(name="address_id")
     private Address address;


     public Float getTotal(){
         return this.quantity * this.orderPrice;
     }
 }

