package main;

import java.util.Arrays;
import main.model.Transfer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration tests for application.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class ApplicationTests {

    public static final Logger logger = LoggerFactory.getLogger(ApplicationTests.class);

    @LocalServerPort
    int port;

    private static HttpHeaders headers = new HttpHeaders();

    /*
     * Initialize the headers of requests
     */
    static {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void doTransfer() throws Exception {
        doTransfer(new Transfer(20.5, 1, 2), port);
    }

    @Test
    public void getTransfer() throws Exception {
        long transferId = doTransfer(new Transfer(10.0, 2, 1), port).getId();

        // check success
        String url = "http://localhost:" + port + "/api/transfer/" + transferId;
        logger.info("Call getTransfer {}", url);
        ResponseEntity<Transfer> entity = new TestRestTemplate().exchange(
                url, HttpMethod.GET, new HttpEntity<Void>(headers), Transfer.class);
        logger.info("Results of getTransfer call: {}", entity.getBody());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals("Wrong result (id doesn't match):\n" + entity.getBody(), entity
                .getBody().getId(), transferId);

        // check not found
        url = "http://localhost:" + port + "/api/transfer/0";
        logger.info("Call getTransfer {}", url);
        entity = new TestRestTemplate().exchange(
                url, HttpMethod.GET, new HttpEntity<Void>(headers), Transfer.class);
        logger.info("Results of getTransfer call: {}", entity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
    }

    @Test
    public void getTransfersByAccount() throws Exception {
        Transfer[] transfers = {
            doTransfer(new Transfer(4.44, 3, 4), port),
            doTransfer(new Transfer(0.01, 4, 3), port),
            doTransfer(new Transfer(5.03, 1, 3), port)
        };

        String url = "http://localhost:" + this.port + "/api/transfer/account/3";
        logger.info("Call getTransfersByAccount {}", url);
        ResponseEntity<Transfer[]> entity = new TestRestTemplate().exchange(
                url, HttpMethod.GET, new HttpEntity<Void>(headers), Transfer[].class);
        logger.info("Results of getTransfersByAccount call: {}", Arrays.toString(entity.getBody()));
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertTrue("Wrong result (id doesn't exists):\n" + Arrays.toString(entity.getBody()),
                Arrays.asList(entity.getBody()).containsAll(Arrays.asList(transfers)));
    }

    /**
     * Do transfer call
     * @param transfer input transfer
     * @param port port of api server
     * @return processed transfer
     */
    private static Transfer doTransfer(Transfer transfer, int port) {
        String url = "http://localhost:" + port + "/api/transfer";
        logger.info("Call transfer {}: {}", url, transfer);
        ResponseEntity<Transfer> entity = new TestRestTemplate().postForEntity(
                url, new HttpEntity<Transfer>(transfer, headers), Transfer.class);
        logger.info("Results of transfer call: {}", entity.getBody());
        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
        assertTrue("Wrong transfer result (id is not set):\n" + entity.getBody(),
                entity.getBody().getId() > 0);
        return entity.getBody();
    }

}
