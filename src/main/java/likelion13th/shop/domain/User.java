package likelion13th.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    private String username;

    private String provider_id;
    private Boolean deleteable; //베릴님꺼에는 db에 비트라 되어있어요
    private Long phone_numer;

    private int mileage;
    private int recent_total;

    //erd에는 있길래..
    //private String grade;
    //private String profile_img_path;
    //private String password;

    @CreatedDate
    private LocalDate sub_date;

    @Embedded
    private Address address;


}
