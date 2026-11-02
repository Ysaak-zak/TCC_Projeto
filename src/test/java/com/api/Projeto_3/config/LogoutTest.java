package com.api.Projeto_3.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import jakarta.servlet.Filter;

class LogoutTest {
    @Test
    void sairEncerraSessaoDoAtletaERedirecionaParaHome() throws Exception {
        try (var context = new AnnotationConfigWebApplicationContext()) {
            context.setServletContext(new MockServletContext());
            context.register(WebSecurityConfig.class);
            context.refresh();
            var mvc = MockMvcBuilders.webAppContextSetup(context)
                    .addFilters(context.getBean("springSecurityFilterChain", Filter.class)).build();
            var session = new MockHttpSession();
            var atleta = User.withUsername("4rf").password("senha").roles("ATLETA").build();
            var authentication = new UsernamePasswordAuthenticationToken(atleta, null, atleta.getAuthorities());
            session.setAttribute("SPRING_SECURITY_CONTEXT", new SecurityContextImpl(authentication));

            // A página recebe o mesmo token usado pelo formulário de logout.
            var pagina = mvc.perform(get("/atleta").session(session)).andReturn();
            var csrf = (CsrfToken) pagina.getRequest().getAttribute(CsrfToken.class.getName());
            String token = csrf.getToken();

            mvc.perform(post("/logout").accept("text/html").session(session))
                    .andExpect(status().isForbidden());
            assertFalse(session.isInvalid());

            mvc.perform(post("/logout").accept("text/html").session(session).param(csrf.getParameterName(), token))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/home"));
            assertTrue(session.isInvalid());
        }
    }
}
