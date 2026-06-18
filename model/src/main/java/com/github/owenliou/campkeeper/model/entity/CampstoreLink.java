package com.github.owenliou.campkeeper.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "campstore_link")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampstoreLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_name", nullable = false, length = 50)
    private String storeName;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String link;

    @Column(nullable = false)
    private Integer sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_name", referencedColumnName = "store_name", insertable = false, updatable = false)
    private Campstore campstore;
}