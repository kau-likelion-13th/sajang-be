package likelion13th.shop.domain.entity;


import jakarta.persistence.Column;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(updatable = false) // 수정시 관여 X
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(insertable = false) // 삽입시 관여 X
    private LocalDateTime updatedAt;
}
