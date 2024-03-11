package com.LUXURYCLIQ.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;

    private String fileName;

    @ManyToOne
    @JoinColumn(name = "product_id") // Update "product_id" to match the column name in Image table
    private Product product;


//    public Image(String fileName, Product product) {
//        this.fileName=fileName;
//        this.product=product;
//    }

}


