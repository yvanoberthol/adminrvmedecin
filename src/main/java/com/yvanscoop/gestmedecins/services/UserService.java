package com.yvanscoop.gestmedecins.services;

import com.yvanscoop.gestmedecins.entities.User;
import com.yvanscoop.gestmedecins.repositories.UserRepository;
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
public class UserService {

    //private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    private final List<User> getAll(String mot) {
        return userRepository.findByUser(mot + "%");
    }

    public Page<User> findPaginated(String mot, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;

        if (getAll(mot).size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, getAll(mot).size());
            list = getAll(mot).subList(startItem, toIndex);
        }

        Page<User> UserPage
                = new PageImpl<User>(list, PageRequest.of(currentPage, pageSize), getAll(mot).size());

        return UserPage;
    }

    public User getOne(Long id) {
        return userRepository.getOne(id);
    }

    public User save(User User) {
        return userRepository.save(User);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void desactivate(String username) {
        userRepository.desactivateUser(username);
    }

    public void activate(String username) {
        userRepository.activateUser(username);
    }

    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }

    public User getByNom(String nom) {
        return userRepository.getByUsername(nom);
    }
}
