package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    //저장
    public Member save(Member member){
        member.setId(++sequence);
        log.info("save된 member={}",member);
        store.put(member.getId(), member);
        return member;
    }

    //++id로 조회로 회원조회
    public Member findById(Long id){
        return store.get(id);
    }

    //로그인 아이디로 회원조회
    public Optional<Member> findByLoginId(String loginId){

        /*List<Member> members = findAll();
        for (Member member : members) {
            if(member.getLoginId().equals(loginId)){
                return Optional.of(member);
            }
        }
        return Optional.empty();*/

        List<Member> memberList = findAll();
        return memberList.stream().filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }


    //멤버 리스트 조회
    public List<Member> findAll(){ return new ArrayList<>(store.values());
    }

    public void clear(){
        store.clear();
    }
}
