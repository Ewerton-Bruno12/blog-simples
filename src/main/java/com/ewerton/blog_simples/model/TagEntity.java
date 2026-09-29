package com.ewerton.blog_simples.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_tag")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
