package main.service;

import main.model.Transfer;
import java.util.List;

/**
 * Interface of transfer service
 */
public interface TransferService {

	Transfer findById(long id);

	List<Transfer> findByAccountId(long accountId);

	Transfer transfer(Transfer transfer);

}
