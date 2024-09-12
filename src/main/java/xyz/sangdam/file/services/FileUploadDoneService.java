package xyz.sangdam.file.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.sangdam.global.Utils;
import xyz.sangdam.global.exceptions.BadRequestException;
import xyz.sangdam.global.rests.ApiRequest;

@Service
@RequiredArgsConstructor
public class FileUploadDoneService {
    private final ApiRequest apiRequest;
    private final Utils utils;

    public void process(String gid) {
        ApiRequest result = apiRequest.request("/done/" + gid, "file-service");
        if (!result.getStatus().is2xxSuccessful()) {
            throw new BadRequestException(utils.getMessage("Fail.file.done"));
        }
    }
}