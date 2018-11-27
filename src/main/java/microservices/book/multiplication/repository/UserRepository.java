package microservices.book.multiplication.repository;

import org.springframework.data.repository.CrudRepository;

import microservices.book.multiplication.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long>{
	Optional<User> findByAlias(final String alias);
}
