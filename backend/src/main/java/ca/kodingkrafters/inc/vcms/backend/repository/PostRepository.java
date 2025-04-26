package ca.kodingkrafters.inc.vcms.backend.repository;

import ca.kodingkrafters.inc.vcms.backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
