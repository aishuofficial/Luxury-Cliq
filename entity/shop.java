//package com.LUXURYCLIQ.entity;
//
//import lombok.*;
//import org.hibernate.annotations.GenericGenerator;
//import org.hibernate.annotations.Type;
//
//import javax.persistence.*;
//import java.util.UUID;
//
//@Entity
//@Table(name = "product_details")
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
//@Builder
//public class shop {
//
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
//    @Type(type = "org.hibernate.type.UUIDCharType")
//    private UUID uuid;
//
//
//        @OneToOne(fetch = FetchType.LAZY)
//        @JoinColumn(name = "product_id")
//        private Product product;
//
//        @Column(name = "description", columnDefinition = "TEXT")
//        private String description;
//
//        @Column(name = "specifications", columnDefinition = "TEXT")
//        private String specifications;
//
//        @OneToOne(mappedBy = "productDetails", fetch = FetchType.LAZY)
//        private Image productImage;
//
//
//    }
//
//
