package com.test.football;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class TestController {

    public final static  String APIKey = "9bb66184e0c8145384fd2cc0f7b914ada57b4e8fd2e4d6d586adcc27c257a978";

    @RequestMapping("/country/{countryName}")
    public ServerResponseBody getByCountName(@PathVariable(value = "countryName") String c) {

        ServerResponseBody body = new ServerResponseBody();
        if (c != null && !c.isEmpty()) {
            System.out.println("Country Name passed");


            RestTemplate restTemplate = new RestTemplate();

            CountryInfo countryInfo = getCountryByName(c);

            if (countryInfo != null) {
                body.setCountryId(countryInfo.getCountry_id());
                body.setCountryName(countryInfo.getCountry_name());


                LeagueInfo leagueInfo = getLeagueByCountryId(countryInfo.getCountry_id());
                body.setLeagueId(leagueInfo.getLeague_id());
                body.setLeagueName(leagueInfo.getLeague_name());

            } else  {
                return null;
            }


            String getStandingUrl
                    = "https://apifootball.com/api/?action=get_standings&league_id="+body.getLeagueId()+"&APIkey="+APIKey;
            System.out.println(getStandingUrl);
            ResponseEntity<List<StandingsInfo>> standingsResponse
                    = restTemplate.exchange(getStandingUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<StandingsInfo>>() {});

            List<StandingsInfo>  standingsInfoList =  standingsResponse.getBody();


            for (StandingsInfo standingsInfo: standingsInfoList) {
                if (standingsInfo.getLeague_id().equals(body.getLeagueId())) {
                    body.setTeamName(standingsInfo.getTeam_name());
                    body.setOverall_league_position(standingsInfo.getOverall_league_position());
                    break;
                }
            }
        }

        return body;
    }

    @RequestMapping("/league/{leagueName}")
    public ServerResponseBody getByLeagueName(@PathVariable(value = "leagueName") String c) {


        ServerResponseBody body = new ServerResponseBody();
        if (c != null && !c.isEmpty()) {
            System.out.println("League Name passed");


            RestTemplate restTemplate = new RestTemplate();

            body.setLeagueName(c);


            String leagueUrl
                    = "https://apifootball.com/api/?action=get_leagues&APIkey="+APIKey;
            ResponseEntity<List<LeagueInfo>> leagueResponse
                    = restTemplate.exchange(leagueUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<LeagueInfo>>() {});

            List<LeagueInfo> leagueInfoList  =  leagueResponse.getBody();


            for (LeagueInfo leagueInfo: leagueInfoList) {
                if (leagueInfo.getLeague_name().equals(c)) {
                    body.setLeagueId(leagueInfo.getLeague_id());
                    body.setLeagueName(leagueInfo.getLeague_name());
                    body.setCountryName(leagueInfo.getCountry_name());
                    body.setCountryId(leagueInfo.getCountry_id());
                    break;
                }
            }


            String getStandingUrl
                    = "https://apifootball.com/api/?action=get_standings&league_id="+body.getLeagueId()+"&APIkey="+APIKey;
            System.out.println(getStandingUrl);
            ResponseEntity<List<StandingsInfo>> standingsResponse
                    = restTemplate.exchange(getStandingUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<StandingsInfo>>() {});

            List<StandingsInfo>  standingsInfoList =  standingsResponse.getBody();


            for (StandingsInfo standingsInfo: standingsInfoList) {
                if (standingsInfo.getLeague_id().equals(body.getLeagueId())) {
                    body.setTeamName(standingsInfo.getTeam_name());
                    body.setOverall_league_position(standingsInfo.getOverall_league_position());
                    break;
                }
            }
        }

        return body;



    }


    @RequestMapping("/team/{teamName}")
    public ServerResponseBody getByTeamName(@PathVariable(value = "teamName") String c) {


        ServerResponseBody body = new ServerResponseBody();
        if (c != null && !c.isEmpty()) {
            System.out.println("League Name passed");


            RestTemplate restTemplate = new RestTemplate();

            body.setLeagueName(c);


            String leagueUrl
                    = "https://apifootball.com/api/?action=get_leagues&APIkey="+APIKey;
            ResponseEntity<List<LeagueInfo>> leagueResponse
                    = restTemplate.exchange(leagueUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<LeagueInfo>>() {});

            List<LeagueInfo> leagueInfoList  =  leagueResponse.getBody();


            for (LeagueInfo leagueInfo: leagueInfoList) {


                String getStandingUrl
                        = "https://apifootball.com/api/?action=get_standings&league_id="+leagueInfo.getLeague_id()+"&APIkey="+APIKey;
                ResponseEntity<List<StandingsInfo>> standingsResponse
                        = restTemplate.exchange(getStandingUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<StandingsInfo>>() {});

                List<StandingsInfo>  standingsInfoList =  standingsResponse.getBody();


                for (StandingsInfo standingsInfo: standingsInfoList) {
                    if (standingsInfo.getTeam_name().equals(c)) {
                        body.setTeamName(standingsInfo.getTeam_name());
                        body.setOverall_league_position(standingsInfo.getOverall_league_position());
                        body.setCountryName(standingsInfo.getCountry_name());
                        body.setLeagueName(standingsInfo.getLeague_name());
                        body.setLeagueId(standingsInfo.getLeague_id());
                        break;
                    }
                }
            }


            String fooResourceUrl
                    = "https://apifootball.com/api/?action=get_countries&APIkey="+APIKey;
            ResponseEntity<List<CountryInfo>> response
                    = restTemplate.exchange(fooResourceUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<CountryInfo>>() {});

            List<CountryInfo> countryInfoList = response.getBody();


            for (CountryInfo countryInfo : countryInfoList) {
                if (countryInfo.getCountry_name().equals(body.getCountryName())) {
                    body.setCountryName(countryInfo.getCountry_name());
                    body.setCountryId(countryInfo.getCountry_id());
                    break;

                }
            }



        }

        return body;


    }


    private CountryInfo getCountryByName(String name) {

        RestTemplate restTemplate = new RestTemplate();
        String countryUrl = "https://apifootball.com/api/?action=get_countries&APIkey=" + APIKey;
        ResponseEntity<List<CountryInfo>> response = restTemplate.exchange(countryUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CountryInfo>>() {
                });

        List<CountryInfo> countryInfoList = response.getBody();

        for (CountryInfo countryInfo : countryInfoList) {
            if (countryInfo.getCountry_name().equals(name)) {
                return countryInfo;

            }

        }

        return null;
    }


    public LeagueInfo getLeagueByCountryId(String countryId) {


        RestTemplate restTemplate = new RestTemplate();
        String leagueUrl
                = "https://apifootball.com/api/?action=get_leagues&country_id="+countryId+"&APIkey="+APIKey;
        ResponseEntity<List<LeagueInfo>> leagueResponse
                = restTemplate.exchange(leagueUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<LeagueInfo>>() {});

        List<LeagueInfo> leagueInfoList  =  leagueResponse.getBody();


        for (LeagueInfo leagueInfo: leagueInfoList) {
            if (leagueInfo.getCountry_id().equals(countryId)) {
                return leagueInfo;

            }
        }

        return null;

    }




}