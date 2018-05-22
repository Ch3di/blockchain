import java.security.MessageDigest;
import java.util.Vector;
import javax.xml.bind.DatatypeConverter;
class Block {
    private int index;
    public String timestamp;
    public String data;
    private String previousHash;
    private String hash;
    private int nonce;

    public Block(int index, String timestamp, String data, String previousHash)
    {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
        this.nonce = 0;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "Block { \n" +
                "index=" + index +
                ",\n timestamp='" + timestamp + '\'' +
                ",\n data='" + data + '\'' +
                ",\n previousHash='" + previousHash + '\'' +
                ",\n hash='" + hash + '\'' +
                "\n }";
    }

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

    public void setData(String data) {
        this.data = data;
    }

    String calculateHash()
    {
        String dataHash = index+timestamp+data + previousHash+ nonce;
        byte[] hash=null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(dataHash.getBytes("UTF-8"));
        } catch (Exception e)
        {
            System.out.println("an error occurred while hashing");
        }
        return DatatypeConverter.printHexBinary(hash);
    }
    void mineBlock(int difficulty)
    {
        String zeros = new String(new char[difficulty]).replace("\0","0");
        while(! hash.substring(0,difficulty).equals(zeros) )
        {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block was mined:" +hash);
    }
}



class Blockchain {
    Vector<Block> chain;
    int difficulty;
    public Blockchain()
    {
        chain = new Vector<Block>();
        difficulty =3;
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
        block.mineBlock(difficulty);
        chain.add(block);
    }

    @Override
    public String toString() {
        return "Blockchain { \n" +
                 chain +
                '}';
    }
    boolean isValidChain()
    {
        for(int i=1;i<chain.size();i++)
        {
            Block currentBlock = chain.elementAt(i);
            Block previousBlock = chain.elementAt(i-1);
            if(!currentBlock.getHash().equals(currentBlock.calculateHash()))
                return false;
            if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()))
                return false;
        }
        return true;
    }
    Block searchByIndex(int indx)
    {
        for(int i=0;i<chain.size();i++)
            if(chain.elementAt(i).getIndex()== indx)
                return chain.elementAt(i);
        return null;
    }
}

public  class ChadyCoin
{
    public static  void main(String args[])
    {
        Blockchain ChadyCoin = new Blockchain();
        Vector<Block> blocks;
        blocks = new Vector<Block>();
        blocks.add(new Block(1,"21/05/2018","{ reciever : 0x0235, sender : 0x2189, amount: 200$€ }",""));
        blocks.add(new Block(2,"27/05/2018","{ reciever : 0x02e5, sender : 0x2f89, amount: 12$€ }",""));
        ChadyCoin.addBlock(blocks.elementAt(0));
        ChadyCoin.addBlock(blocks.elementAt(1));
        System.out.println(ChadyCoin);

        // printing the validity of the chain before playing around with its blocks
        System.out.println(ChadyCoin.isValidChain());

        // Try to change the hash of a block
        try {
            ChadyCoin.searchByIndex(1).setData("{ reciever : 0x0235, sender : 0x2189, amount: 70000000000000$€ }");
        } catch (NullPointerException e)
        {
            System.out.println("Block not found");
        }
        // changing the hash of one block corrupts the chain
        System.out.println(ChadyCoin.isValidChain());


    }
}