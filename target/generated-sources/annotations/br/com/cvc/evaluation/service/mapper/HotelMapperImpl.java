package br.com.cvc.evaluation.service.mapper;

import br.com.cvc.evaluation.broker.dto.BrokerHotel;
import br.com.cvc.evaluation.broker.dto.BrokerHotelRoom;
import br.com.cvc.evaluation.domain.Hotel;
import br.com.cvc.evaluation.domain.Hotel.HotelBuilder;
import br.com.cvc.evaluation.domain.Room;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-27T10:01:38-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class HotelMapperImpl implements HotelMapper {

    @Override
    public Hotel toDomain(BrokerHotel broker) {
        if ( broker == null ) {
            return null;
        }

        HotelBuilder hotel = Hotel.builder();

        hotel.id( broker.getId() );
        hotel.cityName( broker.getCityName() );
        hotel.name( broker.getName() );
        hotel.rooms( brokerHotelRoomListToRoomList( broker.getRooms() ) );

        return hotel.build();
    }

    protected Room brokerHotelRoomToRoom(BrokerHotelRoom brokerHotelRoom) {
        if ( brokerHotelRoom == null ) {
            return null;
        }

        Room room = new Room();

        room.setRoomID( brokerHotelRoom.getRoomID() );
        room.setCategoryName( brokerHotelRoom.getCategoryName() );

        return room;
    }

    protected List<Room> brokerHotelRoomListToRoomList(List<BrokerHotelRoom> list) {
        if ( list == null ) {
            return null;
        }

        List<Room> list1 = new ArrayList<Room>( list.size() );
        for ( BrokerHotelRoom brokerHotelRoom : list ) {
            list1.add( brokerHotelRoomToRoom( brokerHotelRoom ) );
        }

        return list1;
    }
}
