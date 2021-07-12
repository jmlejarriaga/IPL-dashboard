package com.fullstack.ipldashboard.batch;

import com.fullstack.ipldashboard.model.Team;
import com.fullstack.ipldashboard.repository.MatchRepository;
import com.fullstack.ipldashboard.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public JobCompletionNotificationListener(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logInsertedMatches();
            Map<String, Team> teamData = createAllTeams();
            teamData.forEach((k, v) -> teamRepository.save(v));
            //Checks inserted teams
            teamRepository.findAll().forEach(team -> log.info("{} | {} | {} | {}", team.getId(), team.getTeamName(), team.getTotalMatches(), team.getTotalWins()));
        }
    }

    private Map<String, Team> createAllTeams() {
        Map<String, Team> teamData = new HashMap<>();

        matchRepository.findAllTeam1()
                .stream()
                .map(result -> Team.builder().teamName((String) result[0]).totalMatches((long) result[1]).build())
                .collect(Collectors.toList())
                .forEach(team -> teamData.put(team.getTeamName(), team));

        matchRepository.findAllTeam2()
                .forEach(result -> {
                    Team team = teamData.get((String) result[0]);
                    team.setTotalMatches(team.getTotalMatches() + (long) result[1]);
                });

        matchRepository.findAllMatchWinner()
                .forEach(result -> {
                    Team team = teamData.get((String) result[0]);
                    if (null != team) {
                        team.setTotalWins((long) result[1]);
                    }
                });

        return teamData;
    }

    private void logInsertedMatches() {
        log.info("!!! JOB FINISHED! Time to verify the results");
        //Checks inserted matches
        matchRepository.findAll().forEach(match -> log.info("Team 1: {} | Team 2: {} | Date: {}", match.getTeam1(), match.getTeam2(), match.getDate()));
    }
}