package eadarak.tuto.gestion_utilisateurs.services;

import java.util.List;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import eadarak.tuto.gestion_utilisateurs.models.Utilisateur;
import eadarak.tuto.gestion_utilisateurs.repositories.UtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UtilisateurService {
    private final UtilisateurRepository uRepository;

    public Utilisateur creerUtilisateur(@Valid Utilisateur user) {
        try {
            Utilisateur createdUser = this.uRepository.save(user);
            log.info("Création de l'utilisateur avec l'ID {} effectuée avec succès.", user.getId());
            return createdUser;
        } catch (DataIntegrityViolationException e) {
            log.error("Erreur de contrainte de base de données lors de la création de l'utilisateur : {}",
                    e.getMessage(), e);
            throw new RuntimeException("Conflit avec les données existantes.");
        } catch (Exception e) {
            log.error("Erreur générale lors de la création de l'utilisateur : {}", e.getMessage(), e);
            throw e; // Propager si nécessaire pour une gestion globale
        }
    }

    public List<Utilisateur> listerUtilisateur(){
        List<Utilisateur> utilisateurs = uRepository.findAll();

        if(utilisateurs.isEmpty()){
            log.info("Aucun utilisateur trouvé dans la base de données.");
        }

        return utilisateurs;
    }

    public Utilisateur trouverUtilisateurParId(int id) {
        return uRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur avec l'ID " + id + " introuvable."));
    }
    
    public Utilisateur modifierUtilisateur(int id, @Valid Utilisateur utilisateurMiseAJour) {
        return uRepository.findById(id)
                .map(utilisateurExistant -> {
                    // Logs des modifications en cours
                    log.info("Mise à jour de l'utilisateur avec l'ID {}", id);
    
                    utilisateurExistant.setNom(utilisateurMiseAJour.getNom());
                    utilisateurExistant.setEmail(utilisateurMiseAJour.getEmail());
                    utilisateurExistant.setAge(utilisateurMiseAJour.getAge());
    
                    // Sauvegarder l'utilisateur mis à jour
                    Utilisateur utilisateurModifie = uRepository.save(utilisateurExistant);
                    log.info("Utilisateur mis à jour avec succès : {}", utilisateurModifie);
    
                    return utilisateurModifie;
                })
                .orElseThrow(() -> new RuntimeException("Utilisateur avec l'ID " + id + " introuvable."));
    }
    

    public void supprimerUtilisateur(int id){
        uRepository.deleteById(id);
        log.info("Utilisateur avec l'ID {} supprimé avec succès.", id);
    }

    

}
