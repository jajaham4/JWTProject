package studio.aroundhub.springjwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import studio.aroundhub.springjwt.dto.JoinDTO;
import studio.aroundhub.springjwt.service.JoinService;

@Controller
@ResponseBody // 응답본문
public class JoinController {
    // 값을 불변의 값으로 만들기 위한 코딩
    private final JoinService joinService;

    public JoinController(JoinService joinService){

        this.joinService = joinService;
    }

    @PostMapping("/join")
    // JoinDTO는 인자값 // 서비스로 값을 보냄
    public String JoinProcess(JoinDTO joinDTO) {

        joinService.JoinProcess(joinDTO);

        return "ok";
    }
}
