package model;

public enum Type {
Feu,Eau,Plante,Electrique,Sol,Roche, Neutre;
	//,Vol,Normal,Combat,Insecte,Dragon,Glace,Spectre,Psy,Poison,Tenèbre,Acier,Fee
}
/* effets des attaque vs types suivant :

Feu > Plante > Sol > Roche > Électrique > Eau > Feu

 

Feu : 			Plante+, Glace+, Insecte+, Acier+ 			Feu-, Eau-, Roche-, Dragon-
Eau : 			Feu+, Roche+, Sol+							Eau-, Plante-, Dragon-		
Plante : 		Eau+, Roche+, Sol+							Feu-, Plante-, Dragon-, Vol-, Insecte-, Poison-, Acier-
Sol : 			Electrique+, Roche+, Feu+, Poison+, Acier+	Vol0		Plante-, Insecte-
Roche : 		Vol+, Feu+, Insecte+, Glace+				Combat-, Acier-, Sol-
Electrique :	Eau+, Vol+									Sol0		Dragon-, Plante-, Electrique- 
Vol : 			Plante+, Insecte+, Combat+					Roche-, Electrique-, Acier-
Normal : 													Spectre0	Roche-, Acier-
Combat : 		Normal+, Roche+, Acier+, Glace+, Ténèbre+	Spectre0	Vol-, Psy-, Fee-, Poison-, Insecte-
Insecte : 		Psy+, Plante+, Ténèbre+,					Feu-, Combat-, Spectre-, Vol-, Poison-, Acier-, Fée-
Dragon : 		Dragon+										Fée0 		Acier-
Glace : 		Plante+, Vol+, Sol+, Dragon+				Acier-, Feu-, Eau-, Glace-
Spectre : 		Spectre+, Psy+								Normal0		Ténèbre-
Psy : 			Poison+, Combat+							Ténèbre0	Acier-, Psy-
Poison : 		Plante+, Fée+								Acier0		Poison-, Sol-, Roche-, Spectre-
Tenèbre : 		Spectre+, Psy+								Ténèbre-, Fée-, Combat-
Acier : 		Roche+, Glace+, Fée+						Eau-, Feu-, Acier-, Electrique- 
Fée : 			Ténèbre+, Combat+, Dragon+					Acier-, Poison-, Feu-


*/