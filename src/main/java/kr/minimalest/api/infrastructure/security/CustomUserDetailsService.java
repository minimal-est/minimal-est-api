package kr.minimalest.api.infrastructure.security;

import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(Email.of(email))
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("해당 email의 User를 찾을 수 없습니다: " + email));
    }
}
