package gui;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class GUIManager {
  private final STVGui gui;
  private String input;
  private final CyclicBarrier barrier = new CyclicBarrier(2);

  public GUIManager() {
    this.gui = new STVGui(this);
  }

  public String getInput() {
    try {
      this.barrier.await();
    } catch (InterruptedException | BrokenBarrierException e) {
    }
    this.println(input);
    return this.input;
  }

  public void println(Object o) {
    this.gui.textPane.setText(gui.textPane.getText() + o.toString() + "\n");
  }

  public void setInput(String s) {
    this.input = s;
  }

  public CyclicBarrier getBarrier() {
    return this.barrier;
  }

  public void initialize() {
    this.gui.createUIComponents();
  }

}
