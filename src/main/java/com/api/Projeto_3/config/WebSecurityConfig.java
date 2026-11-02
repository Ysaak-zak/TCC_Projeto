package com.api.Projeto_3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {

   @Bean
    SecurityFilterChain secuguraFilter(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
            (req) -> req
            .requestMatchers("/home", "/cadastro/**", "/favicon.ico", "/error").permitAll()
            .requestMatchers("/css/**", "/img/**", "/js/**", "/iconsTrei/**").permitAll()
            .requestMatchers("/login").permitAll()
            .requestMatchers("/atleta").hasRole("ATLETA")
            .requestMatchers("/atleta/perfil").hasRole("ATLETA")
            .requestMatchers("/medico", "/medico/**").hasRole("MEDICO")
            .requestMatchers("/treinador", "/treinador/**").hasRole("TREINADOR")
        )
        .formLogin(form -> form
            .loginPage("/login")               
            .loginProcessingUrl("/login")       
            .usernameParameter("cpf")           
            .passwordParameter("password")      
            .successHandler((req, res, aut) -> {
                String autRoles = aut.getAuthorities().iterator().next().getAuthority();

                switch (autRoles) {
                    case "ROLE_ATLETA":
                        res.sendRedirect("/atleta");
                        break;
                    case "ROLE_MEDICO":
                        res.sendRedirect("/medico");
                        break;
                    case "ROLE_TREINADOR":
                        res.sendRedirect("/treinador");
                        break;
                    default:           
                        break;
                }
            })
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/home")
        )
        .httpBasic(Customizer.withDefaults());
        
        return http.build();   
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
   


}
