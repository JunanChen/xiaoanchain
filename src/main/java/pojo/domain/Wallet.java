package pojo.domain;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import utils.EncryptUtil;

import java.math.BigDecimal;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author junan
 * @version V1.0
 * @className Wallet
 * @disc
 * @date 2019/9/19 9:08
 */
public class Wallet {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    //公钥
    private PublicKey publicKey;

    //私钥
    private PrivateKey privateKey;

    //钱包自身的UTXO(钱包自身的未使用的输出)
    public Map<String,TransactionOutput> UTXOs = new HashMap<>();

    public Wallet(){
        createKeyPair();
    }

    //创建公钥私钥
    private void createKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //返回余额并保存钱包自身的UTXO
    public BigDecimal getBalance() {
        BigDecimal total = new BigDecimal(0);
        // 遍历全局的 UTXOs 找出属于我的输出，并计算余额
        for (Map.Entry<String, TransactionOutput> item : Transaction.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(publicKey)) {                                                // 查看输出是否属于我即货币是不是我的
                UTXOs.put(UTXO.transactionOutputId, UTXO);                               // 添加到未使用的交易列表中
                total = total.add(UTXO.amount);
            }
        }
        return total;
    }

    //创建并返回属于这个钱包的一个新交易
    public Transaction createTransaction(PublicKey _recipient, BigDecimal value) {
        if (getBalance().compareTo(value) == -1) {                                       //搜集余额并检查金额
            System.out.println("余额不足，创建交易失败！");
            return null;
        }

        //创建输入列表
        List<TransactionInput> inputs = new ArrayList<>();
        BigDecimal total = new BigDecimal(0);
        for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            total = total.add(UTXO.amount);
            inputs.add(new TransactionInput(UTXO.transactionOutputId));                  //把之前别人发给我的交易的输出作为本次交易的输入
            if(total.compareTo(value) == 1) break;
        }
        Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
        //添加签名
        newTransaction.generateSignature(privateKey);
        //移除了被使用的输出
        for(TransactionInput input: inputs){
            UTXOs.remove(input.transactionOutputId);
        }
        return newTransaction;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String toString() {
        return EncryptUtil.getStringFromKey(publicKey) + "  :  " + EncryptUtil.getStringFromKey(privateKey);
    }
}