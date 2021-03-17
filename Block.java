import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Vector;

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

        Formatter formatter = new Formatter();
        assert hash != null;
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
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