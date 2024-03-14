package insetec.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "images")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Image {

    @Id
    private String id;

    private String type;

    @JsonIgnoreProperties("images")
    @OneToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;
}
