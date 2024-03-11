package com.LUXURYCLIQ.Repository;

import com.LUXURYCLIQ.entity.Ordermanagement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrdermanagementRepository extends JpaRepository<Ordermanagement, UUID> {

    List<Ordermanagement> findByUserUuid(UUID uuid);


//    String findByUserName(String username);

    List<Ordermanagement> findByUser_Username(String username);
}
