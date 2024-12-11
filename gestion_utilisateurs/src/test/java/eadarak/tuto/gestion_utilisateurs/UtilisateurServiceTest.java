package eadarak.tuto.gestion_utilisateurs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import eadarak.tuto.gestion_utilisateurs.models.Utilisateur;
import eadarak.tuto.gestion_utilisateurs.repositories.UtilisateurRepository;
import eadarak.tuto.gestion_utilisateurs.services.UtilisateurService;

// @ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurService utilisateurService;

    @Test
    void testCreerUtilisateur_Succes() {
        // GIVEN: un utilisateur valide
        Utilisateur utilisateur = new Utilisateur(0, "Jean Dupont", "jean.dupont@example.com", 25);

        // WHEN: on simule que le dépôt sauvegarde l'utilisateur
        when(utilisateurRepository.save(any(Utilisateur.class)))
                .thenReturn(new Utilisateur(1, "Jean Dupont", "jean.dupont@example.com", 25));

        // THEN: on appelle la méthode de création
        Utilisateur utilisateurCree = utilisateurService.creerUtilisateur(utilisateur);

        // ASSERTIONS
        assertNotNull(utilisateurCree, "L'utilisateur créé ne doit pas être nul.");
        assertEquals(1, utilisateurCree.getId(), "L'ID de l'utilisateur créé doit être 1.");
        assertEquals("Jean Dupont", utilisateurCree.getNom(), "Le nom doit correspondre.");
        assertEquals("jean.dupont@example.com", utilisateurCree.getEmail(), "L'email doit correspondre.");
        assertEquals(25, utilisateurCree.getAge(), "L'âge doit correspondre.");

        // Vérifie que la méthode save() a bien été appelée
        verify(utilisateurRepository, times(1)).save(any(Utilisateur.class));
    }

    @Test
    void testCreerUtilisateur_ErreurContrainte() {
        // GIVEN: un utilisateur qui provoque une exception de contrainte
        Utilisateur utilisateur = new Utilisateur(0, "Jean Dupont", "jean.dupont@example.com", 25);

        // WHEN: le dépôt lève une DataIntegrityViolationException
        when(utilisateurRepository.save(utilisateur))
                .thenThrow(new RuntimeException("Conflit avec les données existantes."));

        // THEN: on s'attend à une RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            utilisateurService.creerUtilisateur(utilisateur);
        });

        // ASSERTIONS
        assertEquals("Conflit avec les données existantes.", exception.getMessage());

        // Vérifie que la méthode save() a bien été appelée
        verify(utilisateurRepository, times(1)).save(utilisateur);
    }

    @Test
    void listerUtilisateurTest() {
        // GIVEN: une liste d'utilisateurs
        List<Utilisateur> utilisateurs = new ArrayList<>();
        utilisateurs.add(new Utilisateur(1, "Jean Dupont", "jean.dupont@example.com", 25));
        utilisateurs.add(new Utilisateur(2, "Marie Curie", "marie.curie@example.com", 30));

        // WHEN: on simule que le dépôt retourne cette liste
        when(utilisateurRepository.findAll()).thenReturn(utilisateurs);

        // THEN: on appelle la méthode et on vérifie les résultats
        List<Utilisateur> uList = utilisateurService.listerUtilisateur();

        // ASSERTIONS: vérifier que la liste retournée correspond à celle simulée
        assertNotNull(uList, "La liste des utilisateurs ne doit pas être nulle.");
        assertEquals(2, uList.size(), "La liste doit contenir exactement 2 utilisateurs.");
        assertEquals("Jean Dupont", uList.get(0).getNom(), "Le premier utilisateur doit être Jean Dupont.");
        assertEquals("Marie Curie", uList.get(1).getNom(), "Le second utilisateur doit être Marie Curie.");

        // Vérification que la méthode `findAll` a été appelée une fois
        verify(utilisateurRepository, times(1)).findAll();
    }

    @Test
    void trouverUtilisateurTest_Sucess() {
        // GIVEN: un utilisateur existant dans le dépôt
        Utilisateur utilisateur = new Utilisateur(1, "John Doe", "jd@example.com", 26);

        // WHEN: on appelle la méthode pour trouver l'utilisateur
        when(utilisateurRepository.findById(1)).thenReturn(java.util.Optional.of(utilisateur));

        // WHEN: on appelle la méthode pour trouver l'utilisateur
        Utilisateur utilisateurTrouve = utilisateurService.trouverUtilisateurParId(1);

        // Assertions
        assertNotNull(utilisateurTrouve, "L'utilisateur trouvé ne doit pas être null.");
        assertEquals(1, utilisateurTrouve.getId(), "L'ID de l'utilisateur doit être 1.");
        assertEquals("John Doe", utilisateurTrouve.getNom(), "Le nom de l'utilisateur doit être John Doe.");
        assertEquals("jd@example.com", utilisateurTrouve.getEmail(), "L'email de l'utilisateur doit être correct.");

        // Vérifier que la méthode findById a été appelée une fois avec l'ID 1
        verify(utilisateurRepository, times(1)).findById(1);

    }

    @Test
    void trouverUtilisateurTest_Failed() {
        // GIVEN: aucun utilisateur trouvé pour l'ID fourni

        when(utilisateurRepository.findById(2)).thenReturn(java.util.Optional.empty());
        // WHEN & THEN: vérifier que l'appel déclenche une exception

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            utilisateurService.trouverUtilisateurParId(2);
        });

        // Vérifier que l'exception contient le bon message
        assertEquals("Utilisateur avec l'ID 2 introuvable.", exception.getMessage());

        // Vérifier que la méthode findById a été appelée une fois avec l'ID 2
        verify(utilisateurRepository, times(1)).findById(2);

    }

    @Test
    void testModifierUtilisateur_Succes() {
        // GIVEN: un utilisateur existant et une mise à jour valide
        int utilisateurId = 1;
        Utilisateur utilisateurExistant = new Utilisateur(utilisateurId, "Jean Dupont", "jean.dupont@example.com", 25);
        Utilisateur utilisateurMiseAJour = new Utilisateur(utilisateurId, "Jean Michel", "jean.michel@example.com", 30);

        // Simuler le comportement du repository
        when(utilisateurRepository.findById(utilisateurId)).thenReturn(java.util.Optional.of(utilisateurExistant));
        when(utilisateurRepository.save(utilisateurExistant)).thenReturn(utilisateurMiseAJour);

        // WHEN: on appelle la méthode de modification
        Utilisateur utilisateurModifie = utilisateurService.modifierUtilisateur(utilisateurId, utilisateurMiseAJour);

        // THEN: vérifier que l'utilisateur est modifié avec succès
        assertNotNull(utilisateurModifie, "L'utilisateur modifié ne doit pas être null.");
        assertEquals("Jean Michel", utilisateurModifie.getNom(), "Le nom de l'utilisateur doit être mis à jour.");
        assertEquals("jean.michel@example.com", utilisateurModifie.getEmail(),
                "L'email de l'utilisateur doit être mis à jour.");
        assertEquals(30, utilisateurModifie.getAge(), "L'âge de l'utilisateur doit être mis à jour.");

        // Vérifier que les bonnes méthodes ont été appelées
        verify(utilisateurRepository, times(1)).findById(utilisateurId);
        verify(utilisateurRepository, times(1)).save(utilisateurExistant);
    }

    @Test
    void testModifierUtilisateur_Echec() {
        // GIVEN: un ID inexistant pour un utilisateur
        int utilisateurId = 2;
        Utilisateur utilisateurMiseAJour = new Utilisateur(utilisateurId, "Jean Michel", "jean.michel@example.com", 30);

        // Simuler l'absence d'utilisateur dans le repository
        when(utilisateurRepository.findById(utilisateurId)).thenReturn(java.util.Optional.empty());

        // WHEN & THEN: vérifier que l'appel déclenche une exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            utilisateurService.modifierUtilisateur(utilisateurId, utilisateurMiseAJour);
        });

        // Vérifier que l'exception contient le bon message
        assertEquals("Utilisateur avec l'ID " + utilisateurId + " introuvable.", exception.getMessage());

        // Vérifier que la méthode findById a été appelée une fois et save n'a pas été
        // appelé
        verify(utilisateurRepository, times(1)).findById(utilisateurId);
        verify(utilisateurRepository, times(0)).save(any());
    }

    @Test
    void testSupprimerUtilisateur_Succes() {
        // GIVEN: un utilisateur existant avec un ID donné
        int utilisateurId = 1;

        // WHEN: on appelle la méthode de suppression
        utilisateurService.supprimerUtilisateur(utilisateurId);

        // THEN: vérifier que la méthode deleteById a été appelée
        verify(utilisateurRepository, times(1)).deleteById(utilisateurId);
    }

}
