package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.hibernate.criterion.AbstractEmptinessExpression;

public class MultiplicationServiceImplTest {
	
	private MultiplicationServiceImpl multiplicationServiceImpl;
	
	@Mock
	private RandomGeneratorService randomGeneratorService;
	
	@Mock
	private MultiplicationResultAttemptRepository attemptRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService, attemptRepository, userRepository);
	}
	
	@Test
	public void createRandomMultiplicationTest() {
		given(randomGeneratorService.generateRandomFactor()).willReturn(50,30);
		
		Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();
		
		assertThat(multiplication.getFactorA()).isEqualTo(50);
		assertThat(multiplication.getFactorB()).isEqualTo(30);
	}
	
	@Test
	public void checkCorrectAttempTest() {
		Multiplication multiplication = new Multiplication(50,60);
		User user = new User("Hoang");
		MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
				user, multiplication, 3000, false);
		MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(
				user, multiplication, 3000, true);
		
		given(userRepository.findByAlias("Hoang")).willReturn(Optional.empty());
		
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
		
		assertThat(attemptResult).isTrue();
		verify(attemptRepository).save(verifiedAttempt);
	}
	
	@Test
	public void checkWrongAttemptTest() {
		Multiplication multiplication = new Multiplication(50,60);
		User user = new User("Hoang");
		MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
				user, multiplication, 3010, false);
		
		given(userRepository.findByAlias("Hoang")).willReturn(Optional.empty());
		
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
		
		assertThat(attemptResult).isFalse();
		verify(attemptRepository).save(verifiedAttempt);
	}
}
