package microservices.book.multiplication.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationResultAttemptRepository
	extends CrudRepository<MultiplicationResultAttempt, Long>{
	List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias);
}
