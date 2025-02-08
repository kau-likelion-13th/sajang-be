package likelion13th.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter

public class Category {
    @Id
    @GeneratedValue
    @Column(name="category_id", nullable=false, unique = true)
    private Long id;

    @Column(name="category_name", nullable = false)
    private String name;

    //Item과 다대다 연관관계 설정
    @ManyToMany
    @JoinTable(name="category_item", //중간 테이블 자동으로 생성
            joinColumns = @JoinColumn(name="category_id"),
            inverseJoinColumns=@JoinColumn(name="item_id"))
    private List<Item> items = new ArrayList<>();
}
