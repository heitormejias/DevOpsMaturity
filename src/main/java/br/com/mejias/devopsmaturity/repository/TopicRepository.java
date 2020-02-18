package br.com.mejias.devopsmaturity.repository;

import br.com.mejias.devopsmaturity.domain.Topic;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Topic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

}
