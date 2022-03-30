package com.threatgator.usermanagement.service;

import com.threatgator.usermanagement.model.Assets;
import com.threatgator.usermanagement.model.Users;
import org.springframework.boot.autoconfigure.security.SecurityProperties;


import java.util.List;

// Users Service Interface
public interface AssetsService {
    public Assets saveAsset(Assets assets);
    public List<Assets> getAllAssets();
    public List<Assets> getUserAssets(int admin_id);
}
