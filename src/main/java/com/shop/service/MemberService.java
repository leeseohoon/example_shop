package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// @Transactional           = 비즈니스 로직을 담당하는 서비스 계층 클래스에 @Transactional 어노테이션을 선업합니다. 로직을 처리하다가 에러가 발생 하였다면,
//                            변경 된 데이터를 로직을 수행하기 이전 상태로 콜백 시켜줍니다.
// @RequiredArgsConstructor = 빈을 주입하는 방법으로는 @Autowired 어노테이션을 이용하거나, 필드 주입(@setter), 생성자 주입을 하는 방법이 있습니다.
//                            이 어노테이션은 final(상수)이나 @NonNull이 붙은 필드에 생성자를 생성해줍니다. 빈에 생성자가 1개이고 생성자의 파라미터 타입이 빈으로 등록이 가능 하다면
//                            @Autowired 어노테이션 없이 의존성 주입이 가능합니다.
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    // 이미 가입된 회원의 경우 IllegalStateException으로 예외를 발생 시킵니다.
    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

}