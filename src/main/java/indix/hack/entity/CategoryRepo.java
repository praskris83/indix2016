/**
 * 
 */
package indix.hack.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author user
 *
 */
public interface CategoryRepo extends JpaRepository<Category, Long> {

	public List<Category> findByIdBetween(long start, long end);
}
