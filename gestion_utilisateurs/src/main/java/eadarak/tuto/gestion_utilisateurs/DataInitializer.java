package eadarak.tuto.gestion_utilisateurs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import eadarak.tuto.gestion_utilisateurs.models.Utilisateur;
import eadarak.tuto.gestion_utilisateurs.repositories.UtilisateurRepository;
import eadarak.tuto.gestion_utilisateurs.services.UtilisateurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UtilisateurService utilisateurService;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public void run(String... args) throws Exception {
        // Supprimer les données existantes pour réinitialiser la base
        utilisateurRepository.deleteAll();

        // Ajouter des données de test
        Utilisateur user1 = new Utilisateur(0, "Alice", "alice@example.com", 25);
        Utilisateur user2 = new Utilisateur(0, "Bob", "bob@example.com", 30);

        utilisateurService.creerUtilisateur(user1);
        utilisateurService.creerUtilisateur(user2);

        log.info("Données initiales insérées avec succès.");
        log.info("Nombre d'utilisateurs : {}", utilisateurRepository.count());
        
    }
    
}
