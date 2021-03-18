1. Testovacie súbory pre algoritmy najkratších ciest sú 
   hlavne v adresári ShortestPath. Príklad Florida.hrn 
   má veľké ceny hrán, preto treba voliť nekonečno rovné
   polovici max. zobraziteľného čísla.

2. Súbory pre monotónne usporiadanie a s ním súvisiace problémy -
   najkratšia cesta v acyklickom digrafe a CPM sú v adresári
   ACYKL. 
   Pre CPM metódu sú tam dva príklady súbory *.hrn obsahujú 
   digraf a príslušné súbory *.tim príslušné doby spracovania
   elementárnych operácií. Menší súbor rieši CPM projekt, ktorý
   je v mojich slajdoch 20,21 a 26 - 28 tu:
   http://frcatel.fri.uniza.sk/users/paluch/Prezentacie/GrafPrez_05.pdf

3. Súbory pre hľadanie záporného cyklu v digrafe sú v adresári
   CYKL_DIGRAF.

4. Pre Kruskala sú použiteľné všetky súbory typu *.hrn 
   vo všetkých adresároch, prednostne však v ShortestPath.
   POZOR, pre veľké grafy ako napríklad Florida, Kruskal trvá
   nekonečne dlho.

Nie je zaručené, že všetky súbory sú zotriedené podľa 
prvého stĺpca, preto je pred vytvorením poľa smerníkov S[]
potrebné usporiadať pole H[][] nejakým triediacim algoritmom, 
prinajhoršom aj týmto:

// vymeni riadky i, j v poli H[][]
void SWAPH(int i, int j) {
	int tmp = 0;
	for (int k = 0; k <= 2; k += 1) {
		tmp = H[i][k];
		H[i][k] = H[j][k];
		H[j][k] = tmp;
	}
}

// BubleSort -- zotriedi pole H[][] 
// podla stlpca 0 neklesajuco
void sortH(void) {
	int zlepsenie = 1;
	while (zlepsenie != 0) {
		zlepsenie = 0;
		for (int k = 1; k < m; k += 1) {
			if (H[k][0] > H[k + 1][0]) {// Ked zamenime H[k][0], H[k + 1][0]
				SWAPH(k, k + 1);        // za H[k][2], H[k + 1][2], dostaneme
				zlepsenie = 1;          // usporiadanie podľa ceny hrán
                                        // neklesajuco preKruskala II.				
				//	printf("Riadky %d a %d neboli dobre usporiadane", k, k + 1);
				//	getchar();
			}
		}
	}
}

