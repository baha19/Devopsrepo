package tn.esprit.spring.Services.Etudiant;

import tn.esprit.spring.DAO.Entities.Etudiant;

import java.util.List;
import java.util.Map;

public interface IEtudiantService {
    Etudiant addOrUpdate(Etudiant e);
    List<Etudiant> findAll();
    Etudiant findById(long id);
    void deleteById(long id);
    void delete(Etudiant e);
    public Map<String, Long> repartitionEtudiantsParEcole();
}
