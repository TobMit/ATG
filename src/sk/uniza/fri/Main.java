 /*
  * To change this license header, choose License Headers in Project Properties.
  * To change this template file, choose Tools | Templates
  * and open the template in the editor.
  */
package sk.uniza.fri;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

 /**
 *
 * @author tomas
 */
class PrikladyGrafy {
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws IOException {
        String meno = "";
        Graf g = null;
        Scanner volba = new Scanner(System.in);
        System.out.println("Vyberte digrafu: ");
        System.out.println("1. Florida");
        System.out.println("2. NewYork");
        System.out.println("3. Pr1");
        System.out.println("4. SlovRep");
        System.out.println("5. Strakonice");
        System.out.println("6. TEST_mini");
        System.out.println("0. Koniec");
        System.out.print("Vaša volba: ");

        switch (volba.nextInt()) {
            case 1:
                g = Graf.nacitajSubor("src/ATG_DAT/ShortestPath/Florida.hrn");
                meno = "Florida";
                break;
            case 2:
                g = Graf.nacitajSubor("src/ATG_DAT/ShortestPath/NewYork.hrn");
                meno = "NewYork";
                break;
            case 3:
                g = Graf.nacitajSubor("src/ATG_DAT/ShortestPath/pr1.hrn");
                meno = "Pr1";
                break;
            case 4:
                g = Graf.nacitajSubor("src/ATG_DAT/ShortestPath/SlovRep.hrn");
                meno = "SlovRep";
                break;
            case 5:
                g = Graf.nacitajSubor("src/ATG_DAT/ShortestPath/Strakonice.hrn");
                meno = "Strakonice";
                break;
            case 6:
                g = Graf.nacitajSubor("src/ATG_DAT/ShortestPath/TEST_mini.hrn");
                meno = "TEST_mini";
                break;
            default:
                System.exit(0);
        }

        while (true) {
            System.out.println();
            System.out.println("Vyberte algoritmu: ");
            System.out.println("1. Zakladny Algoritmus");
            System.out.println("2. Floydov Algoritmus");
            System.out.println("3. Matica smernikov");
            System.out.println("4. Informácie o digrafe");
            System.out.println("5. Výpis digrafu");
            System.out.println("6. LababelSet");
            System.out.println("0. Koniec");
            System.out.print("Vaša volba: ");

            switch (volba.nextInt()) {
                case 1:
                    g.zakladnyAlgoritmus();
                    break;
                case 2:
                    g.cvicenieTriFloydovAlgoritmus();
                    break;
                case 3:
                    System.out.println("Zadajte smernik: ");
                    g.maticaSmernikov(volba.nextInt());
                    break;
                case 4:
                    System.out.printf("Meno: %s\n", meno);
                    g.printInfo();
                    break;
                case 5:
                    g.zobrazenieMatice();
                    break;
                case 6:
                    g.labelSetAlgoritmus();
                    break;
                default:
                    System.exit(0);
            }

            System.out.println("\nPre pokračovanie stlačte ENTER");
            System.in.read();

        }

        //Graf g = Graf.nacitajSubor("src/ATG_DAT/ShortestPath/TEST_mini.hrn");


        //g.cvicenieTriFloydovAlgoritmus();
        //g.zakladnyAlgoritmus();
        //g.poleSmernikovMojaVerzia(7);
        //g.maticaSmernikov(5);
        //g.zobrazenieShellSort();
    }
}

