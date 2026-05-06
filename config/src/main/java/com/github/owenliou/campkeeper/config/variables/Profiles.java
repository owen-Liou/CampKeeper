package com.github.owenliou.campkeeper.config.variables;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Slf4j
public class Profiles {

    public static final String DEV = "dev";
    public static final String DEV_LAB = "dev-lab";
    public static final String LAB = "lab";
    public static final String PROD = "prod";
    public static final String SANDBOX = "sandbox";
    public static final String STAGE = "stage";
    public static final String STAGE_GCP = "stage-gcp";

    //    public static final String DEVS = "dev | dev-lab";
//    public static final String NOT_DEV = "!dev & !dev-lab";
    public static final String NOT_PRODS = "!prod";
    public static final String PRODS = "prod";

    public static final String TEST = "test";
    public static final String NOT_TEST = "!test";

    private static ConfigurableEnvironment env;

    public Profiles(ConfigurableEnvironment env) {
        this.env = env;
    }

    public static boolean isCurrentProfileMatch(String... profiles) {
/*
        return Stream.of(env.getActiveProfiles())
                .anyMatch(profile -> Stream.of(profiles).anyMatch(profile::equalsIgnoreCase));
*/
        return env.matchesProfiles(profiles);
    }

    public String activeProfiles() {
        String[] activeProfiles = env.getActiveProfiles();
        Assert.notEmpty(activeProfiles, "activeProfiles is empty");
        return String.join(", ", activeProfiles);
    }

}
