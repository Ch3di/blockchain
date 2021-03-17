import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

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