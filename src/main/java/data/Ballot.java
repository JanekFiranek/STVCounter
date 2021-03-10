package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ballot {
  private List<String> candidates;
  private List<Double> weights;
  private int value;
  private int currentPreference;

  public Ballot(List<String> candidates) {
    this.candidates = candidates;
    this.weights = new ArrayList<>(Arrays.asList(1.0));
    this.value = 1;
    this.currentPreference = 0;
  }

  public void addWeight(double weight) {
    this.weights.add(weight);
    this.value *= weight;
  }

  public int getValue() {
    return this.value;
  }

  public List<String> getCandidates() {
    return candidates;
  }

  public int getCurrentPreference() {
    return currentPreference;
  }

  public void setCurrentPreference(final int i) {
    this.currentPreference = i;
  }

  @Override
  public String toString() {
    return this.candidates.toString();
  }
}
