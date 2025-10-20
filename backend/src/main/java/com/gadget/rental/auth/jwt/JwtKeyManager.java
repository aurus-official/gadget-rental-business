package com.gadget.rental.auth.jwt;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.gadget.rental.rental.RentalGadgetModel;
import com.gadget.rental.rental.RentalGadgetRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class JwtKeyManager {

    private static Logger LOGGER = LoggerFactory.getLogger(JwtKeyManager.class);
    private final Map<String, String> allActiveKeysMap = new HashMap<>();
    private final JwtKeyRepository jwtKeyRepository;
    private final RentalGadgetRepository rentalGadgetRepository;
    private final int JWT_KEY_INTERVAL_MONTH = 3;
    private final int JWT_KEY_OVERLAP_WINDOW_HOUR = 5;
    private JwtKeyModel primaryJwtKey;
    private JwtKeyModel expiredJwtKey;
    private int jwtKeyRotationMultiplier;

    @Autowired
    public JwtKeyManager(JwtKeyRepository jwtKeyRepository, RentalGadgetRepository rentalGadgetRepository) {
        this.jwtKeyRepository = jwtKeyRepository;
        this.rentalGadgetRepository = rentalGadgetRepository;
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
            RentalGadgetModel temp = new RentalGadgetModel();
            temp.setName(String.format("NAME #%d.", i));

            rentalGadgetRepository.save(temp);
        }

        LOGGER.info("Done setting up default jwt keys.");
    }

    private void addNextJwtKey(JwtKeyModel jwtKeyModel) {
        jwtKeyRepository.save(jwtKeyModel);
    }

    private void changeCurrentJwtKey() {
        JwtKeyModel nextPrimaryJwtKey = jwtKeyRepository.findNextPrimaryJwtKey(primaryJwtKey.getValidUntil());
        primaryJwtKey = nextPrimaryJwtKey;
        primaryJwtKey.setActive(true);
        allActiveKeysMap.put(primaryJwtKey.getKeyId(), primaryJwtKey.getSecretKey());
    }

    private void deleteExpiredJwtKey() {
        expiredJwtKey = primaryJwtKey;
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
    // @Scheduled(fixedDelay = 60_000, initialDelay = 60_000)
    public void rotateJwtKeys() {
        LOGGER.info("START : " + primaryJwtKey.getSecretKey());
        allActiveKeysMap
                .forEach((key, value) -> System.out.println(String.format("KEY : %s,\nVALUE : %s", key, value)));

        this.deleteExpiredJwtKey();
        this.addNextJwtKey(this.generateNewJwtKey());
        this.changeCurrentJwtKey();
        LOGGER.info("-----------------");
        allActiveKeysMap
                .forEach((key, value) -> System.out.println(String.format("KEY : %s,\nVALUE : %s", key, value)));
        LOGGER.info("END : " + primaryJwtKey.getSecretKey());

        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            allActiveKeysMap.remove(expiredJwtKey.getKeyId());
        }, JWT_KEY_OVERLAP_WINDOW_HOUR, TimeUnit.HOURS);
    }
}
