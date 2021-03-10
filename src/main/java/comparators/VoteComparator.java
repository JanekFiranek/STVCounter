package comparators;

import java.util.Comparator;
import java.util.Map;

public class VoteComparator implements Comparator<String> {

  private final Map<String, Integer> voteCount;

  public VoteComparator(final Map<String, Integer> voteCount) {
    this.voteCount = voteCount;
  }

  @Override
  public int compare(final String o1, final String o2) {
    return this.voteCount.get(o2) - this.voteCount.get(o1);
  }
}
