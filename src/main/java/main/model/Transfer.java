package main.model;

import static main.helper.Amount.fromAmountFormat;
import static main.helper.Amount.toAmountFormat;

/**
 * Class {@code Transfer} is a class for transfers.
 */
public class Transfer extends Model {
    private long amount;
    private long fromAccountId;
    private long toAccountId;

    public Transfer() {
        super();
    }

    public Transfer(double amount, long fromAccountId, long toAccountId) {
        super();
        this.amount = toAmountFormat(amount);
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }

    public Transfer(long id, Transfer transfer) {
        super(id);
        this.amount = transfer.amount;
        this.fromAccountId = transfer.fromAccountId;
        this.toAccountId = transfer.toAccountId;
    }

    public void setAmount(double amount) {
        this.amount = toAmountFormat(amount);
    }

    public double getAmount() {
        return fromAmountFormat(this.amount);
    }

    public long getFromAccountId() {
        return fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    @Override
    public String toString() {
        return "Transfer [id=" + id + ", amount=" + getAmount() + ", from=" + fromAccountId + ", to=" + toAccountId + "]";
    }

}
