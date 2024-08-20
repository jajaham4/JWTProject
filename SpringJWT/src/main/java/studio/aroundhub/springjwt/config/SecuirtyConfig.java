package studio.aroundhub.springjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import studio.aroundhub.springjwt.jwt.JWTFilter;
import studio.aroundhub.springjwt.jwt.JWTUtil;
import studio.aroundhub.springjwt.jwt.LoginFilter;

@Configuration
@EnableWebSecurity
public class SecuirtyConfig {
    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;

    private final JWTUtil jwtUtil;

    public SecuirtyConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
        this.authenticationConfiguration =authenticationConfiguration;
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }


    // 비밀번호를 암호화하는 메서드
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // csrf disable // JWT는 Stateless상태로 관리
        http
                .csrf((auth) -> auth.disable());
        // Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());
        // basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());
        // 경로별 인가 작업 (권한)
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "join").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated());
        //JWTFilter 등록
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        // 필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 세션 설정 (STATELESS 상태 설정)
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
