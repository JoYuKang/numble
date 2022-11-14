package com.project.numble.application.user.service;

import static com.project.numble.application.user.service.util.UserOpenApiResponseKeyConst.GOOGLE_GEOLOCATION_COORDINATES_KEY;
import static com.project.numble.application.user.service.util.UserOpenApiResponseKeyConst.GOOGLE_GEOLOCATION_LATITUDE_KEY;
import static com.project.numble.application.user.service.util.UserOpenApiResponseKeyConst.GOOGLE_GEOLOCATION_LONGITUDE_KEY;
import static com.project.numble.application.user.service.util.UserOpenApiResponseKeyConst.KAKAO_FIRST_REGION_KEY;
import static com.project.numble.application.user.service.util.UserOpenApiResponseKeyConst.KAKAO_FULL_ADDRESS_KEY;
import static com.project.numble.application.user.service.util.UserOpenApiResponseKeyConst.KAKAO_LOCAL_ADDRESS_KEY;
import static com.project.numble.application.user.service.util.UserOpenApiResponseKeyConst.KAKAO_SECOND_REGION_KEY;
import static com.project.numble.application.user.service.util.UserOpenApiResponseKeyConst.KAKAO_THIRD_REGION_KEY;
import static com.project.numble.application.user.service.util.UserOpenApiUrlConst.GOOGLE_GEOLOCATION_COORDINATES_URL;
import static com.project.numble.application.user.service.util.UserOpenApiUrlConst.KAKAO_LOCAL_COORDINATES_ADDRESS_URL;
import static com.project.numble.application.user.service.util.UserOpenApiUrlConst.KAKAO_LOCAL_SEARCH_ADDRESS_URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.numble.application.auth.dto.request.SignUpRequest;
import com.project.numble.application.auth.repository.SignInLogRepository;
import com.project.numble.application.user.domain.Address;
import com.project.numble.application.user.domain.Animal;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.domain.enums.AnimalType;
import com.project.numble.application.user.dto.request.AddAddressRequest;
import com.project.numble.application.user.dto.request.AddAnimalsRequest;
import com.project.numble.application.user.dto.response.FindAddressByClientIpResponse;
import com.project.numble.application.user.dto.response.FindAddressByQueryResponse;
import com.project.numble.application.user.dto.response.GetAddressResponse;
import com.project.numble.application.user.dto.response.GetUserStaticInfoResponse;
import com.project.numble.application.user.repository.AddressRepository;
import com.project.numble.application.user.repository.AnimalRepository;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.application.user.repository.exception.UserNotFoundException;
import com.project.numble.application.user.service.exception.UserEmailAlreadyExistsException;
import com.project.numble.application.user.service.exception.UserNicknameAlreadyExistsException;
import com.project.numble.application.user.service.util.UrlConnectionIOException;
import com.project.numble.core.resolver.UserInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StandardUserService implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AnimalRepository animalRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignInLogRepository signInLogRepository;

    @Value("${google.map.key}")
    private String googleMapKey;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoLocalKey;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public void signUp(SignUpRequest request) {
        validateSignUp(request);

        userRepository.save(request.toUser(passwordEncoder));
    }

    @Override
    public FindAddressByClientIpResponse searchAddressByIp() {
        try {
            Map<String, String> coordinates = getCoordinates();
            Map<String, String> address =
                getAddress(
                    String.valueOf(coordinates.get(GOOGLE_GEOLOCATION_LONGITUDE_KEY)),
                    String.valueOf(coordinates.get(GOOGLE_GEOLOCATION_LATITUDE_KEY))
                );

            return new FindAddressByClientIpResponse(
                address.get(KAKAO_FULL_ADDRESS_KEY),
                address.get(KAKAO_FIRST_REGION_KEY),
                address.get(KAKAO_SECOND_REGION_KEY),
                address.get(KAKAO_THIRD_REGION_KEY)
            );
        } catch (IOException e) {
            throw new UrlConnectionIOException(e);
        }
    }

    @Override
    public FindAddressByQueryResponse searchAddressByQuery(String query) {
        try {
            Map<String, String> address = getAddressByQuery(query);
            String addressName = address.get("address_name");
            String[] addressArr = addressName.split(" ");

            return new FindAddressByQueryResponse(addressArr[0], addressArr[1]);
        }
        catch (NullPointerException | IndexOutOfBoundsException e) {
            return new FindAddressByQueryResponse();
        }
        catch (IOException e) {
            throw new UrlConnectionIOException(e);
        }
    }

    @Override
    public void addAddress(UserInfo userInfo, AddAddressRequest request) {
        User user = userRepository.findById(userInfo.getUserId())
            .orElseThrow(UserNotFoundException::new);

        Address address = AddAddressRequest.toAddress(request, user);

        addressRepository.save(address);
    }

    @Override
    public GetAddressResponse getAddress(UserInfo userInfo) {
        Address address = addressRepository.findByUserId(userInfo.getUserId()).orElseGet(() -> new Address());

        return GetAddressResponse.fromAddress(address);
    }

    @Override
    public void addAnimals(UserInfo userInfo, AddAnimalsRequest request) {
        User user = userRepository.findById(userInfo.getUserId())
            .orElseThrow(UserNotFoundException::new);

        request.getAnimalTypes().stream().map(AnimalType::getAnimalType).forEach(animalType -> {
            Animal animal = new Animal(animalType);

            user.addAnimal(animal);
            animalRepository.save(animal);
        });
    }

    @Override
    public GetUserStaticInfoResponse getUserStaticInfo(UserInfo userInfo) {
        User user = userRepository.findStaticUserInfoById(userInfo.getUserId())
            .orElseThrow(UserNotFoundException::new);

        return GetUserStaticInfoResponse.fromUser(user, signInLogRepository.countAllByUserId(user.getId()));
    }

    @Override
    public void delAddress(UserInfo userInfo) {
        addressRepository.deleteByUserId(userInfo.getUserId());
    }

    @Override
    @Transactional
    public void withdrawalUser(UserInfo userInfo) {
        User user = userRepository.findById(userInfo.getUserId())
            .orElseThrow(UserNotFoundException::new);

        user.withdrawal();
    }

    private Map<String, String> getAddressByQuery(String query) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(KAKAO_LOCAL_SEARCH_ADDRESS_URL);
        urlBuilder.append(URLEncoder.encode(query, StandardCharsets.UTF_8));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "KakaoAK " + kakaoLocalKey);

        StringBuilder sb = readResponse(conn);

        Map<String, List<Map<String, String>>> map = objectMapper.readValue(sb.toString(), HashMap.class);
        return map.get(KAKAO_LOCAL_ADDRESS_KEY).get(0);
    }

    private Map<String, String> getCoordinates() throws IOException {
        StringBuilder urlBuilder = new StringBuilder(GOOGLE_GEOLOCATION_COORDINATES_URL);
        urlBuilder.append(googleMapKey);

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Content-Length", "0");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = "".getBytes(StandardCharsets.UTF_8);

            os.write(input, 0, input.length);
        }

        StringBuilder sb = readResponse(conn);

        Map<String, Map<String, String>> map = objectMapper.readValue(sb.toString(), HashMap.class);
        return map.get(GOOGLE_GEOLOCATION_COORDINATES_KEY);
    }

    private Map<String, String> getAddress(String x, String y) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(KAKAO_LOCAL_COORDINATES_ADDRESS_URL);
        urlBuilder.append(x);
        urlBuilder.append("&y=");
        urlBuilder.append(y);

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "KakaoAK " + kakaoLocalKey);

        StringBuilder sb = readResponse(conn);

        Map<String, List<Map<String, String>>> map = objectMapper.readValue(sb.toString(), HashMap.class);
        return map.get(KAKAO_LOCAL_ADDRESS_KEY).get(0);
    }

    private StringBuilder readResponse(HttpURLConnection conn) throws IOException {
        BufferedReader br;

        if (conn.getResponseCode() >= HttpStatus.OK.value()
            && conn.getResponseCode() < HttpStatus.MULTIPLE_CHOICES.value()) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        conn.disconnect();

        return sb;
    }

    private void validateSignUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserEmailAlreadyExistsException();
        }

        if (userRepository.existsByNickname(signUpRequest.getNickname())) {
            throw new UserNicknameAlreadyExistsException();
        }
    }
}
