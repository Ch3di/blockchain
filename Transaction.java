
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
