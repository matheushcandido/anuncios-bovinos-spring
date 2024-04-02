package insetec.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "verification-codes")
@Data
public class Sms {
    @Id
    private String id;

    private String userId;

    private String code;

    private String phoneNumber;
}
