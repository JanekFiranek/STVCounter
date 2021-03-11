import data.Ballot;
import data.Elect;
import gui.GUIManager;
import gui.STVGui;
import voting.VoteCounter;
import voting.VoteParser;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    GUIManager gui = new GUIManager();
    gui.initialize();
    gui.println("Podaj sciezke do pliku .csv z wynikami: ");
    String path = gui.getInput();
    gui.println("Podaj liczbe miejsc do obsadzenia: ");
    int seatsToFill = Integer.valueOf(gui.getInput());
    gui.println("Podaj maksymalną liczbe wyborów: ");
    int maxChoices = Integer.valueOf(gui.getInput());
    gui.println("Podaj ewentualnych wykluczonych kandydatów, po przecinku i spacji: ");
    String excludedArg = gui.getInput();
    String[] excludedCandidates = excludedArg.split(", ");
    gui.println("Zapisz logi pod ścieżką: ");
    String savePath = gui.getInput();
    try {
      System.setOut(new PrintStream(new FileOutputStream(Paths.get(savePath.strip()).toFile())));
    } catch (IOException e) {
      System.out.println("Nieprawidlowa sciezka zapisu!");
    }

    try {
      Reader file = new InputStreamReader(new FileInputStream(Paths.get(path.strip()).toFile()));
      VoteParser parser = new VoteParser(file, maxChoices, excludedCandidates);
      List<Ballot> ballots = parser.parseResults();
      VoteCounter counter = new VoteCounter();
      List<Elect> elect = counter.calculateSTV(ballots, seatsToFill);
      gui.println("Wyniki wyborów: ");
      elect.forEach(gui::println);
      gui.println("Logi zapisano do pliku");

    } catch (FileNotFoundException e) {
      System.out.println("Nieprawidlowa sciezka odczytu!");
    } catch (IOException e) {
      System.out.println("Wystapil blad I/O: ");
      e.printStackTrace();
    }
  }
}
