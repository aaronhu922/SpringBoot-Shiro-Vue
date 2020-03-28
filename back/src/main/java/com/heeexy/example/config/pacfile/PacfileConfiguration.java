package com.heeexy.example.config.pacfile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PacfileConfiguration {

    public String getPacfilePath() {
        return pacfilePath;
    }

    @Value("${pacfile.folder.path}")
    private String pacfilePath;

    public String getPacfileURL() {
        return pacfileURL;
    }

    @Value("${pacfile.server.url}")
    private String pacfileURL;
}
