package com.example.healthapp.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.example.healthapp.security.*;
import com.example.healthapp.web.filter.SpaWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final JHipsterProperties jHipsterProperties;

    public SecurityConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .cors(withDefaults())
            .csrf(csrf -> csrf.disable())
            .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
            .headers(headers ->
                headers
                    .contentSecurityPolicy(csp -> csp.policyDirectives(jHipsterProperties.getSecurity().getContentSecurityPolicy()))
                    .frameOptions(FrameOptionsConfig::sameOrigin)
                    .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                    .permissionsPolicyHeader(permissions ->
                        permissions.policy(
                            "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"
                        )
                    )
            )
            .authorizeHttpRequests(authz ->
                // prettier-ignore
                authz
                    .requestMatchers(mvc.pattern("/index.html"), mvc.pattern("/*.js"), mvc.pattern("/*.txt"), mvc.pattern("/*.json"), mvc.pattern("/*.map"), mvc.pattern("/*.css")).permitAll()
                    .requestMatchers(mvc.pattern("/*.ico"), mvc.pattern("/*.png"), mvc.pattern("/*.svg"), mvc.pattern("/*.webapp")).permitAll()
                    .requestMatchers(mvc.pattern("/app/**")).permitAll()
                    .requestMatchers(mvc.pattern("/i18n/**")).permitAll()
                    .requestMatchers(mvc.pattern("/content/**")).permitAll()
                    .requestMatchers(mvc.pattern("/swagger-ui/**")).permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/authenticate")).permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/authenticate")).permitAll()
                    .requestMatchers(mvc.pattern("/api/register")).permitAll()
                    .requestMatchers(mvc.pattern("/api/activate")).permitAll()
                    .requestMatchers(mvc.pattern("/api/account/reset-password/init")).permitAll()
                    .requestMatchers(mvc.pattern("/api/account/reset-password/finish")).permitAll()
                    .requestMatchers(mvc.pattern("/api/admin/**")).hasAuthority(AuthoritiesConstants.ADMIN)
                    // Alocare tratament (decizie clinică)
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/alocare-trataments/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.PACIENT)
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/alocare-trataments/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC)
                    .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/alocare-trataments/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC)
                    .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/alocare-trataments/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC)
                    .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/alocare-trataments/**")).hasAuthority(AuthoritiesConstants.ADMIN)
                    // Administrare
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/administrares/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/administrares/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/administrares/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/administrares/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/administrares/**")).hasAuthority(AuthoritiesConstants.ADMIN)
                    // Reactii adverse
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/reactie-adversas/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.FARMACIST, AuthoritiesConstants.PACIENT)
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/reactie-adversas/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.FARMACIST, AuthoritiesConstants.PACIENT)
                    .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/reactie-adversas/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/reactie-adversas/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/reactie-adversas/**")).hasAuthority(AuthoritiesConstants.ADMIN)
                    // Monitorizare
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/monitorizaris/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.PACIENT)
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/monitorizaris/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.PACIENT)
                    .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/monitorizaris/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.PACIENT)
                    .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/monitorizaris/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.PACIENT)
                    .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/monitorizaris/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC)
                    // Feedback
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/feedbacks/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.PACIENT)
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/feedbacks/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.PACIENT)
                    .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/feedbacks/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.PACIENT)
                    .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/feedbacks/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.PACIENT)
                    .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/feedbacks/**")).hasAuthority(AuthoritiesConstants.ADMIN)
                    // Decision Log (read-only pentru medic/admin; generat de sistem)
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/decision-logs/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC)
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/decision-logs/**")).denyAll()
                    .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/decision-logs/**")).denyAll()
                    .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/decision-logs/**")).denyAll()
                    .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/decision-logs/**")).denyAll()
                    // Medicament
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/medicaments/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/medicaments/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/medicaments/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/medicaments/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/medicaments/**")).hasAuthority(AuthoritiesConstants.ADMIN)
                    // External Drug Info
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/external-drug-infos/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/external-drug-infos/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/external-drug-infos/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/external-drug-infos/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/external-drug-infos/**")).hasAuthority(AuthoritiesConstants.ADMIN)
                    // Studii Literatura
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/studii-literaturas/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/studii-literaturas/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC)
                    .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/studii-literaturas/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC)
                    .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/studii-literaturas/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC)
                    .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/studii-literaturas/**")).hasAuthority(AuthoritiesConstants.ADMIN)
                    // Raport Analitic
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/raport-analitics/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC, AuthoritiesConstants.FARMACIST)
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/raport-analitics/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC)
                    .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/raport-analitics/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC)
                    .requestMatchers(mvc.pattern(HttpMethod.PATCH, "/api/raport-analitics/**")).hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MEDIC)
                    .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/raport-analitics/**")).hasAuthority(AuthoritiesConstants.ADMIN)
                    .requestMatchers(mvc.pattern("/api/**")).authenticated()
                    .requestMatchers(mvc.pattern("/v3/api-docs/**")).hasAuthority(AuthoritiesConstants.ADMIN)
                    .requestMatchers(mvc.pattern("/management/health")).permitAll()
                    .requestMatchers(mvc.pattern("/management/health/**")).permitAll()
                    .requestMatchers(mvc.pattern("/management/info")).permitAll()
                    .requestMatchers(mvc.pattern("/management/prometheus")).permitAll()
                    .requestMatchers(mvc.pattern("/management/**")).hasAuthority(AuthoritiesConstants.ADMIN)
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptions ->
                exceptions
                    .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
}
