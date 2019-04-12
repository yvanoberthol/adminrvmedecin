package com.yvanscoop.gestmedecins.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "compte_medecin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompteMedecin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(unique = true)
    private String login;

    private String password;

    private boolean enabled;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medecin_id", referencedColumnName = "id", nullable = false)
    private Medecin medecin;
}
