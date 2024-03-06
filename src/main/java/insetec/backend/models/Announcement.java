package insetec.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "announcements")
@Data
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private double price;

    @Temporal(TemporalType.DATE)
    private Date birth;

    private String rga;

    private String code;

    private String idBreed;

    private String idBreedPaternal;

    private String idBreedMaternal;

    private String note;

    private User user;

}
