package voting;

import com.opencsv.CSVReader;
import data.Ballot;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class VoteParser {

  private static final Pattern VOTE_PATTERN = Pattern.compile("(\\d) Wybór");
  private final Reader csvReader;
  private final int maxChoices;
  private final String[] excludedCandidates;

  public VoteParser(final Reader csvReader, final int maxChoices, final String[] excludedCandidates) {
    this.csvReader = csvReader;
    this.maxChoices = maxChoices;
    this.excludedCandidates = excludedCandidates;
  }

  /*public List<Ballot> parseResults() throws IOException {
    CSVReader csvParser = new CSVReader(this.csvReader);
    String[] firstLine = csvParser.readNext();
    List<Ballot> ballots = new ArrayList<>();
    final List<String> candidates = Arrays.stream(Arrays.copyOfRange(firstLine, 2, firstLine.length)).map(n -> n.substring(52, n.length() - 1)).collect(Collectors.toList());
    Set<String> candidatesSet = new HashSet<>(candidates);
    for(String candidate : candidatesSet) {
      if(Collections.frequency(candidates, candidate) > 1) {
        System.out.println("Uwaga! " + candidate + " występuje więcej niż raz w pliku.");
      }
    }
    for (String[] line : csvParser) {
      String[] choices = new String[this.maxChoices];
      for (int i = 1; i < line.length; i++) {
        Matcher matcher = VOTE_PATTERN.matcher(line[i]);
        if (matcher.matches()) {
          choices[Integer.parseInt(matcher.group(1)) - 1] = candidates.get(i - 2);
        }
      }
      List<String> choicesList = new ArrayList<>(Arrays.asList(choices));
      choicesList.removeIf(Objects::isNull);
      for (String excludedCandidate : excludedCandidates) {
        choicesList.removeIf(n -> {
          boolean toBeRemoved = n.equals(excludedCandidate);
          if(toBeRemoved) {
            System.out.println("Wyrzucam " + excludedCandidate);
          }
          return toBeRemoved;
        });
      }
      if (choicesList.size() > 0) {
        ballots.add(new Ballot(choicesList));
      }
    }
    return ballots;
  }
  */

  //Szybki fix na wybory na kanclerza bo spierdoliłem format znowu xD
  public List<Ballot> parseResults() throws IOException {
    CSVReader csvParser = new CSVReader(this.csvReader);
    String[] firstLine = csvParser.readNext();
    List<Ballot> ballots = new ArrayList<>();
    for(String[] line : csvParser) {
      List<String> choicesList = new ArrayList<>();
      choicesList.add(line[2]);
      choicesList.add(line[3]);
      choicesList.removeIf(n -> n.length() == 0);
      if(choicesList.size() > 0) {
        ballots.add(new Ballot(choicesList));
      }
    }
    return ballots;
  }

}
