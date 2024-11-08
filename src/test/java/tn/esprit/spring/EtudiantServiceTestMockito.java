package tn.esprit.spring;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import java.util.*;
import java.time.LocalDate;
public class EtudiantServiceTestMockito {
    @Mock
    private EtudiantRepository etudiantRepository;  // Mock du repository

    @InjectMocks
    private EtudiantService etudiantService;  // Service à tester

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialisation des mocks
    }

    // Test de la méthode addOrUpdate
    @Test
    public void testAddOrUpdate() {
        Etudiant etudiant = new Etudiant(1, "Nom", "Prenom", 123456789, "Ecole A", LocalDate.of(2000, 1, 1));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant result = etudiantService.addOrUpdate(etudiant);

        assertNotNull(result);
        assertEquals("Nom", result.getNomEt());
        assertEquals("Ecole A", result.getEcole());
        verify(etudiantRepository, times(1)).save(etudiant);  // Vérifie que save() a été appelé une fois
    }

    // Test de la méthode findAll
    @Test
    public void testFindAll() {
        Etudiant etudiant1 = new Etudiant(1, "Nom1", "Prenom1", 123456789, "Ecole A", LocalDate.of(2000, 1, 1));
        Etudiant etudiant2 = new Etudiant(2, "Nom2", "Prenom2", 987654321, "Ecole B", LocalDate.of(2000, 1, 2));

        List<Etudiant> etudiants = Arrays.asList(etudiant1, etudiant2);
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Nom1", result.get(0).getNomEt());
        verify(etudiantRepository, times(1)).findAll();  // Vérifie que findAll() a été appelé une fois
    }

    // Test de la méthode findById
    @Test
    public void testFindById() {
        Etudiant etudiant = new Etudiant(1, "Nom", "Prenom", 123456789, "Ecole A", LocalDate.of(2000, 1, 1));
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.findById(1L);

        assertNotNull(result);
        assertEquals("Nom", result.getNomEt());
        assertEquals("Ecole A", result.getEcole());
        verify(etudiantRepository, times(1)).findById(1L);  // Vérifie que findById() a été appelé une fois
    }

    // Test de la méthode deleteById
    @Test
    public void testDeleteById() {
        Etudiant etudiant = new Etudiant(1, "Nom", "Prenom", 123456789, "Ecole A", LocalDate.of(2000, 1, 1));
        doNothing().when(etudiantRepository).deleteById(1L);  // Simule la suppression sans rien faire

        etudiantService.deleteById(1L);

        verify(etudiantRepository, times(1)).deleteById(1L);  // Vérifie que deleteById() a été appelé une fois
    }

    // Test de la méthode delete
    @Test
    public void testDelete() {
        Etudiant etudiant = new Etudiant(1, "Nom", "Prenom", 123456789, "Ecole A", LocalDate.of(2000, 1, 1));
        doNothing().when(etudiantRepository).delete(etudiant);  // Simule la suppression sans rien faire

        etudiantService.delete(etudiant);

        verify(etudiantRepository, times(1)).delete(etudiant);  // Vérifie que delete() a été appelé une fois
    }

    // Test de la méthode repartitionEtudiantsParEcole avec une liste vide
    @Test
    public void testRepartitionEtudiantsParEcoleAvecListeVide() {
        when(etudiantRepository.findAll()).thenReturn(Collections.emptyList());  // Liste vide d'étudiants

        Map<String, Long> resultat = etudiantService.repartitionEtudiantsParEcole();

        assertNotNull(resultat);
        assertTrue(resultat.isEmpty(), "La répartition doit être vide lorsque la liste d'étudiants est vide.");
    }

    // Test de la méthode repartitionEtudiantsParEcole avec des étudiants dans une seule école
    @Test
    public void testRepartitionEtudiantsParEcoleAvecUneSeuleEcole() {
        List<Etudiant> etudiants = new ArrayList<>();
        Etudiant etudiant1 = new Etudiant(1, "Nom1", "Prenom1", 123456789, "Ecole A", LocalDate.of(2000, 1, 1));
        Etudiant etudiant2 = new Etudiant(2, "Nom2", "Prenom2", 987654321, "Ecole A", LocalDate.of(2000, 1, 2));
        etudiants.add(etudiant1);
        etudiants.add(etudiant2);

        when(etudiantRepository.findAll()).thenReturn(etudiants);

        Map<String, Long> resultat = etudiantService.repartitionEtudiantsParEcole();

        assertNotNull(resultat);
        assertEquals(1, resultat.size());
        assertEquals(2L, resultat.get("Ecole A"));  // Les 2 étudiants sont dans "Ecole A"
    }

    // Test de la méthode repartitionEtudiantsParEcole avec des étudiants dans plusieurs écoles
    @Test
    public void testRepartitionEtudiantsParEcoleAvecPlusieursEcoles() {
        List<Etudiant> etudiants = new ArrayList<>();
        Etudiant etudiant1 = new Etudiant(1, "Nom1", "Prenom1", 123456789, "Ecole A", LocalDate.of(2000, 1, 1));
        Etudiant etudiant2 = new Etudiant(2, "Nom2", "Prenom2", 987654321, "Ecole A", LocalDate.of(2000, 1, 2));
        Etudiant etudiant3 = new Etudiant(3, "Nom3", "Prenom3", 112233445, "Ecole B", LocalDate.of(2000, 1, 3));
        Etudiant etudiant4 = new Etudiant(4, "Nom4", "Prenom4", 667788990, "Ecole C", LocalDate.of(2000, 1, 4));
        Etudiant etudiant5 = new Etudiant(5, "Nom5", "Prenom5", 556677889, "Ecole C", LocalDate.of(2000, 1, 5));
        etudiants.add(etudiant1);
        etudiants.add(etudiant2);
        etudiants.add(etudiant3);
        etudiants.add(etudiant4);
        etudiants.add(etudiant5);

        when(etudiantRepository.findAll()).thenReturn(etudiants);

        Map<String, Long> resultat = etudiantService.repartitionEtudiantsParEcole();

        assertNotNull(resultat);
        assertEquals(3, resultat.size());  // Il y a 3 écoles distinctes
        assertEquals(2L, resultat.get("Ecole A"));  // 2 étudiants dans "Ecole A"
        assertEquals(1L, resultat.get("Ecole B"));  // 1 étudiant dans "Ecole B"
        assertEquals(2L, resultat.get("Ecole C"));  // 2 étudiants dans "Ecole C"
    }
}
