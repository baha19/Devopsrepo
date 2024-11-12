package tn.esprit.spring.Services.Etudiant;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EtudiantService implements IEtudiantService {
    EtudiantRepository repo;

    @Override
    public Etudiant addOrUpdate(Etudiant e) {
        return repo.save(e);
    }

    @Override
    public List<Etudiant> findAll() {
        return repo.findAll();
    }

    @Override
    public Etudiant findById(long id) {
        return repo.findById(id).get();
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Etudiant e) {
        repo.delete(e);
    }
    // Obtenir la répartition des étudiants par école
    @Override
    public Map<String, Long> repartitionEtudiantsParEcole() {
        List<Etudiant> etudiants = repo.findAll();
        Map<String, Long> repartition = new HashMap<>();

        for (Etudiant etudiant : etudiants) {
            String ecole = etudiant.getEcole();
            repartition.put(ecole, repartition.getOrDefault(ecole, 0L) + 1);
        }

        return repartition;
    }
}
