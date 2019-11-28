package pojo.domain;

import utils.EncryptUtil;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author junan
 * @version V1.0
 * @className Transaction
 * @disc 交易类封装
 * @date 2019/9/18 11:27
 */
public class Transaction {

    //交易hash
    private String transactionHash;
    //发送者
    private PublicKey sender;
    //接受者
    private PublicKey recipient;
    //交易金额
    private BigDecimal amount;
    //多少个交易已经被创建
    private static int sequence = 0;
    //输入列表
    private List<TransactionInput> inputs = new ArrayList<>();
    //输出列表
    private List<TransactionOutput> outputs = new ArrayList<>();
    //数字签名，防止其他人从我们的钱包中发送资金
    private byte[] signature;
    //未使用的输出集合
    public static Map<String, TransactionOutput> UTXOs = new HashMap<>();

    private Transaction() {
    }

    public Transaction(PublicKey from, PublicKey to, BigDecimal amount, List<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.amount = amount;
        this.inputs = inputs;
        transactionHash = calculateHash();
    }

    //计算交易hash
    private String calculateHash() {
        sequence++; //增加sequence，用来防治两个不同的交易有相同的hash值
        return EncryptUtil.getSHA256(
                EncryptUtil.getStringFromKey(sender) +
                        recipient +
                        amount + sequence
        );
    }

    //签名所有我们不想被篡改的数据
    public void generateSignature(PrivateKey privateKey) {
        String data = EncryptUtil.getStringFromKey(sender) + EncryptUtil.getStringFromKey(recipient) + amount;
        signature = EncryptUtil.applyECDSASig(privateKey, data);
    }

    //验证我们已签名的数据没有被窜给过
    public boolean verifySignature() {
        String data = EncryptUtil.getStringFromKey(sender) + EncryptUtil.getStringFromKey(recipient) + amount;
        return EncryptUtil.verifyECDSASig(sender, data, signature);
    }

    // 处理交易
    public boolean processTransaction() {

        //验证签名
        if(verifySignature() == false) {
            System.out.println("交易签名验证失败！");
            return false;
        }

        //收集交易的输入（必须注意的是输入是未被使用的）
        for(TransactionInput i : inputs) {
            i.UTXO = Transaction.UTXOs.get(i.transactionOutputId);
        }

        //创建交易输出
        BigDecimal leftOver = getInputsValue().subtract(amount);                      // 获得输入的剩余金额
        transactionHash = calculateHash();                                            // 计算 hash
        outputs.add(new TransactionOutput( this.recipient, amount, transactionHash));  // 发送金额给收款人
        outputs.add(new TransactionOutput( this.sender, leftOver, transactionHash));   // 把剩余金额返回给付款人

        //把输出增加到未使用的列表中
        for(TransactionOutput o : outputs) {
            Transaction.UTXOs.put(o.transactionOutputId , o);
        }

        //把已经使用的交易的输入从UTXO中移除
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue;                    //如果交易不存在忽略它
            Transaction.UTXOs.remove(i.UTXO.transactionOutputId);
        }
        return true;
    }

    //返回余额
    public BigDecimal getInputsValue() {
        BigDecimal total = new BigDecimal(0);
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue;                    //如果交易不存在忽略它
            total = total.add(i.UTXO.amount);
        }
        return total;
    }

    //返回输出的总和
    public BigDecimal getOutputsValue() {
        BigDecimal total = new BigDecimal(0);
        for(TransactionOutput o : outputs) {
            total = total.add(o.amount);
        }
        return total;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public PublicKey getSender() {
        return sender;
    }

    public void setSender(PublicKey sender) {
        this.sender = sender;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public void setRecipient(PublicKey recipient) {
        this.recipient = recipient;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public static int getSequence() {
        return sequence;
    }

    public static void setSequence(int sequence) {
        Transaction.sequence = sequence;
    }

    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<TransactionInput> inputs) {
        this.inputs = inputs;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TransactionOutput> outputs) {
        this.outputs = outputs;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionHash='" + transactionHash + '\'' +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", amount=" + amount +
                ", inputs=" + inputs +
                ", outputs=" + outputs +
                '}';
    }
}