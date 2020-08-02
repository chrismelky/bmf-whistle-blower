package tz.or.mkapafoundation.whistleblower.repository;

import tz.or.mkapafoundation.whistleblower.domain.Comment;
import tz.or.mkapafoundation.whistleblower.service.dto.CommentDTO;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByComplain_Id(Long complainId);
}
