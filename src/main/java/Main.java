import data.Ballot;
import data.Elect;
import voting.VoteCounter;
import voting.VoteParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Podaj sciezke do pliku .csv z wynikami: ");
    String path = scanner.nextLine();
    System.out.print("Podaj liczbe miejsc do obsadzenia: ");
    int seatsToFill = Integer.valueOf(scanner.nextLine());
    System.out.print("Podaj maksymalną liczbe wyborów: ");
    int maxChoices = Integer.valueOf(scanner.nextLine());
    System.out.println("Podaj ewentualnych wykluczonych kandydatów, po przecinku i spacji: ");
    String excludedArg = scanner.nextLine();
    String[] excludedCandidates = excludedArg.split(", ");
    System.out.print("Zapisz logi pod ścieżką: ");
    String savePath = scanner.nextLine();
    try {
      System.setOut(new PrintStream(Files.newOutputStream(Paths.get(savePath.strip()))));
    } catch (IOException e) {
      System.out.println("Nieprawidlowa sciezka zapisu!");
    }

    try {
      Reader file = new InputStreamReader(Files.newInputStream(Paths.get(path.strip())));
      VoteParser parser = new VoteParser(file, maxChoices, excludedCandidates);
      List<Ballot> ballots = parser.parseResults();
      VoteCounter counter = new VoteCounter();
      List<Elect> elect = counter.calculateSTV(ballots, seatsToFill);
      System.out.println("===Koniec===");
      elect.forEach(System.out::println);

    } catch (FileNotFoundException e) {
      System.out.println("Nieprawidlowa sciezka odczytu!");
    } catch (IOException e) {
      System.out.println("Wystapil blad I/O: ");
      e.printStackTrace();
    }
  }
}
