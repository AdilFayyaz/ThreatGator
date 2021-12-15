package com.threatgator.source_management.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.threatgator.source_management.model.Source;
import com.threatgator.source_management.service.SourcesService;

import java.util.List;
@Service
public class SourcesServiceImpl implements SourcesService {
    @Autowired
    private SourceRepository sourceRepository;
    @Override
    public Source saveSource(Source source) {
        return sourceRepository.save(source);
    }

    @Override
    public List<Source> getAllSources() {
        return sourceRepository.findAll();
    }
}
