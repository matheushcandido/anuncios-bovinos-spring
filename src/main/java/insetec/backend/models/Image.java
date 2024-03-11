package insetec.backend.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "images")
@Data
public class Image {

    @Id
    private String id;

    private String type;

    @OneToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;
}
