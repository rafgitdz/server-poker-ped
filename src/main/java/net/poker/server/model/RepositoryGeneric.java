package net.poker.server.model;

import java.util.List;

public interface RepositoryGeneric<T, TId> {

	public T load(TId id);

	public T save(T entity);

	public T save(T entity, TId id);

	public void delete(TId id);

	public List<T> loadAll();

	public T update(T entity);
}
