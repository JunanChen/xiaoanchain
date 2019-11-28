package pojo.domain;

import utils.EncryptUtil;
import utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author junan
 * @version V1.0
 * @className Block
 * @disc
 * @date 2019/9/16 11:45
 */
public class Block implements Serializable {

    private static final long serialVersionUID = 398677310091891619L;
    //块索引
    private Long index;
    //当前区块hash值
    private String hash;
    //上一个区块hash值
    private String previousHash;

    public String merkleRoot;
    //交易列表
    private List<Transaction> transactions;
    //区块时间戳
    private Long timeStamp;
    //工作量证明 (一个随机数)
    private Integer nonce = 0;
    //挖矿难度
    private int difficulty;

    private Block(){}

    public Block(Long previousIndex, String previousHash, List<Transaction> transactions, int difficulty) {
        this.index = previousIndex + 1;
        this.previousHash = previousHash;
        if(transactions != null && transactions.size() > 0)
            this.transactions = transactions;
        else
            this.transactions = new ArrayList<>();
        this.timeStamp = System.currentTimeMillis();
        this.difficulty = difficulty;
    }

    /**
     * 提高计算难度（挖矿）
     */
    public void mineBlock() {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = new String(new char[difficulty]).replace('\0', '0');
        hash = calculationHash();
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce++;
            hash = calculationHash();
        }
    }

    /**
     * 计算当前hash
     * @return
     */
    public String calculationHash(){
        return EncryptUtil.getSHA256(previousHash + timeStamp + merkleRoot + nonce);
    }

    /**
     * 向区块添加一笔交易
     * @param transaction
     */
    public Boolean addTransaction(Transaction transaction) {
        //处理交易并检查是否有效，创世纪区块将被忽略
        if(transaction == null) return false;
        if((previousHash != "0")) {
            if((transaction.processTransaction() != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }


    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    @Override
    public String toString() {
        return "Block{" +
                "index=" + index +
                ", hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", transactions=" + transactions +
                ", timeStamp=" + timeStamp +
                ", nonce=" + nonce +
                '}';
    }
}
