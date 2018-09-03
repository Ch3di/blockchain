import java.security.MessageDigest;
import java.util.Vector;
import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;
import java.text.SimpleDateFormat;

class Transaction {
    String fromAddress;
    String toAddress;
    int amount;
    public Transaction(String fromAddress, String toAddress, int amount) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{\n" +
                "\t fromAddress='" + fromAddress + '\'' +
                "\n\t toAddress='" + toAddress + '\'' +
                "\n\t amount=" + amount +
                "\n}";
    }
}
class Block {
    private int index;
    public String timestamp;
    public Vector<Transaction> transactions;
    private String previousHash;
    private String hash;
    private int nonce;

    public Block(int index, String timestamp, Vector<Transaction> transactions)
    {
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.previousHash = "";
        this.hash = calculateHash();
        this.nonce = 0;
    }

    public int getIndex() {
        return index;
    }

    public String transactionsToString()
    {
        String transactionText="";
        for(int i=0;i<transactions.size();i++)
            transactionText += transactions.elementAt(i).toString();
        return transactionText;

    }
    @Override
    public String toString() {
        return "Block { \n" +
                "index=" + index +
                ",\n timestamp='" + timestamp + '\'' +
                ",\n Transactions='" + (transactions!=null ? transactions.toString() : "No Transactions in this block") + '\'' +
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

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public void setTransaction(Transaction transaction)
    {
        this.transactions.add(transaction);
    }

    public Transaction getTransaction(int indx) {
        return transactions.elementAt(indx);
    }

    String calculateHash()
    {
        String dataHash = index+timestamp+transactions + previousHash+ nonce;
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
    Vector<Transaction> pendingTransactions;
    int difficulty;
    int miningReward;
    public Blockchain(int difficulty,int miningReward)
    {
        chain = new Vector<Block>();
        pendingTransactions = new Vector<Transaction>();
        this.difficulty =difficulty;
        this.miningReward = miningReward;
        initChain();
    }
    private void initChain()
    {
        // Initiating the blockchain with the Genesis Block
        chain.add(new Block(0,now(),null));
    }
    public Block getLatestBlock()
    {
        return chain.lastElement();
    }
    public void addBlock(String minerAdress)
    {
        Block block = new Block(getLatestBlock().getIndex()+1,now(),pendingTransactions);
        block.setPreviousHash(getLatestBlock().getHash());
        block.setHash(block.calculateHash());
        block.mineBlock(difficulty);
        chain.add(block);
        // Delete all mined transaction from the Vector
        pendingTransactions = new Vector<Transaction>();
        // Reward the miner
        pendingTransactions.add(new Transaction("Virtual-Machine",minerAdress,miningReward));
    }
    public void createTransaction(Transaction transaction)
    {
        // Add a transaction to the pending list
        pendingTransactions.add(transaction);
    }
    public int getBalance(String address)
    {
        int balance = 0;
        for(int i=1;i<chain.size();i++)
        {
            Vector<Transaction> currentTransactionList = chain.elementAt(i).transactions;
            for(int j=0;j<currentTransactionList.size();j++)
            {
                if(currentTransactionList.elementAt(j).toAddress.equals(address))
                    balance += currentTransactionList.elementAt(j).amount;

                if(currentTransactionList.elementAt(j).fromAddress.equals(address))
                    balance -= currentTransactionList.elementAt(j).amount;
            }
        }
        return balance;
    }
    // returns the current date
    String now() {
        final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
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
        Blockchain ChadyCoin = new Blockchain(2,50);
        // Create two Transactions and mine them into one Block
        ChadyCoin.createTransaction(new Transaction("0x2219","0x2ADE5",40));
        ChadyCoin.createTransaction(new Transaction("0x2219","0x13685",500));
        ChadyCoin.addBlock("0x868E");

        // Create for Transactions and mine them into one Block
        ChadyCoin.createTransaction(new Transaction("0x2E19","0x2BBE5",411));
        ChadyCoin.createTransaction(new Transaction("0x2F19","0x13A8A",703));
        ChadyCoin.createTransaction(new Transaction("0x2E1B","0x2ACF5",45));
        ChadyCoin.createTransaction(new Transaction("0x2A1C","0x13E8E",210));
        ChadyCoin.addBlock("0x2E1B");

        // Check the balance of the first miner: He must get the reward after the second Block got mined
        System.out.println(ChadyCoin.getBalance("0x868E"));

        // printing the validity of the chain before playing around with its blocks
        System.out.println("Validity status : " + ChadyCoin.isValidChain());

        // Try to change the hash of a block
        try {
            // change the 1st transaction of the 2nd Block
            ChadyCoin.searchByIndex(1).setTransaction(new Transaction("0x0235E","0x2189F",999000));
        } catch (NullPointerException e)
        {
            System.out.println("Block or Transaction is not found ");
        }
        // changing the hash of one block corrupts the chain
        System.out.println("Validity status : " + ChadyCoin.isValidChain());

        // Show the whole Blockchain
        System.out.println(ChadyCoin);

    }
}
