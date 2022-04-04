package com.threatgator.usermanagement.service;

import com.threatgator.usermanagement.model.Assets;
import com.threatgator.usermanagement.model.Users;
import com.threatgator.usermanagement.repository.AssetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AssetsServiceImplemented implements AssetsService{
    @Autowired
    private AssetsRepository assetsRepository;

    @Override
    public Assets saveAsset(Assets assets){return assetsRepository.save(assets);}

    @Override
    public List<Assets> getAllAssets() {return (List<Assets>) assetsRepository.findAll();}

}
