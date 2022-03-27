package br.com.cvc.evaluation.service.mapper;

import br.com.cvc.evaluation.broker.dto.BrokerHotelRoom;
import br.com.cvc.evaluation.domain.PriceDetail;
import br.com.cvc.evaluation.domain.Room;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-27T10:01:38-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class RoomMapperImpl implements RoomMapper {

    @Override
    public Room toDomain(BrokerHotelRoom broker) {
        if ( broker == null ) {
            return null;
        }

        Room room = new Room();

        room.setRoomID( broker.getRoomID() );
        room.setCategoryName( broker.getCategoryName() );

        room.setTotalPrice( BigDecimal.ZERO );
        room.setPriceDetail( PriceDetail.builder().pricePerDayAdult(BigDecimal.ZERO).pricePerDayAdult(BigDecimal.ZERO).build() );

        return room;
    }
}
