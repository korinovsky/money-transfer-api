package main.controller;

import java.util.List;

import main.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import main.service.TransferService;
import main.util.ErrorMessage;

/**
 * Controller for working with transfers
 */
@RestController
@RequestMapping("/api")
public class TransferController {

	public static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    /**
     * Service which will do all data retrieval/manipulation work
     */
	@Autowired
    private TransferService transferService;

    /**
     * Retrieve transfer info by transfer id
     * @param id transfer id
     * @return response with transfer or error
     */
    @RequestMapping(value = "/transfer/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getTransfer(@PathVariable("id") long id) {
        logger.info("Fetching Transfer with id {}", id);
        Transfer transfer = transferService.findById(id);
        if (transfer == null) {
            logger.error("Transfer with id {} not found.", id);
            return new ResponseEntity<ErrorMessage>(new ErrorMessage("Transfer with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Transfer>(transfer, HttpStatus.OK);
    }

    /**
     * Retrieve list of transfers by account id
     * @param id account id
     * @return response with list of transfers info or error
     */
    @RequestMapping(value = "/transfer/account/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getTransfersByAccount(@PathVariable("id") long id) {
        logger.info("Fetching Transfer with Account id {}", id);
        List<Transfer> transfers = transferService.findByAccountId(id);
        if (transfers.isEmpty()) {
            logger.error("Transfers with Account id {} not found.", id);
            return new ResponseEntity<ErrorMessage>(new ErrorMessage("Transfer with Account id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Transfer>>(transfers, HttpStatus.OK);
    }

    /**
     * Process a transfer of funds
     * @param transfer transfer info
     * @return response with transfer or error
     */
	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	public ResponseEntity<?> createAccount(@RequestBody Transfer transfer) {
		logger.info("Creating Transfer : {}", transfer);
		try {
            transfer = transferService.transfer(transfer);
        }
        catch (Exception e) {
            logger.error("Unable to transfer. {}", e.getMessage());
            return new ResponseEntity<ErrorMessage>(
                    new ErrorMessage("Unable to transfer. " +
                            e.getMessage()),HttpStatus.CONFLICT
            );
        }
        return new ResponseEntity<Transfer>(transfer, HttpStatus.CREATED);
	}

}