package sk.uniza.fri;


//import java.awt.image.Kernel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.lang.reflect.Array;
//import java.util.Arrays;
import java.util.Scanner;


public class MonotonneOcislovanieVrcholov2 {

    private final int n; // pocet vrcholov grafu
    private final int m; // pocet hran grafu
    private final int[][] h; // pole udajov o hranach
    private final int[] z;
    private final int[] e;
    private int nE;
    private final int[] s;
    //private int i; //index vrchola ktoreho odstranenie simulujem v ideg
    private int k; //pocet clenov postupnosti V


    public MonotonneOcislovanieVrcholov2(int paPocetVrcholov, int paPocetHran) {
        this.n = paPocetVrcholov;
        this.m = paPocetHran;
        this.h = new int[1 + this.m][3];

        this.z = new int[this.n + 1];
        this.e = new int[this.n + 1];
        this.nE = 0;
        this.s = new int[this.n + 2];
    }
    /*
    Nacitanie grafu zo suboru:
    Na kazdom riadku su tri cisla, prve a druhe cislo su cisla vrcholov
    a tretie cislo je ohodnotenie hrany.
    Pocet vrcholov aj pocet hran sa urci automaticky. Pocet hran je rovny
    poctu riadkov v subore a pocet vrcholov je rovny najvacsiemu cislu
    vrcholu v udajoch o hranach.
    */
    public static MonotonneOcislovanieVrcholov2 nacitajSubor(String nazovSuboru) throws FileNotFoundException {
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
            s.nextInt();

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
        MonotonneOcislovanieVrcholov2 g = new MonotonneOcislovanieVrcholov2 (pocetVrcholov, pocetHran);

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

        // roztriedenie matice
        g.shellSort(0);
        return g;
    }
    //------------------------------------- Sorty -----------------------------------------------------
    //ShellSort -- zotriedi pole H[][] podla stlpca 0 neklesajuco
    private void shellSort(int s) {
        //double gap;
        int gap;
        for (gap = this.m / 2 + 1; gap >= 1; gap = ( 5 * gap / 11)) {
            if (gap == 2) {
                gap = 3;
            }
            System.out.printf("gap = %d\n", gap);
            for (int i = 1; i + gap <= this.m; i++) {
                int j = i + gap;
                int ki;
                if (this.h[i][s] > this.h[j][s]) {
                    this.swap(i, j);
                    for (ki = i; ki - gap >= 1; ki = ki - gap) {
                        int l = ki - gap;
                        if (this.h[l][s] <= this.h[ki][s]) {
                            break;
                        }
                        this.swap(l, ki);
                    }
                }
            }
        }
    }
    private void swap(int i, int j) {
        int odlozenieH;
        for (int ki = 0; ki <= 2; ki++) {
            odlozenieH = this.h[i][ki];
            this.h[i][ki] = this.h[j][ki];
            this.h[j][ki] = odlozenieH;
        }
    }
    public void maticaSmernikov(int r) {
        //int[] S = new int[n + 2];

        //vynulovanie pola
        for (int i = 0; i < this.n + 1; i++) {
            this.s[i] = 0;
        }

        //s[i] ukazuje na prvý riadok ola H taký te H[k][0] = i

        for (int kk = 1; kk <= this.m; kk++) {
            int i = this.h[kk][0];
            if (this.s[i] == 0) {
                this.s[i] = kk;
            }
            this.s[this.n + 1] = this.m + 1;
        }

        for (int i = this.n; i >= 1; i--) {
            if (this.s[i] == 0) {
                this.s[i] = this.s[i + 1];
            }
        }

        if (r > 0) {
            //Výpis hrán
            for (int i = this.s[r]; i < this.s[r + 1]; i++) {
                int j = this.h[i][1];
                System.out.printf("(%d, %d), cena %d\n", r, j, this.h[i][2]);
            }
        }
    }
    //--------------------------------------- Label Set ----------------------------------------------
    public void monotonneocislovanie() {
        //-1 preto aby sa nám nevypisovali hrany.
        this.maticaSmernikov(-1);

        // V - monotonna postupnost vrcholov
        int[] V = new int[this.n + 1];

        //vstupne stupne vrcholov
        int[] ideg = new int[this.n + 1];
        this.shellSort(0);

        //vynulovanie pola
        for (int i = 1; i < this.n + 1; i++) {
            ideg[i] = 0;
        }

        //Krok 1
        //vypocet vstupnych stupov vrcholov
        for (int j = 1; j < this.m + 1; j++) {
            int vv = this.h[j][1];
            ideg[vv]++;
        }

        //priradenie vrcholov s ideg rovnym 0 do postunosti
        this.k = 0;//nastavenia k na aktualny pocet v postupnosti, cize 0;
        for (int i = 1; i < this.n + 1; i++) {
            if (ideg[i] == 0) {
                this.k++;
                V[this.k] = i;
            }
        }
        //Teraz je V[1], V[2], ..., V[k] postupnost vsetkych vrcholov s ideg 0

        //Simolovanie postunenho odstranovania vrcholov V[1], V[2]... z digrafu

        //Krok 2
        for (int i = 1; i < this.n + 1; i++) {
            for (int j = this.s[V[i]]; j < this.s[V[i] + 1]; j++) {
                int w = this.h[j][1];
                ideg[w]--;
                if (ideg[w] == 0) {
                    this.k++;
                    V[this.k] = w;
                }
            }
            if (this.k == this.n) {
                break;
            }
        }

        //Kontrola ci graf je acyklicky
        //Ak je acyklický tak sa dá prečíslovať tak že platí i < K
        if (this.k != this.n) {
            System.out.println("Graf nie je acyklicky.");
        }
        System.out.println("Postunost je: ");
        for (int i = 0; i < V.length; i++) {
            System.out.print(V[i] + " ");
        }
    }
}


