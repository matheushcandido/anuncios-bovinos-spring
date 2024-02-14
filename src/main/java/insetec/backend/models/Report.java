package insetec.backend.models;

import insetec.backend.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "reports")
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String idAnnouncement;

    private boolean sold;

    private boolean wrongAddress;

    private boolean wrongPrice;

    private boolean joke;

    private boolean wrongCategory;

    private boolean duplicated;

    private boolean illegal;

    private boolean fraud;

    private String note;

    private Status active = Status.ACTIVE;

}
