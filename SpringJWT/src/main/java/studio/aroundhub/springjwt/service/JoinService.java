package studio.aroundhub.springjwt.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import studio.aroundhub.springjwt.dto.JoinDTO;
import studio.aroundhub.springjwt.entity.UserEntity;
import studio.aroundhub.springjwt.respository.UserRepository;

@Service
// UserRepository에서 주입을 받음
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 주입받은 BCryptPassword를 초기화
    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void JoinProcess(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        boolean isExist = userRepository.existsByUsername(username);

        if(isExist){

            return;
        }

        UserEntity data = new UserEntity();
        data.setUsername(username);
        //bCryptPasswordEncoder -> 주입받음
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setRole("ROLE_ADMIN");
        // DB에 저장
        userRepository.save(data);
    }
}
