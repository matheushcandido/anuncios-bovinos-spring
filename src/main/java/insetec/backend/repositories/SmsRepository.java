package insetec.backend.repositories;

import insetec.backend.models.Sms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<Sms, String> {

}
