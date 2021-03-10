package comparators;

import data.Ballot;

import java.util.Comparator;
import java.util.List;

public class BordaComparator implements Comparator<String> {

  private final List<Ballot> ballots;
  private final List<String> hopefuls;

  public BordaComparator(final List<Ballot> ballots, final List<String> hopefuls) {
    this.ballots = ballots;
    this.hopefuls = hopefuls;
  }

  public int calculateBordaScore(String candidate, List<String> hopefuls) {
    int sum = 0;
    for (int i = 0; i < this.ballots.size(); i++) {
      Ballot b = this.ballots.get(i);
      for (int j = 0; j < b.getCandidates().size(); j++) {
        if (b.getCandidates().get(j).equals(candidate)) {
          sum += hopefuls.size() - (j + 1);
        }
      }
    }
    return sum;
  }

  @Override
  public int compare(final String o1, final String o2) {
    return this.calculateBordaScore(o2, this.hopefuls) - this.calculateBordaScore(o1, this.hopefuls);
  }
}
