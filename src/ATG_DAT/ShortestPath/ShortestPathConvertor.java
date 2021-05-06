package ATG_DAT.ShortestPath;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * 2. 3. 2021 - 15:26
 *
 * @author Tobias
 */
public class ShortestPathConvertor {
    private final int n; // pocet vrcholov grafu
    private final int m; // pocet hran grafu
    private final int[][] h; // pole udajov o hranach
    private final String menoSuboru;

    public ShortestPathConvertor(int paPocetVrcholov, int paPocetHran, String nazovSuboru) {
        this.n = paPocetVrcholov;
        this.m = paPocetHran;
        this.h = new int[1 + this.m][3];
        this.menoSuboru = nazovSuboru;
    }
    /*
    Nacitanie grafu zo suboru:
    Na kazdom riadku su tri cisla, prve a druhe cislo su cisla vrcholov
    a tretie cislo je ohodnotenie hrany.
    Pocet vrcholov aj pocet hran sa urci automaticky. Pocet hran je rovny
    poctu riadkov v subore a pocet vrcholov je rovny najvacsiemu cislu
    vrcholu v udajoch o hranach.
    */
    public static ShortestPathConvertor nacitajSubor(String nazovSuboru)
            throws FileNotFoundException {
        File file = new File(nazovSuboru);
        String menoSuboru = file.getName();
        //System.exit(0);
        // otvorim subor a pripravim Scanner pre nacitavanie
        Scanner s = new Scanner(new FileInputStream(nazovSuboru));

        // najskor len zistim pocet vrcholov a pocet hran
        int pocetVrcholov = 1;
        int pocetHran = 0;
        // prejdem cely subor
        while (s.hasNext()) {
            // nacitam udaje o hrane
            int u = s.nextInt();
            int v = s.nextInt();
            int c = s.nextInt();

            // nacital som hranu, zvysim ich pocet o 1
            pocetHran++;

            // skontrolujem, ci netreba zvysit pocet vrcholov
            if (pocetVrcholov < u) {
                pocetVrcholov = u;
            }
            if (pocetVrcholov < v) {
                pocetVrcholov = v;
            }
        }
        // ukoncim nacitavanie zo suboru
        s.close();

        // vytvorim objekt grafu s potrebnym poctom vrcholo v aj hran
        ShortestPathConvertor g = new ShortestPathConvertor(pocetVrcholov, pocetHran, menoSuboru);

        // po druhy krat otvorim ten isty subor,
        // uz pozanm pocet vrcholov aj hran a mam alokovanu pamat
        s = new Scanner(new FileInputStream(nazovSuboru));

        // postune nacitam vsetky hrany
        // tentokrat si ich uz budem aj ukladat do pamate
        for (int j = 1; j <= pocetHran; j++) {
            int u = s.nextInt();
            int v = s.nextInt();
            int c = s.nextInt();

            g.h[j][0] = u;
            g.h[j][1] = v;
            g.h[j][2] = c;
        }
        return g;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String cestaSuboru = "src/ATG_DAT/ShortestPath/Florida.hrn";
        ShortestPathConvertor convertor = ShortestPathConvertor.nacitajSubor(cestaSuboru);
        convertor.skonvertuj(cestaSuboru);
    }

    private void skonvertuj(String cestaSuboru) {
        System.out.println("Konvertujem");
        File saveSubor = new File(cestaSuboru.replace(menoSuboru, "") + menoSuboru + "b");
        try (DataOutputStream save = new DataOutputStream(new FileOutputStream(saveSubor))) {
            save.writeInt(0x415447); // V hex sústave ATG
            save.writeInt(this.n);
            save.writeInt(this.m);// pocet hrán
            int desatPercent = (m / 10)  * 1;
            int dvadsatPercent = (m / 10)  * 2;
            int tridsatPercent = (m / 10)  * 3;
            int stirydsatPercent = (m / 10)  * 4;
            int pedesiatPercent = (m / 10)  * 5;
            int sedesiatPercent = (m / 10)  * 6;
            int sedemdesiatPercent = (m / 10)  * 7;
            int osemdesiatPercent = (m / 10)  * 8;
            int devedesiatPercent = (m / 10)  * 9;
            for (int i = 0; i < m + 1 ; i++) {
                if (i == desatPercent) {
                    System.out.println("Skonvetované na 10%");
                } else if (i == dvadsatPercent) {
                    System.out.println("Skonvetované na 20%");
                } else if (i == tridsatPercent) {
                    System.out.println("Skonvetované na 30%");
                } else if (i == stirydsatPercent) {
                    System.out.println("Skonvetované na 40%");
                } else if (i == pedesiatPercent) {
                    System.out.println("Skonvetované na 50%");
                } else if (i == sedesiatPercent) {
                    System.out.println("Skonvetované na 60%");
                } else if (i == sedemdesiatPercent) {
                    System.out.println("Skonvetované na 70%");
                } else if (i == osemdesiatPercent) {
                    System.out.println("Skonvetované na 80%");
                } else if (i == devedesiatPercent) {
                    System.out.println("Skonvetované na 90%");
                }
                save.writeInt(this.h[i][0]);
                save.writeInt(this.h[i][1]);
                save.writeInt(this.h[i][2]);
            }
            save.close();
            System.out.println("Skonvertované.");
        } catch (FileNotFoundException e) {
            System.out.println("Nepodarilo sa ulozit súbor - asi zly nazov.");
        } catch (IOException e) {
            System.out.println("Nepodarilo sa ulozit subor.");
            e.printStackTrace(System.out);
        }
    }
}
