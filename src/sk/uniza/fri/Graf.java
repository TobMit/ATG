/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri;


//import java.awt.image.Kernel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.lang.reflect.Array;
//import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author tomas
 */
public class Graf {

    private int n; // pocet vrcholov grafu
    private int m; // pocet hran grafu
    private int H[][]; // pole udajov o hranach
    private int[] z;
    private int[] E;
    private int nE;
    private int[] t;
    private int[] x;
    private int[] S;

    public Graf(int paPocetVrcholov, int paPocetHran) {
        this.n = paPocetVrcholov;
        this.m = paPocetHran;
        this.H = new int[1 + m][3];

        this.z = new int[this.n + 1];
        this.E = new int[this.n + 1];
        this.nE = 0;
        this.S = new int[this.n + 2];

        this.t = new int[this.n + 1];
        this.x = new int[this.n + 1];
    }
    /*
    Nacitanie grafu zo suboru:
    Na kazdom riadku su tri cisla, prve a druhe cislo su cisla vrcholov
    a tretie cislo je ohodnotenie hrany.
    Pocet vrcholov aj pocet hran sa urci automaticky. Pocet hran je rovny
    poctu riadkov v subore a pocet vrcholov je rovny najvacsiemu cislu
    vrcholu v udajoch o hranach.
    */
    public static Graf nacitajSubor(String nazovSuboru)
            throws FileNotFoundException {
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
        Graf g = new Graf(pocetVrcholov, pocetHran);

        // po druhy krat otvorim ten isty subor,
        // uz pozanm pocet vrcholov aj hran a mam alokovanu pamat
        s = new Scanner(new FileInputStream(nazovSuboru));

        // postune nacitam vsetky hrany
        // tentokrat si ich uz budem aj ukladat do pamate
        for (int j = 1; j <= pocetHran; j++) {
            int u = s.nextInt();
            int v = s.nextInt();
            int c = s.nextInt();

            g.H[j][0] = u;
            g.H[j][1] = v;
            g.H[j][2] = c;
        }

        // roztriedenie matice
        g.shellSort(0);
        return g;
    }

    public void zakladnyAlgoritmus() {
        int v;
        int u;
        //nacitanie vstupov
        Scanner scanner = new Scanner(System.in);
        System.out.println("Zaciatocny vrchol");
        u = scanner.nextInt();
        System.out.println("Konecny vrchol");
        v = scanner.nextInt();
        //System.out.println(u + " " + v);

        // 1. krok - inicializácia
        int[] t = new int[this.n + 1];
        int[] x = new int[this.n + 1];

        for (int i = 0; i <= this.n; i++) {
            t[i] = Integer.MAX_VALUE / 2 - 2;
            x[i] = 0;
        }

        //Hodnota zaciatocneho vrcholu na 0
        t[u] = 0;

        //2.Krok

        boolean zlesenie = true;

        while (zlesenie) {
            zlesenie = false;


            for (int k = 1; k <= this.m; k++) {
                //Zaciatok hrany
                int i = H[k][0];
                //Koniec hrany
                int j = H[k][1];
                //Cena hrany
                int cij = H[k][2];

                if (t[j] > t[i] + cij) {
                    t[j] = t[i] + cij;
                    x[j] = i;
                    zlesenie = true;
                }

            }
        }
        System.out.println("Vzdialenosť z vrchola " + u + " do vrchola " + v + " je " + t[v]);

        //Najkratsia cesta
        int w = v;
        System.out.println(w);
        while (x[w] > 0) {
            System.out.println(x[w]);
            w = x[w];
        }
    }



    public void printInfo() {
        System.out.println("Pocet vrcholov: " + n);
        System.out.println("Pocet hran: " + m);
    }

