package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


// @Column(unique = true)       = 이메일을 통해 회원을 구분해야하기 때문에 동일한 값이 들어오지 않도록 unique로 지정
// @Enumerated(EnumType.STRING) = 자바의 enum 타입을 엔티티 속성으로 지정 가능, Enum을 사용할 때 기본적으로 순서가 저장 되는데
//                                enum의 순서가 바뀔 경우 문제가 발생 할 수 있으므로, 해당 옵션을 사용하여 저장하는 것을 권장
@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;



    // member 엔티티를 생성하는 메소드로, Member 엔티티에 회원을 생성하는 메소드를 만들어서 관리를 한다면 코드가 변경되더라도
    // 한군데만 수정하면 되는 이점이 있습니다.
    // String password = passwordEncoder.encode(memberFormDto.getPassword());
    // 스프링 시큐리티 설정 클래스에 등록한 BCryptPasswordEncoder Bean을 파라미터로 넘겨서 비밀번호를 암호화 합니다.

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole((Role.USER));
        // Role의 성격이 ADMIN 또는 USER 냐에 따라서 가입 되는 속성이 달라진다.
        // member.setRole(Role.ADMIN);
        return member;
    }

}
