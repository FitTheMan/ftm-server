package com.ftm.server.entity.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String name;

    @Lob private String detail;

    private String brand;

    private String link;

    @Builder(access = AccessLevel.PRIVATE)
    private PostProduct(Post post, String name, String detail, String brand, String link) {
        this.post = post;
        this.name = name;
        this.detail = detail;
        this.brand = brand;
        this.link = link;
    }
}
