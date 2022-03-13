package br.com.cvc.evaluation.fixtures;

import br.com.cvc.evaluation.broker.dto.BrokerHotel;
import br.com.cvc.evaluation.broker.dto.BrokerHotelRoom;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class BrokerHotelFixture implements TemplateLoader {
    public static final String VALIDO = "valido";

    @Override
    public void load() {
        Fixture.of(BrokerHotel.class).addTemplate(VALIDO, new Rule(){
            {
                add("id", random(Integer.class, range(1, 999)));
                add("name", name());
                add("cityCode", random(Integer.class, range(1000, 1999)));
                add("cityName", name());
                add("rooms", has(3).of(BrokerHotelRoom.class, BrokerHotelRoomFixture.VALIDO));
            }
        });
    }
}
