package com.threatgator.source_management.service;
import com.threatgator.source_management.model.Source;
import java.util.List;
import java.util.Optional;

public interface SourcesService {
    public Source saveSource(Source source);
    public List<Source> getAllSources();
    public Source findOne(int id);
    public String deleteSource(int id);
}
