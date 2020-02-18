package br.com.mejias.devopsmaturity.repository;

import br.com.mejias.devopsmaturity.domain.Tools;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Tools entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToolsRepository extends JpaRepository<Tools, Long> {

}
