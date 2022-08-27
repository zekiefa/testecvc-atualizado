package br.com.cvc.evaluation.fixtures;

import java.util.UUID;

import br.com.cvc.evaluation.domain.Login;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class LoginFixture implements TemplateLoader {
    public static final String VALID = "valid";
    @Override
    public void load() {
        Fixture.of(Login.class).addTemplate(VALID, new Rule(){
            {
                add("user", "usuario");
                add("passwd", UUID.randomUUID().toString());
            }
        });
    }
}
