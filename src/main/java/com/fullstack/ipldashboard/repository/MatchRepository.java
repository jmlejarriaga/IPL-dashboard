package com.fullstack.ipldashboard.repository;

import com.fullstack.ipldashboard.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query(value = "SELECT DISTINCT m.team1, COUNT(*) FROM Match m GROUP BY m.team1")
    List<Object[]> findAllTeam1();

    @Query(value = "SELECT DISTINCT m.team2, COUNT(*) FROM Match m GROUP BY m.team2")
    List<Object[]> findAllTeam2();

    @Query(value = "SELECT m.matchWinner, COUNT(*) FROM Match m GROUP BY m.matchWinner")
    List<Object[]> findAllMatchWinner();

}
