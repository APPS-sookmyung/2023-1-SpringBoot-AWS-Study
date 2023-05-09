package example.org.domain.user;

import example.org.domain.BaseTimeEntity;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import static javax.persistence.GenerationType.IDENTITY;


@Getter
@Table(name="users")
@Entity
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING) //JPA로 데이터베이스를 저장할 때 Enum 값을 어떤 형태로 저장할지를 결정
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name,String email,String picture, Role role){
        this.name=name;
        this.email=email;
        this.picture=picture;
        this.role=role;
    }

    public User update(String name,String picture){
        this.name=name;
        this.picture=picture;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

}
