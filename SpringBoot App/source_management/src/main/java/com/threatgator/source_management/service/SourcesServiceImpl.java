package com.threatgator.source_management.service;

import com.threatgator.source_management.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.threatgator.source_management.model.Source;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Source findOne(int id) {
        return sourceRepository.findById(id).get();
    }

    @Override
    public String deleteSource(int id) {
        sourceRepository.deleteById(id);
        return "{'message': 'Account deleted successfully'}";
    }
}
