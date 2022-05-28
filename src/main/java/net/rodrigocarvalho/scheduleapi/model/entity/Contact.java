package net.rodrigocarvalho.scheduleapi.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter @Setter
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotEmpty
    private String name;

    @Column
    @NotEmpty
    private String email;

    @Column
    private Boolean favorite;

    @Column
    @Lob
    private byte[] photo;

}
