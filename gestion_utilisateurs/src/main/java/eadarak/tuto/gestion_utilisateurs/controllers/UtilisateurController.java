package eadarak.tuto.gestion_utilisateurs.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eadarak.tuto.gestion_utilisateurs.models.Utilisateur;
import eadarak.tuto.gestion_utilisateurs.services.UtilisateurService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/utilisateurs")
@AllArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @PostMapping
    public ResponseEntity<Utilisateur> creerUtilisateur(@RequestBody @Valid Utilisateur utilisateur) {
        try {
            Utilisateur utilisateurCree = utilisateurService.creerUtilisateur(utilisateur);
            return ResponseEntity.status(HttpStatus.CREATED).body(utilisateurCree);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> modifierUtilisateur(@PathVariable int id, @RequestBody @Valid Utilisateur utilisateurMiseAJour) {
        try {
            Utilisateur utilisateurModifie = utilisateurService.modifierUtilisateur(id, utilisateurMiseAJour);
            return ResponseEntity.ok(utilisateurModifie);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerUtilisateur(@PathVariable int id) {
        try {
            utilisateurService.supprimerUtilisateur(id);
            return ResponseEntity.ok("Utilisateur supprimé avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Utilisateur>> listerUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.listerUtilisateur();
        if (utilisateurs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(utilisateurs);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> chercherUtilisateur(@PathVariable int id) {
        Utilisateur user = utilisateurService.trouverUtilisateurParId(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(user);
    }
}
