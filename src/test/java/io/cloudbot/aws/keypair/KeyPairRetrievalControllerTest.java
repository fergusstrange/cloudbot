package io.cloudbot.aws.keypair;

import com.amazonaws.services.ec2.model.KeyPair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class KeyPairRetrievalControllerTest {

    @Mock
    private KeyPairRetrievalService keyPairRetrievalService;

    @InjectMocks
    private KeyPairRetrievalController keyPairRetrievalController;

    @Test
    public void shouldReturnErrorMessageWhenNoKey() throws Exception {
        standaloneSetup(keyPairRetrievalController).build()
                .perform(MockMvcRequestBuilders.get("/keyPair/123456-abcdef"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(TEXT_PLAIN))
                .andExpect(content().string("The key doesn't exist. It might have expired."));
    }

    @Test
    public void shouldReturnKeyAndCorrectHeadersWhenKeyExists() throws Exception {
        given(keyPairRetrievalService.retrieveKey("123456-abcdef")).willReturn(new KeyPair()
                .withKeyName("123456-abcdef")
                .withKeyMaterial("aPrivateKey"));

        standaloneSetup(keyPairRetrievalController).build()
                .perform(MockMvcRequestBuilders.get("/keyPair/123456-abcdef"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_PLAIN))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"123456-abcdef.pem\""))
                .andExpect(content().string("aPrivateKey"));
    }
}