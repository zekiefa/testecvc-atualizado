package br.com.cvc.evaluation.config;

import java.util.Collections;

import br.com.cvc.evaluation.fixtures.UserFixture;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class SpringSecurityTestConfig extends WebSecurityConfigurerAdapter {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        FixtureFactoryLoader.loadTemplates("br.com.cvc.evaluation.fixtures");

        final var passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        final var userBuilder = User.builder().passwordEncoder(passwordEncoder::encode);
        final var adminAuthorities = AuthorityUtils.createAuthorityList("admin");
        final var userAuthorities = AuthorityUtils.createAuthorityList("user");

        return new InMemoryUserDetailsManager(Fixture
                        .from(br.com.cvc.evaluation.domain.User.class)
                        .gimme(2, UserFixture.VALID));
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> new UsernamePasswordAuthenticationToken(
                        authentication.getPrincipal(),
                        authentication.getCredentials(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
