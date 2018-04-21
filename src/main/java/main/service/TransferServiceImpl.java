package main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import main.model.Account;
import main.model.Transfer;
import org.springframework.stereotype.Service;

import static main.helper.Amount.toAmountFormat;

/**
 * Service for working with transfers
 */
@Service("transferService")
public class TransferServiceImpl implements TransferService {
	
	private static final AtomicLong counter = new AtomicLong();

	private static List<Account> accounts;
	private static List<Transfer> transfers;

    /*
     * Initialize the start state of the service
     */
	static {
		accounts = new ArrayList<Account>();
        transfers = new ArrayList<Transfer>();

        // Dummy accounts
        int id = 1;
        for (long amount : new long[]{10000, 20000, 3000, 45000}) {
            accounts.add(new Account(id++, amount));
        }

	}

	/**
	 * Finds account by id
	 * @param id account id
	 * @return account info
	 */
	private Account findAccountById(long id) {
		for(Account account : accounts){
			if(account.getId() == id){
				return account;
			}
		}
		return null;
	}

	/**
	 * Finds transfer by id
	 * @param id transfer id
	 * @return transfer info
	 */
 	public Transfer findById(long id) {
		for(Transfer transfer : transfers){
			if(transfer.getId() == id){
				return transfer;
			}
		}
		return null;
	}

	/**
	 * Finds transfer by account id
	 * @param accountId account id
	 * @return list of transfers
	 */
	public List<Transfer> findByAccountId(long accountId) {
		List<Transfer> result = new ArrayList<Transfer>();
		for(Transfer transfer : transfers){
			if(transfer.getFromAccountId() == accountId || transfer.getToAccountId() == accountId){
				result.add(transfer);
			}
		}
		return result;
	}

	/**
	 * Process transfer
	 * @param transfer transfer info
	 * @return processed transfer
	 */
	public Transfer transfer(Transfer transfer) {
		Account first, second,
				from = findAccountById(transfer.getFromAccountId()),
				to = findAccountById(transfer.getToAccountId());
		if (from == null) {
			throw new IllegalArgumentException("An Account with id " + transfer.getFromAccountId() + " not found");
		}
		if (to == null) {
			throw new IllegalArgumentException("An Account with id " + transfer.getToAccountId() + " not found");
		}
		if (from.equals(to)) {
			throw new IllegalArgumentException("The same accounts for transfer");
		}

		// predefined order to avoid deadlock
		if (from.getId() < to.getId()) {
			first = from;
			second = to;
		}
		else {
			first = to;
			second = from;
		}
		synchronized (first) {
			synchronized (second) {
				from.withdraw(toAmountFormat(transfer.getAmount()));
				to.deposit(toAmountFormat(transfer.getAmount()));
                transfers.add(transfer = new Transfer(counter.incrementAndGet(), transfer));
			}
		}
		return transfer;
	}

}
