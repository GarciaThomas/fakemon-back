package model;

public enum Type {
Feu,Eau,Plante,Electrique,Sol,Roche,Neutre;
	//,Vol,Normal,Combat,Insecte,Dragon,Glace,Spectre,Psy,Poison,Ténèbre,Acier,Fee
}
/* effets des attaque vs types suivant :

Feu > Plante > Sol > Roche > �lectrique > Eau > Feu

 

Feu : 			Plante+, Glace+, Insecte+, Acier+ 			Feu-, Eau-, Roche-, Dragon-
Eau : 			Feu+, Roche+, Sol+							Eau-, Plante-, Dragon-		
Plante : 		Eau+, Roche+, Sol+							Feu-, Plante-, Dragon-, Vol-, Insecte-, Poison-, Acier-
Sol : 			Electrique+, Roche+, Feu+, Poison+, Acier+	Vol0		Plante-, Insecte-
Roche : 		Vol+, Feu+, Insecte+, Glace+				Combat-, Acier-, Sol-
Electrique :	Eau+, Vol+									Sol0		Dragon-, Plante-, Electrique- 
Vol : 			Plante+, Insecte+, Combat+					Roche-, Electrique-, Acier-
Normal : 													Spectre0	Roche-, Acier-
Combat : 		Normal+, Roche+, Acier+, Glace+, T�n�bre+	Spectre0	Vol-, Psy-, Fee-, Poison-, Insecte-
Insecte : 		Psy+, Plante+, T�n�bre+,					Feu-, Combat-, Spectre-, Vol-, Poison-, Acier-, F�e-
Dragon : 		Dragon+										F�e0 		Acier-
Glace : 		Plante+, Vol+, Sol+, Dragon+				Acier-, Feu-, Eau-, Glace-
Spectre : 		Spectre+, Psy+								Normal0		T�n�bre-
Psy : 			Poison+, Combat+							T�n�bre0	Acier-, Psy-
Poison : 		Plante+, F�e+								Acier0		Poison-, Sol-, Roche-, Spectre-
Ten�bre : 		Spectre+, Psy+								T�n�bre-, F�e-, Combat-
Acier : 		Roche+, Glace+, F�e+						Eau-, Feu-, Acier-, Electrique- 
F�e : 			T�n�bre+, Combat+, Dragon+					Acier-, Poison-, Feu-


*/