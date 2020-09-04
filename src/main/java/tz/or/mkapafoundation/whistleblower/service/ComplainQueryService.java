package tz.or.mkapafoundation.whistleblower.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import tz.or.mkapafoundation.whistleblower.domain.Complain;
import tz.or.mkapafoundation.whistleblower.domain.*; // for static metamodels
import tz.or.mkapafoundation.whistleblower.repository.ComplainRepository;
import tz.or.mkapafoundation.whistleblower.service.dto.ComplainCriteria;
import tz.or.mkapafoundation.whistleblower.service.dto.ComplainDTO;
import tz.or.mkapafoundation.whistleblower.service.mapper.ComplainMapper;

/**
 * Service for executing complex queries for {@link Complain} entities in the database.
 * The main input is a {@link ComplainCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ComplainDTO} or a {@link Page} of {@link ComplainDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComplainQueryService extends QueryService<Complain> {

    private final Logger log = LoggerFactory.getLogger(ComplainQueryService.class);

    private final ComplainRepository complainRepository;

    private final ComplainMapper complainMapper;

    public ComplainQueryService(ComplainRepository complainRepository, ComplainMapper complainMapper) {
        this.complainRepository = complainRepository;
        this.complainMapper = complainMapper;
    }

    /**
     * Return a {@link List} of {@link ComplainDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ComplainDTO> findByCriteria(ComplainCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Complain> specification = createSpecification(criteria);
       return complainRepository.findAll(specification).stream().map(complainMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Return a {@link Page} of {@link ComplainDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ComplainDTO> findByCriteria(ComplainCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Complain> specification = createSpecification(criteria);
        return complainRepository.findAll(specification, page)
            .map(complainMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ComplainCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Complain> specification = createSpecification(criteria);
        return complainRepository.count(specification);
    }

    /**
     * Function to convert {@link ComplainCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Complain> createSpecification(ComplainCriteria criteria) {
        Specification<Complain> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Complain_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Complain_.name));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), Complain_.position));
            }
            if (criteria.getOrganisation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrganisation(), Complain_.organisation));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Complain_.email));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Complain_.status));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Complain_.phoneNumber));
            }
            if (criteria.getControlNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getControlNumber(), Complain_.controlNumber));
            }
            if (criteria.getWitnessesId() != null) {
                specification = specification.and(buildSpecification(criteria.getWitnessesId(),
                    root -> root.join(Complain_.witnesses, JoinType.LEFT).get(Witness_.id)));
            }
            if (criteria.getSuspectsId() != null) {
                specification = specification.and(buildSpecification(criteria.getSuspectsId(),
                    root -> root.join(Complain_.suspects, JoinType.LEFT).get(Suspect_.id)));
            }
            if (criteria.getAttachmentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getAttachmentsId(),
                    root -> root.join(Complain_.attachments, JoinType.LEFT).get(Attachment_.id)));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Complain_.category, JoinType.LEFT).get(Category_.id)));
            }
            if (criteria.getReceiversId() != null) {
                specification = specification.and(buildSpecification(criteria.getReceiversId(),
                    root -> root.join(Complain_.receivers, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
