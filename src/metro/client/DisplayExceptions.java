package metro.client;

public class DisplayExceptions {

	public void noStation() {
		System.out.println("Invalid station.Enter valid station.");
	}
	public void transactionException() {
		System.out.println("Cannot swipe out before swipe in.");
	}
	
	public void updateBalanceException()
	{
		System.out.println("Balance Updated failed");
	}
	
	public void insufficientBalance()
	{
		System.out.println("Balance is less than required minimum balance.");
	}
}
