package com.fullstack.ipldashboard.batch;

import com.fullstack.ipldashboard.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {
    private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public Match process(final MatchInput matchInput) {
        Teams teams = setTeam1AndTeam2(matchInput);
        return new Match (
                Long.parseLong(matchInput.getId()),
                matchInput.getCity(),
                LocalDate.parse(matchInput.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                matchInput.getPlayer_of_match(),
                matchInput.getVenue(),
                teams.firstInningsTeam,
                teams.secondInningsTeam,
                matchInput.getToss_winner(),
                matchInput.getToss_decision(),
                matchInput.getWinner(),
                matchInput.getResult(),
                matchInput.getResult_margin(),
                matchInput.getUmpire1(),
                matchInput.getUmpire2());
    }

    private Teams setTeam1AndTeam2(MatchInput matchInput) {
        Teams teams = new Teams();
        if ("bat".equals(matchInput.getToss_decision())) {
            teams.firstInningsTeam = matchInput.getToss_winner();
            teams.secondInningsTeam = matchInput.getToss_winner().equals(matchInput.getTeam1()) ? matchInput.getTeam2() : matchInput.getTeam1();
        } else {
            teams.secondInningsTeam = matchInput.getToss_winner();
            teams.firstInningsTeam = matchInput.getToss_winner().equals(matchInput.getTeam1()) ? matchInput.getTeam2() : matchInput.getTeam1();
        }
        return teams;
    }

    private static class Teams {
        String firstInningsTeam;
        String secondInningsTeam;
    }
}

