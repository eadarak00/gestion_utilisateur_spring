package eadarak.tuto.gestion_utilisateurs.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Le nom ne doit pas etre \" \"")
    @NotNull(message = "Le nom ne doit pas etre NULL")
    private String nom;

    @NotBlank(message = "L'email ne doit pas etre vide")
    @NotNull(message = "L'email ne doit pas etre NULL")
    @Email(message = "L'adresse e-mail doit être valide")
    private String email;

    @NotNull(message = "L'age ne doit pas etre NULL")
    @Min(value = 18, message = "L'age doit etre superieur a 18")
    private int age;
}
