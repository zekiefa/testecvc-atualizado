package br.com.cvc.evaluation.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.cvc.evaluation.broker.BrokerService;
import br.com.cvc.evaluation.broker.dto.BrokerHotel;
import br.com.cvc.evaluation.broker.dto.BrokerHotelRoom;
import br.com.cvc.evaluation.domain.Hotel;
import br.com.cvc.evaluation.domain.PriceDetail;
import br.com.cvc.evaluation.domain.Room;
import br.com.cvc.evaluation.exceptions.BookingPeriodInvalidException;
import br.com.cvc.evaluation.exceptions.HotelNotFoundException;
import br.com.cvc.evaluation.service.mapper.HotelMapper;
import br.com.cvc.evaluation.service.mapper.RoomMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookingService {
	private static final Long ONE_DAY = Long.valueOf("1");
	private static final Integer ONE_PAX = 1;

	private final BrokerService brokerService;

	private final FeeService feeService;

	private final HotelMapper hotelMapper;

	private final RoomMapper roomMapper;

	public BookingService(final BrokerService brokerService, final FeeService feeService, final HotelMapper hotelMapper,
					final RoomMapper roomMapper) {
		this.brokerService = brokerService;
		this.feeService = feeService;
		this.hotelMapper = hotelMapper;
		this.roomMapper = roomMapper;
	}

	private BigDecimal calculateTotalPrice(final BigDecimal paxPrice, final Long days) {
		log.info("Calculating total price: pax price {} for {} days", paxPrice, days);
		final var fee = this.feeService.calculateFee(paxPrice, days);

		return paxPrice.add(fee).multiply(BigDecimal.valueOf(days));
	}

	private Long calculatePeriod(final LocalDate checkin, final LocalDate checkout) {
		log.info("Calculating period: checking {}, checkout {}", checkin, checkout);

		if (checkin.isAfter(checkout)) {
			throw new BookingPeriodInvalidException("The checkin date is greater than the checkout date.");
		}

		return checkin.until(checkout, ChronoUnit.DAYS);
	}

	private Room calculateTotalPrice(final BrokerHotelRoom brokerHotelRoom, final Long days, final Integer adults,
			final Integer child) {
		log.info("Calculating total price: room {}, {} days, {} adults, {} child",
						brokerHotelRoom.getCategoryName(), days, adults, child);
		final var room = this.roomMapper.toDomain(brokerHotelRoom);
		var pricePerDayAdult = BigDecimal.ZERO;
		var pricePerDayChild = BigDecimal.ZERO;
		var totalPrice = BigDecimal.ZERO;

		if (adults > 0) {
			pricePerDayAdult = this.calculateTotalPrice(brokerHotelRoom.getPrice().getAdult(), ONE_DAY);
			totalPrice = totalPrice.add(brokerHotelRoom.getPrice().getAdult().multiply(BigDecimal.valueOf(days)));
		}

		if (child > 0) {
			pricePerDayChild = this.calculateTotalPrice(brokerHotelRoom.getPrice().getChild(), ONE_DAY);
			totalPrice = totalPrice.add(brokerHotelRoom.getPrice().getChild().multiply(BigDecimal.valueOf(days)));
		}

		log.info("Total price: {}", totalPrice);
		room.setPriceDetail(PriceDetail.builder()
										.pricePerDayAdult(pricePerDayAdult)
										.pricePerDayChild(pricePerDayChild)
										.build());
		room.setTotalPrice(totalPrice);

		return room;
	}

	private Hotel calculateBooking(final BrokerHotel brokerHotel, final Long days, final Integer adults,
			final Integer child) {
		log.info("Calculating booking: hotel {}, {} days, {} adults, {} child",
						brokerHotel.getName(), days, adults, child);
		final var hotel = this.hotelMapper.toDomain(brokerHotel);
		hotel.setRooms(brokerHotel.getRooms().stream()
				.map(brokerHotelRoom -> this.calculateTotalPrice(brokerHotelRoom, days, adults, child))
				.collect(Collectors.toList()));

		log.info("Booking result {}", hotel);
		return hotel;
	}

	public Optional<Hotel> getHotelDetails(final Integer codeHotel) {
		log.info("Finding hotel details: {}", codeHotel);
		final var hotelDetails = this.brokerService.getHotelDetails(codeHotel);

		if (hotelDetails.isEmpty()) {
			throw new HotelNotFoundException("No hotel details");
		}

		return hotelDetails.map(brokerHotel -> this.calculateBooking(brokerHotel, ONE_DAY, ONE_PAX, ONE_PAX));
	}

	public List<Hotel> findHotels(final Integer cityCode, final LocalDate checkin, final LocalDate checkout,
			final Integer adults, final Integer child) {
		log.info("Finding hotels: city {}, checkin {}, checkout {}", cityCode, checkin, checkout);
		final var hotels = this.brokerService.findHotelsByCity(cityCode);
		final var period = this.calculatePeriod(checkin, checkout);

		log.info("Result of searching hotels: {}", hotels.size());
		return hotels.stream().map(brokerHotel -> this.calculateBooking(brokerHotel, period, adults, child))
				.collect(Collectors.toList());
	}
}
