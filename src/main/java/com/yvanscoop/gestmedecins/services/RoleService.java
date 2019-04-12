package com.yvanscoop.gestmedecins.services;

import com.yvanscoop.gestmedecins.entities.Role;
import com.yvanscoop.gestmedecins.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository RoleRepository;

    private final List<Role> getAll(String mot) {
        return RoleRepository.findByRole(mot + "%");
    }

    public Page<Role> findPaginated(String mot, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Role> list;

        if (getAll(mot).size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, getAll(mot).size());
            list = getAll(mot).subList(startItem, toIndex);
        }

        Page<Role> RolePage
                = new PageImpl<Role>(list, PageRequest.of(currentPage, pageSize), getAll(mot).size());

        return RolePage;
    }

    public Role getOne(Long id) {
        return RoleRepository.getOne(id);
    }

    public Role save(Role Role) {
        return RoleRepository.save(Role);
    }

    public void delete(Long id) {
        RoleRepository.deleteById(id);
    }

    public boolean exists(Long id) {
        return RoleRepository.existsById(id);
    }

    public Role getByNom(String nom) {
        return RoleRepository.findRole(nom);
    }
}
