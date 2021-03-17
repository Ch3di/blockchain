public  class ChadyCoin
{
    public static  void main(String[] args)
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
