package com.threatgator.source_management.service;

import com.threatgator.source_management.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.threatgator.source_management.model.Source;

import java.util.List;
import java.util.Optional;
// Sources Service Class
@Service
public class SourcesServiceImpl implements SourcesService {
    @Autowired
    private SourceRepository sourceRepository;
    // Save a source
    @Override
    public Source saveSource(Source source) {
        return sourceRepository.save(source);
    }
    // Get all sources
    @Override
    public List<Source> getAllSources() {
        return sourceRepository.findAll();
    }
    // Fine one specific source via ID
    @Override
    public Source findOne(int id) {
        return sourceRepository.findById(id).get();
    }
    // Delete a source
    @Override
    public String deleteSource(int id) {
        sourceRepository.deleteById(id);
        return "{'message': 'Account deleted successfully'}";
    }
}
