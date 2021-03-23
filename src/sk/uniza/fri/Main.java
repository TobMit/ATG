 /*
  * To change this license header, choose License Headers in Project Properties.
  * To change this template file, choose Tools | Templates
  * and open the template in the editor.
  */
package sk.uniza.fri;
import java.io.FileNotFoundException;

/**
 *
 * @author tomas
 */
class PrikladyGrafy {
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Graf g = Graf.nacitajSubor("src/ATG_DAT/ShortestPath/TEST_mini.hrn");

        //g.cvicenieTriFloydovAlgoritmus();
        g.zakladnyAlgoritmus();
        //g.poleSmernikovMojaVerzia(7);
        g.maticaSmernikov(5);
        //g.zobrazenieShellSort();
    }
}

