package modele;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Joueur {

	private String nom;
	private String prenom;
	private String ville;

	public Joueur() {
		this("", "", "");
	}

}
