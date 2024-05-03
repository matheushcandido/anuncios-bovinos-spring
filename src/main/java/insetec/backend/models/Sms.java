package insetec.backend.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "verification-codes")
@Data
public class Sms {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    private String code;

    private String phoneNumber;
}
