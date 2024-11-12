package tn.esprit.spring;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(SpringExtension.class)

@SpringBootTest
public class EtudiantServiceTestJunit {
    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    private Etudiant etudiant;

    @BeforeEach
    public void setUp() {
        // Créer une instance de la classe Etudiant pour les tests
        MockitoAnnotations.openMocks(this);
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEt("John");
        etudiant.setPrenomEt("Doe");
        etudiant.setCin(123456);
        etudiant.setEcole("Ecole A");
        etudiant.setDateNaissance(LocalDate.of(2000, 1, 1));
    }

    @Test
    public void testAddOrUpdateEtudiant() {
        // Mock le comportement du repository pour save
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        // Appel de la méthode du service
        Etudiant savedEtudiant = etudiantService.addOrUpdate(etudiant);

        // Vérifications
        assertNotNull(savedEtudiant);
        assertEquals(etudiant.getNomEt(), savedEtudiant.getNomEt());
        assertEquals(etudiant.getPrenomEt(), savedEtudiant.getPrenomEt());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testFindAllEtudiants() {
        // Simuler des données retournées par le repository
        List<Etudiant> etudiants = Arrays.asList(new Etudiant(), new Etudiant());
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        // Appeler la méthode du service
        List<Etudiant> result = etudiantService.findAll();

        // Vérifier que le résultat n'est pas null et contient des données
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindEtudiantById() {
        // Mock le comportement du repository pour findById
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        // Appel de la méthode du service
        Etudiant foundEtudiant = etudiantService.findById(1L);

        // Vérifications
        assertNotNull(foundEtudiant);
        assertEquals(etudiant.getIdEtudiant(), foundEtudiant.getIdEtudiant());
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteEtudiantById() {
        // Appel de la méthode du service
        etudiantService.deleteById(1L);

        // Vérifications
        verify(etudiantRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteEtudiant() {
        // Appel de la méthode du service
        etudiantService.delete(etudiant);

        // Vérifications
        verify(etudiantRepository, times(1)).delete(etudiant);
    }

    @Test
    public void testRepartitionEtudiantsParEcoleAvecListeVide() {
        when(etudiantRepository.findAll()).thenReturn(Collections.emptyList());  // Liste vide d'étudiants

        Map<String, Long> resultat = etudiantService.repartitionEtudiantsParEcole();

        assertNotNull(resultat);
        assertTrue(resultat.isEmpty(), "La répartition doit être vide lorsque la liste d'étudiants est vide.");
    }

    // Test avec des étudiants d'une seule école
    @Test
    public void testRepartitionEtudiantsParEcoleAvecUneSeuleEcole() {
        List<Etudiant> etudiants = new ArrayList<>();

        // Création d'étudiants avec tous les attributs nécessaires
        Etudiant etudiant1 = new Etudiant(1, "Nom1", "Prenom1", 123456789, "Ecole A", LocalDate.now());
        Etudiant etudiant2 = new Etudiant(2, "Nom2", "Prenom2", 987654321, "Ecole A", LocalDate.now());

        etudiants.add(etudiant1);
        etudiants.add(etudiant2);

        when(etudiantRepository.findAll()).thenReturn(etudiants);  // Simulation de la récupération des étudiants

        Map<String, Long> resultat = etudiantService.repartitionEtudiantsParEcole();

        assertNotNull(resultat);
        assertEquals(1, resultat.size());
        assertEquals(2L, resultat.get("Ecole A"));  // Les 2 étudiants sont dans "Ecole A"
    }

    // Test avec des étudiants répartis sur plusieurs écoles
    @Test
    public void testRepartitionEtudiantsParEcoleAvecPlusieursEcoles() {
        List<Etudiant> etudiants = new ArrayList<>();

        // Création d'étudiants avec tous les attributs nécessaires
        Etudiant etudiant1 = new Etudiant(1, "Nom1", "Prenom1", 123456789, "Ecole A", LocalDate.now());
        Etudiant etudiant2 = new Etudiant(2, "Nom2", "Prenom2", 987654321, "Ecole A", LocalDate.now());
        Etudiant etudiant3 = new Etudiant(3, "Nom3", "Prenom3", 112233445, "Ecole B", LocalDate.now());
        Etudiant etudiant4 = new Etudiant(4, "Nom4", "Prenom4", 667788990, "Ecole C", LocalDate.now());
        Etudiant etudiant5 = new Etudiant(5, "Nom5", "Prenom5", 556677889, "Ecole C", LocalDate.now());

        etudiants.add(etudiant1);
        etudiants.add(etudiant2);
        etudiants.add(etudiant3);
        etudiants.add(etudiant4);
        etudiants.add(etudiant5);

        when(etudiantRepository.findAll()).thenReturn(etudiants);  // Simulation de la récupération des étudiants

        Map<String, Long> resultat = etudiantService.repartitionEtudiantsParEcole();

        assertNotNull(resultat);
        assertEquals(3, resultat.size());  // Il y a 3 écoles distinctes
        assertEquals(2L, resultat.get("Ecole A"));  // 2 étudiants dans "Ecole A"
        assertEquals(1L, resultat.get("Ecole B"));  // 1 étudiant dans "Ecole B"
        assertEquals(2L, resultat.get("Ecole C"));  // 2 étudiants dans "Ecole C"
    }
}
