package pojo.domain;

import utils.EncryptUtil;

import java.math.BigDecimal;
import java.security.PublicKey;

/**
 * @author junan
 * @version V1.0
 * @className TransactionOutput
 * @disc
 * @date 2019/9/19 10:03
 */
public class TransactionOutput {

    public String transactionOutputId;
    //持有者的公钥
    public PublicKey recipientPublicKey;
    //持有者的金额
    public BigDecimal amount;
    //交易编号
    public String parentTransactionId;

    //构造器
    public TransactionOutput(PublicKey recipientPublicKey, BigDecimal amount, String parentTransactionId) {
        this.recipientPublicKey = recipientPublicKey;
        this.amount = amount;
        this.parentTransactionId = parentTransactionId;
        this.transactionOutputId = EncryptUtil.getSHA256(EncryptUtil.getStringFromKey(recipientPublicKey) + amount + parentTransactionId);
    }

    //用来验证是否属于你
    public boolean isMine(PublicKey publicKey) {
        return (publicKey.equals(recipientPublicKey));
    }
}