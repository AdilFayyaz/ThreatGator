package com.threatgator.usermanagement.service;

import com.threatgator.usermanagement.model.Organization;
import com.threatgator.usermanagement.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationRepository OrganizationRepository;

    @Override
    public Optional<Organization> getOrganization(Integer id) {
        return OrganizationRepository.findById(id);
    }

    @Override
    public Optional<Organization> getOrganization(String name) {
        return OrganizationRepository.findByName(name);
    }


    @Override
    public String getSector(Integer id) {
        Optional<Organization> organization=OrganizationRepository.findById(id);
        if (organization.isPresent())
            return organization.get().getSector();
        else
            return "";
    }

    @Override
    public String getName(Integer id) {
        Optional<Organization> organization=OrganizationRepository.findById(id);
        if (organization.isPresent())
            return organization.get().getName();
        else
            return "";
    }

    @Override
    public String getCountry(Integer id) {
        Optional<Organization> organization=OrganizationRepository.findById(id);
        if (organization.isPresent())
            return organization.get().getCountry();
        else
            return "";
    }

    @Override
    public Organization saveOrganization(Organization organization) {
        return OrganizationRepository.save(organization);
    }

    @Override
    public List<Organization> getAll(){
        return OrganizationRepository.findAll();
    }
}
