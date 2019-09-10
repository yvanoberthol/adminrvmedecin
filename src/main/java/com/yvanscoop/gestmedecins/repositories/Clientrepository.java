package com.yvanscoop.gestmedecins.repositories;

import com.yvanscoop.gestmedecins.entities.Rv;
import com.yvanscoop.gestmedecins.entities.security.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Clientrepository extends JpaRepository<Client,Long> {

    @Override
    List<Client> findAll();

    @Override
    Client getOne(Long id);

    @Query("select c from Client c where c.firstName like ?1 or c.lastName like ?1")
    List<Client> findByClient(String s);

    @Query("select r from Rv r where r.client.email = ?1")
    List<Rv> findRvByClient(String email);
}
