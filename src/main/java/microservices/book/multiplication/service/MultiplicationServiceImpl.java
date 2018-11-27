package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {
	private RandomGeneratorService randomGeneratorService;
	
	@Autowired
	public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService) {
		this.randomGeneratorService = randomGeneratorService;
	}
	
	@Override
	public Multiplication createRandomMultiplication() {
		int factorA = randomGeneratorService.generateRandomFactor();
		int factorB = randomGeneratorService.generateRandomFactor();
		
		return new Multiplication(factorA, factorB);
	}
	
	@Override
	public boolean checkAttempt(final MultiplicationResultAttempt resultAttempt) {
		boolean correct = resultAttempt.getResultAttempt() == 
				resultAttempt.getMultiplication().getFactorA() * 
				resultAttempt.getMultiplication().getFactorB();
		
		Assert.isTrue(!resultAttempt.isCorrect(),"You cna't send an attempt marked as correct!!");
		
		MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(
				resultAttempt.getUser(), resultAttempt.getMultiplication(), 
				resultAttempt.getResultAttempt(), 
				correct);
		
		return correct;
	}
}
