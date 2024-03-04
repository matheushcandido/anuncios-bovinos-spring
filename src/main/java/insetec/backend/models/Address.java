package insetec.backend.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "adresses")
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String zip;

    private String street;

    private String neighborhood;

    private String number;

    private String city;

    private String state;

    private String idUser;
}
