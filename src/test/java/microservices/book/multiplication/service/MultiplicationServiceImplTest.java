package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class MultiplicationServiceImplTest {
	
	private MultiplicationServiceImpl multiplicationServiceImpl;
	
	@Mock
	private RandomGeneratorService randomGeneratorService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService);
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
		
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
		
		assertThat(attemptResult).isTrue();
	}
	
	@Test
	public void checkWrongAttemptTest() {
		Multiplication multiplication = new Multiplication(50,60);
		User user = new User("Hoang");
		MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
				user, multiplication, 3010, false);
		
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
		
		assertThat(attemptResult).isFalse();
	}
}
