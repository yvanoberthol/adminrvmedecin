package com.yvanscoop.gestmedecins.services;

import com.yvanscoop.gestmedecins.entities.Specialite;
import com.yvanscoop.gestmedecins.repositories.SpecialiteRepository;
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
public class SpecialiteService {
    @Autowired
    private SpecialiteRepository specialiteRepository;

    public List<Specialite> getAll(String mot) {
        return specialiteRepository.findByNom(mot + "%");
    }

    public Page<Specialite> findPaginated(String mot, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Specialite> list;

        if (getAll(mot).size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, getAll(mot).size());
            list = getAll(mot).subList(startItem, toIndex);
        }

        Page<Specialite> specialitePage
                = new PageImpl<Specialite>(list, PageRequest.of(currentPage, pageSize), getAll(mot).size());

        return specialitePage;
    }

    public Specialite getOne(Long id) {
        return specialiteRepository.getOne(id);
    }

    public Specialite save(Specialite specialite) {
        return specialiteRepository.save(specialite);
    }

    public void delete(Long id) {
        specialiteRepository.deleteById(id);
    }

    public boolean exists(Long id) {
        return specialiteRepository.existsById(id);
    }

    public Specialite getByNom(String nom) {
        return specialiteRepository.getSpecialiteByNom(nom);
    }

}
