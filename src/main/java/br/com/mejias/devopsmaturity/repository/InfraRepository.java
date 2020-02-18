package br.com.mejias.devopsmaturity.repository;

import br.com.mejias.devopsmaturity.domain.Infra;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Infra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfraRepository extends JpaRepository<Infra, Long> {

}
