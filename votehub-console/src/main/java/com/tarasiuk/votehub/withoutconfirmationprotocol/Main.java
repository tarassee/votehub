package com.tarasiuk.votehub.withoutconfirmationprotocol;

import com.tarasiuk.votehub.util.RandomSelectionUtil;
import com.tarasiuk.votehub.withoutconfirmationprotocol.data.Voter;
import com.tarasiuk.votehub.withoutconfirmationprotocol.facility.ElectionCommission;
import com.tarasiuk.votehub.withoutconfirmationprotocol.facility.RegistrationBureau;
import com.tarasiuk.votehub.withoutconfirmationprotocol.facility.VotingApp;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.tarasiuk.votehub.withoutconfirmationprotocol.constant.ElectionConstant.CITIZEN_QUANTITY;

public class Main {

    /**
     * withoutconfirmationprotocol: greenScenario, redScenario
     */
    public static void main(String[] args) {
        String scenario = args[0];
        if (Objects.equals(scenario, "green")) {
            greenScenario();
        } else if (Objects.equals(scenario, "red")) {
            redScenario();
        }
    }

    private static void greenScenario() {
        // 0. Initialization
        var candidatesList = List.of("Zaroshenko", "Pelensky");
        var registrationBureau = new RegistrationBureau();
        var electionCommission = new ElectionCommission(candidatesList);

        // 1. Підготовка:

        // БР підраховує кількість потенційних виборців, генерує потрібну кількість ІД номерів та надсилає їх до ВК.
        var votersIds = registrationBureau.generateIds(CITIZEN_QUANTITY);

        // ВК створює таку ж кількість ключів (відкритих та закритих) для генератора випадкових бітів. Для шифрування бюлетенів ключі генеруються однакові для всіх виборців. ВК зберігає ІД виборців та відповідний їм закритий ключ.
        // ВК створює токени, які містять в собі ІД виборців та їх відкриті ключі та надсилає їх до БР.
        var votersTokens = electionCommission.receiveVotersIdsAndGenerateTokens(votersIds);
        registrationBureau.receiveVotersTokens(votersTokens);

        // 2. Реєстрація:
        Collection<Voter> voters = List.of(
                new Voter("Tom", "Black", true),
                new Voter("John", "Lo", true),
                new Voter("Lia", "Lee", true),
                new Voter("Kim", "Chan", true)
        );
        voters.forEach(registrationBureau::registerAndUpdateVoter);


        // 3. Голосування:
        // Виборець встановлює програмний додаток для Е-голосування на свій пристрій (персональний комп’ютер наприклад), входить до свого профілю.
        var votingApp = new VotingApp();
        voters.forEach(voter -> {
            var voterCredentials = voter.getCredentials();
            var userName = voterCredentials.userName();
            var password = voterCredentials.password();
            boolean isAuthenticated = votingApp.authenticateVoterThroughRegistrationBureau(userName, password, registrationBureau);
            voter.setAuthenticatedToVote(isAuthenticated);
        });

        // Після чого підключає токен до свого пристрою.
        voters = voters.stream()
                .filter(Voter::isAuthenticatedToVote)
                .peek(voter -> votingApp.activateVoterWithToken(voter, voter.getCredentials().electionToken()))
                .toList();

        // Виборець обирає кандидата, за якого хоче віддати свій голос, та запускає процес формування Е-бюлетеня та його шифрування.
        voters.forEach(voter -> {
            var candidate = RandomSelectionUtil.selectOneRandomly(candidatesList);
            votingApp.startVotingProcess(voter, candidate, electionCommission);
        });

        // 4. Підрахунок голосів:
        electionCommission.showVotingResults();
    }


    private static void redScenario() {
        // 0. Initialization
        var candidatesList = List.of("Zaroshenko", "Pelensky");
        var registrationBureau = new RegistrationBureau();
        var electionCommission = new ElectionCommission(candidatesList);

        // 1. Підготовка:

        // БР підраховує кількість потенційних виборців, генерує потрібну кількість ІД номерів та надсилає їх до ВК.
        var votersIds = registrationBureau.generateIds(CITIZEN_QUANTITY);

        // ВК створює таку ж кількість ключів (відкритих та закритих) для генератора випадкових бітів. Для шифрування бюлетенів ключі генеруються однакові для всіх виборців. ВК зберігає ІД виборців та відповідний їм закритий ключ.
        // ВК створює токени, які містять в собі ІД виборців та їх відкриті ключі та надсилає їх до БР.
        var votersTokens = electionCommission.receiveVotersIdsAndGenerateTokens(votersIds);
        registrationBureau.receiveVotersTokens(votersTokens);

        // 2. Реєстрація:
        Collection<Voter> voters = List.of(
                new Voter("Tom", "Black", false), // is not eligible to register/vote
                new Voter("John", "Lo", true), // failed authentication
                new Voter("Lia", "Lee", true), // not valid candidate value
                new Voter("Kim", "Chan", true),
                new Voter("Kim", "Chan", true), // try to vote second time
                new Voter("Kate", "Brown", true) // out of range generated tokens
        );
        voters = voters.stream().map(registrationBureau::registerAndUpdateVoter)
                .filter(Objects::nonNull)
                .toList();


        // 3. Голосування:
        // Виборець встановлює програмний додаток для Е-голосування на свій пристрій (персональний комп’ютер наприклад), входить до свого профілю.
        var votingApp = new VotingApp();
        voters.forEach(voter -> {
            var voterCredentials = voter.getCredentials();
            var userName = voterCredentials.userName();
            var password = voterCredentials.password();
            boolean isAuthenticated = votingApp.authenticateVoterThroughRegistrationBureau(userName, password, registrationBureau);
            voter.setAuthenticatedToVote(isAuthenticated);
        });

        // Mocking voter to be not authenticated to vote
        voters.stream().toList().get(0).setAuthenticatedToVote(false);
        // Mocking voter to have the same VoterId - so that simulate situation when someone is trying to vote two times
        var voterId = voters.stream().toList().get(2).getCredentials().electionToken().getVoterId();
        voters.stream().toList().get(3).getCredentials().electionToken().setVoterId(voterId);

        // Після чого підключає токен до свого пристрою.
        voters = voters.stream()
                .filter(Main::validateAuthenticatedVoter)
                .peek(voter -> votingApp.activateVoterWithToken(voter, voter.getCredentials().electionToken()))
                .toList();

        // Виборець обирає кандидата, за якого хоче віддати свій голос, та запускає процес формування Е-бюлетеня та його шифрування.
        voters.forEach(voter -> {
            var candidate = RandomSelectionUtil.selectOneRandomly(candidatesList);

            if (voter.getFirstName().equals("Lia")) {
                candidate = "INVALID_CANDIDATE";
            }

            votingApp.startVotingProcess(voter, candidate, electionCommission);
        });

        // 4. Підрахунок голосів:
        electionCommission.showVotingResults();
    }

    private static boolean validateAuthenticatedVoter(Voter voter) {
        if (voter.isAuthenticatedToVote()) {
            return true;
        } else {
            System.out.printf("Error! Voter is not authenticated to vote! Voter: %s %s\n", voter.getFirstName(), voter.getLastName());
            return false;
        }
    }

}
