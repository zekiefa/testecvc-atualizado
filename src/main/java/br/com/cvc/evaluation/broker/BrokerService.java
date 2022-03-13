package br.com.cvc.evaluation.broker;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.cvc.evaluation.broker.dto.BrokerHotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BrokerService {
	@Value("${broker.hotels.endpoint}")
	private String hotelsEndpoint;

	@Value("${broker.hotel.details.endpoint}")
	private String hotelDetailsEndpoint;

	public List<BrokerHotel> findHotelsByCity(final Integer codeCity) {
		final ResponseEntity<List<BrokerHotel>> response = WebClient.create()
						.get()
						.uri(this.hotelsEndpoint.concat(codeCity.toString()))
						.retrieve()
						.toEntityList(BrokerHotel.class)
						.block();

		assert response != null;
		return response.getBody();
	}

	public Optional<BrokerHotel> getHotelDetails(final Integer codeHotel) {
		final ResponseEntity<List<BrokerHotel>> response = WebClient.create()
						.get()
						.uri(this.hotelDetailsEndpoint.concat(codeHotel.toString()))
						.retrieve()
						.toEntityList(BrokerHotel.class)
						.block();

		assert response != null;
		if (!Objects.requireNonNull(response.getBody()).isEmpty()) {
			return Optional.of(response.getBody().get(0));
		}

		return Optional.empty();
	}

}
