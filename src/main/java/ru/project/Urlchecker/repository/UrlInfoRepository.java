package ru.project.Urlchecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.Urlchecker.tables.UrlInfo;

@Repository
public interface UrlInfoRepository extends JpaRepository<UrlInfo,Long> {
}
