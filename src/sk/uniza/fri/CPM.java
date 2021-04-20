package sk.uniza.fri;


//import java.awt.image.Kernel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.lang.reflect.Array;
//import java.util.Arrays;
import java.util.Scanner;


public class CPM {

    private final int[][] h;
    private final int m;
    private final int n;

    private int[] Z;//Najskor mozny zaciatok cinnosti
    private int[] K;//Najneskor nutny koniec cinnosti
    //Smerniky na konstrukciu kritickej cesty
    private int[] y;
    private int[] x;
    private int[] s; //Smernik
    private int[] P; //trvanie elementarnej cinnosti
    //private int i; //index vrchola ktoreho odstranenie simulujeme v ideg
    private int k; //pocet clenov postupnosti V

    private int[] V; //Monotonne usporiadanie vrcholov;

    private int T; //Trvanie celeho projektu


    public CPM(int paPocetVrcholov, int paPocetHran) throws FileNotFoundException {
        this.n = paPocetVrcholov;
        this.m = paPocetHran;
        this.h = new int[1 + this.m][3];

        this.V = new int[this.n + 1];

        this.Z = new int[this.n + 1];
        this.K = new int[this.n + 1];

        this.y = new int[this.n + 1];
        this.x = new int[this.n + 1];
        this.P = new int[this.n + 1];

        this.s = new int[this.n + 2];
        this.nacitanieSuboruTrvaniaCinnosti();
    }

