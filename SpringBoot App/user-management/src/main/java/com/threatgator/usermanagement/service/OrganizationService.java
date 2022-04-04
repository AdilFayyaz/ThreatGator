package com.threatgator.usermanagement.service;

import com.threatgator.usermanagement.model.Assets;
import com.threatgator.usermanagement.model.Organization;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {
    public Optional<Organization> getOrganization(Integer id);
    public String getSector(Integer id);
    public String getName(Integer id);
    public String getCountry(Integer id);
    public Organization saveOrganization(Organization organization);
    public List<Organization> getAll();
//    public List<Assets> getAssets(Integer id);
//    public Assets addAsset(Assets asset, Integer id);
}
