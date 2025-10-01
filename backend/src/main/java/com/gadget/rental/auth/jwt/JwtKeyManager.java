package com.gadget.rental.auth.jwt;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class JwtKeyManager {

    private final JwtKeyRepository jwtKeyRepository;
    private final Map<String, String> allActiveKeysMap = new HashMap<>();
    private final int JWT_KEY_INTERVAL_MONTH = 3;
    private final int JWT_KEY_OVERLAP_WINDOW_HOUR = 5;
    private JwtKeyModel primaryJwtKey;
    private JwtKeyModel expiredJwtKey;
    private int jwtKeyRotationMultiplier;
    private static Logger LOGGER = LoggerFactory.getLogger(JwtKeyManager.class);

    @Autowired
    public JwtKeyManager(JwtKeyRepository jwtKeyRepository) {
        this.jwtKeyRepository = jwtKeyRepository;
        this.jwtKeyRotationMultiplier = 0;
    }

    @Scheduled(initialDelay = 0)
    private void loadDefaultJwtKeys() {
        for (int i = 0; i < 3; ++i) {
            JwtKeyModel jwtKeyModel = generateNewJwtKey();
            addNextJwtKey(jwtKeyModel);

            if (i == 0) {
                jwtKeyModel.setActive(true);
                primaryJwtKey = jwtKeyModel;
                allActiveKeysMap.put(jwtKeyModel.getKeyId(), jwtKeyModel.getSecretKey());
            }
        }

        LOGGER.info("Done setting up default jwt keys.");
    }

    private void addNextJwtKey(JwtKeyModel jwtKeyModel) {
        jwtKeyRepository.save(jwtKeyModel);
    }

    private void changeCurrentJwtKey() {
        JwtKeyModel nextPrimaryJwtKey = jwtKeyRepository.findNextPrimaryJwtKey(primaryJwtKey.getValidUntil());
        expiredJwtKey = primaryJwtKey;
        primaryJwtKey = nextPrimaryJwtKey;
        primaryJwtKey.setActive(true);
        allActiveKeysMap.put(primaryJwtKey.getKeyId(), primaryJwtKey.getSecretKey());
    }

    private void deleteExpiredJwtKey() {
        jwtKeyRepository.delete(expiredJwtKey);
    }

    private JwtKeyModel generateNewJwtKey() {
        byte[] jwtKey = JwtKeyGenerator.generateJwtSecretKey();
        JwtKeyModel jwtKeyModel = new JwtKeyModel();
        ZonedDateTime createdAt = ZonedDateTime.now(ZoneId.of("Z"))
                .plusMonths(JWT_KEY_INTERVAL_MONTH * jwtKeyRotationMultiplier);
        jwtKeyModel.setKeyId(JwtKeyGenerator.generateJwtKeyId(createdAt.getMonth().toString(),
                String.valueOf(createdAt.getYear())));
        jwtKeyModel.setValidFrom(createdAt);
        jwtKeyModel.setValidUntil(
                ZonedDateTime.now(ZoneId.of("Z")).plusHours(JWT_KEY_OVERLAP_WINDOW_HOUR)
                        .plusMonths((JWT_KEY_INTERVAL_MONTH * (jwtKeyRotationMultiplier + 1))));
        jwtKeyModel.setSecretKey(JwtKeyBase64Util.encodeJwtSecretKey(jwtKey));
        jwtKeyRotationMultiplier++;

        return jwtKeyModel;

    }

    public JwtKeyModel getPrimaryJwtKey() {
        return primaryJwtKey;
    }

    public Map<String, String> getAllActiveKeysMap() {
        return allActiveKeysMap;
    }

    @Scheduled(cron = "0 0 1 1,4,7,10 * *")
    public void rotateJwtKeys() {
        this.addNextJwtKey(this.generateNewJwtKey());
        this.changeCurrentJwtKey();

        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            this.deleteExpiredJwtKey();
        }, 5, TimeUnit.HOURS);
    }
}