    public void nacitanieSuboruTrvaniaCinnosti() throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("src/ATG_DAT/ACYKL/CPM_mini.tim"));
        int riadok = 0;
        while (s.hasNext()) {
            riadok++;
            this.P[riadok] = s.nextInt();
        }

        if (riadok != this.n) {
            System.out.println("Pocet vrcholov sa nerovna poctu elementarnych cinnosti.");
            System.exit(0);
        }
    }

    /*
    Nacitanie grafu zo suboru:
    Na kazdom riadku su tri cisla, prve a druhe cislo su cisla vrcholov
    a tretie cislo je ohodnotenie hrany.
    Pocet vrcholov aj pocet hran sa urci automaticky. Pocet hran je rovny
    poctu riadkov v subore a pocet vrcholov je rovny najvacsiemu cislu
    vrcholu v udajoch o hranach.
    */
    public static CPM nacitajSubor(String nazovSuboru) throws FileNotFoundException {
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
        CPM g = new CPM(pocetVrcholov, pocetHran);

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
        g.shellSortH(0);
        return g;
    }

    public void cpm() {
        this.monotonneOcislovanie();
        /*
         * Zaciatok CPM
         */

        //Vypocet najskor moznych zaciatkov cinnosti

        //Inicializacia
        for (int i = 1; i < this.n + 1; i++) {
            this.x[i] = 0;
            this.Z[i] = 0;
        }

        //Posledne V[n] nema naslednikov - preto iba this.n
        for (int i = 1; i < this.n; i++) {
            int r = this.V[i];
            //Riadky v ktorych existuje hrana (r, w)
            for (int j = this.s[r]; j < this.s[r + 1]; j++) {
                int w = this.h[j][1]; //Cinnost ktore je koncom hrany z r
                //this.Z[r] + this.P[r] je koniec cinnosti r
                if (this.Z[w] < this.Z[r] + this.P[r]) {
                    this.Z[w] = this.Z[r] + this.P[r];
                    this.x[w] = r;
                }
            }
        }

        //Vypocitanie trvania T projektu ako maximum najskor moznych
        // koncov vsetkych cinnosti
        //this.Z[i] + this.P[i] je najskor mozny koniec cinnosti i
        this.T = 0;
        for (int i = 1; i < this.n + 1; i++) {
            if (this.T < this.Z[i] + this.P[i]) {
                this.T = this.Z[i] + this.P[i];
            }
        }

        //Vypocitanie najneskor nutnych koncov cinnosti

        for (int i = 1; i < this.n + 1; i++) {
            this.K[i] = this.T;
            this.y[i] = 0;
        }

        for (int i = this.n - 1; i > 0; i--) {
            int r = this.V[i];
            for (int j = this.s[r]; j < this.s[r + 1]; j++) {
                int w = this.h[j][1];
                if (this.K[r] > this.K[w] - this.P[w]) {
                    this.K[r] = this.K[w] - this.P[w];
                    this.y[r] = w;
                }
            }
        }

        System.out.println("Celkove trvanie projektu: " + this.T);

        //Vypisanie cinnosti, trvanie cinnosti, zaciatku cinnosti,
        // konca cinnosti, rezervy cinnosti

        System.out.println("Nezoradene");
        StringBuilder vypis = new StringBuilder();
        for (int i = 1; i < this.n + 1; i++) {
            vypis.append("Cinnost ").append(i).append(": ")
                    .append("trvanie cinnosti ").append(this.P[i])
                    .append(", z[i]: ").append(this.Z[i])
                    .append(", k[i] : ").append(this.K[i])
                    .append(", rezerva cinnosti : ").append(this.K[i] - this.Z[i] - this.P[i])
                    .append("\n");
        }
        System.out.println(vypis);

        vypis = new StringBuilder();
        System.out.println("Zoradene");
        for (int t = 1; t < this.n + 1; t++) {
            int i = this.V[t];
            vypis.append("Cinnost ").append(i).append(": ")
                    .append("trvanie cinnosti ").append(this.P[i])
                    .append(", z[i]: ").append(this.Z[i])
                    .append(", k[i] : ").append(this.K[i])
                    .append(", rezerva cinnosti : ").append(this.K[i] - this.Z[i] - this.P[i])
                    .append("\n");
        }

        System.out.println(vypis);

    }

    private void monotonneOcislovanie() {
        this.smernikovyVektor();


        //vstupne stupne vrcholov
        int[] ideg = new int[this.n + 1];

        this.shellSortH(0);

        //vynulovanie pola ideg
        for (int j = 1; j < this.n + 1; j++) {
            ideg[j] = 0;
        }

        //vypocet vstupnych stupnov vrcholov
        for (int j = 1; j < this.m + 1; j++) {
            int vv = this.h[j][1];
            ideg[vv]++;
        }

        //priradenie vrcholov s ideg rovnym 0 do postupnosti
        this.k = 0; //nastavenia k na aktualny pocet prvkov v postupnosti, cize 0
        for (int j = 1; j < this.n + 1; j++) {

            if (ideg[j] == 0) {
                this.k++;
                this.V[this.k] = j;
            }
        }

        //Teraz je V[1], V[2], ..., V[k] postupnost vsetkych vrcholov s ideg 0

        //Simulovanie postupneho odstranovania vrcholov V[1], V[2]... z digrafu

        for (int i = 1; i < this.n + 1; i++) {
            for (int j = this.s[this.V[i]]; j < this.s[this.V[i] + 1]; j++) {
                int w = this.h[j][1];
                ideg[w]--;
                if (ideg[w] == 0) {
                    this.k++;
                    this.V[this.k] = w;
                }
            }
            if (this.k == this.n) {
                break;
            }
        }
        //Kontrola ci graf je acyklicky
        if (this.k != this.n) {
            System.out.println("Graf nie je acyklicky");
        } else {
            System.out.println("Postupnost je: ");
            for (int i = 1; i < this.V.length; i++) {
                System.out.print(this.V[i] + " ");
            }
        }
    }

    // ShellSort -- zotriedi pole H[][] podla stlpca 0 neklesajuco
    public void shellSortH(int stupecTriedenia) {
        int gap;
        for (gap = this.m / 2 + 1; gap >= 1; gap = (int) (gap / 2.2)) {
            if (gap == 2) {
                gap = 3;
            }
            for (int i = 1; i + gap <= this.m; i += 1) {
                int j = i + gap;
                int k;
                if (this.h[i][stupecTriedenia] > this.h[j][stupecTriedenia]) {
                    this.swapH(i, j);
                    for (k = i; k - gap >= 1; k = k - gap) {
                        int l = k - gap;
                        if (this.h[l][stupecTriedenia] <= this.h[k][stupecTriedenia]) {
                            break;
                        }
                        this.swapH(l, k);
                    }
                }
            }
        }
    }

    // vymeni riadky i, j v poli H[][]
    void swapH(int i, int j) {
        int tmp = 0;
        for (int k = 0; k <= 2; k += 1) {
            tmp = this.h[i][k];
            this.h[i][k] = this.h[j][k];
            this.h[j][k] = tmp;
        }
    }

    public void smernikovyVektor() {
        for (int i = 0; i < this.n + 2; i++) {
            this.s[i] = 0;
        }

        //S[i] bude ukazovat na prvy riadok pola H tak, ze H[S[i][0]] = i. Ak H v stlpci 0 neobsahuje i, potom S[i] = 0

        for (int k = 1; k <= this.m; k++) {
            int i = this.h[k][0];
            if (this.s[i] == 0) {
                this.s[i] = k;
            }
        }
        this.s[this.n + 1] = this.m + 1;

        //Ak s[i] = 0, nastav S[i] = k, kde k je prvy riadok pola H taky, ze H[k][0] > i

        for (int i = this.n; i >= 1; i--) {
            if (this.s[i] == 0) {
                this.s[i] = this.s[i + 1];
            }
        }
    }
}