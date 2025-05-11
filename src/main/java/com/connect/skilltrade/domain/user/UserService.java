package com.connect.skilltrade.domain.user;

import com.connect.skilltrade.domain.user.dto.UserSignUpRequest;
import com.connect.skilltrade.domain.user.provider.OidcProviderFactory;
import com.connect.skilltrade.security.infrastructure.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final OidcProviderFactory oidcProviderFactory;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public TokenResponse signUp(UserSignUpRequest request) {
        String providerId = oidcProviderFactory.getProviderId(request.provider(), request.idToken());

        userRepository.findByProviderId(providerId)
                .ifPresent(it->{
                    // TODO
                    throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
                });
        User user = createNewUser(request, providerId);


//        return TokenResponse.from(jwtProvider.generateToken(user.getId()));
        return null;
    }

    private User createNewUser(UserSignUpRequest dto,String providerId) {
        User user = User.builder()
                .provider(dto.provider())
                .providerId(providerId)
                .build();

        return userRepository.save(user);
    }
}
