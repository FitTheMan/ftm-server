package com.ftm.server.adapter.out.tika;

import com.ftm.server.application.port.out.tika.TikaFileTypeDetectionPort;
import com.ftm.server.common.annotation.Adapter;
import java.io.IOException;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

@Adapter
public class TikaFileDetectionAdapter implements TikaFileTypeDetectionPort {
    @Override
    public String detectFileType(MultipartFile file) throws IOException { // 이미지 type 추출
        Tika tika = new Tika();
        return tika.detect(file.getInputStream());
    }
}
