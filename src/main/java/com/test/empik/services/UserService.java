package com.test.empik.services;

import com.test.empik.exceptions.ExternalServiceErrorException;
import com.test.empik.exceptions.NotFoundException;
import com.test.empik.models.User;
import com.test.empik.payload.externalApi.UserDetailsExternalApiResponse;
import com.test.empik.payload.response.UserDetailsResponse;
import com.test.empik.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class UserService {
    private final WebClient webClient;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserService(WebClient webClient, ModelMapper modelMapper, UserRepository userRepository) {
        this.webClient = webClient;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.addMappings(new PropertyMap<UserDetailsExternalApiResponse, UserDetailsResponse>() {
            @Override
            protected void configure() {
                skip(destination.getCalculations());
                map().setId(String.valueOf(source.getId()));
            }
        });
    }

    @Transactional
    public UserDetailsResponse getUserDetails(String login) {
        UserDetailsExternalApiResponse userDetailsExternalApiResponse = webClient
                .get()
                .uri("/{login}", login)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.error(new NotFoundException("exception.userNotFound")))
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new ExternalServiceErrorException("exception.generalException")))
                .bodyToMono(UserDetailsExternalApiResponse.class)
                .block();
        updateRequestCount(login);
        return this.mapToUserDetailsResponse(userDetailsExternalApiResponse);
    }

    private UserDetailsResponse mapToUserDetailsResponse(UserDetailsExternalApiResponse externalApiResponse) {
        UserDetailsResponse userDetailsResponse = this.modelMapper.map(externalApiResponse, UserDetailsResponse.class);
        userDetailsResponse.setCalculations(String.valueOf(this.getCalculationsValue(externalApiResponse)));
        return userDetailsResponse;
    }

    private Double getCalculationsValue(UserDetailsExternalApiResponse externalApiResponse) {
        try{
            return (6.0 / (externalApiResponse.getFollowers() * (2.0 + externalApiResponse.getPublicRepos())));
        } catch (ArithmeticException e){
            return 0.0;
        }
    }

    private void updateRequestCount(String login) {
        Optional<User> optionalUser = userRepository.findByLogin(login);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            user.setRequestCount(user.getRequestCount() + 1);
        } else {
            user = new User();
            user.setLogin(login);
            user.setRequestCount(1);
        }
        userRepository.save(user);
    }
}
