package likelion13th.shop.domain;

import jakarta.persistence.*;
import likelion13th.shop.domain.entity.BaseEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "item")
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String item_name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String thumbnail_img;

    @Column(nullable = false)
    private String brand;

    //Category와 다대다 연관관계 설정
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //Item과 일대다 연관관계 설정
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
}
