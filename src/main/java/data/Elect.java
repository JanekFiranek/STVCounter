package data;

public class Elect {
  private final String name;
  private final int roundElected;
  private final int voteCount;

  public Elect(final String name, final int roundElected, final int voteCount) {
    this.name = name;
    this.roundElected = roundElected;
    this.voteCount = voteCount;
  }

  public String getName() {
    return name;
  }

  public int getRoundElected() {
    return roundElected;
  }

  public int getVoteCount() {
    return voteCount;
  }

  @Override
  public String toString() {
    return String.format("%s wybrany w rundzie %d, z liczbą głosów %d", this.name, this.roundElected, this.voteCount);
  }
}
