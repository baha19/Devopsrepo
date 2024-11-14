package tn.esprit.spring.DAO.Entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class BlocTest {

    private Bloc bloc;

    @BeforeEach
    public void setUp() {
        bloc = new Bloc();
        bloc.setIdBloc(1L);
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(100);
    }

    @Test
    public void testGettersAndSetters() {
        Assertions.assertEquals(1L, bloc.getIdBloc());
        Assertions.assertEquals("Bloc A", bloc.getNomBloc());
        Assertions.assertEquals(100, bloc.getCapaciteBloc());
    }

    @Test
    public void testSetChambres() {
        List<Chambre> chambres = new ArrayList<>();
        bloc.setChambres(chambres);
        Assertions.assertEquals(chambres, bloc.getChambres());
    }
}
