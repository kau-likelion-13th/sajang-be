package likelion13th.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "category")
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false, unique = true)
    private Long id;

    @Column(name="category_name", nullable = false)
    private String name;

    //Item과 다대다 연관관계 설정
    @ManyToMany
    @JsonIgnore //무한 루프 방지  (카테고리 내부에서 items 목록을 JSON 변환에서 제외)
    @JoinTable(name="category_item", //중간 테이블 자동으로 생성
            joinColumns = @JoinColumn(name="category_id"),
            inverseJoinColumns=@JoinColumn(name="item_id"))
    private List<Item> items = new ArrayList<>();

    // 양방향 관계 설정
    public void addItem(Item item) {
        this.items.add(item);
        item.getCategories().add(this);
    }

    public Category(String name) {
        this.name = name;
    }
}
