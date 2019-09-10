package com.yvanscoop.gestmedecins.repositories;

import com.yvanscoop.gestmedecins.entities.Rv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Rvrepository extends JpaRepository<Rv,Long> {

    @Query("select r from Rv r where r.client.email = ?1")
    List<Rv> findRvByClient(String email);
}
