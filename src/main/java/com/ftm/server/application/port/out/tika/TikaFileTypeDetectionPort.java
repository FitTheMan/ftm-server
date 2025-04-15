package com.ftm.server.application.port.out.tika;

import com.ftm.server.common.annotation.Port;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

@Port
public interface TikaFileTypeDetectionPort {
    String detectFileType(MultipartFile file) throws IOException;
}
