package com.LUXURYCLIQ.Repository;

import com.LUXURYCLIQ.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository

public interface AddressRepository extends JpaRepository <Address, UUID>{


        List<Address> findByUser_Username(String username);

    List<Address> findByCity(String City);

    Address findByUuid(UUID uuid);



}