    public void cvicenieTriFloydovAlgoritmus() {

        int[][] maticaVzdialenosti = new int [this.n + 1][this.n + 1];

        //Vynovanie pola
        for (int i = 1; i < this.n + 1; i++) {
            for (int j = 1; j < this.n + 1; j++) {
                maticaVzdialenosti[i][j] = Integer.MAX_VALUE / 2 - 2;
            }
        }

        for (int i = 1; i < this.n + 1; i++) {
            maticaVzdialenosti[i][i] = 0;
        }

        // vytvorenie matice maticaVzdialenosti
        for (int h = 1; h < this.m + 1; h++) {
            int i = this.H[h][0];
            int j = this.H[h][1];
            int c = this.H[h][2];

            maticaVzdialenosti[i][j] = c;
        }
        for (int k = 1; k < this.n + 1; k++) {
            for (int i = 1; i < this.n + 1; i++) {
                for (int j = 1; j < this.n + 1; j++) {
                    if (maticaVzdialenosti[i][j] > maticaVzdialenosti[i][k] + maticaVzdialenosti[k][j]) {
                        maticaVzdialenosti[i][j] = maticaVzdialenosti[i][k] + maticaVzdialenosti[k][j];
                    }
                }
            }
        }

        // pekný vipis matice.
        for (int i = 1; i < this.n + 1; i++) {
            System.out.printf("\n [");
            for (int j = 1; j < this.n + 1; j++) {
                if (maticaVzdialenosti[i][j] < m + 1) {
                    System.out.printf("%3d, ", maticaVzdialenosti[i][j]);
               } else {
                    System.out.printf(" - , ");
                }
            }
            System.out.printf("]\n");
        }
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
                int k;
                if (this.H[i][s] > this.H[j][s]) {
                    this.swap(i, j);
                    for (k = i; k - gap >= 1; k = k - gap) {
                        int l = k - gap;
                        if (this.H[l][s] <= this.H[k][s]) {
                            break;
                        }
                        this.swap(l, k);
                    }
                }
            }
        }
    }

    //BubleSort -- zotriedi pole H[][] podla stlpca 0 neklesajuco
    private void bubleSort() {
        int zlepsenie = 1;
        while (zlepsenie != 0) {
            zlepsenie = 0;
            for (int k = 1; k < m; k++) {
                if (this.H[k][0] > this.H[k + 1][0]) {
                    this.swap(k, k + 1);
                    zlepsenie = 1;
                }
            }
        }
    }

    private void swap(int i, int j) {
        int odlozenieH = 0;
        for (int k = 0; k <= 2; k++) {
            odlozenieH = this.H[i][k];
            this.H[i][k] = this.H[j][k];
            this.H[j][k] = odlozenieH;
        }
    }


    public void zobrazenieMatice() {
        //this.shellSort(2);
        //m - počet hrán
        //n - počet vrcholov
        for (int i = 1; i <= this.m; i++) {
            System.out.printf("%2d.  ", i);
            for (int j = 0; j < 3; j++) {
                System.out.print(this.H[i][j] + " ");
            }
            System.out.println();
        }
    }

    // -------------------------------------- Matice Smerníkov ---------------------------------------
    public void poleSmernikovMojaVerzia(int r) {
        //inicialzicia vynulovanie pola S[]
        int velkostMatice = this.H[m][0];// velkosť polaS je určená najväčším prvom v pole H
        int[] poleS = new int[velkostMatice + 1]; //<--- v prezentáci nie je určená veľkosť matice, takže sa ju snažím vypočítať podľa posledného prvku
        //for (int i = 0; i < n + 1; i++) {  //<----------------------- nerozumiem ako je to myslel, pretože n je omnoho väčšie ako veľkosť matice
                                           //dalej nie je určené presne aká byť veľká matica,
        //    poleS[i] = 0;
       // }

        //vynulovanie pola podlaS mňa
        for (int i = 0; i < poleS.length; i++) {
            poleS[i] = 0;
        }

        //poleS bude ukazovať na prvý riadok pola H tak, že H[poleS[i]][0] = i.
        //Ak v H vstlpci 0 neobsahuje i potom poleS[i] = 0
        //Inak. PoleS[i] --> i (index) je číslo vrchola na prvom mieste poľa H
        //               --> hodnota poleS[i] je reálny riadok kde sa i nachádza prvý krát
        for (int k = 1; k <= m; k++) {
            int i = this.H[k][0];
            if (poleS[i] == 0) {
                poleS[i] = k;
            }

        }

        //poleS[this.n + 1] = this.m + 1;  <--- ukáže na riadok nevyužitý v riadku n + 1


        //výpisy aby som chápal čo sa tam vlastne robí
//        System.out.println("n:" + this.n + " m:" + this.m + " veľkosť pola H: " + this.H.length + " veľkosť pola S:" + poleS.length);
//        System.out.println(" i  S[i]");
//        for (int i = 0; i < poleS.length; i++) {
//            System.out.printf("%2d  %2d\n", i, poleS[i]);
//        }
//        System.out.printf("\n");


        //Môže sa stať že tabuľka nejaké prky neobsahuje, preto ich treba zaplniť
        //robí sa to tak že sa zaplní číslom ktoré nasleduje, preto sa poleS prechádza odzadu
        for (int i = poleS.length - 1; i >= 1; i--) {
            if (poleS[i] == 0) {
                poleS[i] = poleS[i + 1];
            }
        }


        //výpis všetkých hrán z H+(r)
        for (int i = poleS[r]; i < poleS[r + 1]; i++) {
            int j = this.H[i][1];
            System.out.printf("(%d, %d), cena %d\n", r, j, this.H[i][2]);
        }
    }

    public void maticaSmernikov(int r) {
        //int[] S = new int[n + 2];

        //vynulovanie pola
        for (int i = 0; i < n + 1; i++) {
            S[i] = 0;
        }

        //s[i] ukazuje na prvý riadok ola H taký te H[k][0] = i

        for (int k = 1; k <= m; k++) {
            int i = this.H[k][0];
            if (S[i] == 0) {
                S[i] = k;
            }
            S[n + 1] = m + 1;
        }

        for (int i = n; i >= 1; i--) {
            if (S[i] == 0) {
                S[i] = S[i + 1];
            }
        }

        if (r > 0) {
            //Výpis hrán
            for (int i = S[r]; i < S[r + 1]; i++) {
                int j = this.H[i][1];
                System.out.printf("(%d, %d), cena %d\n", r, j, this.H[i][2]);
            }
        }
    }

    //--------------------------------------- Label Set ----------------------------------------------
    public void labelSetAlgoritmus() {
        //-1 preto aby sa nám nevypisovali hrany.
        this.maticaSmernikov(-1);
        int u;
        int v;

        //Vrcholy mnoziny E su E[1], E[2], ..., E[nE - 1], E[nE]
        //nE - pocet prvkov mnoziny E
        int[] E = new int[this.n + 1];

        //z[i] = 0 ak vrchol i nepatri do mnoziny E
        //z[i] = 1 ak vrchol i patri do mnoziny E
        int[] z = new int[this.n + 1];

        this.shellSort(0);


        //Nacitanie vstupu
        Scanner sc = new Scanner(System.in);
        System.out.println("Zaciatocny vrchol");
        u = sc.nextInt();
        System.out.println("Konecny vrchol");
        v = sc.nextInt();

        //Inicializacia
        for (int i = 1; i < this.n + 1; i++) {
            this.t[i] = Integer.MAX_VALUE / 2 - 2;
            this.x[i] = 0;
            this.E[i] = 0;
            this.z[i] = 0;
        }

        //u je startovaci vrchol
        this.t[u] = 0;

        //Vlozenie zaciatocneho vrchola do mnoziny E
        this.insertToEndE(u);
        while (this.nE > 0) {
            int r = this.extractFromEndE(); //label correct

            //int r = this.extractMinFromE(); //toto bude label set
            //int r = this.extractFromBeginningE(); //label correct
            //if (r == v) stop //V pripade label correct staci skoncit ked sme sa dostali do ciela
            /*
            if (r == v) {
                break;
            }
             */
            for (int k = this.S[r]; k < this.S[r + 1]; k++) {
                int j = this.H[k][1];
                int crj = this.H[k][2];

                if (this.t[j] > this.t[r] + crj) {
                    this.t[j] = this.t[r] + crj;
                    this.x[j] = r;
                    this.insertToEndE(j);
                }
            }
        }

        System.out.println("Vzdialenost z vrchola " + u + " do vrchola " + v + " je " + this.t[v]);

        //Najkratsia cesta
        int w = v;

        System.out.print("Cela cesta: ");
        while (this.x[w] > 0) {
            System.out.print(this.x[w] + " ");
            w = this.x[w];
        }
    }

    private void insertToEndE(int j) {
        if (this.z[j] == 0) {
            this.nE++;
            this.E[nE] = j;
            this.z[j] = 1;
        }
    }

    private int extractFromE() {
        int w = this.E[this.nE];
        this.E[this.nE] = 0;
        this.z[w] = 0;
        return w;
    }

    //Vybranie prvka z konca pola E
    private int extractFromEndE() {
        int w = this.E[this.nE];
        this.E[this.nE] = 0;
        this.z[w] = 0;
        this.nE--;
        return w;
    }

    private int extractFromBeginningE() {
        int w = this.E[1];
        this.E[1] = this.E[this.nE];
        this.E[this.nE] = 0;
        this.nE--;
        this.z[w] = 0;
        return w;
    }

    private int extractMinFromE() {
        //Hladanie prvku postupnosti E s minimalnym t

        int temp = Integer.MAX_VALUE;
        int imin = 1; //index prvku E s minimalnym t

        for (int i = 1; i < this.nE + 1; i++) {
            if (this.t[i] < temp) {
                temp = this.t[i];
                imin = i;
            }
        }

        int w = this.E[imin];
        this.E[imin] = this.E[this.nE];
        this.E[this.nE] = 0;
        this.nE--;
        this.z[w] = 0;
        return w;

    }


    //------------------------------------ Kruskalov Algoritmus ------------------------------------
    public void kruskalovAlgoritmus2() {
        int[] kostra;
        int [] k;
        int pocetHranVkostre = 0;

        k = new int[this.n + 1];
        kostra = new int[this.n + 1];

        //Zotriedenie pole hrán H podla ceny hran (vzrastuco)
        this.shellSort(2);

        //Krok 2
        for (int i = 1; i < this.n + 1; i++) {
            k[i] = i; //Kazdy vrchol je vo svojom vlastnom komponente
        }

        pocetHranVkostre = 0;

        int u;
        int v;
        for (int j = 1; j < this.m + 1; j++) {
            u = this.H[j][0];
            v = this.H[j][1];

            if (k[u] != k[v]) {
                pocetHranVkostre++;

                //Na riadku j sme nasli hranu, ktoru zaradime do kostry
                kostra[pocetHranVkostre] = j;
                int kmax = Math.max(k[u], k[v]);
                int kmin = Math.min(k[u], k[v]);

                for (int i = 1; i < this.n + 1; i++) {
                    if (k[i] == kmax) {
                        k[i] = kmin;
                    }
                }
            }
        }
        // Vypocet ceny kostry
        int cenaKostry = 0;
        for (int j = 1; j < pocetHranVkostre + 1; j++) {
            cenaKostry += this.H[j][2];
        }

        System.out.println("Pocet hran kostry je " + pocetHranVkostre);
        System.out.println("Cena kostry je " + cenaKostry);
        System.out.println("Hrany kostry su: ");
        for (int i = 1; i < pocetHranVkostre + 1; i++) {
            System.out.printf("[%d, %d]\n", this.H[i][0], this.H[i][1]);
        }
    }

}

