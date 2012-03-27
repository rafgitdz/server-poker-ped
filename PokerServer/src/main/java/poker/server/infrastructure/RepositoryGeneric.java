package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure interface : RepositoryGeneric
 */

import java.util.List;

/**
 * Generic interface involving all database requests.
 */
public interface RepositoryGeneric<T, TId> {

	public T load(TId id);

	public T save(T entity);

	public T saveOrUpdate(T entity, TId id);

	public void delete(TId id);

	public List<T> loadAll();

	public T update(T entity);
}
