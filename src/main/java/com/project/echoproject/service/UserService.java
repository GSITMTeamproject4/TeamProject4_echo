import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class UserService {


    private final UserRepository userRepository;


    public void registerUser(UserDto userdto) {
        // 사용자 정보 저장
        SiteUser user = new SiteUser();
        user.setUserId(userdto.getUserId());
        user.setUserName(userdto.getUsername());
        user.setEmail(userdto.getEmail());
        user.setPhoneNum(userdto.getPhoneNum());
        user.setGender(userdto.getGender());

        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(userdto.getPassword()));

        // 파일 업로드 처리는 여기서 추가적으로 구현할 수 있음

        userRepository.save(user);
    }
}