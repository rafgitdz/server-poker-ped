package poker.server.infrastructure;

/**
 * @author PokerServerGroup
 * 
 *         Infrastructure class : RepositoryGenericJPA
 */

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class RepositoryGenericJPA<T, TId> implements RepositoryGeneric<T, TId> {

	@PersistenceContext(unitName = "PokerServerPU")
	protected EntityManager em;
	private Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public RepositoryGenericJPA() {

		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public T save(T entity) {

		em.persist(entity);
		return entity;
	}

	@Override
	public T saveOrUpdate(T entity, TId id) {

		if (em.find(persistentClass, id) != null)
			em.merge(entity);
		else
			em.persist(entity);

		return entity;
	}

	@Override
	public T update(T entity) {
		return em.merge(entity);
	}

	@Override
	public T load(TId id) {
		return em.find(persistentClass, id);
	}

	@Override
	public void delete(TId id) {
		T entity = em.find(persistentClass, id);
		em.remove(entity);
	}

	@Override
	public List<T> loadAll() {
		@SuppressWarnings("unchecked")
		List<T> entities = em.createQuery("from " + persistentClass.getName())
				.getResultList();
		return entities;
	}
}
