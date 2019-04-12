package com.yvanscoop.gestmedecins.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
public class TokenRv {

    @Id
    @GeneratedValue
    private Long id;

    private String token;

    private Date expiryDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rv_id", referencedColumnName = "id", nullable = false)
    private Rv rv;

    public TokenRv(String token){
        this.token = token;
    }

    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(calendar.getTime().getTime());
    }
}
