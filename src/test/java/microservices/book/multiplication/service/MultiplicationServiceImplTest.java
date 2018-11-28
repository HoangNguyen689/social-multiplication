package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.List;

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
		verify(attemptRepository).save(attempt);
	}
	
	@Test
	public void retrieveStatsTest() {
		Multiplication multiplication = new Multiplication(50, 60);
		User user = new User("Hoang");
		MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(
				user, multiplication, 3010, false);
		MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(
				user, multiplication, 3011, false);
		
		List<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);
		
		given(userRepository.findByAlias("Hoang")).willReturn(Optional.empty());
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("Hoang"))
        	.willReturn(latestAttempts);
        
        List<MultiplicationResultAttempt> latestAttemptsResult =
                multiplicationServiceImpl.getStatsForUser("Hoang");
		
        assertThat(latestAttemptsResult).isEqualTo(latestAttempts);
	}
}
