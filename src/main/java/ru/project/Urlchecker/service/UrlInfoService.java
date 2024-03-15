package ru.project.Urlchecker.service;

import org.springframework.stereotype.Service;
import ru.project.Urlchecker.dto.UrlInfoDTO;
import ru.project.Urlchecker.repository.UrlInfoRepository;
import ru.project.Urlchecker.tables.UrlInfo;

import java.util.List;

@Service
public class UrlInfoService {
    private final UrlInfoRepository urlInfoRepository;

    public UrlInfoService(UrlInfoRepository urlInfoRepository) {
        this.urlInfoRepository = urlInfoRepository;
    }

    public UrlInfo create(UrlInfoDTO dto) {
        return urlInfoRepository.save(new UrlInfo(dto.getUrl(),dto.getStatus(),dto.getDelay(),dto.getResponse_period())) ;
    }
    public List<UrlInfo> readAll(){
        return urlInfoRepository.findAll();
    }
    public UrlInfo update(UrlInfo urlInfo) {
        return urlInfoRepository.save(urlInfo);
    }
    public void delete(Long id) {
        urlInfoRepository.deleteById(id);
    }
}
