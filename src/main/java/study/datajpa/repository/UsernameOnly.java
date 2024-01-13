package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

//    @Value("#{target.username + ' ' + target.age}") // username이랑 age를 합친 문자열을 반환해줌 ( Open Projection )
    // 위의 @Value 를 사용한 (Open Projection) 말고 밑에처럼 String getUsername() 만 사용하면
    // 정확하게 매칭되는 데이터만 뽑아서 가져옴 (Close Projection)
    String getUsername();
}
