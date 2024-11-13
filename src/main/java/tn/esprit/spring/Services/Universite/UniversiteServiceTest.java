package tn.esprit.spring.Services.Universite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniversiteServiceTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteService universiteService;

    private Universite universite;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        universite = Universite.builder()
                .idUniversite(1L)
                .nomUniversite("Universite Test")
                .adresse("Adresse Test")
                .build();
    }

    @Test
    void testAddOrUpdate() {
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        Universite result = universiteService.addOrUpdate(universite);

        assertNotNull(result);
        assertEquals(universite.getNomUniversite(), result.getNomUniversite());
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void testFindAll() {
        List<Universite> universites = new ArrayList<>();
        universites.add(universite);
        when(universiteRepository.findAll()).thenReturn(universites);

        List<Universite> result = universiteService.findAll();

        assertEquals(1, result.size());
        assertEquals(universite.getNomUniversite(), result.get(0).getNomUniversite());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(universiteRepository.findById(anyLong())).thenReturn(Optional.of(universite));

        Universite result = universiteService.findById(1L);

        assertNotNull(result);
        assertEquals(universite.getIdUniversite(), result.getIdUniversite());
        verify(universiteRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        doNothing().when(universiteRepository).deleteById(anyLong());

        universiteService.deleteById(1L);

        verify(universiteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete() {
        doNothing().when(universiteRepository).delete(any(Universite.class));

        universiteService.delete(universite);

        verify(universiteRepository, times(1)).delete(universite);
    }
}

