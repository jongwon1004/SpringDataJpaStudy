package study.datajpa.repository;

public interface NestedClosedProjections {

    // 중첩 Projection 을 사용하면 , getUsername 같은경우엔 select 절에서 딱 1개 (username) 만 집어서 가져오지만
    // Team 의 정보를 가져올때 team 엔티티의 모든 정보를 가져온다 ->  최적화 불가 (join을 사용하면 애매해짐)

    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}
