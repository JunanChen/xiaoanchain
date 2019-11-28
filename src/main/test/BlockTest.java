import core.BlockChain;
import org.junit.Test;
import pojo.domain.Block;
import pojo.domain.Transaction;
import pojo.domain.TransactionOutput;
import pojo.domain.Wallet;
import utils.ChainCheckUtil;

import java.math.BigDecimal;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author junan
 * @version V1.0
 * @className BlockTest
 * @disc
 * @date 2019/9/18 16:31
 */
public class BlockTest {

    //未使用的输出集合
//    public static Map<String, TransactionOutput> UTXOs = new HashMap<>();

    @Test
    public void test() {
        BlockChain blockChain = new BlockChain();
        List<Transaction> transactions = new ArrayList<>();
//        transactions.add(new Transaction("1", "2", new BigDecimal(1)));
        long start = System.currentTimeMillis();
        blockChain.addBlock(new Block(blockChain.lastBlock().getIndex(), blockChain.lastBlock().getHash(), transactions, 5));
        long end = System.currentTimeMillis();
        System.out.println(blockChain.isChainValid());
        System.out.println(end - start);
    }

    @Test
    public void walletTest() {
        Wallet wallet = new Wallet();
        Wallet wallet1 = new Wallet();
        System.out.println(wallet);
        Transaction transaction = new Transaction(wallet.getPublicKey(), wallet1.getPublicKey(), new BigDecimal(5), null);
        transaction.generateSignature(wallet.getPrivateKey());
    }

    @Test
    public void transactionTest(){
        // 创建区块链对象
        BlockChain blockChain = new BlockChain();
        // 创建初始钱包
        Wallet coinbase = new Wallet();
        // 创建第一个钱包
        Wallet wallet1 = new Wallet();
        // 创建第二个钱包
        Wallet wallet2 = new Wallet();

        // 手动创建第一笔交易（初始钱包给第一个钱包转50个币）交易的输入为一个空的 list，以后的交易只能从钱包发起（你可以认为这第一笔钱来路不明，凭空产生，哈哈）
        Transaction transaction = new Transaction(coinbase.getPublicKey(), wallet1.getPublicKey(), new BigDecimal(50), new ArrayList<>());
        // 给这笔交易签名
        transaction.generateSignature(coinbase.getPrivateKey());
        // 手动给交易设置输出
        transaction.getOutputs().add(new TransactionOutput(transaction.getRecipient(), transaction.getAmount(), transaction.getTransactionHash())); //手工添加交易输出
        // 将交易的输出写到全局的 UTXOs 中
        Transaction.UTXOs.put(transaction.getOutputs().get(0).transactionOutputId, transaction.getOutputs().get(0));
        // 创建第一个区块
        Block block1 = new Block(0L, "0", null, 1);
        // 给这个区块添加一笔交易
        block1.addTransaction(transaction);
        // 把这个创世区块加入区块链中
        blockChain.addBlock(block1);
        //输出初始钱包的余额（知道为什么结果是0吗？）
        System.out.println("初始钱包的余额为：" + coinbase.getBalance());

        // 创建第二个区块
        Block block2 = new Block(blockChain.lastBlock().getIndex(), blockChain.lastBlock().getHash(), null, 1);
        System.out.println("\n钱包1的余额为: " + wallet1.getBalance());
        System.out.println("\n钱包1给钱包2转 40.9 个币...");
        block2.addTransaction(wallet1.createTransaction(wallet2.getPublicKey(), new BigDecimal("40.9")));
        blockChain.addBlock(block2);

        // 统计钱包余额
        System.out.println("\n钱包1的余额为: " + wallet1.getBalance());
        System.out.println("钱包2的余额为: " + wallet2.getBalance());
        // 创建第三个区块
        Block block3 = new Block(blockChain.lastBlock().getIndex(), blockChain.lastBlock().getHash(), null, 1);
        System.out.println("\n钱包1给钱包2转 1000 个币...");
        // 钱包创建交易的时候会判断币够不够
        block3.addTransaction(wallet1.createTransaction(wallet2.getPublicKey(), new BigDecimal(1000)));
        blockChain.addBlock(block3);

        // 统计钱包余额
        System.out.println("\n钱包1的余额为: " + wallet1.getBalance());
        System.out.println("钱包2的余额为: " + wallet2.getBalance());
        // 创建第四个区块
        Block block4 = new Block(blockChain.lastBlock().getIndex(), blockChain.lastBlock().getHash(), null, 1);
        System.out.println("\n钱包2给钱包1转20个币...");
        block4.addTransaction(wallet2.createTransaction( wallet1.getPublicKey(), new BigDecimal(20)));
        blockChain.addBlock(block4);

        // 统计钱包余额
        System.out.println("\n钱包1的余额为:" + wallet1.getBalance());
        System.out.println("钱包2的余额为: " + wallet2.getBalance());
        System.out.println("区块是否合法  ==> " + blockChain.isChainValid());
    }
}