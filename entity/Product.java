package com.LUXURYCLIQ.entity;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;

    @Column(unique=true)
    private String name;
    private String description;

    private BigDecimal price;

    private UUID deletedBy;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category ;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL) // Update "product" to match the field name in Image entity
    private List<Image> images;

   private int quantity;


    private boolean deleted = false;

    private boolean enabled=true;
}



