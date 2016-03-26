package io.cloudbot.aws.keypair;

import com.amazonaws.services.ec2.model.KeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/keyPair", produces = TEXT_PLAIN_VALUE)
public class KeyPairRetrievalController {

    private final KeyPairRetrievalService keyPairRetrievalService;

    @Autowired
    public KeyPairRetrievalController(KeyPairRetrievalService keyPairRetrievalService) {
        this.keyPairRetrievalService = keyPairRetrievalService;
    }

    @ResponseBody
    @RequestMapping(method = GET, value = "/{keyName}")
    public ResponseEntity<String> retrieveKey(@PathVariable String keyName) {
        return response(keyPairRetrievalService.retrieveKey(keyName));
    }

    private ResponseEntity<String> response(KeyPair keyPair) {
        return Optional.ofNullable(keyPair)
                .map(key -> ResponseEntity.status(OK).header("Content-Disposition", downloadHeader(keyPair.getKeyName())).body(key.getKeyMaterial()))
                .orElse(ResponseEntity.status(NOT_FOUND).body("The key doesn't exist. It might have expired."));
    }

    private String downloadHeader(String keyName) {
        return String.format("attachment; filename=\"%s.pem\"", keyName);
    }
}
