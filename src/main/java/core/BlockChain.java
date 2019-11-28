package core;

import pojo.domain.Block;

import java.util.LinkedList;
import java.util.List;

/**
 * @author junan
 * @version V1.0
 * @className BlockChain
 * @disc
 * @date 2019/9/16 11:58
 */
public class BlockChain {

    private List<Block> blocks;

    public BlockChain() {
        this(null);
    }

    public BlockChain(List<Block> blocks) {
        //创建区块链
        if(blocks == null)
            blocks = new LinkedList<>();
        this.blocks = blocks;
    }

    //向区块链加入一个区块
    public void addBlock(Block block) {
        block.mineBlock();
        this.blocks.add(block);
    }

    //  获取区块的大小
    public int size() {
        return blocks.size();
    }

    //按索引获取区块
    public Block get(int index) {
        return blocks.get(index - 1);
    }

    /**
     *  获取当前区块链最后一个区块
     * @return
     */
    public Block lastBlock() {
        return blocks.get(blocks.size() - 1);
    }

    /**
     * 当前区块链是否有效
     * @return
     */
    public boolean isChainValid () {
        Block currentBlock  = this.lastBlock();   //获取区块链最后一个区块
        Block previousBlock = this.get(this.size() - 1);      // 获取最后一个前一个区块
        //比较计算最后一个区块的hash
        if(!currentBlock.getHash().equals(currentBlock.calculationHash()))
            return false;
        //这个你猜猜
        if(!currentBlock.getPreviousHash().equals(previousBlock.getHash()))
            return false;
        return true;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
}