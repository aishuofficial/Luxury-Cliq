package com.LUXURYCLIQ.Service;


import com.LUXURYCLIQ.Repository.AddressRepository;
import com.LUXURYCLIQ.entity.Address;
import com.LUXURYCLIQ.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class
AddressService {

    public AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }



    public Address createAddress(User user, Address address) {
        address.setUser(user);
        return addressRepository.save(address);
    }



    public  void upadateAddress(Address address)
    {
        System.out.println("in sr"+ address);

        Address address1=addressRepository.save(address);


    }


    public List<Address> getAddressesByUsername(String username) {
        return addressRepository.findByUser_Username(username);
    }

    public Address getAddressById(UUID uuid) {
        return addressRepository.findById(uuid).orElseThrow();


    }


    public void saveAddress(Address address) {
        addressRepository.save(address);
    }



    public void deleteAddress(Address address) {

            address. setDeleted(true);
            addressRepository.save(address);

        }

    }

