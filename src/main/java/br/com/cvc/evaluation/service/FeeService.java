package br.com.cvc.evaluation.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FeeService {
	// o ideal é que a comissão seja parametrizada via banco de dados
	// ou disponibilizada por um serviço externo
	private final BigDecimal fee;

	public FeeService(@Value("${booking.fee}") final BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal calculateFee(final BigDecimal paxPrice, final Long days) {
		final var totalPricePax = paxPrice.multiply(BigDecimal.valueOf(days));

		return totalPricePax.multiply(this.fee);
	}
}
