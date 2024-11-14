package tn.esprit.spring.Services.Bloc;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;

import java.util.Arrays;

class BlocServiceTest {

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private BlocService blocService;

    private Bloc bloc;
    private Chambre chambre1;
    private Chambre chambre2;

    @BeforeEach
    void setUp() {
        // Initialiser les mocks
        MockitoAnnotations.openMocks(this);

        // Initialisation des objets à tester
        chambre1 = new Chambre();
        chambre1.setNumeroChambre(101L);

        chambre2 = new Chambre();
        chambre2.setNumeroChambre(102L);

        bloc = new Bloc();
        bloc.setNomBloc("Bloc 1");
        bloc.setCapaciteBloc(10L);
        bloc.setChambres(Arrays.asList(chambre1, chambre2));
    }

    @Test
    void testAddOrUpdate() {
        // Arrange
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre1).thenReturn(chambre2);

        // Act
        Bloc result = blocService.addOrUpdate(bloc);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getChambres().size());
        verify(blocRepository, times(1)).save(bloc); // Vérifie que save a été appelé une fois
        verify(chambreRepository, times(2)).save(any(Chambre.class)); // Vérifie que save a été appelé deux fois
    }
}
