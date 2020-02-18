package br.com.mejias.devopsmaturity.web.rest;

import br.com.mejias.devopsmaturity.domain.Tools;
import br.com.mejias.devopsmaturity.repository.ToolsRepository;
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
 * REST controller for managing {@link br.com.mejias.devopsmaturity.domain.Tools}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ToolsResource {

    private final Logger log = LoggerFactory.getLogger(ToolsResource.class);

    private static final String ENTITY_NAME = "tools";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToolsRepository toolsRepository;

    public ToolsResource(ToolsRepository toolsRepository) {
        this.toolsRepository = toolsRepository;
    }

    /**
     * {@code POST  /tools} : Create a new tools.
     *
     * @param tools the tools to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tools, or with status {@code 400 (Bad Request)} if the tools has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tools")
    public ResponseEntity<Tools> createTools(@RequestBody Tools tools) throws URISyntaxException {
        log.debug("REST request to save Tools : {}", tools);
        if (tools.getId() != null) {
            throw new BadRequestAlertException("A new tools cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tools result = toolsRepository.save(tools);
        return ResponseEntity.created(new URI("/api/tools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tools} : Updates an existing tools.
     *
     * @param tools the tools to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tools,
     * or with status {@code 400 (Bad Request)} if the tools is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tools couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tools")
    public ResponseEntity<Tools> updateTools(@RequestBody Tools tools) throws URISyntaxException {
        log.debug("REST request to update Tools : {}", tools);
        if (tools.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Tools result = toolsRepository.save(tools);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tools.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tools} : get all the tools.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tools in body.
     */
    @GetMapping("/tools")
    public ResponseEntity<List<Tools>> getAllTools(Pageable pageable) {
        log.debug("REST request to get a page of Tools");
        Page<Tools> page = toolsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tools/:id} : get the "id" tools.
     *
     * @param id the id of the tools to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tools, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tools/{id}")
    public ResponseEntity<Tools> getTools(@PathVariable Long id) {
        log.debug("REST request to get Tools : {}", id);
        Optional<Tools> tools = toolsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tools);
    }

    /**
     * {@code DELETE  /tools/:id} : delete the "id" tools.
     *
     * @param id the id of the tools to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tools/{id}")
    public ResponseEntity<Void> deleteTools(@PathVariable Long id) {
        log.debug("REST request to delete Tools : {}", id);
        toolsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
