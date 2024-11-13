package tn.esprit.spring.Services.Universite;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

class UniversiteServiceTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteService universiteService;

    private Universite universite;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        universite = Universite.builder()
                .idUniversite(1L)
                .nomUniversite("University A")
                .adresse("123 Main St")
                .build(); // Create a sample Universite object
    }

    @Test
    void testAddOrUpdate() {
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite); // Mock the save method

        Universite result = universiteService.addOrUpdate(universite); // Call the service method

        assertNotNull(result); // Check the result is not null
        assertEquals("University A", result.getNomUniversite()); // Verify the properties of the result
        verify(universiteRepository, times(1)).save(universite); // Verify the save call
    }

    @Test
    void testFindAll() {
        List<Universite> universites = new ArrayList<>();
        universites.add(universite);
        when(universiteRepository.findAll()).thenReturn(universites); // Mock the repository response

        List<Universite> result = universiteService.findAll(); // Call the service method

        assertEquals(1, result.size()); // Check the size of the result
        verify(universiteRepository, times(1)).findAll(); // Verify the findAll call
    }

    @Test
    void testFindById() {
        when(universiteRepository.findById(anyLong())).thenReturn(Optional.of(universite)); // Mock finding the universite

        Universite result = universiteService.findById(1L); // Call the service method

        assertNotNull(result); // Check the result is not null
        assertEquals(1L, result.getIdUniversite()); // Verify the ID of the result
        verify(universiteRepository, times(1)).findById(1L); // Verify the findById call
    }

    @Test
    void testDeleteById() {
        doNothing().when(universiteRepository).deleteById(anyLong()); // Mock the deleteById method

        universiteService.deleteById(1L); // Call the service method

        verify(universiteRepository, times(1)).deleteById(1L); // Verify the deleteById call
    }

    @Test
    void testDelete() {
        doNothing().when(universiteRepository).delete(any(Universite.class)); // Mock the delete method

        universiteService.delete(universite); // Call the service method

        verify(universiteRepository, times(1)).delete(universite); // Verify the delete call
    }
}
