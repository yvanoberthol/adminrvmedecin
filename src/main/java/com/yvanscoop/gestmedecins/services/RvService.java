package com.yvanscoop.gestmedecins.services;

import com.yvanscoop.gestmedecins.entities.Rv;
import com.yvanscoop.gestmedecins.repositories.Rvrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RvService {

    @Autowired
    private Rvrepository rvrepository;


    public List<Rv> getRvByPatient(String email){
        return rvrepository.findRvByClient(email);
    }
}
