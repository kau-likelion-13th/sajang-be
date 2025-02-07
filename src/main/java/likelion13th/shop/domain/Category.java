package likelion13th.shop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
//
public class Category {
    @Id
    @GeneratedValue
    @Column(name="category_id")
    private Long id;

    @Column(name="category_name")
    private String name;
}
