import java.security.MessageDigest;
import java.util.Vector;
import javax.xml.bind.DatatypeConverter;
class Block {
    private int index;
    public String timestamp;
    public String data;

    @Override
    public String toString() {
        return "Block{" +
                "index=" + index +
                ", timestamp='" + timestamp + '\'' +
                ", data='" + data + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }

    private String previousHash;

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    private String hash;
    public Blockchain(int index, String timestamp, String data, String previousHash)
    {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }
    String calculateHash()
    {
        String dataHash = index+timestamp+data + previousHash;
        byte[] hash;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(dataHash.getBytes("UTF-8"));
        } catch (Exception e)
        {
            System.out.println("an error occurred while hashing");
        }
        return DatatypeConverter.printHexBinary(hash);
    }
}



class Blockchain {
    Vector<Block> chain;
    public Blockchain()
    {
        chain = new Vector<Block>();
        initChain();
    }
    private void initChain()
    {
        chain.add(new Block(0,"20/05/2018","Genesis Block","null"));
    }
    public Block getLatestBlock()
    {
        return chain.lastElement();
    }
    public void addBlock(Block block)
    {
        block.setPreviousHash(getLatestBlock().getHash());
        block.setHash(block.calculateHash());
        chain.add(block);
    }

    @Override
    public String toString() {
        return "Blockchain{" +
                "chain=" + chain +
                '}';
    }
}

public  class ChadyCoin
{
    public static  void main(String args[])
    {
        Blockchain ChadyCoin = new Blockchain();
        Block[] blocks;
        blocks[0] = new Block(1,"21/05/2018","{ reciever : 0x0235, sender : 0x2189, amount: 200$€ }","");
        blocks[1] = new Block(1,"27/05/2018","{ reciever : 0x02e5, sender : 0x2f89, amount: 12$€ }","");
        ChadyCoin.addBlock(blocks[0]);
        ChadyCoin.addBlock(blocks[1]);
        System.out.println(ChadyCoin);
    }
}