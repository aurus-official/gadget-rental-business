package com.gadget.rental.auth.jwt;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO : HANDLE THIS SHIT IN @SCHEDULED
@Component
public class JwtKeyManager {

    private final JwtKeyRepository jwtKeyRepository;
    private static final Map<String, String> allActiveKeysMap = new HashMap<>();
    private static final int JWT_KEY_INTERVAL = 3;
    private static final int JWT_KEY_OVERLAP_WINDOW = 5;

    @Autowired
    JwtKeyManager(JwtKeyRepository jwtKeyRepository) {
        this.jwtKeyRepository = jwtKeyRepository;
    }

    public void loadDefaultJwtKeys() {
        for (int i = 0; i < 3; ++i) {
            byte[] jwtKey = JwtKeyGenerator.generateJwtSecretKey();
            JwtKeyModel jwtKeyModel = new JwtKeyModel();
            ZonedDateTime createdAt = ZonedDateTime.now(ZoneId.of("Z")).plusMonths(JWT_KEY_INTERVAL * i);
            jwtKeyModel.setKeyId(JwtKeyGenerator.generateJwtKeyId(createdAt.getMonth().toString(),
                    String.valueOf(createdAt.getYear())));
            jwtKeyModel.setValidFrom(createdAt);
            jwtKeyModel.setValidUntil(
                    ZonedDateTime.now(ZoneId.of("Z")).plusHours(JWT_KEY_OVERLAP_WINDOW)
                            .plusMonths(JWT_KEY_INTERVAL * (i + 1)));
            jwtKeyModel.setSecretKey(JwtKeyGenerator.encodeJwtSecretKey(jwtKey));

            if (i == 0) {
                jwtKeyModel.setPrimary(true);
                jwtKeyModel.setActive(true);
            }

            jwtKeyRepository.save(jwtKeyModel);
        }
    }

    public void renewJwtKey() {
        JwtKeyModel primaryKey = jwtKeyRepository.findPrimaryJwtKey();

        byte[] jwtKey = JwtKeyGenerator.generateJwtSecretKey();

        JwtKeyModel jwtKeyModel = new JwtKeyModel();
        jwtKeyModel.setValidFrom(ZonedDateTime.now(ZoneId.of("Z")));
        jwtKeyModel.setValidUntil(ZonedDateTime.now(ZoneId.of("Z")).plusMonths(3l));
        jwtKeyModel.setActive(true);
        jwtKeyModel.setSecretKey(JwtKeyGenerator.encodeJwtSecretKey(jwtKey));
        jwtKeyRepository.save(jwtKeyModel);
    }

    public void setupActiveJwtKeysMap() {
        Iterable<JwtKeyModel> jwtKeysIterable = jwtKeyRepository.findAll();
        StreamSupport.stream(jwtKeysIterable.spliterator(), false)
                .filter(jwtKeys -> jwtKeys.isPrimary())
                .forEach(jwtKeys -> JwtKeyManager.allActiveKeysMap.put(jwtKeys.getKeyId(), jwtKeys.getSecretKey()));
    }
}
