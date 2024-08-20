package studio.aroundhub.springjwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDTO {
    // 회원가입 데이터를 받음 -> controller로 보냄
    private String username;
    private String password;

}
