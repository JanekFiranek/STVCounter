package voting;

import comparators.BordaComparator;
import comparators.VoteComparator;
import data.Ballot;
import data.Elect;

import java.util.*;
import java.util.stream.Collectors;

public class VoteCounter {

  public void redistributeBallots(String selected, double weight, List<String> hopefuls, Map<String, List<Ballot>> allocated, Map<String, Integer> voteCount) {
    List<Ballot> transferred = new ArrayList<>();
    for (Ballot ballot : allocated.get(selected)) {
      boolean reallocated = false;
      int i = ballot.getCurrentPreference() + 1;
      while (!reallocated && i < ballot.getCandidates().size()) {
        String recipient = ballot.getCandidates().get(i);
        if (hopefuls.contains(recipient)) {
          ballot.setCurrentPreference(i);
          ballot.addWeight(weight);
          int currentValue = ballot.getValue();
          if (allocated.keySet().contains(recipient)) {
            allocated.get(recipient).add(ballot);
          } else {
            allocated.put(recipient, new ArrayList<>(Arrays.asList(ballot)));
          }
          if (voteCount.keySet().contains(recipient)) {
            voteCount.put(recipient, voteCount.get(recipient) + currentValue);
          } else {
            voteCount.put(recipient, currentValue);
          }
          voteCount.put(selected, voteCount.get(selected) - currentValue);
          reallocated = true;
          transferred.add(ballot);
        } else {
          i++;
        }

        allocated.put(selected, allocated.get(selected).stream().filter(transferred::contains).collect(Collectors.toList()));
      }
    }
  }

  public void elect(String candidate, int currentRound, List<Elect> elected, Map<String, Integer> voteCount) {
    Elect elect = new Elect(candidate, currentRound, voteCount.get(candidate));
    elected.add(elect);
    System.out.println(elect);
  }

  public String selectFirst(List<String> hopefuls, List<Ballot> ballots, Map<String, Integer> voteCount) {
    BordaComparator bordaComparator = new BordaComparator(ballots, hopefuls);
    int maxVotes = hopefuls.stream().mapToInt(voteCount::get).max().getAsInt();
    System.out.println("Najwieksza liczba glosow - " + maxVotes);
    List<String> leaders = hopefuls.stream().filter(n -> voteCount.get(n) == maxVotes).collect(Collectors.toList());
    for (int i = 0; i < leaders.size(); i++) {
      System.out.println(String.format("Potencjalny elekt %s ma %d głosów i wynik borda %d", leaders.get(i), maxVotes, bordaComparator.calculateBordaScore(leaders.get(i), hopefuls)));
    }
    leaders.sort(bordaComparator);
    return leaders.get(0);
  }

  public String selectLast(List<String> hopefuls, List<Ballot> ballots, Map<String, Integer> voteCount) {
    BordaComparator bordaComparator = new BordaComparator(ballots, hopefuls);
    int minVotes = hopefuls.stream().mapToInt(voteCount::get).min().getAsInt();
    System.out.println("Najmniejsza liczba glosow - " + minVotes);
    List<String> leaders = hopefuls.stream().filter(n -> voteCount.get(n) == minVotes).collect(Collectors.toList());
    for (int i = 0; i < leaders.size(); i++) {
      System.out.println(String.format("Potetncjalny eliminowany %s ma %d głosów i wynik borda %d", leaders.get(i), minVotes, bordaComparator.calculateBordaScore(leaders.get(i), hopefuls)));
    }
    leaders.sort(bordaComparator.reversed());
    return leaders.get(0);
  }

  public List<Elect> calculateSTV(List<Ballot> ballots, int seatsToFill) {
    Map<String, List<Ballot>> allocated = new HashMap<>();
    Map<String, Integer> voteCount = new HashMap<>();
    List<String> hopefuls = new ArrayList<>();
    List<String> eliminated = new ArrayList<>();
    List<Elect> elected = new ArrayList<>();
    List<String> candidates = new ArrayList<>();

    int droopQuote = (int) (1 + ((double) ballots.size() / (seatsToFill + 1)));
    System.out.println(String.format("Kwota droopa jest cechą z (1 + (%d / (%d + 1))) i wynosi %d", ballots.size(), seatsToFill, droopQuote));

    for (Ballot b : ballots) {
      String selected = b.getCandidates().get(0);
      for (String candidate : b.getCandidates()) {
        if (!candidates.contains(candidate)) {
          candidates.add(candidate);
          voteCount.put(candidate, 0);
        }
        if (!allocated.keySet().contains(candidate)) {
          allocated.put(candidate, new ArrayList<>());
        }
      }
      allocated.get(selected).add(b);
      voteCount.put(selected, voteCount.get(selected) + 1);
    }


    hopefuls.addAll(candidates);

    for (String candidate : candidates) {
      System.out.println(String.format("START: %s - %d", candidate, voteCount.get(candidate)));

    }

    int currentRound = 1;
    while (elected.size() < seatsToFill && hopefuls.size() > 0) {
      System.out.println("===Runda " + currentRound + "===");
      VoteComparator voteComparator = new VoteComparator(voteCount);

      List<String> sortedHopefuls = new ArrayList<>(hopefuls);
      sortedHopefuls.sort(voteComparator);
      int surplus = voteCount.get(sortedHopefuls.get(0)) - droopQuote;

      if (surplus >= 0 || hopefuls.size() <= (seatsToFill - elected.size())) {
        String electName = this.selectFirst(sortedHopefuls, ballots, voteCount);
        this.elect(electName, currentRound, elected, voteCount);
        hopefuls.remove(electName);

        if (surplus > 0) {
          double weight = ((double) surplus / voteCount.get(electName));
          this.redistributeBallots(electName, weight, hopefuls, allocated, voteCount);
        }
      } else {
        String eliminatedName = this.selectLast(sortedHopefuls, ballots, voteCount);
        hopefuls.remove(eliminatedName);
        eliminated.add(eliminatedName);
        System.out.println(String.format("W %d rundzie odpada %s", currentRound, eliminatedName));
        this.redistributeBallots(eliminatedName, 1.0, hopefuls, allocated, voteCount);
      }
      currentRound++;
    }

    while ((seatsToFill - elected.size()) > 0 && eliminated.size() > 0) {
      System.out.println("===Runda " + currentRound + "===");
      String electedName = eliminated.remove(0);
      this.elect(electedName, currentRound, elected, voteCount);
      currentRound++;
    }
    return elected;
  }
}

