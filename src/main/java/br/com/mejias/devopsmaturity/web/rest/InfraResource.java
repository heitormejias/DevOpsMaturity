package br.com.mejias.devopsmaturity.web.rest;

import br.com.mejias.devopsmaturity.domain.Infra;
import br.com.mejias.devopsmaturity.repository.InfraRepository;
import br.com.mejias.devopsmaturity.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link br.com.mejias.devopsmaturity.domain.Infra}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InfraResource {

    private final Logger log = LoggerFactory.getLogger(InfraResource.class);

    private static final String ENTITY_NAME = "infra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfraRepository infraRepository;

    public InfraResource(InfraRepository infraRepository) {
        this.infraRepository = infraRepository;
    }

    /**
     * {@code POST  /infras} : Create a new infra.
     *
     * @param infra the infra to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infra, or with status {@code 400 (Bad Request)} if the infra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/infras")
    public ResponseEntity<Infra> createInfra(@RequestBody Infra infra) throws URISyntaxException {
        log.debug("REST request to save Infra : {}", infra);
        if (infra.getId() != null) {
            throw new BadRequestAlertException("A new infra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Infra result = infraRepository.save(infra);
        return ResponseEntity.created(new URI("/api/infras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /infras} : Updates an existing infra.
     *
     * @param infra the infra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infra,
     * or with status {@code 400 (Bad Request)} if the infra is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/infras")
    public ResponseEntity<Infra> updateInfra(@RequestBody Infra infra) throws URISyntaxException {
        log.debug("REST request to update Infra : {}", infra);
        if (infra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Infra result = infraRepository.save(infra);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, infra.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /infras} : get all the infras.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infras in body.
     */
    @GetMapping("/infras")
    public ResponseEntity<List<Infra>> getAllInfras(Pageable pageable) {
        log.debug("REST request to get a page of Infras");
        Page<Infra> page = infraRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /infras/:id} : get the "id" infra.
     *
     * @param id the id of the infra to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infra, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/infras/{id}")
    public ResponseEntity<Infra> getInfra(@PathVariable Long id) {
        log.debug("REST request to get Infra : {}", id);
        Optional<Infra> infra = infraRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(infra);
    }

    /**
     * {@code DELETE  /infras/:id} : delete the "id" infra.
     *
     * @param id the id of the infra to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/infras/{id}")
    public ResponseEntity<Void> deleteInfra(@PathVariable Long id) {
        log.debug("REST request to delete Infra : {}", id);
        infraRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
