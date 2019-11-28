package pojo.domain;

/**
 * @author junan
 * @version V1.0
 * @className TransactionInput
 * @disc
 * @date 2019/9/19 10:03
 */
public class TransactionInput {

    //指向交易输出类 -> transactionId
    public String transactionOutputId;

    //包含所有未使用的交易输出
    public TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}