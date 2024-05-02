package insetec.backend.repositories;

import insetec.backend.models.Sms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SmsRepository extends JpaRepository<Sms, String> {

    @Query("SELECT * FROM verification-codes WHERE user_id = :userId")
    Sms findByUserId(@Param("userId") String userId);

}
