package modele;

import lombok.Data;

import java.util.List;

/**
 * Classe qui réunie {@link Configuration} et List<{@link Epreuve}>
 */
@Data
public class ConfigTournoi {

    private int nombreTour;

    private String tarif;

    private int nombreJoueurEquipe;

    private List<Epreuve> epreuves;
}
