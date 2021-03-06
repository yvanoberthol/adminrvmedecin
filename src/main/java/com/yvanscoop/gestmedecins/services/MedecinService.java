package com.yvanscoop.gestmedecins.services;

import com.yvanscoop.gestmedecins.entities.Medecin;
import com.yvanscoop.gestmedecins.repositories.MedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class MedecinService {

    @Autowired
    private MedecinRepository medecinRepository;

    public List<Medecin> getAll(String mot) {
        return medecinRepository.findByMedecin(mot + "%");
    }


    public int countActif(){
        return medecinRepository.findByMedecinActif().size();
    }

    public Page<Medecin> findPaginated(String mot, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Medecin> list;

        if (getAll(mot).size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, getAll(mot).size());
            list = getAll(mot).subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), getAll(mot).size());

    }

    public Medecin getOne(Long id) {
        return medecinRepository.getOne(id);
    }

    public Medecin save(Medecin Medecin) {
        return medecinRepository.save(Medecin);
    }

    public void delete(Long id) {
        medecinRepository.deleteById(id);
    }

    public boolean exists(Long id) {
        return medecinRepository.existsById(id);
    }

    public Medecin getByNom(String nom) {
        return medecinRepository.getByMedecinname(nom);
    }

    public Medecin getByMatricule(String matricule) {
        return medecinRepository.findByMatricule(matricule);
    }

    public Medecin getByEmail(String email) {
        return medecinRepository.findByEmail(email);
    }

    public String[] headers(){
        return medecinRepository.headers();
    }
}
