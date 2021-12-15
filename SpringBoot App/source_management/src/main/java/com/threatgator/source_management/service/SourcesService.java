package com.threatgator.source_management.service;
import com.threatgator.source_management.model.Source;
import java.util.List;

public interface SourcesService {
    public Source saveSource(Source source);
    public List<Source> getAllSources();
}
