package insetec.backend.repositories;

import insetec.backend.models.Sms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SmsRepository extends JpaRepository<Sms, String> {

    @Query("SELECT sms FROM Sms sms WHERE sms.userId = :userId")
    Sms findByUserId(@Param("userId") String userId);

}
