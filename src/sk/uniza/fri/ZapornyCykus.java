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
public class ZapornyCykus {

    private final int n; // pocet vrcholov grafu
    private final int m; // pocet hran grafu
    private final int[][] h; // pole udajov o hranach
    private final int[] z;
    private final int[] e;
    private int nE;
    private final int[] t;
    private final int[] x;
    private final int[] s;

    public ZapornyCykus(int paPocetVrcholov, int paPocetHran) {
        this.n = paPocetVrcholov;
        this.m = paPocetHran;
        this.h = new int[1 + this.m][3];

        this.z = new int[this.n + 1];
        this.e = new int[this.n + 1];
        this.nE = 0;
        this.s = new int[this.n + 2];

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
    public static ZapornyCykus nacitajSubor(String nazovSuboru)
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
        ZapornyCykus g = new ZapornyCykus(pocetVrcholov, pocetHran);

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
                int k;
                if (this.h[i][s] > this.h[j][s]) {
                    this.swap(i, j);
                    for (k = i; k - gap >= 1; k = k - gap) {
                        int l = k - gap;
                        if (this.h[l][s] <= this.h[k][s]) {
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
            for (int k = 1; k < this.m; k++) {
                if (this.h[k][0] > this.h[k + 1][0]) {
                    this.swap(k, k + 1);
                    zlepsenie = 1;
                }
            }
        }
    }

    private void swap(int i, int j) {
        int odlozenieH;
        for (int k = 0; k <= 2; k++) {
            odlozenieH = this.h[i][k];
            this.h[i][k] = this.h[j][k];
            this.h[j][k] = odlozenieH;
        }
    }


    // -------------------------------------- Matice Smerníkov ---------------------------------------


    public void maticaSmernikov(int r) {
        //int[] S = new int[n + 2];

        //vynulovanie pola
        for (int i = 0; i < this.n + 1; i++) {
            this.s[i] = 0;
        }

        //s[i] ukazuje na prvý riadok ola H taký te H[k][0] = i

        for (int k = 1; k <= this.m; k++) {
            int i = this.h[k][0];
            if (this.s[i] == 0) {
                this.s[i] = k;
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

    public void zapornyCyklus() {
        //-1 preto aby sa nám nevypisovali hrany v matice smerníkov.
        this.maticaSmernikov(-1);
        int u;
        int v;

        //Vrcholy mnoziny E su E[1], E[2], ..., E[nE - 1], E[nE]
        //nE - pocet prvkov mnoziny E
        //int[] E = new int[this.n + 1];

        //z[i] = 0 ak vrchol i nepatri do mnoziny E
        //z[i] = 1 ak vrchol i patri do mnoziny E
        //int[] z = new int[this.n + 1];

        this.shellSort(0);

        //Inicializacia
        for (int i = 1; i < this.n + 1; i++) {
            this.t[i] = 0;
            this.x[i] = 0;
            this.e[i] = 0;
            this.z[i] = 0;
        }
        this.nE = 0;

        //Vlozenie zaciatocneho vrchola do mnoziny E
        for (int i = 1; i < this.n + 1 ; i++) {
            this.insertToEndE(i);
        }
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
            for (int k = this.s[r]; k < this.s[r + 1]; k++) {
                int j = this.h[k][1];
                int crj = this.h[k][2];

                if (this.t[j] > this.t[r] + crj) {
                    this.t[j] = this.t[r] + crj;
                    this.x[j] = r;
                    this.insertToEndE(j);
                    //Ideme kontrolovať či sme neuzavreli cyklus
                    int[] cykus = new int[this.n + 1];
                    cykus[1] = j;
                    for (int c = 1; c < this.n + 1; c++) {
                        int tmp = x[cykus[c]];
                        if (tmp == 0) {
                            // tu sme nenašli cyklus
                            break;
                        }
                        //dasli vrcholo toho pripadneho cyklu
                        cykus[c + 1] = tmp;
                        if (tmp == j) {
                            // tu sme nasli cyklus
                            System.out.printf("Cyklus T:\n");
                            for (int t = c + 1; t > 0; t--) {
                                System.out.println(cykus[t]);
                            }
                            return;
                        }


                    }
                }
            }
        }
        System.out.println("Nexistuje cyklus zápornej ceny");

    }

    private void insertToEndE(int j) {
        if (this.z[j] == 0) {
            this.nE++;
            this.e[this.nE] = j;
            this.z[j] = 1;
        }
    }

    private int extractFromE() {
        int w = this.e[this.nE];
        this.e[this.nE] = 0;
        this.z[w] = 0;
        return w;
    }

    //Vybranie prvka z konca pola E
    private int extractFromEndE() {
        int w = this.e[this.nE];
        this.e[this.nE] = 0;
        this.z[w] = 0;
        this.nE--;
        return w;
    }

    private int extractFromBeginningE() {
        int w = this.e[1];
        this.e[1] = this.e[this.nE];
        this.e[this.nE] = 0;
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

        int w = this.e[imin];
        this.e[imin] = this.e[this.nE];
        this.e[this.nE] = 0;
        this.nE--;
        this.z[w] = 0;
        return w;

    }




}


